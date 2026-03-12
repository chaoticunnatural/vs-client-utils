package dev.wren.crowsnest.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.valkyrienskies.core.api.ships.LoadedShip;

import dev.wren.crowsnest.internal.Utility;
import dev.wren.crowsnest.internal.operation.OperationDefinition;
import dev.wren.crowsnest.internal.pipeline.ImmutablePipeline;
import dev.wren.crowsnest.internal.pipeline.Pipeline;
import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;
import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;

import java.util.function.Supplier;

/**
 * it can't be <i>that</i> hard, can it?
 * <a href="https://tenor.com/view/clueless-gif-24395495">:clueless:</a>
 */
public class DynamicShipInfoCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("ship")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .then(Commands.argument("pipeline", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
                        LoadedShip ship = Utility.getShipAtPos(ctx.getSource().getUnsidedLevel(), pos);

                        String pipelineText = ctx.getArgument("pipeline", String.class);
                        System.out.println(pipelineText);
                        ImmutablePipeline pipeline = Pipeline.parse(pipelineText).immutable();

                        Object value = ship;

                        for (OperationDefinition<?, ?> operation : pipeline.operations()) {
                            Object result = operation.perform(value);

                            value = TypeBridgeRegistry.getBridge(result.getClass()).convert(result);
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