package com.soybeani.items.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.ActionResult;

/**
 * @author soybean
 * @date 2025/1/6 16:06
 * @description
 */
public class YingliSwordItem extends SwordItem {
    public YingliSwordItem(Settings settings) {
        super(ToolMaterials.NETHERITE,settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}
