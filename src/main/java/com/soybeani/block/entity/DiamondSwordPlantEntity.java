package com.soybeani.block.entity;

import com.soybeani.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class DiamondSwordPlantEntity extends BlockEntity {
    public DiamondSwordPlantEntity(BlockPos pos, BlockState state) {
        super(ModBlock.DIAMOND_SWORD_PLANT_TYPE, pos, state);
    }
} 