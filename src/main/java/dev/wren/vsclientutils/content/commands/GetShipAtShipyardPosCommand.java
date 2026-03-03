package dev.wren.vsclientutils.content.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import dev.wren.vsclientutils.content.internal.Utils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetShipAtShipyardPosCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("shipFromShipyardPos")
            .then(Commands.argument("pos", BlockPosArgument.blockPos())
                .executes(ctx -> {
                    Long shipId = Utils.getShipIdAtPos(ctx.getSource().getUnsidedLevel(), BlockPosArgument.getBlockPos(ctx, "pos"));
                    String slug = Utils.getShipSlugAtPos(ctx.getSource().getUnsidedLevel(), BlockPosArgument.getBlockPos(ctx, "pos"));

                    if (shipId == -1) {
                        ctx.getSource().sendFailure(Component.literal("No ship found!"));
                    } else {
                        ctx.getSource().sendSuccess(() -> Component.literal("Found ship with id " + shipId + " and slug " + slug), false);
                    }

                    return Command.SINGLE_SUCCESS;
                })
            );
    }
}
