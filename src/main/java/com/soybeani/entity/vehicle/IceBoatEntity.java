package com.soybeani.entity.vehicle;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.EntityRegisterClient;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.*;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
// CustomBoatEntity.java
public class IceBoatEntity extends BoatEntity {

    public IceBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.ICE_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }
    @Override
    public void tick() {
        if(!(isSubmergedInWater() || isTouchingWater())){
            int range = 8;
            BlockPos blockPos = this.getBlockPos();
            World world = this.getWorld();
            BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Iterator var7 = BlockPos.iterate(blockPos.add(-range, -1, -range), blockPos.add(range, -1, range)).iterator();
            while(var7.hasNext()) {
                BlockPos blockPos2 = (BlockPos)var7.next();
                if (blockPos2.isWithinDistance(this.getPos(), (double)range)) {
                    mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                    BlockState blockState2 = world.getBlockState(mutable);
                    if (blockState2.isAir()) {
                        BlockState blockState3 = world.getBlockState(blockPos2);
                        if ((blockState3 == FrostedIceBlock.getMeltedState() || blockState3 == blockState) && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                            world.setBlockState(blockPos2, blockState);
                            world.scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, 200);
                        }
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public Item asItem() {
        return ItemsRegister.ICE_BOAT;
    }

}

// CustomBoatItem.java
