package com.soybeani.data.provider;

import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2024/11/16 14:14
 * @description
 */
public class ModItemTagProvider extends FabricTagProvider<Item> {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ItemsRegister.WHEAT_SWORD)
                .add(ItemsRegister.GRASS_SWORD2)
                .add(ItemsRegister.GRASS_SWORD);
        getOrCreateTagBuilder(ItemTags.BOATS)
                .add(ItemsRegister.ICE_BOAT)
                .add(ItemsRegister.ICE2_BOAT)
                .add(ItemsRegister.FLY_BOAT);
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemsRegister.AIR_PICKAXE);


    }
}
