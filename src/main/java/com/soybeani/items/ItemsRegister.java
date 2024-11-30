package com.soybeani.items;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.items.effect.EmeraldStatusEffect;
import com.soybeani.items.effect.LapisStatusEffect;
import com.soybeani.items.effect.RedStoneStatusEffect;
import com.soybeani.items.food.FoodRegister;
import com.soybeani.items.item.*;
import com.soybeani.items.material.AirMaterial;
import com.soybeani.items.material.GrassMaterial;
import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;



/**
 * @author soybean
 * @date 2024/10/8 15:44
 * @description
 */
public class ItemsRegister {
    public static final RegistryKey<StatusEffect> EMERALD_EFFECT_KEY = register("emerald_effect");
    public static final RegistryKey<StatusEffect> LAPIS_EFFECT_KEY = register("lapis_effect");
    public static final RegistryKey<StatusEffect> RED_STONE_EFFECT_KEY = register("redstone_effect");
    public static final StatusEffect EMERALD_EFFECT = register(new EmeraldStatusEffect(),EMERALD_EFFECT_KEY);
    public static final StatusEffect LAPIS_EFFECT = register(new LapisStatusEffect(),LAPIS_EFFECT_KEY);
    public static final StatusEffect RED_STONE_EFFECT = register(new RedStoneStatusEffect(),RED_STONE_EFFECT_KEY);
    public static final RegistryEntry<StatusEffect> EMERLD_EFFECT_ENTRY = Registries.STATUS_EFFECT.getEntry(EMERALD_EFFECT_KEY).orElseThrow(() -> new IllegalStateException("Red Stone Effect not registered"));
    public static final RegistryEntry<StatusEffect> LAPIS_EFFECT_ENTRY = Registries.STATUS_EFFECT.getEntry(LAPIS_EFFECT_KEY).orElseThrow(() -> new IllegalStateException("Red Stone Effect not registered"));
    public static final RegistryEntry<StatusEffect> RED_STONE_EFFECT_ENTRY = Registries.STATUS_EFFECT.getEntry(RED_STONE_EFFECT_KEY).orElseThrow(() -> new IllegalStateException("Red Stone Effect not registered"));
    public static final Item GRASS_SWORD = register(new SwordItemOfGrass(new GrassMaterial(5,null),new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 0, -3.0F))), "grass_sword");
    public static final Item GRASS_SWORD2 = register(new SwordItemOfGrass(GrassMaterial.INSTANCE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 2, -2.4F))), "grass_sword2");
    public static final Item WHEAT_SWORD = register(new SwordItemOfGrass(new GrassMaterial(20,Items.WHEAT),new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 4, -2.6F))), "wheat_sword");
    public static final Item XIAOMI14 = register(new PhoneItem(new Item.Settings()), "xiaomi14");
    public static final Item XIAOMI14BUTTON = register(new ButtonItem(new Item.Settings()), "xiaomi14button");
    public static final Item ICE_BOAT = register(new IceBoatItem(new Item.Settings().maxCount(1)), "ice_boat");
    public static final Item ICE2_BOAT = register(new Ice2BoatItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "ice2_boat");
    public static final Item FLY_BOAT = register(new FlyBoatItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "fly_boat");
    public static final Item LIGHTNING_SPYGLASS = register(new LightningSpyglassItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)),"lightning_spyglass");
    public static final Item NIRVANA_SPYGLASS = register(new NirvanaSpyglassItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)),"nirvana_spyglass");
    public static final Item PURPLE_LIGHTNING = register(new PurpleLightningItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE)),"purple_lightning");
    public static final Item LIGHTNING =  register(new LightningItem(new Item.Settings().maxCount(16)),"lightning");
    public static final Item AIR_PICKAXE = register(new UnbreakablePickaxeItem(AirMaterial.INSTANCE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(AirMaterial.INSTANCE, 9999.0F, 9999F)).maxCount(1).maxDamage(-1)),"air_pickaxe");
    public static final Item EMERALD_APPLE = register(new Item(new Item.Settings().rarity(Rarity.EPIC).food(FoodRegister.EMERALD_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)),"emerald_apple");
    public static final Item LAPIS_APPLE = register(new Item(new Item.Settings().rarity(Rarity.EPIC).food(FoodRegister.LAPIS_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)),"lapis_apple");
    public static final Item REDSTONE_APPLE = register(new Item(new Item.Settings().rarity(Rarity.EPIC).food(FoodRegister.REDSTONE_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)),"redstone_apple");
    public static final Item DETECT_STAFF = register(new DetectStaffItem(new Item.Settings(),false),"detect_staff");
    public static final Item GOLDEN_DETECT_STAFF = register(new DetectStaffItem(new Item.Settings(),true),"golden_detect_staff");
    public static final Item FLYING_STICK = register(new FlyingStickItem(new Item.Settings().maxCount(1)),"flying_stick");
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, COMMON_ITEM_GROUP_KEY, COMMON_CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(FUN_ITEM_GROUP_KEY).register(itemGroup->{ //有趣物品
            itemGroup.add(XIAOMI14);
            itemGroup.add(XIAOMI14BUTTON);
        });
        ItemGroupEvents.modifyEntriesEvent(COMMON_ITEM_GROUP_KEY).register(itemGroup->{ //普通物品
            itemGroup.add(AIR_PICKAXE);
            itemGroup.add(GRASS_SWORD);
            itemGroup.add(GRASS_SWORD2);
            itemGroup.add(WHEAT_SWORD);
            itemGroup.add(ICE_BOAT);
            itemGroup.add(ICE2_BOAT);
            itemGroup.add(FLY_BOAT);
            itemGroup.add(LIGHTNING_SPYGLASS);
            itemGroup.add(NIRVANA_SPYGLASS);
            itemGroup.add(LIGHTNING);
            itemGroup.add(PURPLE_LIGHTNING);
            itemGroup.add(ModBlock.SUPER_SLIME_BLOCK.asItem());
            itemGroup.add(ModBlock.SUPER_SLIME_BLOCK_MAX.asItem());
            itemGroup.add(EMERALD_APPLE);
            itemGroup.add(LAPIS_APPLE);
            itemGroup.add(REDSTONE_APPLE);
            itemGroup.add(DETECT_STAFF);
            itemGroup.add(GOLDEN_DETECT_STAFF);
            itemGroup.add(FLYING_STICK);
            itemGroup.add(ModBlock.TT_BLOCK);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> FUN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final RegistryKey<ItemGroup> COMMON_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "common_item_group"));
    public static final ItemGroup ABSTRACT_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.XIAOMI14BUTTON))
            .displayName(Text.translatable("itemGroup."+InitValue.MOD_ID))
            .build();

    public static final ItemGroup COMMON_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.WHEAT_SWORD))
            .displayName(Text.translatable("itemGroup."+InitValue.MOD_ID+".common"))
            .build();


    //通用注册
    public static Item register(Item item,String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static RegistryKey<StatusEffect> register(String id){
        Identifier statusID = Identifier.of(InitValue.MOD_ID, id);
        return RegistryKey.of(RegistryKeys.STATUS_EFFECT, statusID);
    }

    public static StatusEffect register(StatusEffect statusEffect,RegistryKey<StatusEffect> key){
        return Registry.register(Registries.STATUS_EFFECT, key.getValue(), statusEffect);
    }
    public static RegistryEntry<StatusEffect> effectGetEntry(StatusEffect statusEffect){

        return RegistryEntry.of(statusEffect);
    }
}
