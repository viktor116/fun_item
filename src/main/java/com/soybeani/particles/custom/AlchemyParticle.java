package com.soybeani.particles.custom;

import com.soybeani.particles.ParticlesRegister;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/**
 * @author soybean
 * @date 2025/1/10 15:19
 * @description
 */
public class AlchemyParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    public AlchemyParticle(ClientWorld world, double x, double y, double z,SpriteProvider spriteProvider) {
        super(world, x, y, z);
        // 设置粒子的初始大小
        this.scale = 4.0F; // 让粒子变大
        // 设置粒子的移动速度
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        // 设置粒子的生命周期（法阵粒子通常会停留更久）
        this.maxAge = 100;  // 60 tick（3秒）
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        // 这里可以添加额外的逻辑，比如旋转、扩展等
        this.setVelocity(0, 0.0, 0); // 在Y轴上有轻微上升
        // 让粒子绕 Y 轴旋转，增大 angle 值
        this.prevAngle = this.angle;
        this.angle += 0.5F;

        // 保持 angle 在 0 到 360 度之间，避免溢出
        if (this.angle >= 360.0F) {
            this.angle -= 360.0F;
        }
        // 可以加一些旋转或更复杂的运动效果
        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new AlchemyParticle(world, x, y, z,this.spriteProvider);
        }
    }
}
