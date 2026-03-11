package dev.wren.crowsnest.internal.pipeline;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.wren.crowsnest.internal.argument.ArgumentParser;
import dev.wren.crowsnest.internal.argument.ArgumentSet;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.operation.OperationNode;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import net.minecraft.commands.CommandSourceStack;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.ArrayList;
import java.util.List;

/**
 * blame izzy for the class name
 */
public class TransmascFemboyPipeline {

    private final List<OperationNode> nodes;

    public TransmascFemboyPipeline() {
        this.nodes = new ArrayList<>();
    }

    public TransmascFemboyPipeline(List<OperationNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public void addOperation(OperationNode node) {
        nodes.add(node);
    }

    public List<OperationNode> getOperations() {
        return nodes;
    }

    public Class<?> getCurrentOutputType(Class<?> rootType) {
        Class<?> type = rootType;
        for (OperationNode node : nodes) {
            OperationDefinition<?, ?> op = node.operation();
            if (!op.inputType().isAssignableFrom(type)) {
                throw new IllegalStateException(
                        "Operation " + op.name() + " incompatible with " + type.getSimpleName()
                );
            }
            type = TypeBridgeRegistry.getBridge(op.returnType()).to();
        }
        return type;
    }

    public static TransmascFemboyPipeline parse(CommandContext<CommandSourceStack> ctx, String input) throws CommandSyntaxException {
        StringReader reader = new StringReader(input);
        TransmascFemboyPipeline pipeline = new TransmascFemboyPipeline();
        Class<?> currentType = LoadedShip.class;

        skipWhitespace(reader);

        while (reader.canRead()) {
            String opName = reader.readUnquotedString();
            OperationDefinition<?, ?> op = OperationRegistry.getOperation(currentType, opName);
            if (op == null) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()
                        .createWithContext(reader, opName);
            }

            ArgumentSet args = ArgumentParser.parseArguments(ctx, op.getArgumentDescriptors());
            pipeline.addOperation(new OperationNode(op, args));

            currentType = TypeBridgeRegistry.getBridge(op.returnType()).to();
            skipWhitespace(reader);
        }

        return pipeline;
    }


    public static TransmascFemboyPipeline parsePartial(CommandContext<CommandSourceStack> ctx, String input) {
        StringReader reader = new StringReader(input);
        TransmascFemboyPipeline pipeline = new TransmascFemboyPipeline();
        Class<?> currentType = LoadedShip.class;

        while (reader.canRead()) {
            skipWhitespace(reader);
            if (!reader.canRead()) break;

            String opName = reader.readUnquotedString();
            OperationDefinition<?, ?> op = OperationRegistry.getOperation(currentType, opName);
            if (op == null) break;

            ArgumentSet args = ArgumentParser.parseArguments(ctx, op.getArgumentDescriptors());
            pipeline.addOperation(new OperationNode(op, args));

            currentType = TypeBridgeRegistry.getBridge(op.returnType()).to();
        }

        return pipeline;
    }

    private static void skipWhitespace(StringReader reader) {
        while (reader.canRead() && Character.isWhitespace(reader.peek())) {
            reader.skip();
        }
    }

    public ImmutablePipeline immutable() {
        return new ImmutablePipeline(this);
    }
}