package dev.wren.vsclientutils.content.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.vsclientutils.content.internal.Utils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import static dev.wren.vsclientutils.content.internal.Utils.formatBlockPos;

public class ShipyardToWorldPosCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("shipyardToWorldPos")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .executes(ctx -> {
                    BlockPos localPos = BlockPosArgument.getBlockPos(ctx, "pos");
                    BlockPos worldPos = Utils.getWorldPos(ctx.getSource().getUnsidedLevel(), localPos);

                    ctx.getSource().sendSuccess(() -> Component.literal("Shipyard position " + formatBlockPos(localPos) + " corresponds to world position " + formatBlockPos(worldPos)), false);

                    return Command.SINGLE_SUCCESS;
                })
            );
    }

}
