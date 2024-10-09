package com.soybeani.event.entity;

import com.soybeani.items.weapon.SwordItemOfGrass;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
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
            SwordItemOfGrass.EventRegister(player.getStackInHand(hand),world,world.getBlockState(pos),pos);
            return ActionResult.PASS;
        });
    }
}
