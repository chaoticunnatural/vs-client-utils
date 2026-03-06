package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;

public class AllCommands {

    public static void registerClient(RegisterClientCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("crowsnest")
            .then(ShipyardToWorldPosCommand.register())
            .then(ShipInfoCommand.register())
            ;

        LiteralCommandNode<CommandSourceStack> rootNode = event.getDispatcher().register(root);

        event.getDispatcher().register(Commands.literal("cn").redirect(rootNode));
    }
}
