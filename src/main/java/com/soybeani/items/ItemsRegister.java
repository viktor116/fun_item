package com.soybeani.items;

import com.soybeani.config.InitValue;
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
    public static final Item GLASS_SWORD = register(new SwordItem(ToolMaterials.GOLD,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))), "grass_sword");

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register(itemGroup->{
            itemGroup.add(GLASS_SWORD);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.DIAMOND_AXE))
            .displayName(Text.translatable("itemGroup.fun_item"))
            .build();

    //通用注册
    public static Item register(Item item,String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        Item registeredItem = Registry.register(Registries.ITEM, itemID, item);
        return registeredItem;
    }
}
