package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

public class AllCommands {

    public static void registerClient(RegisterClientCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> clientRoot = Commands.literal("crowsnest")
            .then(ShipyardToWorldPosCommand.register())
            .then(DynamicShipInfoCommand.register())
            ;

        LiteralCommandNode<CommandSourceStack> clientRootNode = event.getDispatcher().register(clientRoot);

        event.getDispatcher().register(Commands.literal("cn").redirect(clientRootNode));
    }

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> commandRoot = Commands.literal("crowsnest");

        LiteralCommandNode<CommandSourceStack> commandRootNode = event.getDispatcher().register(commandRoot);

        event.getDispatcher().register(Commands.literal("cn").redirect(commandRootNode));
    }
}
