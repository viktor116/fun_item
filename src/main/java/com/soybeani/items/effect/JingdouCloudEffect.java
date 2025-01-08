package com.soybeani.items.effect;

import com.soybeani.items.ItemsRegister;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class JingdouCloudEffect extends StatusEffect {
    public JingdouCloudEffect() {
        super(StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0xFFFFFF);
    }
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            int jumpBoostLevel = amplifier + 49;
            int speedLevel = amplifier + 49;

            // 添加跳跃力和速度效果
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 3, jumpBoostLevel, false, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 3, speedLevel, false, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 3, 0, false, false, false));

            // 在玩家脚底生成烟雾粒子
            if (player.getWorld() instanceof ServerWorld serverWorld) {
                double posX = player.getX();
                double posY = player.getY() - 0.5; // 使粒子出现在脚底位置
                if(player.hasVehicle()){
                    posY++;
                }
                double posZ = player.getZ();

                // 在玩家脚底生成大量的烟雾粒子
                for (int i = 0; i < 5; i++) {
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, posX, posY, posZ, 10, 0.2, 0.1, 0.2, 0.1); // 产生烟雾
                }
            }
        }
        return true;
    }

    public static boolean hasEffect(PlayerEntity player) {
        return player.hasStatusEffect(ItemsRegister.JING_DOU_CLOUD_EFFECT_ENTRY);
    }
}
