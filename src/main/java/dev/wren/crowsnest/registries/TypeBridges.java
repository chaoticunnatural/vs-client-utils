package dev.wren.crowsnest.registries;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;

import dev.wren.crowsnest.internal.registries.TypeBridgeRegistry;

public class TypeBridges {

    public static void register() {
        TypeBridgeRegistry.registerBridge(Vector3dc.class, Vec3.class, v -> new Vec3(v.x(), v.y(), v.z()));
        TypeBridgeRegistry.registerBridge(AABBic.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
        TypeBridgeRegistry.registerBridge(AABBdc.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
    }

}
