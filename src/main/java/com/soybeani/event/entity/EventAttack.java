package com.soybeani.event.entity;

import com.soybeani.event.EventRegister;
import com.soybeani.items.effect.EmeraldStatusEffect;
import com.soybeani.items.item.TalismanItem;
import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;

/**
 * @author soybean
 * @date 2024/10/9 15:19
 * @description 攻击相关事件
 */
public class EventAttack {
    public static void register(){
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction)-> {
            //草剑事件注册
            SwordItemOfGrass.EventRegister(player,player.getStackInHand(hand),world,world.getBlockState(pos),pos);

            return ActionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            EmeraldStatusEffect.EventRegister(player,world,entity);
            TalismanItem.EventRegister(player,hand,world,entity,hitResult);
            return ActionResult.PASS;
        });
    }
}
