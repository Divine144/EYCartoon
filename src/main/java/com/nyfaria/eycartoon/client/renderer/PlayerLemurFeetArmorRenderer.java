package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.PlayerLemurFeetArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class PlayerLemurFeetArmorRenderer  extends GeoArmorRenderer<PlayerLemurFeetArmorItem> {

    public PlayerLemurFeetArmorRenderer() {
        super(new SimpleAnimatedGeoModel<>(EYCartoon.MODID, "models/armor", "player_lemur_feet"));
        this.headBone = null;
        this.bodyBone = null;
        this.rightArmBone = null;
        this.leftArmBone = null;
        this.rightLegBone = "armorLeftLeg";
        this.leftLegBone = "armorRightLeg";
        rightBootBone = "armorLeftBoot";
        leftBootBone = "armorRightBoot";
    }

    @Override
    public void render(float partialTick, PoseStack poseStack, VertexConsumer buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(5, 2, 5);
        poseStack.translate(0, -0.8, 0);
        super.render(partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
