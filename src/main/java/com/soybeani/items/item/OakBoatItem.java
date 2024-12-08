package com.soybeani.items.item;

import com.soybeani.entity.custom.MinecartEntity;
import com.soybeani.entity.custom.OakBoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class OakBoatItem extends Item {

    public OakBoatItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        HitResult hitResult = raycast(context.getWorld(), context.getPlayer(), RaycastContext.FluidHandling.ANY);
        BlockHitResult blockHitResult = (BlockHitResult) hitResult;
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (context.getWorld().getBlockState(blockPos).isAir()) {
            return ActionResult.PASS;
        }
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = player.getStackInHand(context.getHand());
        World world = player.getWorld();
        OakBoatEntity entity = OakBoatEntity.OAK_BOAT.create(world);
        if (entity != null) {
            entity.setPosition(
                    blockPos.getX() + 0.5, // 中心对齐
                    blockPos.getY() + 1.0, // 在方块上方1格
                    blockPos.getZ() + 0.5  // 中心对齐
            );
            entity.setYaw(player.getYaw());
            world.spawnEntity(entity);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient); // 返回成功
        }

        return ActionResult.FAIL; // 如果实体创建失败返回失败
    }
}
