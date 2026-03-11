package dev.wren.crowsnest.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;

import dev.wren.crowsnest.internal.Utility;

import static dev.wren.crowsnest.internal.Utility.l;

public class ShipyardToWorldPosCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("shipyardToWorldPos")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .executes(ctx -> {
                    BlockPos localPos = BlockPosArgument.getBlockPos(ctx, "pos");
                    BlockPos worldPos = Utility.getWorldPos(ctx.getSource().getUnsidedLevel(), localPos);

                    ctx.getSource().sendSuccess(l("Shipyard position " + formatBlockPos(localPos) + " corresponds to world position " + formatBlockPos(worldPos)), false);

                    return Command.SINGLE_SUCCESS;
                })
            );
    }

    static String formatBlockPos(BlockPos pos) {
        return "("+pos.getX()+", "+pos.getY()+", "+pos.getZ()+")";
    }

}
