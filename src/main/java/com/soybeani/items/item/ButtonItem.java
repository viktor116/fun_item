package com.soybeani.items.item;

import com.soybeani.items.ItemsRegister;
import com.soybeani.utils.CommonUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/16 14:14
 * @description
 */
public class ButtonItem extends Item {

    //需要爆炸的物品
    public final static Item EXPLOSIVE_ITEM = ItemsRegister.XIAOMI14;
    public final static float EXPLOSIVE_POWER = 15.0f;
    public ButtonItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.sendMessage(Text.of("小米手机真的好玩"), true);
        if (!world.isClient) {
            // 遍历玩家周围的方块
            for (int x = -50; x <= 50; x++) {
                for (int z = -50; z <= 50; z++) {
                    BlockPos pos = user.getBlockPos().add(x, 0, z);
                    // 检查掉落物
                    for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, user.getBoundingBox().expand(100), item -> item.getStack().getItem() == EXPLOSIVE_ITEM)) {
                        world.createExplosion(user, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), EXPLOSIVE_POWER, World.ExplosionSourceType.TNT);
                    }
                    // 检查方块内的物品
                    if (world.getBlockEntity(pos) instanceof net.minecraft.block.entity.ChestBlockEntity chest) {
                        for (int i = 0; i < chest.size(); i++) {
                            ItemStack stack = chest.getStack(i);
                            if (stack.getItem() == EXPLOSIVE_ITEM) {
                                world.createExplosion(user, pos.getX(), pos.getY(), pos.getZ(), EXPLOSIVE_POWER, World.ExplosionSourceType.TNT);
                            }
                        }
                    }
                }
            }
            // 检查生物的背包
            for (MobEntity mobEntity : world.getEntitiesByClass(MobEntity.class, user.getBoundingBox().expand(100), entity -> true)) {
                for (ItemStack stack : mobEntity.getHandItems()) {
                    if (stack.getItem() == EXPLOSIVE_ITEM) {
                        world.createExplosion(user, mobEntity.getX(), mobEntity.getY(), mobEntity.getZ(), EXPLOSIVE_POWER, World.ExplosionSourceType.TNT);
                    }
                }
            }
            List<? extends PlayerEntity> players = world.getPlayers();
            for ( PlayerEntity player :players ) {
                // 检查玩家背包
                for (ItemStack stack : player.getInventory().main) {
                    if (stack.getItem() == EXPLOSIVE_ITEM) {
                        world.createExplosion(player, player.getX(), player.getY(), player.getZ(), EXPLOSIVE_POWER, World.ExplosionSourceType.TNT);
                    }
                }
            }
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
