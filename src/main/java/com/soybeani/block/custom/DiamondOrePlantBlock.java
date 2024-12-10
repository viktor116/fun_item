package com.soybeani.block.custom;

import com.soybeani.block.entity.DiamondOrePlantEntity;
import com.soybeani.block.entity.PigPlantBlockEntity;
import com.soybeani.config.InitValue;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soybean
 * @date 2024/12/2 17:53
 * @description
 */
public class DiamondOrePlantBlock extends Block implements BlockEntityProvider, Fertilizable {
    public static final IntProperty AGE = Properties.AGE_7;

    public DiamondOrePlantBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DiamondOrePlantEntity(pos, state);
    }

    public float getScale(BlockState state) {
        return (state.get(AGE) + 1) / 8.0f;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 7;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age < 7 && random.nextInt(10) == 0) {
            world.setBlockState(pos, state.with(AGE, age + 1), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isOf(Blocks.STONE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        float scale = getScale(state);
        // 从中心向外扩展，保持碰撞箱居中
        return Block.createCuboidShape(
            8.0 - (scale * 8.0),  // 左边界
            0.0,                   // 底部
            8.0 - (scale * 8.0),  // 前边界
            8.0 + (scale * 8.0),  // 右边界
            scale * 16.0,         // 顶部
            8.0 + (scale * 8.0)   // 后边界
        );
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(AGE) < 7;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) < 7 ;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(7, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!world.isClient) {
            // 未成熟时掉落原来的物品（作物本身）
            dropStack(world, pos, new ItemStack(Items.DIAMOND));
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        if (state.get(AGE) >= 7) {
            // 完全成熟时掉落1-3个钻石
            int amount = InitValue.getRandom().nextInt(1, 3);
            drops.add(new ItemStack(Items.DIAMOND, amount));
        } else {
            // 未成熟时掉落原来的物品（作物本身）
//            drops.add(new ItemStack(this));
        }
    
    return drops;
}
}

