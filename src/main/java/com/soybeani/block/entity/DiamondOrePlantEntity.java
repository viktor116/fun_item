package com.soybeani.block.entity;

import com.soybeani.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2024/12/10 11:45
 * @description
 */
public class DiamondOrePlantEntity extends BlockEntity {
    public DiamondOrePlantEntity(BlockPos pos, BlockState state) {
        super(ModBlock.DIAMOND_ORE_PLANT_TYPE, pos, state);
    }
}
