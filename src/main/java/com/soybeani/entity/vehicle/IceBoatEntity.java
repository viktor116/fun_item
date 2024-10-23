package com.soybeani.entity.vehicle;

import com.soybeani.config.InitValue;
import com.soybeani.items.ItemsRegister;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
// CustomBoatEntity.java
public class IceBoatEntity extends BoatEntity {
    public static final EntityType<IceBoatEntity> CUSTOM_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "ice_boat"),
            FabricEntityTypeBuilder.<IceBoatEntity>create(SpawnGroup.MISC, IceBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public IceBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public IceBoatEntity(World world, double x, double y, double z) {
        this(CUSTOM_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public Item asItem() {
        return ItemsRegister.ICE_BOAT;
    }
}

// CustomBoatItem.java
