package dev.wren.crowsnest.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;

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

import static dev.wren.crowsnest.internal.CommandUtility.branchNode;
import static dev.wren.crowsnest.internal.CommandUtility.shipNode;


public class ShipInfoCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() { // mmm yes i must have every single value in LoadedShip
        return Commands.literal("ship")
            .then(
                Commands.argument("pos", BlockPosArgument.blockPos())
                    .then(shipNode("id", LoadedShip::getId, Long.class))
                    .then(shipNode("slug", LoadedShip::getSlug, String.class))
                    .then(shipNode("shipAABB", LoadedShip::getShipAABB, AABBic.class))
                    .then(shipNode("worldAABB", LoadedShip::getWorldAABB, AABBdc.class))
                    .then(shipNode("shipToWorld", LoadedShip::getShipToWorld, Matrix4dc.class))
                    .then(shipNode("worldToShip", LoadedShip::getWorldToShip, Matrix4dc.class))
                    .then(shipNode("angularVelocity", LoadedShip::getAngularVelocity, Vector3dc.class))
                    .then(shipNode("velocity", LoadedShip::getVelocity, Vector3dc.class))
                    .then(branchNode("kinematics", LoadedShip::getKinematics, kinematicsBranch -> {
                        kinematicsBranch.commandNode("velocity", BodyKinematics::getVelocity).typeAdapter(Vector3dc.class);
                        kinematicsBranch.commandNode("rotation", BodyKinematics::getRotation).typeAdapter(Quaterniondc.class);
                        kinematicsBranch.commandNode("position", BodyKinematics::getPosition).typeAdapter(Vector3dc.class);
                        kinematicsBranch.commandNode("angularVelocity", BodyKinematics::getAngularVelocity).typeAdapter(Vector3dc.class);
                        kinematicsBranch.commandNode("scaling", BodyKinematics::getScaling).typeAdapter(Vector3dc.class);
                        kinematicsBranch.commandNode("worldToShip", BodyKinematics::getToModel).typeAdapter(Matrix4dc.class);
                        kinematicsBranch.commandNode("shipToWorld", BodyKinematics::getToWorld).typeAdapter(Matrix4dc.class);
                        kinematicsBranch.commandNode("transform", BodyKinematics::getTransform).typeAdapter(BodyTransform.class);
                    }))
                    .then(branchNode("chunkClaim", LoadedShip::getChunkClaim, chunkClaimBranch -> {
                        chunkClaimBranch.commandNode("size", ChunkClaim::getSize);
                        chunkClaimBranch.dirBranchNode("x", ChunkClaim.class, xBranch -> {
                            xBranch.commandNode("index", ChunkClaim::getXIndex);
                            xBranch.commandNode("start", ChunkClaim::getXStart);
                            xBranch.commandNode("middle", ChunkClaim::getXMiddle);
                            xBranch.commandNode("end", ChunkClaim::getXEnd);
                        });
                        chunkClaimBranch.dirBranchNode("z", ChunkClaim.class, zBranch -> {
                            zBranch.commandNode("index", ChunkClaim::getZIndex);
                            zBranch.commandNode("start", ChunkClaim::getZStart);
                            zBranch.commandNode("middle", ChunkClaim::getZMiddle);
                            zBranch.commandNode("end", ChunkClaim::getZEnd);
                        });
                        chunkClaimBranch.commandNode("toLong", ChunkClaim::toLong);
                    }))
                    .then(shipNode("chunkClaimDimension", LoadedShip::getChunkClaimDimension, String.class))
            );
    }



}
