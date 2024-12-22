package com.soybeani.mixin;


import com.soybeani.items.item.TalismanItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {


    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        Item item = (Item)(Object) this;
        if(item == Items.WOODEN_SWORD && user.getOffHandStack().getItem() instanceof TalismanItem talismanItem){
            if(!world.isClient){
                //符禄剑气
                if(talismanItem.getType() == TalismanItem.Type.NONE){
                    cir.cancel();
                    talismanItem.spawnSwordSlashParticles(user,world,talismanItem.getDefaultStack(),5);
                    cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand)));
                }
            }
        }
    }
}
