package com.soybeani.items.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author soybean
 * @date 2024/11/19 17:22
 * @description
 */
public class LapisStatusEffect extends StatusEffect {
    public LapisStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0x0000FF);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).addExperience(1 << amplifier); // Higher amplifier gives you EXP faster
        }
        return true;
    }
}
