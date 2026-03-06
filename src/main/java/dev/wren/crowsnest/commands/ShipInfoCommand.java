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
                    .then(shipNode("shipToWorld", LoadedShip::getShipToWorld, Matrix4dc.class))
                    .then(shipNode("worldToShip", LoadedShip::getWorldToShip, Matrix4dc.class))
                    .then(shipNode("angularVelocity", LoadedShip::getAngularVelocity, Vector3dc.class))
                    .then(shipNode("velocity", LoadedShip::getVelocity, Vector3dc.class))
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
                    }))
                    .then(shipNode("chunkClaimDimension", LoadedShip::getChunkClaimDimension, String.class))
            );
    }
}
