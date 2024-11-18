package com.soybeani.items.item;

import com.soybeani.config.InitValue;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.entity.vehicle.IceBoatEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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

/**
 * @author soybean
 * @date 2024/10/23 17:56
 * @description
 */
public class Ice2BoatItem extends Item {
    public Ice2BoatItem(Settings settings) {
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
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!world.getBlockState(blockPos).isAir()) {
                Direction direction = blockHitResult.getSide();
                BlockPos spawnPos = blockPos.offset(direction);

                if (!world.isClient) {
                    Ice2BoatEntity boat = new Ice2BoatEntity(world,
                            spawnPos.getX() + 0.5D,
                            spawnPos.getY(),
                            spawnPos.getZ() + 0.5D);
                    boat.setCustomNameVisible(false);
//                    if (user.getDisplayName() != null) {
//                        boat.setCustomName(user.getDisplayName());
//                    }
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

    public static final Text ICE2_BOAT_TOOLTIP = Text.translatable("itemTooltip."+ InitValue.MOD_ID +".ice2_boat").formatted(Formatting.DARK_AQUA);

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(ICE2_BOAT_TOOLTIP);
    }
}
