package dev.wren.vsclientutils.content.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class AllCommands {

    @SubscribeEvent
    public static void register(RegisterClientCommandsEvent event) {

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("vsclientutils")
                .then(GetShipAtShipyardPosCommand.register())
                .then(ShipyardToWorldPosCommand.register());

        LiteralCommandNode<CommandSourceStack> vscuRoot = event.getDispatcher().register(root);

        event.getDispatcher().register(Commands.literal("vscu").redirect(vscuRoot));
    }

}
