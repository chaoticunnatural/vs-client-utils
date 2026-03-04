package dev.wren.crowsnest.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.wren.crowsnest.internal.reg.TypeBranchRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.internal.CommandUtility.shipHandle;

@SuppressWarnings("unchecked")
public class CommandNode<T> {

    private final String name;
    private final Function<LoadedShip, T> extractor;
    private boolean built = false;
    private final boolean directoryBranch;
    private LiteralArgumentBuilder<CommandSourceStack> cached;

    private final List<CommandNode<?>> children = new ArrayList<>();

    private Class<T> adapterType;


    public CommandNode(String name, Function<LoadedShip, T> extractor) {
        this.name = name;
        this.extractor = extractor;
        this.directoryBranch = false;
    }

    public CommandNode(String name, Class<T> type) {
        this.name = name;
        this.extractor = null;
        this.directoryBranch = true;
    }

    private Function<Object, Object> valueWrapper = Function.identity();

    public void wrapValue(Function<Object, Object> wrapper) {
        this.valueWrapper = wrapper;
    }

    private T resolveValue(LoadedShip ship) {
        Object raw = extractor.apply(ship);
        return (T) valueWrapper.apply(raw);
    }

    public void typeAdapter(Class<T> type) {
        this.adapterType = type;
    }

    public void doubleAdapter() {
        this.adapterType = (Class<T>) Double.class;
    }

    public void integerAdapter() {
        this.adapterType = (Class<T>) Integer.class;
    }

    public CommandNode<T> subCommands(Consumer<CommandNode<T>> consumer) {
        consumer.accept(this);
        return this;
    }

    public <R> CommandNode<R> commandNode(String name, Function<T, R> extractor) {

        CommandNode<R> child = new CommandNode<>(name, ship -> extractor.apply(resolveValue(ship)));

        children.add(child);
        return child;
    }

    public <R> CommandNode<R> branchNode(String name, Function<T, R> extractor, Consumer<CommandNode<R>> consumer) {
        CommandNode<R> childBranch = new CommandNode<>(name, ship -> extractor.apply(this.extractor.apply(ship))).subCommands(consumer);

        children.add(childBranch);

        return childBranch;
    }

    public <R> CommandNode<R> dirBranchNode(String name, Class<R> type, Consumer<CommandNode<R>> consumer) {
        CommandNode<R> childBranch = new CommandNode<>(name, type).subCommands(consumer);

        children.add(childBranch);

        return childBranch;
    }

    public LiteralArgumentBuilder<CommandSourceStack> build() {

        if (built) return cached;

        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal(name);

        if (adapterType != null) {
            TypeBranchRegistry.applyIfPresent(adapterType, this);
        }

        if (!directoryBranch) {
            node.executes(ctx -> shipHandle(ctx, extractor, name));
        } else {
            node.executes(ctx -> {
                ctx.getSource().sendFailure(Component.literal("No value specified!"));
                return 0;
            });
        }

        for (CommandNode<?> child : children) {
            node.then(child.build());
        }

        cached = node;
        built = true;

        return node;
    }
}