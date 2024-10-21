package com.soybeani.items.item;

import com.soybeani.sound.SoundRegister;
import com.soybeani.utils.CommonUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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
        int range = random.nextInt(3);
        Text text = null;
        switch (range) {
            case 0:
                text = Text.of("Are You OK");
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.ARE_YOU_OK_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                break;
            case 1:
                text = Text.of("Thank you");
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.ARE_YOU_OK_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                break;
            case 2:
                text = Text.of("Bye");
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.ARE_YOU_OK_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                break;
            default:
                text = Text.of("NO");
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundRegister.ARE_YOU_OK_EVENT, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        user.sendMessage(text, true);
        HungerManager hungerManager = user.getHungerManager();
        if (hungerManager.isNotFull()) {
            hungerManager.add(1, 0.5f);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
