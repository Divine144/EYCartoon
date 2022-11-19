package com.nyfaria.eycartoon.client.renderer;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.JulienCrownArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class JulienCrownArmorRenderer extends GeoArmorRenderer<JulienCrownArmorItem> {

    public JulienCrownArmorRenderer() {
        super(new SimpleAnimatedGeoModel<>(EYCartoon.MODID, "models/armor", "julien_crown"));
        bodyBone = null;
        rightArmBone = null;
        leftArmBone = null;
        rightLegBone = null;
        leftLegBone = null;
        rightBootBone = null;
        leftBootBone = null;
    }
}
