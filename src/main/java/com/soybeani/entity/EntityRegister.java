package com.soybeani.entity;


import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.*;
import com.soybeani.entity.vehicle.FlyBoatEntity;
import com.soybeani.entity.vehicle.Ice2BoatEntity;
import com.soybeani.entity.vehicle.IceBoatEntity;
import com.soybeani.entity.vehicle.Su7CarEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

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
    public static void initialize(){
        FabricDefaultAttributeRegistry.register(SU7,Su7CarEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MinecartEntity.MINECART,MinecartEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(OakBoatEntity.OAK_BOAT,OakBoatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HayBlockEntity.HAY_BLOCK,HayBlockEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DiamondOreEntity.DIAMOND_ORE,DiamondOreEntity.createAttributes());
    }
}
