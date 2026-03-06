package dev.wren.crowsnest.internal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wren.crowsnest.internal.reg.TypeAdapterRegistry;
import dev.wren.crowsnest.internal.reg.TypeBridgeRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.internal.FormatUtility.asCommandOutput;

public class CommandNode<T> {

    private final String name;
    private final Function<LoadedShip, T> resolver;
    private final Class<T> type;

    private final List<CommandNode<?>> children = new ArrayList<>();

    private boolean built = false;
    private LiteralArgumentBuilder<CommandSourceStack> cached;

    @SuppressWarnings("unchecked")
    public CommandNode(String name, Function<LoadedShip, T> resolver, Class<T> type) {
        this.name = name;

        TypeBridgeRegistry.TypeBridge<T, ?> bridge = TypeBridgeRegistry.getBridge(type);

        Class<?> bridgedType = bridge.to();

        Function<LoadedShip, ?> bridgedResolver = resolver.andThen(bridge::convert);

        this.resolver = (Function<LoadedShip, T>) bridgedResolver;
        this.type = (Class<T>) bridgedType;

    }


    // NODES
    public static <I> LiteralArgumentBuilder<CommandSourceStack> shipNode(String name, Function<LoadedShip, I> resolver, Class<I> type) {
        return new CommandNode<>(name, resolver, type).build();
    }

    public static <I> LiteralArgumentBuilder<CommandSourceStack> shipBranch(String name, Function<LoadedShip, I> resolver, Class<I> type, Consumer<CommandNode<I>> subCommands) {
        CommandNode<I> node = new CommandNode<>(name, resolver, type);

        subCommands.accept(node);

        return node.build();
    }

    private static <T> Function<LoadedShip, ?> applyBridge(Function<LoadedShip, T> resolver, Class<T> type) {
        TypeBridgeRegistry.TypeBridge<T, ?> bridge = TypeBridgeRegistry.getBridge(type);

        if (bridge == null) {
            return resolver;
        }

        return resolver.andThen(bridge::convert);
    }

    // SUBNODES
    public <N> CommandNode<N> subNode(String name, Function<T, N> extractor, Class<N> type) {
        Function<LoadedShip, N> newResolver = resolver.andThen(extractor);

        CommandNode<N> child = new CommandNode<>(name, newResolver, type);
        children.add(child);

        return child;
    }

    public <N> CommandNode<N> subBranch(String name, Function<T, N> extractor, Class<N> type, Consumer<CommandNode<N>> subCommands) {
        CommandNode<N> child = subNode(name, extractor, type);
        subCommands.accept(child);
        return child;
    }


    public LiteralArgumentBuilder<CommandSourceStack> build() {

        if (built) return cached;

        LiteralArgumentBuilder<CommandSourceStack> node = Commands.literal(name);

        TypeAdapterRegistry.applyIfPresent(type, this);

        node.executes(this::shipHandle);

        for (CommandNode<?> child : children) {
            node.then(child.build());
        }

        cached = node;
        built = true;

        return node;
    }

    public int shipHandle(CommandContext<CommandSourceStack> context) {
        LoadedShip ship = Utility.getShipAtPos(context.getSource().getUnsidedLevel(), BlockPosArgument.getBlockPos(context, "pos"));

        if (ship == null) {
            context.getSource().sendFailure(Component.literal("No ship found!"));
            return 0;
        }

        T result = resolver.apply(ship);

        context.getSource().sendSuccess(() -> asCommandOutput(name, result), false);

        return Command.SINGLE_SUCCESS;
    }
}