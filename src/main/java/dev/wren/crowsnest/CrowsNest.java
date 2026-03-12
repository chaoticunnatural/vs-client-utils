package dev.wren.crowsnest;

import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.wren.crowsnest.commands.AllCommands;
import dev.wren.crowsnest.registries.*;

@Mod(CrowsNest.MODID)
@Mod.EventBusSubscriber(modid = CrowsNest.MODID)
@SuppressWarnings("unused")
public class CrowsNest {
    public static final String NAME = "Crow's Nest";
    public static final String MODID = "crowsnest";
    public static final Logger LOGGER = LogManager.getLogger();

    public CrowsNest(FMLJavaModLoadingContext context) {
        LOGGER.info("Registering bridges...");
        TypeBridges.register();
        LOGGER.info("Registering formatters...");
        TypeFormatters.register();
        LOGGER.info("Registering operations...");
        Operations.register();

        LOGGER.info("{} ({}) initialized!", NAME, MODID);
    }

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        LOGGER.info("Registering client commands...");
        AllCommands.registerClient(event);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        LOGGER.info("Registering commands...");
        AllCommands.register(event);
    }

}
