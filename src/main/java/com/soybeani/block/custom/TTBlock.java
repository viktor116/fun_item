package com.soybeani.block.custom;

import com.soybeani.block.entity.TTEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

/**
 * @author soybean
 * @date 2024/11/30 16:14
 * @description
 */
public class TTBlock extends Block {
    public TTBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
        TTBlock.primeTnt(world, pos, player);
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
        Item item = stack.getItem();
        if (stack.isOf(Items.FLINT_AND_STEEL)) {
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        } else {
            stack.decrementUnlessCreative(1, player);
        }
        player.incrementStat(Stats.USED.getOrCreateStat(item));
        return ItemActionResult.success(world.isClient);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos,
                               Block block, BlockPos fromPos, boolean notify) {
        if (world.isReceivingRedstonePower(pos)) {
            // 红石触发
            TTEntity tntEntity = new TTEntity(world,
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, null);

            world.spawnEntity(tntEntity);
            world.playSound(null, pos, SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.BLOCKS, 1.0F, 1.0F);

            world.removeBlock(pos, false);
        }
    }

    private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter, boolean chain) {
        if (world.isClient) {
            return;
        }
        TTEntity tntEntity = new TTEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, igniter,chain);
        world.spawnEntity(tntEntity);
        world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(),
                SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
    }

    // 重载原有的方法以保持兼容性
    private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        primeTnt(world, pos, igniter, false); // 默认不是连锁反应
    }

    public static void primeTnt(World world, BlockPos pos, boolean chain) {
        primeTnt(world, pos, null, chain);
    }

    public static void primeTnt(World world, BlockPos pos) {
        primeTnt(world, pos, null, false);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        if (world.isReceivingRedstonePower(pos)) {
            TTBlock.primeTnt(world, pos);
            world.removeBlock(pos, false);
        }
    }
}
