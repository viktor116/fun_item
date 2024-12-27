package com.soybeani.data.provider;

import com.soybeani.block.ModBlock;
import com.soybeani.config.InitValue;
import com.soybeani.entity.custom.CreeperSkeletonEntity;
import com.soybeani.entity.custom.SkeletonZombieEntity;
import com.soybeani.entity.custom.ZombieCreeperEntity;
import com.soybeani.event.keybinds.KeyBindsInputHandler;
import com.soybeani.items.ItemsRegister;
import com.soybeani.items.item.FlyBoatItem;
import com.soybeani.items.item.Ice2BoatItem;
import com.soybeani.items.item.IceBoatItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2024/11/18 11:16
 * @description
 */
public class ModChineseLanguageProvider extends FabricLanguageProvider {
    private static final String LANGUAGE_CODE = "zh_cn";
    public ModChineseLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, LANGUAGE_CODE, registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        } else {
            InitValue.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }
    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemsRegister.COMMON_ITEM_GROUP_KEY,"豆浆普通物品");
        translationBuilder.add(ItemsRegister.FUN_ITEM_GROUP_KEY,"蛋浆生物");
        translationBuilder.add(ItemsRegister.GRASS_SWORD,"小草剑");
        translationBuilder.add(ItemsRegister.GRASS_SWORD2,"草剑");
        translationBuilder.add(ItemsRegister.WHEAT_SWORD,"麦剑");
        translationBuilder.add(ItemsRegister.XIAOMI14,"小米14");
        translationBuilder.add(ItemsRegister.XIAOMI14BUTTON,"米家控制器");
        translationBuilder.add(ItemsRegister.ICE_BOAT,"冰霜船");
        translationBuilder.add(ItemsRegister.ICE2_BOAT,"冰霜王船");
        translationBuilder.add(ItemsRegister.FLY_BOAT,"天空船");
        translationBuilder.add(ItemsRegister.LIGHTNING_SPYGLASS,"大雷之镜");
        translationBuilder.add(ItemsRegister.NIRVANA_SPYGLASS,"寂灭之镜");
        translationBuilder.add(ItemsRegister.PURPLE_LIGHTNING,"紫雷");
        translationBuilder.add(ItemsRegister.LIGHTNING,"雷电");
        translationBuilder.add(ItemsRegister.AIR_PICKAXE,"");
        translationBuilder.add(ItemsRegister.EMERALD_APPLE,"附魔绿苹果");
        translationBuilder.add(ItemsRegister.LAPIS_APPLE,"附魔青苹果");
        translationBuilder.add(ItemsRegister.REDSTONE_APPLE,"附魔红苹果");
        translationBuilder.add(ItemsRegister.DETECT_STAFF,"探测法杖");
        translationBuilder.add(ItemsRegister.GOLDEN_DETECT_STAFF,"金质探测法杖");
        translationBuilder.add(ItemsRegister.RED_STONE_EFFECT,"附魔红苹果");
        translationBuilder.add(ItemsRegister.EMERALD_EFFECT,"附魔绿苹果");
        translationBuilder.add(ItemsRegister.LAPIS_EFFECT,"附魔青苹果");
        translationBuilder.add(ItemsRegister.FLYING_STICK,"小飞棍");
        translationBuilder.add(ItemsRegister.TALISMAN,"剑气符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_BLACK_PURPLE,"死亡阴影符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_BLUE,"雷法符箓");
        translationBuilder.add(ItemsRegister.GATLING_GUN,"加特林");

        translationBuilder.add(ItemsRegister.TALISMAN_GREEN,"道法自然符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_YELLOW_RED,"燃烧符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_PURPLE,"血魔符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_DARKGREEN,"剑魁符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_PINK,"避邪符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_YELLOW,"附身符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_SKYBLUE,"飞行符箓");
        translationBuilder.add(ItemsRegister.TALISMAN_GREY,"凋零之力符箓");

        translationBuilder.add(ItemsRegister.WHEAT_LIVE,"小麦生物");
        translationBuilder.add(ItemsRegister.COW_PLANT,"牛植物");
        translationBuilder.add(ItemsRegister.PIG_PLANT,"猪植物");
        translationBuilder.add(ItemsRegister.ZOMBIE_PLANT,"僵尸植物");
        translationBuilder.add(ItemsRegister.MINECART_LIVE,"矿车生物");
        translationBuilder.add(ItemsRegister.OAK_BOAT_LIVE,"橡木船生物");
        translationBuilder.add(ItemsRegister.HAY_BLOCK_LIVE,"干草块生物");
        translationBuilder.add(ItemsRegister.DIAMOND_ORE_LIVE,"钻石矿石生物"); //生物
        translationBuilder.add(ItemsRegister.DIAMOND_ORE_PLANT,"钻石矿石植物"); //植物
        translationBuilder.add(ItemsRegister.DIAMOND_SWORD_PLANT,"钻石剑植物"); //剑植物
        translationBuilder.add(ItemsRegister.SKELETON_ZOMBIE_SPAWN_EGG,"骷髅僵尸刷怪蛋");
        translationBuilder.add(ItemsRegister.CREEPER_SKELETON_SPAWN_EGG,"爬行者骷髅刷怪蛋");
        translationBuilder.add(ItemsRegister.ZOMBIE_CREEPER_SPAWN_EGG,"僵尸爬行者刷怪蛋");

        translationBuilder.add(SkeletonZombieEntity.SKELETON_ZOMBIE,"骷髅僵尸");
        translationBuilder.add(CreeperSkeletonEntity.CREEPER_SKELETON,"爬行骷髅射手");
        translationBuilder.add(ZombieCreeperEntity.ZOMBIE_CREEPER,"僵尸爬行者");

        translationBuilder.add(ModBlock.AIR_ICE,"空气冰");
        translationBuilder.add(ModBlock.SUPER_SLIME_BLOCK,"超级黏液块");
        translationBuilder.add(ModBlock.SUPER_SLIME_BLOCK_MAX,"超超级黏液块");
        translationBuilder.add(ModBlock.TT_BLOCK,"T_T");

        addText(translationBuilder, Text.of(KeyBindsInputHandler.KEY_CATEGORY_ID),"豆浆有趣物品键位");
        addText(translationBuilder, Text.of(KeyBindsInputHandler.KEY_TOGGLE_NAME),"物品交互");
        addText(translationBuilder, Text.of(KeyBindsInputHandler.KEY_FUNCTION_NAME),"物品功能");
        addText(translationBuilder,IceBoatItem.ICE_BOAT_TOOLTIP,"用冰块制成的船");
        addText(translationBuilder, Ice2BoatItem.ICE2_BOAT_TOOLTIP,"用冰块再次加固的船！");
        addText(translationBuilder, FlyBoatItem.FLY_BOAT_TOOLTIP,"想要突破天空的船");
    }
}
