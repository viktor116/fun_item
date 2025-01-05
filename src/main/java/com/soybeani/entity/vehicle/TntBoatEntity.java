package com.soybeani.entity.vehicle;

import com.soybeani.entity.EntityRegister;
import com.soybeani.items.ItemsRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
// CustomBoatEntity.java
public class TntBoatEntity extends BoatEntity {

    public TntBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public TntBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.TNT_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public Item asItem() {
        return ItemsRegister.TNT_BOAT;
    }
}
