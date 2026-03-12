package dev.wren.crowsnest.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.joml.Quaterniondc;

import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;

import java.text.DecimalFormat;

import static dev.wren.crowsnest.internal.registries.TypeFormatterRegistry.Format;
import static dev.wren.crowsnest.internal.registries.TypeFormatterRegistry.Format.of;

public class TypeFormatters {

    public static void register() {
        TypeFormatterRegistry.registerFormatter(AABB.class, ((aabb, builder) ->
                builder.format("Min: ", ChatFormatting.WHITE)
                        .format(formatXYZPosition(aabb.minX, aabb.minY, aabb.minZ))
                        .format(NEWLINE)
                        .format("Max: ", ChatFormatting.WHITE)
                        .format(formatXYZPosition(aabb.maxX, aabb.maxY, aabb.maxZ))
                        .format(NEWLINE)
                        .format("Size: ", ChatFormatting.WHITE)
                        .format(formatXYZ(aabb.getXsize(), aabb.getYsize(), aabb.getZsize()))
                        .format(NEWLINE)
                        .format("Volume: ")
                        .format(formatNumber(aabb.getXsize() * aabb.getYsize() * aabb.getZsize()), ChatFormatting.YELLOW)
                        .build()
        ));

        TypeFormatterRegistry.registerFormatter(Vec3.class, ((vec3, builder) ->
                builder.format(formatXYZ(vec3.x(), vec3.y(), vec3.z()))
                        .format(NEWLINE)
                        .format("Length: ", ChatFormatting.WHITE)
                        .format(formatNumber(vec3.length()), ChatFormatting.YELLOW)
                        .build()
        ));

        TypeFormatterRegistry.registerFormatter(Quaterniondc.class, (qdc, builder) ->
                builder.format(formatXYZ(qdc.x(), qdc.y(), qdc.z()))
                        .format(SEP)
                        .format("W: ", ChatFormatting.WHITE)
                        .format(formatNumber(qdc.w()), ChatFormatting.YELLOW)
                        .format(NEWLINE)
                        .format("Angle: ", ChatFormatting.WHITE)
                        .format(formatNumber(qdc.angle()), ChatFormatting.GOLD)
                        .build()
        );
    }


    public static Format[] formatXYZ(double x, double y, double z) {
        return new Format[]{
                of("X: ", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of("Y: ", ChatFormatting.WHITE),
                of(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                of("Z: ", ChatFormatting.WHITE),
                of(formatNumber(z), ChatFormatting.BLUE)
        };
    }

    public static Format[] formatXYZPosition(double x, double y, double z) {
        return new Format[]{
                of("(", ChatFormatting.WHITE),
                of(formatNumber(x), ChatFormatting.RED),
                SEP,
                of(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                of(formatNumber(z), ChatFormatting.BLUE),
                of(")", ChatFormatting.WHITE)
        };
    }

    public static Format NEWLINE = of("\n", ChatFormatting.WHITE);

    public static Format SEP = of(", ", ChatFormatting.WHITE);

    private static final DecimalFormat NORMAL_FORMAT = new DecimalFormat("#,##0.###");

    private static final DecimalFormat SMALL_FORMAT = new DecimalFormat("0.#####");

    private static final DecimalFormat SCIENTIFIC_FORMAT = new DecimalFormat("0.###E0");

    public static String formatNumber(double value) {

        double abs = Math.abs(value);

        if (abs == 0) return "0";

        if (abs >= 1_000_000_000) {
            return SCIENTIFIC_FORMAT.format(value);
        }

        if (abs >= 0.001) {
            return NORMAL_FORMAT.format(value);
        }

        return SMALL_FORMAT.format(value);
    }
}
