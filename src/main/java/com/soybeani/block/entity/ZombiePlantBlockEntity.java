package com.soybeani.block.entity;

import com.soybeani.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ZombiePlantBlockEntity extends BlockEntity {

    public ZombiePlantBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.ZOMBIE_PLANT_TYPE, pos, state);
    }
}
