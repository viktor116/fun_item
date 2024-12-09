package com.soybeani.entity;

import com.soybeani.config.InitValue;
import com.soybeani.entity.client.renderer.*;
import com.soybeani.entity.custom.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/11/16 13:38
 * @description
 */
@Environment(EnvType.CLIENT)
public class EntityRegisterClient {

    public static final ModelIdentifier LIGHTNING_SPYGLASS_IN_HAND = ModelIdentifier.ofInventoryVariant(Identifier.of(InitValue.MOD_ID, "lightning_spyglass_in_hand"));
    public static final ModelIdentifier NIRVANA_SPYGLASS_IN_HAND = ModelIdentifier.ofInventoryVariant(Identifier.of(InitValue.MOD_ID, "nirvana_spyglass_in_hand"));

    public static void initializeClient(){
        EntityRendererRegistry.register(EntityRegister.SU7, Su7CarRenderer::new);
        EntityRendererRegistry.register(EntityRegister.ICE_BOAT, IceBoatEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.ICE2_BOAT, Ice2BoatEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.FLY_BOAT, FlyBoatEntityRenderer::new);
        EntityRendererRegistry.register(PurpleLightningEntity.PURPLE_LIGHTNING, PurpleLightningRenderer::new);
        EntityRendererRegistry.register(WheatEntity.WHEAT, WheatRenderer::new);
        EntityRendererRegistry.register(MinecartEntity.MINECART,MinecartEntityRenderer::new);
        EntityRendererRegistry.register(OakBoatEntity.OAK_BOAT,OakBoatEntityRenderer::new);
        EntityRendererRegistry.register(HayBlockEntity.HAY_BLOCK,HayBlockEntityRenderer::new);
        EntityRendererRegistry.register(DiamondOreEntity.DIAMOND_ORE, DiamondOreEntityRenderer::new);
        EntityRendererRegistry.register(SkeletonZombieEntity.SKELETON_ZOMBIE, SkeletonZombieEntityRenderer::new);
        EntityRendererRegistry.register(CreeperSkeletonEntity.CREEPER_SKELETON, CreeperSkeletonEntityRenderer::new);
        EntityRendererRegistry.register(ZombieCreeperEntity.ZOMBIE_CREEPER, ZombieCreeperEntityRenderer::new);
    }

}
