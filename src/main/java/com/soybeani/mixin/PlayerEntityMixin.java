package com.soybeani.mixin;

import com.soybeani.items.ItemsRegister;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "isUsingSpyglass", at = @At("HEAD"), cancellable = true)
    private void isUsingCustomSpyglass(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        boolean isSpyglass = playerEntity.getActiveItem().isOf(Items.SPYGLASS)
                || playerEntity.getActiveItem().isOf(ItemsRegister.LIGHTNING_SPYGLASS);
        cir.setReturnValue(playerEntity.isUsingItem() && isSpyglass);
        cir.cancel();
    }
}