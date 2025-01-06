package com.soybeani.items.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/6 15:37
 * @description
 */
public class TntBatonItem extends Item {
    public TntBatonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        HitResult hitResult = user.raycast(200, 0, true);
        Vec3d pos = hitResult.getPos();
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            world.createExplosion(user, pos.x, pos.y, pos.z, 4.0f, true, World.ExplosionSourceType.TNT);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}
