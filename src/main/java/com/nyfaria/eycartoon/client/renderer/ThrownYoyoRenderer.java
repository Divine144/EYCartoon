package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownYoyoRenderer extends EntityRenderer<ThrownYoyoEntity> {

    private static final int ROTATION_INTERVAL = 5;

    public ThrownYoyoRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public void render(ThrownYoyoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        //boomerang spin
        pMatrixStack.mulPose(Vector3f.ZN.rotationDegrees(90));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(360 * (((float)(pEntity.tickCount % ROTATION_INTERVAL) + pPartialTicks) / (float) ROTATION_INTERVAL)));
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
