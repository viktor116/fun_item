package com.soybeani.items.item;

import com.soybeani.entity.custom.DiamondOreEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DiamondOreEntityItem extends Item {

    public DiamondOreEntityItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;
        
        ItemStack itemStack = player.getStackInHand(context.getHand());
        World world = player.getWorld();
        DiamondOreEntity entity = DiamondOreEntity.DIAMOND_ORE.create(world);
        
        if (entity != null) {
            entity.setPosition(
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 1.0,
                    blockPos.getZ() + 0.5
            );
            entity.setYaw(player.getYaw());
            
            if (world.spawnEntity(entity)) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(world.isClient);
            }
        }

        return ActionResult.FAIL;
    }
} 