package dev.wren.crowsnest;

import dev.wren.crowsnest.commands.AllCommands;
import dev.wren.crowsnest.registries.Operations;
import dev.wren.crowsnest.registries.TypeAdapters;
import dev.wren.crowsnest.registries.TypeBridges;
import dev.wren.crowsnest.registries.TypeFormatters;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CrowsNest.MODID)
@Mod.EventBusSubscriber(modid = CrowsNest.MODID)
public class CrowsNest {
    public static final String MODID = "crowsnest";
    public static final Logger LOGGER = LogManager.getLogger();

    public CrowsNest(FMLJavaModLoadingContext context) {
        TypeBridges.register();
        TypeAdapters.registerAdapters();
        TypeFormatters.register();
        Operations.register();
    }

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        LOGGER.info("Registering client commands...");
        AllCommands.registerClient(event);
    }

}
