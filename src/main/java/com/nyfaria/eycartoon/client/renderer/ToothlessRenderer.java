package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.entity.ToothlessEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ToothlessRenderer extends GeoEntityRenderer<ToothlessEntity> {

    private boolean isLaserOn = false;

    public ToothlessRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<ToothlessEntity> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    protected void applyRotations(ToothlessEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        isLaserOn = entityLiving.isWarp();
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        if(bone.getName().equals("jaw") && isLaserOn){
            stack.pushPose();
            stack.translate(-0.5,0.9,2.1);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            BeaconRenderer.renderBeaconBeam(stack,getCurrentRTB(),BeaconRenderer.BEAM_LOCATION, Minecraft.getInstance().getPartialTick(), 1F,1,0,150, new float[]{0.4f,0,0.7f},0.5f,0.0f);
            stack.popPose();
        }
    }
}
