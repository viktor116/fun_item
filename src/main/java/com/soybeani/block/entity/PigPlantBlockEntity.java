package com.soybeani.block.entity;

import com.soybeani.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PigPlantBlockEntity extends BlockEntity {

    public PigPlantBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.PIG_PLANT_TYPE, pos, state);
    }
}
