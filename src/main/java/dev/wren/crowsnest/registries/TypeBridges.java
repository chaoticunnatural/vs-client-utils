package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.reg.TypeAdapterRegistry;
import dev.wren.crowsnest.internal.reg.TypeBridgeRegistry;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeBridges {

    public static void register() {
        LOGGER.info("Registering type bridges...");
        TypeBridgeRegistry.registerBridge(Vector3dc.class, Vec3.class, v -> new Vec3(v.x(), v.y(), v.z()));
        TypeBridgeRegistry.registerBridge(AABBic.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
        TypeBridgeRegistry.registerBridge(AABBdc.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
    }

}
