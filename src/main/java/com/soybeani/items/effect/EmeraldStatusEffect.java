package com.soybeani.items.effect;

import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/19 17:22
 * @description
 */
public class EmeraldStatusEffect extends StatusEffect {
    public EmeraldStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0x00FF00);
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

    public static boolean hasEffect(PlayerEntity player) {
        return player.hasStatusEffect(ItemsRegister.EMERLD_EFFECT_ENTRY);
    }

    public static void EventRegister(PlayerEntity player , World world, Entity entity){
        if (hasEffect(player) && !world.isClient && entity instanceof LivingEntity livingEntity) {
            ItemEntity emeraldDrop = new ItemEntity(world,
                    livingEntity.getX(),
                    livingEntity.getY(),
                    livingEntity.getZ(),
                    new ItemStack(Items.EMERALD));
            world.spawnEntity(emeraldDrop);
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ARMADILLO_SCUTE_DROP, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }
}
