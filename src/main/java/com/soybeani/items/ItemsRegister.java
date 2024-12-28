package com.soybeani.items;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.CreeperSkeletonEntity;
import com.soybeani.entity.custom.SkeletonZombieEntity;
import com.soybeani.entity.custom.ZombieCreeperEntity;
import com.soybeani.items.effect.EmeraldStatusEffect;
import com.soybeani.items.effect.LapisStatusEffect;
import com.soybeani.items.effect.RedStoneStatusEffect;
import com.soybeani.items.food.FoodRegister;
import com.soybeani.items.item.*;
import com.soybeani.items.item.MinecartItem;
import com.soybeani.items.material.AirMaterial;
import com.soybeani.items.material.GrassMaterial;
import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
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
    public static final Item WHEAT_LIVE = register(new WheatItem(new Item.Settings()),"wheat");
    public static final Item COW_PLANT = register(new AliasedBlockItem(ModBlock.COW_PLANT,new Item.Settings()),"cow_plant");
    public static final Item PIG_PLANT = register(new AliasedBlockItem(ModBlock.PIG_PLANT,new Item.Settings()),"pig_plant");
    public static final Item ZOMBIE_PLANT = register(new AliasedBlockItem(ModBlock.ZOMBIE_PLANT,new Item.Settings()),"zombie_plant");
    public static final Item DIAMOND_ORE_PLANT = register(new AliasedBlockItem(ModBlock.DIAMOND_ORE_PLANT,new Item.Settings()),"diamond_ore_plant");
    public static final Item DIAMOND_SWORD_PLANT = register(new AliasedBlockItem(ModBlock.DIAMOND_SWORD_PLANT,new Item.Settings()),"diamond_sword_plant");
    public static final Item MINECART_LIVE = register(new MinecartItem(new Item.Settings()),"minecart_live");
    public static final Item OAK_BOAT_LIVE = register(new OakBoatItem(new Item.Settings()),"oak_boat_live");
    public static final Item HAY_BLOCK_LIVE = register(new HayBlockEntityItem(new Item.Settings()),"hay_block_live");
    public static final Item DIAMOND_ORE_LIVE = register(new DiamondOreEntityItem(new Item.Settings()), "diamond_ore_live");
    public static final Item SKELETON_ZOMBIE_SPAWN_EGG = register(new SpawnEggItem(SkeletonZombieEntity.SKELETON_ZOMBIE, 0xC1C1C1, 0x494949, new Item.Settings()),"skeleton_zombie_spawn_egg");
    public static final Item CREEPER_SKELETON_SPAWN_EGG = register(new SpawnEggItem(CreeperSkeletonEntity.CREEPER_SKELETON, 894731, 0, new Item.Settings()),"creeper_skeleton_spawn_egg");
    public static final Item ZOMBIE_CREEPER_SPAWN_EGG = register(new SpawnEggItem(ZombieCreeperEntity.ZOMBIE_CREEPER, 44975, 7969893, new Item.Settings()),"zombie_creeper_spawn_egg");
    public static final Item TALISMAN = register(new TalismanItem(new Item.Settings(), TalismanItem.Type.NONE),"talisman"); //剑气
    public static final Item TALISMAN_BLACK_PURPLE= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.BLACK_PURPLE),"talisman_black_purple"); //死亡阴影
    public static final Item TALISMAN_BLUE= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.BLUE),"talisman_blue"); //雷法
    public static final Item TALISMAN_GREEN= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.GREEN),"talisman_green"); //道法自然
    public static final Item TALISMAN_YELLOW_RED= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.YELLOW_RED),"talisman_yellow_red"); //点燃
    public static final Item TALISMAN_PURPLE= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.PURPLE),"talisman_purple"); //血魔
    public static final Item TALISMAN_DARKGREEN= register(new TalismanItem(new Item.Settings(), TalismanItem.Type.DARKGREEN),"talisman_darkgreen"); //剑魁
    public static final Item TALISMAN_PINK = register(new TalismanItem(new Item.Settings(), TalismanItem.Type.PINK),"talisman_pink"); //避邪
    public static final Item TALISMAN_YELLOW = register(new TalismanItem(new Item.Settings(), TalismanItem.Type.YELLOW),"talisman_yellow"); //大人时代变了
    public static final Item TALISMAN_SKYBLUE = register(new TalismanItem(new Item.Settings(), TalismanItem.Type.SKYBLUE),"talisman_skyblue"); //飞行
    public static final Item TALISMAN_GREY = register(new TalismanItem(new Item.Settings(), TalismanItem.Type.GREY),"talisman_grey"); //凋零之力
    public static final Item GATLING_GUN = register(new GatlingItem(new Item.Settings().maxDamage(1000).maxCount(1)),"gatling_gun");
    public static final Item COPPER_BULLET= register(new BulletItem(new Item.Settings()),"copper_bullet");
    public static final Item IRON_BULLET= register(new BulletItem(new Item.Settings()),"iron_bullet");
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, COMMON_ITEM_GROUP_KEY, COMMON_CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(FUN_ITEM_GROUP_KEY).register(itemGroup->{ //生物
            itemGroup.add(WHEAT_LIVE);
            itemGroup.add(COW_PLANT);
            itemGroup.add(PIG_PLANT);
            itemGroup.add(ZOMBIE_PLANT);
            itemGroup.add(MINECART_LIVE);
            itemGroup.add(OAK_BOAT_LIVE);
            itemGroup.add(HAY_BLOCK_LIVE);
            itemGroup.add(DIAMOND_ORE_LIVE);
            itemGroup.add(DIAMOND_ORE_PLANT);
            itemGroup.add(SKELETON_ZOMBIE_SPAWN_EGG);
            itemGroup.add(CREEPER_SKELETON_SPAWN_EGG);
            itemGroup.add(ZOMBIE_CREEPER_SPAWN_EGG);
            itemGroup.add(DIAMOND_SWORD_PLANT);
        });
        ItemGroupEvents.modifyEntriesEvent(COMMON_ITEM_GROUP_KEY).register(itemGroup->{ //普通物品
            itemGroup.add(AIR_PICKAXE);
            itemGroup.add(GRASS_SWORD);
            itemGroup.add(GRASS_SWORD2);
            itemGroup.add(WHEAT_SWORD);
            itemGroup.add(XIAOMI14);
            itemGroup.add(XIAOMI14BUTTON);
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
            itemGroup.add(TALISMAN);
            itemGroup.add(TALISMAN_BLACK_PURPLE);
            itemGroup.add(TALISMAN_BLUE);
            itemGroup.add(TALISMAN_GREEN);
            itemGroup.add(TALISMAN_YELLOW_RED);
            itemGroup.add(TALISMAN_PURPLE);
            itemGroup.add(TALISMAN_DARKGREEN);
            itemGroup.add(TALISMAN_PINK);
            itemGroup.add(TALISMAN_YELLOW);
            itemGroup.add(TALISMAN_SKYBLUE);
            itemGroup.add(TALISMAN_GREY);
            itemGroup.add(GATLING_GUN);
            itemGroup.add(COPPER_BULLET);
            itemGroup.add(IRON_BULLET);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> FUN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final RegistryKey<ItemGroup> COMMON_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "common_item_group"));
    public static final ItemGroup ABSTRACT_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.WHEAT_LIVE))
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
