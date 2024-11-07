package com.soybeani.items;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.custom.PurpleLightningEntity;
import com.soybeani.items.item.*;
import com.soybeani.items.material.GrassMaterial;
import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


/**
 * @author soybean
 * @date 2024/10/8 15:44
 * @description
 */
public class ItemsRegister {

//    public static final Item GLASS_SWORD = register(new Item(new Item.Settings()), "grass_sword");
    public static final Item GRASS_SWORD = register(new SwordItemOfGrass(new GrassMaterial(5,null),new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 0, -3.0F))), "grass_sword");
    public static final Item GRASS_SWORD2 = register(new SwordItemOfGrass(GrassMaterial.INSTANCE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 2, -2.4F))), "grass_sword2");
    public static final Item WHEAT_SWORD = register(new SwordItemOfGrass(new GrassMaterial(20,Items.WHEAT),new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(GrassMaterial.INSTANCE, 4, -2.6F))), "wheat_sword");
    public static final Item XIAOMI14 = register(new PhoneItem(new Item.Settings()), "xiaomi14");
    public static final Item XIAOMI14BUTTON = register(new ButtonItem(new Item.Settings()), "xiaomi14button");
    public static final Item XIAOMI_SU7_EGG = register(new SpawnEggItem(EntityRegister.SU7, 0x576A3C, 0x0000FF, new Item.Settings()), "su7_spawn_egg");
    public static final Item ICE_BOAT = register(new IceBoatItem(new Item.Settings().maxCount(1)), "ice_boat");
    public static final Item ICE2_BOAT = register(new Ice2BoatItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "ice2_boat");
    public static final Item FLY_BOAT = register(new FlyBoatItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)), "fly_boat");
    public static final Item LIGHTNING_SPYGLASS = register(new LightningSpyglassItem(new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON)),"lightning_spyglass");
    public static final Item NIRVANA_SPYGLASS = register(new NirvanaSpyglassItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)),"nirvana_spyglass");
    public static final Item PURPLE_LIGHTNING = Registry.register(
            Registries.ITEM,
            Identifier.of(InitValue.MOD_ID, "purple_lightning"),
            new Item(new Item.Settings()
                    .maxCount(64) // 可以堆叠
                    .rarity(Rarity.RARE)) // 设置为稀有物品
    );
       //    public static final ModelIdentifier LIGHTNING_SPYGLASS_IN_HAND = ModelIdentifier.ofInventoryVariant(Identifier.of(InitValue.MOD_ID, "lightning_spyglass_in_hand"));
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, COMMON_ITEM_GROUP_KEY, COMMON_CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(FUN_ITEM_GROUP_KEY).register(itemGroup->{ //有趣物品
            itemGroup.add(XIAOMI14);
            itemGroup.add(XIAOMI14BUTTON);
            itemGroup.add(XIAOMI_SU7_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(COMMON_ITEM_GROUP_KEY).register(itemGroup->{ //普通物品
            itemGroup.add(GRASS_SWORD);
            itemGroup.add(GRASS_SWORD2);
            itemGroup.add(WHEAT_SWORD);
            itemGroup.add(ICE_BOAT);
            itemGroup.add(ICE2_BOAT);
            itemGroup.add(FLY_BOAT);
            itemGroup.add(LIGHTNING_SPYGLASS);
            itemGroup.add(NIRVANA_SPYGLASS);
            itemGroup.add(PURPLE_LIGHTNING);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> FUN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final RegistryKey<ItemGroup> COMMON_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "common_item_group"));
    public static final ItemGroup ABSTRACT_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.XIAOMI14BUTTON))
            .displayName(Text.translatable("itemGroup.fun_item"))
            .build();

    public static final ItemGroup COMMON_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.WHEAT_SWORD))
            .displayName(Text.translatable("itemGroup.fun_item.common"))
            .build();


    //通用注册
    public static Item register(Item item,String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
