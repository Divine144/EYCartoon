package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownYoyoRenderer extends EntityRenderer<ThrownYoyoEntity> {

    private static final int ROTATION_INTERVAL = 7;

    public ThrownYoyoRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    // TODO: Might need to change some of these values to make it look better, this was code directly from a boomerang-type entity (in buildirl)
    @Override
    public void render(ThrownYoyoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        //normal angle for a sword item to fly pointy-direction-out
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) + 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 45.0F));

        //boomerang spin
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        pMatrixStack.mulPose(Vector3f.ZN.rotationDegrees(360 * (((float)(pEntity.tickCount % ROTATION_INTERVAL) + pPartialTicks) / (float) ROTATION_INTERVAL)));

        Minecraft.getInstance().getItemRenderer().renderStatic(pEntity.getPickupItem(), ItemTransforms.TransformType.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pMatrixStack, pBuffer, 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownYoyoEntity pEntity) {
        System.out.println("amogus");
        return null;
    }
}
