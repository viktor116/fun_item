package com.soybeani.data.provider;

import com.soybeani.block.ModBlock;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

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
        blockStateModelGenerator.registerSingleton(ModBlock.TT_BLOCK, TexturedModel.CUBE_BOTTOM_TOP);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemsRegister.EMERALD_APPLE, Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LAPIS_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.REDSTONE_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DETECT_STAFF,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.GOLDEN_DETECT_STAFF,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.FLYING_STICK,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.WHEAT_LIVE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COW_PLANT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.PIG_PLANT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ZOMBIE_PLANT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIAMOND_SWORD_PLANT,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.MINECART_LIVE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.OAK_BOAT_LIVE,Models.GENERATED);

        //ModelIds.getMinecraftNamespacedItem("hay_block");
        itemModelGenerator.register(ItemsRegister.HAY_BLOCK_LIVE, new Model(Optional.of(Identifier.ofVanilla("block/hay_block")), Optional.empty()));
        itemModelGenerator.register(ItemsRegister.DIAMOND_ORE_LIVE, new Model(Optional.of(Identifier.ofVanilla("block/diamond_ore")), Optional.empty()));
    }
}
