package com.soybeani.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/11 16:38
 * @description
 */
public class SuperSlimeBlockMax extends SlimeBlock {
    public SuperSlimeBlockMax(Settings settings) {
        super(settings);

    }
    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance);
        this.bounce(entity);
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounce(entity);
        }
    }

    private void bounce(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0) {
            double d = 100;
            entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
        }
    }
}
