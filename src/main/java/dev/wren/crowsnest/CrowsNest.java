package dev.wren.crowsnest;

import dev.wren.crowsnest.registries.TypeBranches;
import dev.wren.crowsnest.registries.TypeFormatters;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CrowsNest.MODID)
public class CrowsNest {
    public static final String MODID = "crowsnest";
    public static final Logger LOGGER = LogManager.getLogger();

    public CrowsNest(FMLJavaModLoadingContext context) {
        TypeBranches.registerAdapters();
        TypeBranches.registerBridges();
        TypeFormatters.register();
    }

}
