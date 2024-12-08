package com.soybeani.items.item;

import com.soybeani.entity.custom.HayBlockEntity;
import com.soybeani.entity.custom.WheatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HayBlockEntityItem extends Item {

    public HayBlockEntityItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = player.getStackInHand(context.getHand());
        World world = player.getWorld();
        HayBlockEntity entity = HayBlockEntity.HAY_BLOCK.create(world);
        if (entity != null) {
            entity.setPosition(
                    blockPos.getX() + 0.5, // 中心对齐
                    blockPos.getY() + 1.0, // 在方块上方1格
                    blockPos.getZ() + 0.5  // 中心对齐
            );
            entity.setYaw(player.getYaw());
            world.spawnEntity(entity);
            // 如果不是创造模式，减少物品数量
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient); // 返回成功
        }

        return ActionResult.FAIL; // 如果实体创建失败返回失败
    }
} 