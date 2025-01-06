package com.soybeani.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * @author soybean
 * @date 2025/1/6 17:41
 * @description
 */
public class RayCastUtils {
    public static Entity getTargetEntity(World world, PlayerEntity player, double maxDistance) {
        Vec3d eyePos = player.getEyePos();
        Vec3d rotation = player.getRotationVec(1.0F);
        Vec3d endPos = eyePos.add(rotation.multiply(maxDistance));

        Box box = player.getBoundingBox().stretch(rotation.multiply(maxDistance)).expand(1.0D);

        Entity result = null;
        double closestDistance = maxDistance;

        for (Entity entity : world.getOtherEntities(player, box)) {
            Box entityBox = entity.getBoundingBox().expand(entity.getTargetingMargin());
            Optional<Vec3d> hitPos = entityBox.raycast(eyePos, endPos);

            if (hitPos.isPresent()) {
                double distance = eyePos.distanceTo(hitPos.get());
                if (distance < closestDistance) {
                    result = entity;
                    closestDistance = distance;
                }
            }
        }

        return result;
    }
}
