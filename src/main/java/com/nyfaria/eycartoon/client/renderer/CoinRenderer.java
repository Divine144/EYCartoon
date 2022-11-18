package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.CoinProjectileEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class CoinRenderer extends GeoProjectilesRenderer<CoinProjectileEntity> {

    public CoinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoEntityModel<>(EYCartoon.MODID, "coin"));
    }

    @Override
    public void render(CoinProjectileEntity animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.2F, 0.2F, 0.2F);
        super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
