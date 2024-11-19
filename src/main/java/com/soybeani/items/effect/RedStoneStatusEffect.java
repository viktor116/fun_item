package com.soybeani.items.effect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/11/19 17:22
 * @description
 */
public class RedStoneStatusEffect extends StatusEffect {
    public RedStoneStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0xFF0000);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        BlockPos blockPos = entity.getBlockPos().down();
        World world = entity.getWorld();
        BlockState blockState = Blocks.REDSTONE_WIRE.getDefaultState();
        if(world.getBlockState(blockPos.up()) == Blocks.AIR.getDefaultState() && world.getBlockState(blockPos) != Blocks.AIR.getDefaultState()) {
            world.setBlockState(blockPos.up(),blockState);
        }
        return true;
    }
}
