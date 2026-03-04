package dev.wren.crowsnest.internal.reg;

import dev.wren.crowsnest.internal.CommandNode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

@SuppressWarnings("unchecked")
public class TypeBranchRegistry {

    private static final Map<Class<?>, Consumer<CommandNode<?>>> ADAPTERS = new HashMap<>();
    private static final Map<Class<?>, TypeBridge<?, ?>> BRIDGES = new HashMap<>();

    public static <T> void registerAdapter(Class<T> type, Consumer<CommandNode<T>> adapter) {
        LOGGER.info("Registering adapter for {}", type.getCanonicalName());

        ADAPTERS.put(type, node -> adapter.accept((CommandNode<T>) node));
    }

    public static <F, T> void registerBridge(Class<F> from, Class<T> to, Function<F, T> converter) {
        LOGGER.info("Registering bridge from {} to {}", from.getCanonicalName(), to.getCanonicalName());

        BRIDGES.put(from, new TypeBridge<>(to, converter));
    }

    public static <T> TypeBridge<T, ?> getBridge(Class<T> type) {
        return (TypeBridge<T, ?>) BRIDGES.get(type);
    }

    public static <T> void applyIfPresent(Class<T> type, CommandNode<T> nodeBuilder) {
        if (!(BRIDGES.containsKey(type) || ADAPTERS.containsKey(type))) return;

        Class<?> effectiveType = type;

        TypeBridge<T, ?> bridge = getBridge(type);

        if (bridge != null) {
            effectiveType = bridge.to();

            nodeBuilder.wrapValue(bridge::convert);
        }

        Consumer<CommandNode<?>> adapter = ADAPTERS.get(effectiveType);

        if (adapter != null) {
            adapter.accept(nodeBuilder);
        }
    }

    public static class TypeBridge<F, T> {
        private final Class<T> to;
        private final Function<F, T> converter;

        public TypeBridge(Class<T> to, Function<F, T> converter) {
            this.to = to;
            this.converter = converter;
        }

        public Class<T> to() {
            return to;
        }

        public Object convert(Object value) {
            return converter.apply((F) value);
        }
    }
}
