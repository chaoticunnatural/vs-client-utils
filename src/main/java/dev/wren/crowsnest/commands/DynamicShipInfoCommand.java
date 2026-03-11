package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.crowsnest.internal.*;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.operation.OperationNode;
import dev.wren.crowsnest.internal.pipeline.ImmutablePipeline;
import dev.wren.crowsnest.internal.pipeline.TransmascFemboyPipeline;
import dev.wren.crowsnest.internal.pipeline.PipelineArgument;
import dev.wren.crowsnest.internal.registries.OperationRegistry;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.valkyrienskies.core.api.ships.LoadedShip;

import java.util.function.Supplier;

/**
 * it can't be <i>that</i> hard, can it?
 * <a href="https://tenor.com/view/clueless-gif-24395495">:clueless:</a>
 */
public class DynamicShipInfoCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("ship")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("pipeline", PipelineArgument.pipeline())
                    .suggests((ctx, builder) -> {
                        TransmascFemboyPipeline pipeline;

                        try {
                            pipeline = TransmascFemboyPipeline.parsePartial(ctx, builder.getRemaining());
                        } catch (Exception e) {
                            pipeline = new TransmascFemboyPipeline();
                        }

                        Class<?> cursorType = pipeline.getCurrentOutputType(LoadedShip.class);

                        builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

                        return OperationRegistry.listSuggestions(cursorType, ctx, builder);
                    })
                    .executes(ctx -> {
                        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                        LoadedShip ship = Utility.getShipAtPos(ctx.getSource().getUnsidedLevel(), pos);

                        String pipelineText = ctx.getArgument("pipeline", String.class);
                        ImmutablePipeline pipeline = TransmascFemboyPipeline.parse(ctx, pipelineText).immutable();

                        Object value = ship;

                        for (OperationNode node : pipeline.getOperations()) {
                            OperationDefinition<?, ?> op = node.operation();

                            Object typedInput = op.inputType().cast(value);

                            Object result = op.perform(typedInput, node.arguments());

                            value = TypeBridgeRegistry.getBridge(result.getClass()).safeConvert(result);
                        }

                        // todo replace "result" with command name + operations or smthn
                        ctx.getSource().sendSuccess(formatOutput("result", value), true);

                        return 1;
                    })
                ));
    }

    static Supplier<Component> formatOutput(String name, Object result) {
        MutableComponent prefix = Component.literal(name + ": " + result.getClass().getSimpleName() + "\n");
        return () ->  prefix.append(TypeFormatterRegistry.format(result));
    }
}