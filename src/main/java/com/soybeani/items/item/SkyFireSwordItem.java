package com.soybeani.items.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 16:02
 * @description
 */
public class SkyFireSwordItem extends Item {
    private static final int FIRE_DURATION = 100;

    public SkyFireSwordItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));

            // 判断玩家是否正在右键长按
            if (remainingUseTicks < 20) {  // 根据实际需要调整此值
                this.launchFireballs(playerEntity, world);
            }
        }
    }

    // 发射火焰粒子并造成伤害
    private void launchFireballs(PlayerEntity playerEntity, World world) {
        // 获取玩家视角方向
        Vec3d lookDirection = playerEntity.getRotationVector();

        // 发射大量火焰粒子
        for (int i = 0; i < 10; i++) {  // 控制发射的粒子数量
            double offsetX = (world.getRandom().nextDouble() - 0.5) * 2.0;  // 轻微随机偏移
            double offsetY = (world.getRandom().nextDouble() - 0.5) * 2.0;
            double offsetZ = (world.getRandom().nextDouble() - 0.5) * 2.0;

            // 在玩家前方生成火焰粒子
            world.addParticle(ParticleTypes.FLAME,
                    playerEntity.getX() + offsetX,
                    playerEntity.getY() + 1.5 + offsetY,
                    playerEntity.getZ() + offsetZ,
                    lookDirection.x, lookDirection.y, lookDirection.z);
        }

        // 造成火焰伤害并点燃前方的实体
        Vec3d playerPos = playerEntity.getPos();
        Vec3d direction = lookDirection.multiply(1.5);  // 控制火焰的射程

        // 检测玩家前方的一段距离内的实体
        for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class,
                playerEntity.getBoundingBox().stretch(direction.x, direction.y, direction.z),
                e -> e != playerEntity)) {

            // 判断实体是否在火焰射线范围内
            if (entity.getBoundingBox().intersects(Box.from(playerPos.add(direction)))) {
                entity.setOnFireFor(FIRE_DURATION);  // 设置实体燃烧
                entity.damage(playerEntity.getDamageSources().inFire(), 5.0f);  // 造成火焰伤害（这里伤害值可以根据需要调整）
            }
        }
    }
}
