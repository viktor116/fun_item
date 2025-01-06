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
    private final Identifier TEMPLATE_SPAWN_EGG = Identifier.ofVanilla("item/template_spawn_egg");

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
        itemModelGenerator.register(ItemsRegister.MINECART_LIVE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.OAK_BOAT_LIVE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_BLACK_PURPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_BLUE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_GREEN,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_YELLOW_RED,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_PURPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_DARKGREEN,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_PINK,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_YELLOW,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_SKYBLUE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TALISMAN_GREY,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COPPER_BULLET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.IRON_BULLET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.PREGNANT_SPYGLASS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TNT_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.BOTTOM_ICE_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TELEPORT_CRYSTAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TP_CRYSTAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.JINGDOU_CLOUD,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TNT_BATON,Models.HANDHELD);

        itemModelGenerator.register(ItemsRegister.SKELETON_ZOMBIE_SPAWN_EGG,new Model(Optional.of(TEMPLATE_SPAWN_EGG),Optional.empty()));
        itemModelGenerator.register(ItemsRegister.CREEPER_SKELETON_SPAWN_EGG,new Model(Optional.of(TEMPLATE_SPAWN_EGG),Optional.empty()));
        itemModelGenerator.register(ItemsRegister.ZOMBIE_CREEPER_SPAWN_EGG,new Model(Optional.of(TEMPLATE_SPAWN_EGG),Optional.empty()));

        //ModelIds.getMinecraftNamespacedItem("hay_block");
        itemModelGenerator.register(ItemsRegister.HAY_BLOCK_LIVE, new Model(Optional.of(Identifier.ofVanilla("block/hay_block")), Optional.empty()));
        itemModelGenerator.register(ItemsRegister.DIAMOND_ORE_LIVE, new Model(Optional.of(Identifier.ofVanilla("block/diamond_ore")), Optional.empty()));
        itemModelGenerator.register(ItemsRegister.DIAMOND_ORE_PLANT, new Model(Optional.of(Identifier.ofVanilla("block/diamond_ore")), Optional.empty()));
        itemModelGenerator.register(ItemsRegister.DIAMOND_SWORD_PLANT, new Model(Optional.of(Identifier.ofVanilla("item/diamond")), Optional.empty()));
    }
}
