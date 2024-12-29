package com.soybeani.data.provider;

import com.soybeani.block.ModBlock;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
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
        //铜弹
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.COPPER_BULLET,4)
                .input('S', Items.COPPER_INGOT)
                .pattern("S")
                .pattern("S")
                .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter);;
        //铁弹
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.IRON_BULLET,4)
                .input('S', Items.IRON_INGOT)
                .pattern("S")
                .pattern("S")
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);;

        //雷法
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_BLUE)
                .input(Items.PAPER)
                .input(Items.BLUE_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.BLUE_DYE), conditionsFromItem(Items.BLUE_DYE))
                .offerTo(exporter);

        //道法自然
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_GREEN)
                .input(Items.PAPER)
                .input(Items.LIME_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.LIME_DYE), conditionsFromItem(Items.LIME_DYE))
                .offerTo(exporter);

        //燃烧
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_YELLOW_RED)
                .input(Items.PAPER)
                .input(Items.ORANGE_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.ORANGE_DYE), conditionsFromItem(Items.ORANGE_DYE))
                .offerTo(exporter);

        //剑气
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN)
                .input(Items.PAPER)
                .input(Items.YELLOW_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.YELLOW_DYE), conditionsFromItem(Items.YELLOW_DYE))
                .offerTo(exporter);

        //辟邪
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_PINK)
                .input(Items.PAPER)
                .input(Items.REDSTONE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                .offerTo(exporter);

        //剑魁
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_DARKGREEN)
                .input(Items.PAPER)
                .input(Items.GREEN_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.GREEN_DYE), conditionsFromItem(Items.GREEN_DYE))
                .offerTo(exporter);

        //大人时代变了
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_YELLOW)
                .input(Items.PAPER)
                .input(Items.GOLD_INGOT,8)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);

        //御剑飞行
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_SKYBLUE)
                .input(Items.PAPER)
                .input(Items.LIGHT_BLUE_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.LIGHT_BLUE_DYE), conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .offerTo(exporter);

        //血魔
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_PURPLE)
                .input(Items.PAPER)
                .input(Items.PURPLE_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.PURPLE_DYE), conditionsFromItem(Items.PURPLE_DYE))
                .offerTo(exporter);
        //凋零之力
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_GREY)
                .input(Items.PAPER)
                .input(Items.GRAY_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.GRAY_DYE), conditionsFromItem(Items.GRAY_DYE))
                .offerTo(exporter);

        //死亡之力
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ItemsRegister.TALISMAN_BLACK_PURPLE)
                .input(Items.PAPER)
                .input(Items.BLACK_DYE)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.BLACK_DYE), conditionsFromItem(Items.BLACK_DYE))
                .offerTo(exporter);
    }
}
