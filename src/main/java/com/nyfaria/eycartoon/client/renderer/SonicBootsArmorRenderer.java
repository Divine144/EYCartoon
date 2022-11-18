package com.nyfaria.eycartoon.client.renderer;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.SonicBootsArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class SonicBootsArmorRenderer extends GeoArmorRenderer<SonicBootsArmorItem> {

    public SonicBootsArmorRenderer() {
        super(new SimpleAnimatedGeoModel<>(EYCartoon.MODID, "models/armor", "sonic_boots"));
        headBone = null;
        bodyBone = null;
        rightArmBone = null;
        leftArmBone = null;
        rightLegBone = "armorRightLeg";
        leftLegBone = "armorLeftLeg";
        rightBootBone = "armorRightBoot";
        leftBootBone = "armorLeftBoot";
    }
}
