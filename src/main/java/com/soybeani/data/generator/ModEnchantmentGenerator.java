package com.soybeani.data.generator;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2024/11/13 11:52
 * @description
 */
public class ModEnchantmentGenerator extends FabricDynamicRegistryProvider {

    public ModEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<Item> itemLookup = registries.getWrapperOrThrow(RegistryKeys.ITEM);
//        register(entries, EnchantmentRegister.SNATCH,Enchantment.builder(
//           Enchantment.definition(
//                   itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
//                   15, //probability
//                   1, //max level
//                    Enchantment.leveledCost(1,10), //cost per level(base)
//                    Enchantment.leveledCost(1,15),  //cost per level(max)
//                    7, //anvil cost
//                   AttributeModifierSlot.HAND
//           )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
//                EnchantmentEffectTarget.ATTACKER,
//                EnchantmentEffectTarget.VICTIM,
//                new SnatchEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f))));
//        register(entries, EnchantmentRegister.FLAME_ADDITION,Enchantment.builder(
//                Enchantment.definition(
//                        itemLookup.getOrThrow(ItemTags.PICKAXES),
//                        15, //probability
//                        1, //max level
//                        Enchantment.leveledCost(1,10), //cost per level(base)
//                        Enchantment.leveledCost(1,15),  //cost per level(max)
//                        7, //anvil cost
//                        AttributeModifierSlot.HAND
//                )).addEffect(EnchantmentEffectComponentTypes.HIT_BLOCK,
//                new FlameAdditionEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f))));
//        register(entries, EnchantmentRegister.SWORD_AURA,Enchantment.builder(
//                Enchantment.definition(
//                        itemLookup.getOrThrow(ItemTags.SWORDS),
//                        15, //probability
//                        1, //max level
//                        Enchantment.leveledCost(1,10), //cost per level(base)
//                        Enchantment.leveledCost(1,15),  //cost per level(max)
//                        10, //anvil cost
//                        AttributeModifierSlot.HAND
//                )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
//                EnchantmentEffectTarget.ATTACKER,
//                EnchantmentEffectTarget.VICTIM,
//                new SwordAuraEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f))));
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions){
        entries.add(key, builder.build(key.getValue()),resourceConditions);
    }

    @Override
    public String getName() {
        return "Enchantment Generator";
    }
}
