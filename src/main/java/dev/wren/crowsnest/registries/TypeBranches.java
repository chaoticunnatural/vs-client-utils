package dev.wren.crowsnest.registries;

import dev.wren.crowsnest.internal.reg.TypeBranchRegistry;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;

import static dev.wren.crowsnest.CrowsNest.LOGGER;

public class TypeBranches {

    public static void registerAdapters() {
        LOGGER.info("Registering type adapters...");
        TypeBranchRegistry.registerAdapter(Vec3.class, node -> {
            node.commandNode("x", Vec3::x).doubleAdapter();
            node.commandNode("y", Vec3::y).doubleAdapter();
            node.commandNode("z", Vec3::z).doubleAdapter();
            node.commandNode("length", Vec3::length).doubleAdapter();
            node.commandNode("lengthSqr", Vec3::lengthSqr).doubleAdapter();
            node.commandNode("horizontalDistance", Vec3::horizontalDistance).doubleAdapter();
            node.commandNode("horizontalDistanceSqr", Vec3::horizontalDistanceSqr).doubleAdapter();
            node.commandNode("normalize", Vec3::normalize);
        });

        TypeBranchRegistry.registerAdapter(Quaterniondc.class, node -> {
            node.commandNode("x", Quaterniondc::x).doubleAdapter();
            node.commandNode("y", Quaterniondc::y).doubleAdapter();
            node.commandNode("z", Quaterniondc::z).doubleAdapter();
            node.commandNode("w", Quaterniondc::w).doubleAdapter();
            node.commandNode("angle", Quaterniondc::angle).doubleAdapter();
            node.commandNode("lengthSquared", Quaterniondc::lengthSquared).doubleAdapter();
        });

        TypeBranchRegistry.registerAdapter(AABB.class, node -> {
            node.commandNode("center", AABB::getCenter).typeAdapter(Vec3.class);
            node.commandNode("size", AABB::getSize).doubleAdapter();
            node.commandNode("xSize", AABB::getXsize).doubleAdapter();
            node.commandNode("ySize", AABB::getYsize).doubleAdapter();
            node.commandNode("zSize", AABB::getZsize).doubleAdapter();
        });
    }

    public static void registerBridges() {
        LOGGER.info("Registering type bridges...");
        TypeBranchRegistry.registerBridge(Vector3dc.class, Vec3.class, v -> new Vec3(v.x(), v.y(), v.z()));
        TypeBranchRegistry.registerBridge(AABBic.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
        TypeBranchRegistry.registerBridge(AABBdc.class, AABB.class, box -> new AABB(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ()));
    }
}
