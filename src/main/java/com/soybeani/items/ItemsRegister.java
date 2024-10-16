package com.soybeani.items;

import com.soybeani.config.InitValue;
import com.soybeani.items.item.ButtonItem;
import com.soybeani.items.item.PhoneItem;
import com.soybeani.items.material.GrassMaterial;
import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup->{
            itemGroup.add(GRASS_SWORD);
            itemGroup.add(GRASS_SWORD2);
            itemGroup.add(WHEAT_SWORD);
            itemGroup.add(XIAOMI14);
            itemGroup.add(XIAOMI14BUTTON);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ItemsRegister.GRASS_SWORD2))
            .displayName(Text.translatable("itemGroup.fun_item"))
            .build();

    //通用注册
    public static Item register(Item item,String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }
}
