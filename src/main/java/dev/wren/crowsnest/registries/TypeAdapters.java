package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.reg.TypeAdapterRegistry;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeAdapters {

    public static void registerAdapters() {
        LOGGER.info("Registering type adapters...");
        TypeAdapterRegistry.registerAdapter(Vec3.class, node -> {
            node.subNode("x", Vec3::x, Double.class);
            node.subNode("y", Vec3::y, Double.class);
            node.subNode("z", Vec3::z, Double.class);
            node.subNode("length", Vec3::length, Double.class);
            node.subNode("lengthSqr", Vec3::lengthSqr, Double.class);
            node.subNode("horizontalDistance", Vec3::horizontalDistance, Double.class);
            node.subNode("horizontalDistanceSqr", Vec3::horizontalDistanceSqr, Double.class);
        });

        TypeAdapterRegistry.registerAdapter(Quaterniondc.class, node -> {
            node.subNode("x", Quaterniondc::x, Double.class);
            node.subNode("y", Quaterniondc::y, Double.class);
            node.subNode("z", Quaterniondc::z, Double.class);
            node.subNode("w", Quaterniondc::w, Double.class);
            node.subNode("angle", Quaterniondc::angle, Double.class);
            node.subNode("lengthSquared", Quaterniondc::lengthSquared, Double.class);
        });

        TypeAdapterRegistry.registerAdapter(AABB.class, node -> {
            node.subNode("center", AABB::getCenter, Vec3.class);
            node.subNode("size", AABB::getSize, Double.class);
            node.subNode("xSize", AABB::getXsize, Double.class);
            node.subNode("ySize", AABB::getYsize, Double.class);
            node.subNode("zSize", AABB::getZsize, Double.class);
        });
    }
}
