package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.FormatUtility;
import dev.wren.crowsnest.internal.reg.TypeFormatterRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;

import static dev.wren.crowsnest.CrowsNest.LOGGER;
import static dev.wren.crowsnest.internal.FormatUtility.NEWLINE;

public class TypeFormatters {


    public static void register() {
        LOGGER.info("Registering type formatters...");
        TypeFormatterRegistry.registerFormatter(AABB.class, aabb ->
            Component.literal("Min: ")
                .append(FormatUtility.formatXYZ(aabb.minX, aabb.minY, aabb.minZ))
                .append(NEWLINE)
                .append(Component.literal("Max: "))
                .append(FormatUtility.formatXYZ(aabb.maxX, aabb.maxY, aabb.maxZ))
                .append(NEWLINE)
                .append(Component.literal("Size: "))
                .append(FormatUtility.formatXYZ(aabb.getXsize(), aabb.getYsize(), aabb.getZsize()))
                .append(NEWLINE)
                .append(Component.literal("Volume: "))
                .append(FormatUtility.formatNumber(aabb.getXsize() * aabb.getYsize() * aabb.getZsize()))
        );
    }

}
