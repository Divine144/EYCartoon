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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ToothlessRenderer extends GeoEntityRenderer<ToothlessEntity> {

    private boolean isLaserOn = false;
    private ToothlessEntity currentRenderingEntity;
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
        currentRenderingEntity = animatable;
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
        if (bone.getName().equals("eye") && isLaserOn) {
            stack.pushPose();
            stack.translate(-0.5,1.3,2.1);
            if (this.currentRenderingEntity != null && currentRenderingEntity.getControllingPassenger() != null) {
                if ((this.entityRenderDispatcher.options == null || !this.entityRenderDispatcher.options.getCameraType().isFirstPerson() || (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && Minecraft.getInstance().player != currentRenderingEntity.getControllingPassenger()))) {
                    stack.mulPose(Vector3f.XP.rotationDegrees(120));
                }
                else {
                    stack.mulPose(Vector3f.XP.rotationDegrees(90));
                }
            }
            else {
                stack.mulPose(Vector3f.XP.rotationDegrees(90));
            }
            BeaconRenderer.renderBeaconBeam(stack,getCurrentRTB(),BeaconRenderer.BEAM_LOCATION, Minecraft.getInstance().getPartialTick(), 1F,1,0,150, new float[]{0.4f,0,0.7f},0.3f,0.0f);
            stack.popPose();
        }
    }

    @Override
    public RenderType getRenderType(ToothlessEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
