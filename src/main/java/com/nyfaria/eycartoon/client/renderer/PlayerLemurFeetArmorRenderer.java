package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.items.armor.PlayerLemurFeetArmorItem;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
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
    public void render(GeoModel model, PlayerLemurFeetArmorItem animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(3, 2, 3);
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}
