package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.registries.TypeFormatterRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniondc;

import java.text.DecimalFormat;
import java.util.Map;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeFormatters {


    public static void register() {
        LOGGER.info("Registering type formatters...");

        TypeFormatterRegistry.registerFormatter(AABB.class, ((aabb, builder) ->
                builder.piece("Min: ", ChatFormatting.WHITE)
                        .piece(formatXYZPosition(aabb.minX, aabb.minY, aabb.minZ))
                        .piece(NEWLINE)
                        .piece("Max: ", ChatFormatting.WHITE)
                        .piece(formatXYZPosition(aabb.maxX, aabb.maxY, aabb.maxZ))
                        .piece(NEWLINE)
                        .piece("Size: ", ChatFormatting.WHITE)
                        .piece(formatXYZ(aabb.getXsize(), aabb.getYsize(), aabb.getZsize()))
                        .piece(NEWLINE)
                        .piece("Volume: ")
                        .piece(formatNumber(aabb.getXsize() * aabb.getYsize() * aabb.getZsize()), ChatFormatting.YELLOW)
        ));

        TypeFormatterRegistry.registerFormatter(Vec3.class, ((vec3, builder) ->
                builder.piece(formatXYZ(vec3.x(), vec3.y(), vec3.z()))
                        .piece(NEWLINE)
                        .piece("Length: ", ChatFormatting.WHITE)
                        .piece(formatNumber(vec3.length()), ChatFormatting.YELLOW)
        ));

        TypeFormatterRegistry.registerFormatter(Quaterniondc.class, (qdc, builder) ->
                builder.piece(formatXYZ(qdc.x(), qdc.y(), qdc.z()))
                        .piece(SEP)
                        .piece("W: ", ChatFormatting.WHITE)
                        .piece(formatNumber(qdc.w()), ChatFormatting.YELLOW)
                        .piece(NEWLINE)
                        .piece("Angle: ", ChatFormatting.WHITE)
                        .piece(formatNumber(qdc.angle()), ChatFormatting.GOLD)
        );
    }

    public static Map<String, ChatFormatting[]> formatXYZ(double x, double y, double z) {
        return Map.ofEntries(
                piece("X: ", ChatFormatting.WHITE),
                piece(formatNumber(x), ChatFormatting.RED),
                SEP,
                piece("Y: ", ChatFormatting.WHITE),
                piece(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                piece("Z: ", ChatFormatting.WHITE),
                piece(formatNumber(z), ChatFormatting.BLUE)
        );
    }

    public static Map<String, ChatFormatting[]> formatXYZPosition(double x, double y, double z) {
        return Map.ofEntries(
                piece("(", ChatFormatting.WHITE),
                piece(formatNumber(x), ChatFormatting.RED),
                SEP,
                piece(formatNumber(y), ChatFormatting.GREEN),
                SEP,
                piece(formatNumber(z), ChatFormatting.BLUE),
                piece(")", ChatFormatting.WHITE)
        );
    }

    public static Map.Entry<String, ChatFormatting[]> NEWLINE = piece("\n", ChatFormatting.WHITE);

    public static Map.Entry<String, ChatFormatting[]> SEP = piece(", ", ChatFormatting.WHITE);

    public static Map.Entry<String, ChatFormatting[]> piece(String content, ChatFormatting... styles) {
        return Map.entry(content, styles);
    }

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
