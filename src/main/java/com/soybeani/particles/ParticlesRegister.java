package com.soybeani.particles;

import com.soybeani.config.InitValue;
import com.soybeani.particles.custom.AlchemyParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2025/1/10 14:27
 * @description
 */
public class ParticlesRegister {
//    public static final SimpleParticleType SPARKLE_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, "alchemymod"), FabricParticleTypes.simple());
    public static final SimpleParticleType ALCHEMYMOD = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, "alchemymod"), FabricParticleTypes.simple());
    public static final SimpleParticleType ALCHEMYMOD4 = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, "alchemymod4"), FabricParticleTypes.simple());
    public static void initialize(){

    }

    public static void initializeClient(){
        ParticleFactoryRegistry.getInstance().register(ALCHEMYMOD, AlchemyParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ALCHEMYMOD4, AlchemyParticle.Factory::new);
//        ParticleFactoryRegistry.getInstance().register(SPARKLE_PARTICLE, SonicBoomParticle.Factory::new);
    }
}
