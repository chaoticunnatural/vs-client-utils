package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;

import dev.wren.crowsnest.internal.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;

import net.minecraft.world.phys.AABB;
import org.joml.Matrix4dc;
import org.joml.Quaterniondc;
import org.joml.Vector3dc;
import org.joml.primitives.AABBdc;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.bodies.properties.BodyKinematics;
import org.valkyrienskies.core.api.bodies.properties.BodyTransform;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.properties.ChunkClaim;
import org.valkyrienskies.core.api.ships.properties.IShipActiveChunksSet;

import java.util.function.Consumer;

import static dev.wren.crowsnest.internal.CommandNode.*;


public class ShipInfoCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() { // mmm yes i must have every single value in LoadedShip
        return Commands.literal("ship")
            .then(
                Commands.argument("pos", BlockPosArgument.blockPos())
                    .then(shipNode("id", LoadedShip::getId, Long.class))
                    .then(shipNode("slug", LoadedShip::getSlug, String.class))
                    .then(shipNode("shipAABB", LoadedShip::getShipAABB, AABBic.class))
                    .then(shipNode("worldAABB", LoadedShip::getWorldAABB, AABBdc.class))
                    .then(shipBranch("kinematics", LoadedShip::getKinematics, BodyKinematics.class, kinematicsBranch -> {
                        kinematicsBranch.subNode("velocity", BodyKinematics::getVelocity, Vector3dc.class);
                        kinematicsBranch.subNode("rotation", BodyKinematics::getRotation, Quaterniondc.class);
                        kinematicsBranch.subNode("position", BodyKinematics::getPosition, Vector3dc.class);
                        kinematicsBranch.subNode("angularVelocity", BodyKinematics::getAngularVelocity, Vector3dc.class);
                        kinematicsBranch.subNode("scaling", BodyKinematics::getScaling, Vector3dc.class);
                        kinematicsBranch.subNode("worldToShip", BodyKinematics::getToModel, Matrix4dc.class);
                        kinematicsBranch.subNode("shipToWorld", BodyKinematics::getToWorld, Matrix4dc.class);
                        kinematicsBranch.subNode("transform", BodyKinematics::getTransform, BodyTransform.class);
                    }))
                    .then(shipBranch("chunkClaim", LoadedShip::getChunkClaim, ChunkClaim.class, chunkClaimBranch -> {
                        chunkClaimBranch.subNode("size", ChunkClaim::getSize, Integer.class);
                        chunkClaimBranch.subNode("toLong", ChunkClaim::toLong, Long.class);
                        chunkClaimBranch.subNode("xIndex", ChunkClaim::getXIndex, Integer.class);
                        chunkClaimBranch.subNode("xStart", ChunkClaim::getXStart, Integer.class);
                        chunkClaimBranch.subNode("xMiddle", ChunkClaim::getXMiddle, Integer.class);
                        chunkClaimBranch.subNode("xEnd", ChunkClaim::getXEnd, Integer.class);
                        chunkClaimBranch.subNode("zIndex", ChunkClaim::getZIndex, Integer.class);
                        chunkClaimBranch.subNode("zStart", ChunkClaim::getZStart, Integer.class);
                        chunkClaimBranch.subNode("zMiddle", ChunkClaim::getZMiddle, Integer.class);
                        chunkClaimBranch.subNode("zEnd", ChunkClaim::getZEnd, Integer.class);
                    }))
                    .then(shipNode("chunkClaimDimension", LoadedShip::getChunkClaimDimension, String.class))
            );
    }
}
