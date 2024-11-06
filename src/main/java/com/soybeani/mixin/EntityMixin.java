package com.soybeani.mixin;

import com.soybeani.config.InitValue;
import com.soybeani.entity.EntityRegister;
import com.soybeani.entity.custom.PurpleLightningEntity;
import com.soybeani.items.item.NirvanaSpyglassItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "onStruckByLightning", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",shift = At.Shift.AFTER), cancellable = true)
    private void moreLightningJudge(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        //紫雷
        if(lightning instanceof PurpleLightningEntity){
            Entity entity = (Entity) (Object) this;
            entity.damage(entity.getDamageSources().lightningBolt(), 50.0f);
            ci.cancel(); // 防止原始的 damage 调用
        }
    }
}
