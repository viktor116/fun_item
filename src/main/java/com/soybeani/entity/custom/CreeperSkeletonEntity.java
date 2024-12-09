package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/9 15:48
 * @description
 */
public class CreeperSkeletonEntity extends SkeletonEntity {

    public static final EntityType<CreeperSkeletonEntity> CREEPER_SKELETON= Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("creeper_skeleton"),
            EntityType.Builder.create(CreeperSkeletonEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.99f)
                    .eyeHeight(1.74f)
                    .vehicleAttachment(-0.7f)
                    .maxTrackingRange(8)
                    .build()
    );

    public CreeperSkeletonEntity(EntityType<? extends SkeletonEntity> entityType, World world) {
        super(entityType, world);
    }
}
