package com.soybeani.items.item;

import com.soybeani.entity.vehicle.BottomIceBoatEntity;
import com.soybeani.entity.vehicle.TntBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.List;

public class BottomBoatItem extends Item {
    public BottomBoatItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }

        Vec3d rotationVec = user.getRotationVec(1.0F);
        List<Entity> entities = world.getOtherEntities(user, user.getBoundingBox()
                .stretch(rotationVec.multiply(5.0D)).expand(1.0D));

        if (!entities.isEmpty()) {
            Vec3d eyePos = user.getEyePos();
            for (Entity entity : entities) {
                Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                if (box.contains(eyePos)) {
                    return TypedActionResult.pass(itemStack);
                }
            }
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!world.getBlockState(blockPos).isAir()) {
                Direction direction = blockHitResult.getSide();
                BlockPos spawnPos = blockPos.offset(direction);

                if (!world.isClient) {
                    BottomIceBoatEntity boat = new BottomIceBoatEntity(world,
                            spawnPos.getX() + 0.5D,
                            spawnPos.getY(),
                            spawnPos.getZ() + 0.5D);
                    world.spawnEntity(boat);
                }

                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
                return TypedActionResult.success(itemStack);
            }
        }
        return TypedActionResult.pass(itemStack);
    }
}
