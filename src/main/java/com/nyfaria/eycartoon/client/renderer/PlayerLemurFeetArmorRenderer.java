package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.PlayerLemurFeetArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import software.bernie.geckolib3.geo.render.built.GeoBone;
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

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected void moveAndRotateMatrixToMatchBone(PoseStack stack, GeoBone bone) {
        stack.translate(bone.getPivotX() / 16, bone.getPivotY() / 16, bone.getPivotZ() / 16);
        float xRot = bone.getRotationX() * (180 / (float) Math.PI);
        float yRot = bone.getRotationY() * (180 / (float) Math.PI);
        float zRot = bone.getRotationZ() * (180 / (float) Math.PI);
        stack.mulPose(Vector3f.XP.rotationDegrees(xRot));
        stack.mulPose(Vector3f.YP.rotationDegrees(yRot));
        stack.mulPose(Vector3f.ZP.rotationDegrees(zRot));
    }
}
