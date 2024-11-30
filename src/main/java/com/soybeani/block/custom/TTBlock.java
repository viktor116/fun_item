package com.soybeani.block.custom;

import com.soybeani.block.entity.TTEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public void onPlaced(World world, BlockPos pos, BlockState state,LivingEntity placer, ItemStack itemStack) {

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
}
