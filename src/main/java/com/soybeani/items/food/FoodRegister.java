package com.soybeani.items.food;

import com.soybeani.items.ItemsRegister;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author soybean
 * @date 2024/11/18 10:37
 * @description
 */
public class  FoodRegister {
    public static final FoodComponent EMERALD_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(ItemsRegister.EMERLD_EFFECT_ENTRY, 1200, 1), 1.0f).alwaysEdible().build();
    public static final FoodComponent LAPIS_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(ItemsRegister.LAPIS_EFFECT_ENTRY, 1200, 1), 1.0f).alwaysEdible().build();
    public static final FoodComponent REDSTONE_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(ItemsRegister.RED_STONE_EFFECT_ENTRY, 1200, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 72000, 1),1.0f).alwaysEdible().build();

}
