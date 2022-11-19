package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.items.LadybugYoyoItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedItemModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class LadyBugRenderer extends GeoItemRenderer<LadybugYoyoItem> {
    public LadyBugRenderer() {
        super(new SimpleAnimatedItemModel<>());
    }
}
