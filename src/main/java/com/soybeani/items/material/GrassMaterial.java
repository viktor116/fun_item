package com.soybeani.items.material;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author soybean
 * @date 2024/10/9 14:32
 * @description
 */
public class GrassMaterial implements ToolMaterial {
    private final int durability;
    private final Item repairItem;
    public static final GrassMaterial INSTANCE = new GrassMaterial(10,null);

    public GrassMaterial(int durability, Item repairItem) {
        this.durability = durability;
        this.repairItem = repairItem;
    }

    @Override
    //耐久度
    public int getDurability() {
        return this.durability;
    }

    @Override
    //挖掘速度
    public float getMiningSpeedMultiplier() {
        return 2;
    }

    @Override
    public float getAttackDamage() {
        return 2;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 5;
    }

    @Override
    public Ingredient getRepairIngredient() {
        boolean hasRepairItem = repairItem != null;
        if(hasRepairItem){
            return Ingredient.ofItems(Items.SHORT_GRASS,Items.TALL_GRASS,repairItem);
        }else{
            return Ingredient.ofItems(Items.SHORT_GRASS,Items.TALL_GRASS);
        }
    }

}
