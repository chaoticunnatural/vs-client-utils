package dev.wren.crowsnest.internal.registries;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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


    public static class FormatBuilder {
        private final Map<String, ChatFormatting[]> contentMap;

        public FormatBuilder() {
            contentMap = new HashMap<>();
        }

        public FormatBuilder piece(Map<String, ChatFormatting[]> piece) {
            contentMap.putAll(piece);
            return this;
        }

        public FormatBuilder piece(String content, ChatFormatting... formats) {
            contentMap.put(content, formats);
            return this;
        }

        public FormatBuilder piece(Map.Entry<String, ChatFormatting[]> piece) {
            contentMap.put(piece.getKey(), piece.getValue());
            return this;
        }

        public Component build() {
            MutableComponent initial = Component.literal("");
            for (Map.Entry<String, ChatFormatting[]> piece : this.contentMap.entrySet()) {
                initial.append(Component.literal(piece.getKey()).withStyle(piece.getValue()));
            }

            return initial;
        }
    }
}
