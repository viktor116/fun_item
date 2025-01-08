package com.soybeani.items.item;

import com.soybeani.utils.CommonUtils;
import com.soybeani.utils.RayCastUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 15:54
 * @description
 */
public class TeleportCrystalItem extends Item {
    public TeleportCrystalItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Entity targetEntity = RayCastUtils.getTargetEntity(world, user, 200);
        if (targetEntity != null) {
            Vec3d targetPos = targetEntity.getPos();
            Entity targetVehicle = targetEntity.hasVehicle() ? targetEntity.getVehicle() : null;
            Entity userVehicle = user.hasVehicle() ? user.getVehicle() : null;

            // 如果目标实体有载具，移除目标实体的载具并将玩家设置为载具
            if (targetVehicle != null) {
                targetEntity.stopRiding();
            }

            // 交换目标实体的位置
            targetEntity.setPosition(user.getPos());

            // 将目标实体的乘坐者切换为玩家
            if (userVehicle != null) {
                user.stopRiding();
                targetEntity.startRiding(userVehicle);
            }

            // 交换玩家的位置
            user.setPosition(targetPos);

            // 如果玩家有载具，移除玩家的载具并将目标实体设置为载具
            if (targetVehicle != null) {
                user.stopRiding();
                targetEntity.startRiding(targetVehicle);
            }

            // 播放传送声音效果
            world.playSound(null, targetPos.x, targetPos.y, targetPos.z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);

            // 在服务器上生成粒子效果
            if (world instanceof ServerWorld serverWorld) {
                CommonUtils.spawnSelfParticle(serverWorld, user, ParticleTypes.PORTAL);
                CommonUtils.spawnSelfParticle(serverWorld, targetEntity, ParticleTypes.PORTAL);
                if (targetVehicle != null) {
                    CommonUtils.spawnSelfParticle(serverWorld, targetVehicle, ParticleTypes.PORTAL);
                }
                if (userVehicle != null) {
                    CommonUtils.spawnSelfParticle(serverWorld, userVehicle, ParticleTypes.PORTAL);
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
