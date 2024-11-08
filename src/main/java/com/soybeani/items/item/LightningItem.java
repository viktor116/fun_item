package com.soybeani.items.item;

import com.soybeani.entity.custom.PurpleLightningEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/8 15:51
 * @description
 */
public class LightningItem extends Item {
    public LightningItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        BlockHitResult hitResult = (BlockHitResult) user.raycast(300.0D, 0.0F, true);
        BlockPos blockPos = hitResult.getBlockPos();
        ItemStack itemStack = user.getStackInHand(hand);
        int spacing = 1;
        Vec3d direction = user.getRotationVector().normalize();
        Vec3d playerPos = user.getPos();
        int count = (int) Math.sqrt(Math.pow(playerPos.x - blockPos.getX(), 2) + Math.pow(playerPos.z - blockPos.getZ(), 2));
        double startDistance = 5.0;  // 第一个闪电距离玩家的距离

        for (int i = 0; i < count; i++) {
            double distance = startDistance + (i * spacing);
            Vec3d lightningPos = playerPos.add(
                    direction.x * distance,
                    direction.y * distance,
                    direction.z * distance
            );
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
            lightning.setPosition(lightningPos.getX(), lightningPos.getY(), lightningPos.getZ());
            world.spawnEntity(lightning);
        }

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack);
    }
}
