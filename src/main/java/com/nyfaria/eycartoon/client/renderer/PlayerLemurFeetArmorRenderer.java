package com.nyfaria.eycartoon.client.renderer;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.PlayerLemurFeetArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class PlayerLemurFeetArmorRenderer extends GeoArmorRenderer<PlayerLemurFeetArmorItem> {

    public PlayerLemurFeetArmorRenderer() {
        super(new SimpleAnimatedGeoModel<>(EYCartoon.MODID, "models/armor", "player_lemur_feet"));
        this.headBone = null;
        this.bodyBone = null;
        this.rightArmBone = null;
        this.leftArmBone = null;
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        this.rightBootBone = "armorLeftBoot";
        this.leftBootBone = "armorRightBoot";
    }
}
