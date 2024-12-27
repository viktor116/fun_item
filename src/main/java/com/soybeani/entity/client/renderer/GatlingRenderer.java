package com.soybeani.entity.client.renderer;

import com.soybeani.entity.client.model.GatlingModel;
import com.soybeani.items.item.GatlingItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author soybean
 * @date 2024/12/27 15:28
 * @description
 */
public class GatlingRenderer extends GeoItemRenderer<GatlingItem> {
    public GatlingRenderer() {
        super(new GatlingModel());
    }
}
