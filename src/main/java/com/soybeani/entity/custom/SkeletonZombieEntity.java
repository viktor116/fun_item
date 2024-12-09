package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/9 15:21
 * @description
 */

public class SkeletonZombieEntity extends ZombieEntity{
    public static final EntityType<SkeletonZombieEntity> SKELETON_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("skeleton_zombie"),
            EntityType.Builder.create(SkeletonZombieEntity::new,SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.95f)
                    .eyeHeight(1.74f)
                    .passengerAttachments(2.0125f)
                    .vehicleAttachment(-0.7f)
                    .maxTrackingRange(8)
                    .build()
    );

    public SkeletonZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }
}
