package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.entity.ToothlessEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ToothlessRenderer extends GeoEntityRenderer<ToothlessEntity> {

    private boolean isLaserOn = false;
    private RenderType currentRenderType;

    public ToothlessRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<ToothlessEntity> modelProvider) {
        super(renderManager, modelProvider);
        shadowRadius = 0.6f;
    }

    @Override
    protected void applyRotations(ToothlessEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        isLaserOn = entityLiving.isWarp();
    }

    @Override
    public void render(ToothlessEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderType renderType = getRenderType(animatable, partialTick, poseStack, bufferSource, null, packedLight, getTextureLocation(animatable));
        this.currentRenderType = renderType;
        poseStack.pushPose();
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
        currentRenderType = null;
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (rtb != null && currentRenderType != null) {
            rtb.getBuffer(currentRenderType).normal(red, green, blue);
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if (bone.getName().equals("jaw") && isLaserOn) {
            stack.pushPose();
            stack.translate(-0.5,0.9,2.1);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            BeaconRenderer.renderBeaconBeam(stack,getCurrentRTB(),BeaconRenderer.BEAM_LOCATION, Minecraft.getInstance().getPartialTick(), 1F,1,0,150, new float[]{0.4f,0,0.7f},0.5f,0.0f);
            stack.popPose();
        }
    }

    @Override
    public RenderType getRenderType(ToothlessEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
