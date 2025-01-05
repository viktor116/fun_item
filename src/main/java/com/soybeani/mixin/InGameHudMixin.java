package com.soybeani.mixin;

import com.soybeani.config.InitValue;
import com.soybeani.hud.HudRegister;
import com.soybeani.hud.custom.SpyglassHudOverlay;
import com.soybeani.items.ItemsRegister;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    private float spyglassScale;
    @Shadow
    private MinecraftClient client;
    @Inject(method = "renderMiscOverlays", at = @At("RETURN"))
    private void renderCustomHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        // 在这里添加您自定义的HUD渲染逻辑
        if (this.client.options.getPerspective().isFirstPerson()) {
            if (this.client.player.isUsingSpyglass()) {
                Item playerItemInHand = this.client.player.getStackInHand(Hand.MAIN_HAND).getItem();
                if(playerItemInHand == ItemsRegister.LIGHTNING_SPYGLASS){
                    SpyglassHudOverlay.renderSpyglassOverlay(context, this.spyglassScale, HudRegister.LIGHTNING_SPYGLASS_SCOPE);
                }else if (playerItemInHand == ItemsRegister.NIRVANA_SPYGLASS){
                    SpyglassHudOverlay.renderSpyglassOverlay(context, this.spyglassScale, HudRegister.NIRVANA_SPYGLASS_SCOPE);
                }else if(playerItemInHand == ItemsRegister.PREGNANT_SPYGLASS){
                    SpyglassHudOverlay.renderSpyglassOverlay(context, this.spyglassScale, HudRegister.PREGNANT_SPYGLASS_SCOPE);
                }
            }
        }
   }
}
