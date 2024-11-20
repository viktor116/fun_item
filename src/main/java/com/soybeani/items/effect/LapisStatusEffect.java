package com.soybeani.items.effect;

import com.soybeani.items.ItemsRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

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
       return super.applyUpdateEffect(entity,amplifier);
    }

    public static boolean hasEffect(PlayerEntity player) {
        return player.hasStatusEffect(ItemsRegister.LAPIS_EFFECT_ENTRY);
    }

    public static void EventRegister(PlayerEntity player, BlockEntity entity, World world, BlockState state, BlockPos pos){
        if (LapisStatusEffect.hasEffect(player) && !player.isCreative()) {
            // 获取方块的掉落物
            List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld)world, pos, entity, player, player.getMainHandStack());

            // 复制一份掉落物
            for (ItemStack drop : drops) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), drop.copy());
            }
        }

    }
}
