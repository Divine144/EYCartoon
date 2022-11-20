package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.MegaSnowGolemEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MegaSnowGolemEntityRenderer extends GeoEntityRenderer<MegaSnowGolemEntity> {
    public MegaSnowGolemEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoEntityModel<>(EYCartoon.MODID, "mega_snow_golem"));
    }

    @Override
    public void render(MegaSnowGolemEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(2F, 2F, 2F);
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
