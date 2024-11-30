package com.soybeani.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/30 16:01
 * @description
 */
public class TTEntity extends TntEntity {
    private static final int FUSE = 80; // 4秒
    private static final float EXPLOSION_RADIUS = 4.0f; // 爆炸半径

    public TTEntity(EntityType<? extends TntEntity> entityType, World world) {
        super(entityType, world);
        this.setFuse(FUSE);
    }

    public TTEntity(World world, double x, double y, double z, LivingEntity igniter) {
        super(world, x, y, z, igniter);
        this.setFuse(FUSE);
    }

    @Override
    public void tick() {
        if (this.getFuse() <= 0) {
            this.discard();
            if (!this.getWorld().isClient) {
                this.explode(); // 使用我们自己的爆炸方法
            }
        } else {
            this.setFuse(this.getFuse() - 1);
            // 产生烟雾效果
            this.getWorld().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY() + 0.5, this.getZ(),
                    0.0, 0.0, 0.0
            );
        }
    }

    private void explode() {
        World world = this.getWorld();
        if (!world.isClient) {
            // 获取爆炸中心点
            Vec3d explosionPos = this.getPos();

            // 创建爆炸效果但不破坏方块
            world.createExplosion(this, explosionPos.x, explosionPos.y, explosionPos.z,
                    EXPLOSION_RADIUS, false, World.ExplosionSourceType.TNT);

            // 获取爆炸范围内的所有方块
            Box explosionBox = new Box(
                    explosionPos.x - EXPLOSION_RADIUS, explosionPos.y - EXPLOSION_RADIUS, explosionPos.z - EXPLOSION_RADIUS,
                    explosionPos.x + EXPLOSION_RADIUS, explosionPos.y + EXPLOSION_RADIUS, explosionPos.z + EXPLOSION_RADIUS
            );

            // 遍历范围内的所有方块位置
            BlockPos.iterate(
                    new BlockPos((int)(explosionBox.minX), (int)(explosionBox.minY), (int)(explosionBox.minZ)),
                    new BlockPos((int)(explosionBox.maxX), (int)(explosionBox.maxY), (int)(explosionBox.maxZ))
            ).forEach(pos -> {
                BlockState state = world.getBlockState(pos);
                if (!state.isAir() && !state.isLiquid()) {
                    // 计算到爆炸中心的方向和距离
                    Vec3d blockCenter = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    Vec3d direction = blockCenter.subtract(explosionPos).normalize();
                    double distance = blockCenter.distanceTo(explosionPos);

                    // 根据距离计算力度
                    double power = Math.max(0.5, (EXPLOSION_RADIUS - distance) / EXPLOSION_RADIUS * 2.0);

                    // 创建并发射方块实体
                    FallingBlockEntity fallingBlock = FallingBlockEntity.spawnFromBlock(world, pos, state);
                    if (fallingBlock != null) {
                        fallingBlock.setPosition(blockCenter.x, blockCenter.y, blockCenter.z);

                        // 添加随机性
                        double randomX = (world.random.nextDouble() - 0.5) * 0.2;
                        double randomY = (world.random.nextDouble() - 0.5) * 0.2;
                        double randomZ = (world.random.nextDouble() - 0.5) * 0.2;

                        // 设置速度
                        fallingBlock.setVelocity(
                                direction.x * power + randomX,
                                direction.y * power + 0.8 + randomY,
                                direction.z * power + randomZ
                        );
                        fallingBlock.velocityModified = true;

                        // 移除原方块
                        world.removeBlock(pos, false);
                    }
                }
            });

            // 播放爆炸音效
            world.playSound(null, explosionPos.x, explosionPos.y, explosionPos.z,
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            // 添加爆炸粒子效果
            for (int i = 0; i < 50; i++) {
                double px = explosionPos.x + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                double py = explosionPos.y + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                double pz = explosionPos.z + (world.random.nextDouble() - 0.5) * EXPLOSION_RADIUS * 2;
                world.addParticle(ParticleTypes.EXPLOSION, px, py, pz, 0.0, 0.0, 0.0);
            }
        }

        this.discard();
    }
}
