package com.soybeani.entity.custom;

import com.soybeani.config.InitValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/9 15:51
 * @description
 */
public class ZombieCreeperEntity extends CreeperEntity {

    public static final EntityType<ZombieCreeperEntity> ZOMBIE_CREEPER= Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("zombie_creeper"),
            EntityType.Builder.create(ZombieCreeperEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.7f)
                    .maxTrackingRange(8)
                    .build()
    );
    public ZombieCreeperEntity(EntityType<? extends CreeperEntity> entityType, World world) {
        super(entityType, world);
    }
}
