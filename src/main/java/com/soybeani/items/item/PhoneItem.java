package com.soybeani.items.item;

import com.soybeani.utils.CommonUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        Text text = switch (range) {
            case 0 -> Text.of("Hello");
            case 1 -> Text.of("Thank you");
            case 2 -> Text.of("Bye");
            default -> Text.of("NO");
        };
        user.sendMessage(text, true);
        HungerManager hungerManager = user.getHungerManager();
        if (hungerManager.isNotFull()) {
            hungerManager.add(1, 0.5f);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
