package com.soybeani.items.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author soybean
 * @date 2025/1/6 16:02
 * @description
 */
public class SkyFireSwordItem extends SwordItem {
    private static final int FIRE_DURATION = 100;

    public SkyFireSwordItem(Settings settings) {
        super(ToolMaterials.NETHERITE,settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            player.incrementStat(Stats.USED.getOrCreateStat(this));
            if (player.isUsingItem() && player.getActiveItem() == stack) {
                stack.damage(1, player, EquipmentSlot.MAINHAND);
                this.launchFireballs(player, world);
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = target.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            // 设置目标的燃烧时间
            target.setOnFireFor(5);

            // 在目标周围生成火焰粒子
            serverWorld.spawnParticles(
                    ParticleTypes.FLAME,
                    target.getX(),          // X 坐标
                    target.getY() + 1.0,    // Y 坐标，稍微抬高一点
                    target.getZ(),          // Z 坐标
                    100,                     // 粒子数量
                    3.0,                    // X方向扩散范围
                    2.0,                    // Y方向扩散范围
                    3.0,                    // Z方向扩散范围
                    0.02                    // 粒子速度
            );

            // 播放火焰音效
            world.playSound(
                    null,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    SoundEvents.ENTITY_GENERIC_BURN,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F
            );

            // 检测范围内的其他实体并给予燃烧效果
            Box detectionBox = Box.from(target.getPos()).expand(3, 2, 3);
            List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                    LivingEntity.class,
                    detectionBox,
                    entity -> entity != target && entity != attacker
            );

            for (LivingEntity entity : nearbyEntities) {
                entity.setOnFireFor(3);
                entity.damage(attacker.getDamageSources().onFire(), 2.0f);
            }
        }

        return super.postHit(stack, target, attacker);
    }
    private void launchFireballs(PlayerEntity playerEntity, World world) {
        // 获取玩家视角方向
        Vec3d lookDirection = playerEntity.getRotationVector();
        Vec3d playerPos = playerEntity.getPos();


        world.playSound(
                null, // null 表示所有玩家都能听到
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundEvents.ITEM_FIRECHARGE_USE, // 火焰发射音效
                SoundCategory.PLAYERS,
                1.0F, // 音量
                1.0F  // 音调
        );

        // 存储所有火焰粒子的路径点
        List<Vec3d> pathPoints = new ArrayList<>();
        double pathStepLength = 1.0; // 每隔1格检查一次
        int maxDistance = 10; // 最大射程

        // 发射大量火焰粒子
        for (int i = 0; i < 10; i++) {
            double offsetX = (world.getRandom().nextDouble() - 0.5) * 5.0;
            double offsetY = (world.getRandom().nextDouble() - 0.5) * 2.0;
            double offsetZ = (world.getRandom().nextDouble() - 0.5) * 5.0;

            // 计算这个粒子的起始位置
            Vec3d startPos = new Vec3d(
                    playerEntity.getX() + offsetX,
                    playerEntity.getY() + 1.5 + offsetY,
                    playerEntity.getZ() + offsetZ
            );

            // 为每个粒子生成路径点
            for (double step = 0; step <= maxDistance; step += pathStepLength) {
                Vec3d pathPoint = new Vec3d(
                        startPos.x + lookDirection.x * step,
                        startPos.y + lookDirection.y * step,
                        startPos.z + lookDirection.z * step
                );
                pathPoints.add(pathPoint);
            }

            // 生成火焰粒子
            world.addParticle(
                    ParticleTypes.FLAME,
                    startPos.x,
                    startPos.y,
                    startPos.z,
                    lookDirection.x,
                    lookDirection.y,
                    lookDirection.z
            );
        }

        // 检测范围内的实体
        double checkRadius = 2.0; // 每个检查点的检测半径

        // 获取可能受影响的实体
        Box totalArea = Box.from(playerPos).expand(15, 15, 15);
        List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                LivingEntity.class,
                totalArea,
                e -> e != playerEntity
        );

        // 用于记录已经受到伤害的实体，防止重复伤害
        Set<LivingEntity> damagedEntities = new HashSet<>();

        // 检查每个实体是否在任何路径点的范围内
        for (LivingEntity entity : nearbyEntities) {
            if (damagedEntities.contains(entity)) {
                continue;
            }

            for (Vec3d pathPoint : pathPoints) {
                // 检查实体是否在检查点的范围内
                if (entity.getBoundingBox().intersects(
                        Box.from(pathPoint).expand(checkRadius)
                )) {
                    entity.setOnFireFor(FIRE_DURATION);
                    entity.damage(playerEntity.getDamageSources().inFire(), 5.0f);
                    damagedEntities.add(entity);
                    break;
                }
            }
        }
    }
}
