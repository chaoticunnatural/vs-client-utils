package dev.wren.crowsnest.internal.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeFormatterRegistry {

    private static final Map<Class<?>, Function<Object, Component>> FORMATTERS = new HashMap<>();

    public static <T> void registerFormatter(Class<T> type, BiConsumer<T, FormatBuilder> formatter) {
        LOGGER.info("Registering formatter for {}", type.getCanonicalName());

        FormatBuilder formatBuilder = new FormatBuilder();

        FORMATTERS.put(type, object -> {
            formatter.accept(type.cast(object), formatBuilder);
            return formatBuilder.build();
        });
    }

    public static Component format(Object object) {
        if (object == null) return Component.literal("null");

        Class<?> objClass = object.getClass();

        Function<Object, Component> formatter = FORMATTERS.get(objClass);
        if (formatter != null)
            return formatter.apply(object);

        return Component.literal(object.toString());
    }

    public record Format(String content, ChatFormatting... formats) {
        public static Format of(String content, ChatFormatting... formats) {
            return new Format(content, formats);
        }

        public static Format of(Component content, ChatFormatting... formats) {
            return new Format(content.getString(), formats);
        }
    }

    public static class FormatBuilder {
        private final ArrayList<Format> formatList;

        public FormatBuilder() {
            formatList = new ArrayList<>();
        }

        public FormatBuilder format(Format... format) {
            formatList.addAll(List.of(format));
            return this;
        }

        public FormatBuilder format(String content, ChatFormatting... formats) {
            formatList.add(Format.of(content, formats));
            return this;
        }

        public Component build() {
            MutableComponent initial = Component.literal("");
            for (Format format : this.formatList) {
                initial.append(Component.literal(format.content()).withStyle(format.formats()));
            }

            return initial;
        }
    }
}
