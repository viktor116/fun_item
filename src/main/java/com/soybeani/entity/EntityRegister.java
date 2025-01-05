package com.soybeani.entity;


import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.*;
import com.soybeani.entity.vehicle.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.http.client.entity.EntityBuilder;

/**
 * @author soybean
 * @date 2024/10/19 10:48
 * @description
 */
public class EntityRegister {
    public static final EntityType<Su7CarEntity> SU7 = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(InitValue.MOD_ID, "su7"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE,Su7CarEntity::new) .dimensions(EntityDimensions.fixed(1.5f, 1.5f)).build());

    public static final EntityType<Ice2BoatEntity> ICE2_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "ice2_boat"),
            FabricEntityTypeBuilder.<Ice2BoatEntity>create(SpawnGroup.MISC, Ice2BoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<FlyBoatEntity> FLY_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "fly_boat"),
            FabricEntityTypeBuilder.<FlyBoatEntity>create(SpawnGroup.MISC, FlyBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<IceBoatEntity> ICE_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "ice_boat"),
            FabricEntityTypeBuilder.<IceBoatEntity>create(SpawnGroup.MISC, IceBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<TntBoatEntity> TNT_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "tnt_boat"),
            FabricEntityTypeBuilder.<TntBoatEntity>create(SpawnGroup.MISC, TntBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<BottomIceBoatEntity> BOTTOM_ICE_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "bottom_ice_boat"),
            FabricEntityTypeBuilder.<BottomIceBoatEntity>create(SpawnGroup.MISC, BottomIceBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<FlyingWoodSwordEntity> FLYING_WOOD_SWORD_ENTITY_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "flying_wood_sword"),
            FabricEntityTypeBuilder.<FlyingWoodSwordEntity>create(SpawnGroup.CREATURE, FlyingWoodSwordEntity::new)
                    .dimensions(EntityDimensions.fixed(1.8f, 0.5f))
                    .build()
    );

    public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("bullet_entity"),
            EntityType.Builder.create((EntityType<BulletEntity> entityType, World world) -> new BulletEntity(entityType, world),SpawnGroup.MISC)
                            .dimensions(0.1f, 0.1f)
                            .maxTrackingRange(200)
                            .trackingTickInterval(5)
                            .build());
    public static void initialize(){
        FabricDefaultAttributeRegistry.register(SU7,Su7CarEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MinecartEntity.MINECART,MinecartEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(OakBoatEntity.OAK_BOAT,OakBoatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HayBlockEntity.HAY_BLOCK,HayBlockEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DiamondOreEntity.DIAMOND_ORE,DiamondOreEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SkeletonZombieEntity.SKELETON_ZOMBIE, SkeletonZombieEntity.createSkeletonZombieAttributes());
        FabricDefaultAttributeRegistry.register(CreeperSkeletonEntity.CREEPER_SKELETON, SkeletonEntity.createAbstractSkeletonAttributes());
        FabricDefaultAttributeRegistry.register(ZombieCreeperEntity.ZOMBIE_CREEPER, CreeperEntity.createCreeperAttributes());
    }
}
