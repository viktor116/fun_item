package com.soybeani.items.item;

import com.soybeani.sound.SoundRegister;
import com.soybeani.utils.CommonUtils;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/16 11:42
 * @description
 */
public class PhoneItem extends Item {
    public PhoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Random random = CommonUtils.getRandom();
        //Text text = null;
        //user.sendMessage(text, true);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.LEI_JUN, SoundCategory.BLOCKS, 1.0f, 1.0f);

        HungerManager hungerManager = user.getHungerManager();
        if (hungerManager.isNotFull()) {
            hungerManager.add(1, 0.5f);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }


}
