package com.soybeani.data.provider;

import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

/**
 * @author soybean
 * @date 2024/11/18 11:08
 * @description
 */
public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemsRegister.EMERALD_APPLE, Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LAPIS_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.REDSTONE_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DETECT_STAFF,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.GOLDEN_DETECT_STAFF,Models.HANDHELD);
    }
}
