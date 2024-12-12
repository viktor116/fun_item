package com.soybeani.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class ArrowEntityMixin {

    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        // 检查是否是爆炸箭
        if ((Object)this instanceof ArrowEntity arrow) {
            if (!arrow.getWorld().isClient && arrow.getCommandTags().contains("explosive_arrow")) {
                Entity target = entityHitResult.getEntity();
                // 在击中实体位置创建爆炸
                arrow.getWorld().createExplosion(
                    arrow,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    2.0F,
                    false,
                    World.ExplosionSourceType.MOB
                );
                ci.cancel();  // 取消原有的碰撞处理
            }
        }
    }

    @Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
    private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        // 检查是否是爆炸箭
        if ((Object)this instanceof ArrowEntity arrow) {
            if (!arrow.getWorld().isClient && arrow.getCommandTags().contains("explosive_arrow")) {
                // 在击中方块位置创建爆炸
                arrow.getWorld().createExplosion(
                    arrow,
                    blockHitResult.getPos().x,
                    blockHitResult.getPos().y,
                    blockHitResult.getPos().z,
                    2.0F,
                    false,
                    World.ExplosionSourceType.MOB
                );
                ci.cancel();  // 取消原有的碰撞处理
            }
        }
    }
} 