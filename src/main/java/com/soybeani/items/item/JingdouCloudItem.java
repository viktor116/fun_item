package com.soybeani.items.item;

import com.soybeani.items.ItemsRegister;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 15:53
 * @description
 */
public class JingdouCloudItem extends Item {
    public JingdouCloudItem(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.addStatusEffect(new StatusEffectInstance(ItemsRegister.JING_DOU_CLOUD_EFFECT_ENTRY, 20 * 30, 0));
        if(!user.isInCreativeMode()) user.getStackInHand(hand).decrement(1);
        user.getItemCooldownManager().set(this, 20 * 20);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
