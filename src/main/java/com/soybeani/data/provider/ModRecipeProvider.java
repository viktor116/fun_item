package com.soybeani.data.provider;

import com.soybeani.block.ModBlock;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2024/11/18 11:58
 * @description
 */
public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        //史莱姆方块
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlock.SUPER_SLIME_BLOCK)
                .input('E', Blocks.SLIME_BLOCK)
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .criterion(hasItem(Blocks.SLIME_BLOCK), conditionsFromItem(Blocks.SLIME_BLOCK))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlock.SUPER_SLIME_BLOCK_MAX)
                .input('E', ModBlock.SUPER_SLIME_BLOCK)
                .pattern("EEE")
                .pattern("EEE")
                .pattern("EEE")
                .criterion(hasItem(ModBlock.SUPER_SLIME_BLOCK), conditionsFromItem(ModBlock.SUPER_SLIME_BLOCK))
                .offerTo(exporter);

        //苹果
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ItemsRegister.EMERALD_APPLE)
                .input('E', Items.EMERALD_BLOCK)
                .input('S', Items.APPLE)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .criterion(hasItem(Items.EMERALD_BLOCK), conditionsFromItem(Items.EMERALD_BLOCK))
                .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ItemsRegister.LAPIS_APPLE)
                .input('E', Items.LAPIS_BLOCK)
                .input('S', Items.APPLE)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .criterion(hasItem(Items.LAPIS_BLOCK), conditionsFromItem(Items.LAPIS_BLOCK))
                .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ItemsRegister.REDSTONE_APPLE)
                .input('E', Items.REDSTONE_BLOCK)
                .input('S', Items.APPLE)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                .offerTo(exporter);

        //探测法杖
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemsRegister.DETECT_STAFF)
                .input('S', Items.STICK)
                .input('E', Items.EMERALD)
                .pattern("  E")
                .pattern(" S ")
                .pattern("S  ")
                .criterion(hasItem(Items.EMERALD), conditionsFromItem(Items.EMERALD))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemsRegister.GOLDEN_DETECT_STAFF)
                .input('S', ItemsRegister.DETECT_STAFF)
                .input('E', Items.GOLD_INGOT)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(ItemsRegister.DETECT_STAFF), conditionsFromItem(ItemsRegister.DETECT_STAFF))
                .offerTo(exporter);
        //小飞棍
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ItemsRegister.FLYING_STICK)
                .input('S', Items.GOLD_INGOT)
                .input('E', Items.STICK)
                .pattern("EEE")
                .pattern("ESE")
                .pattern("EEE")
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                .offerTo(exporter);
    }
}
