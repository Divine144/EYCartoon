package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ThrownYoyoRenderer extends EntityRenderer<ThrownYoyoEntity> {

    private static final int ROTATION_INTERVAL = 5;

    public ThrownYoyoRenderer(EntityRendererProvider.Context manager) {
        super(manager);
    }

    @Override
    public void render(ThrownYoyoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        Player player = pEntity.getPlayerOwner();
        if (player != null) {
            pMatrixStack.pushPose();
            pMatrixStack.pushPose();
            if (player.getDirection() == Direction.WEST || player.getDirection() == Direction.EAST) {
                pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(90));
            }
            else {
                pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-90));
            }
            pMatrixStack.mulPose(Vector3f.XN.rotationDegrees(360 * (((float) (pEntity.tickCount % ROTATION_INTERVAL) + pPartialTicks) / (float) ROTATION_INTERVAL)));
            Minecraft.getInstance().getItemRenderer().renderStatic(pEntity.getPickupItem(), ItemTransforms.TransformType.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pMatrixStack, pBuffer, 0);
            pMatrixStack.popPose();
            pMatrixStack.translate(0, -0.1, 0);
            int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getMainHandItem();
            if (!itemstack.canPerformAction(net.minecraftforge.common.ToolActions.FISHING_ROD_CAST)) {
                i = -i;
            }

            float f = player.getAttackAnim(pPartialTicks);
            float f1 = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
            float f2 = Mth.lerp(pPartialTicks, player.yBodyRotO, player.yBodyRot) * ((float) Math.PI / 180F);
            double d0 = Mth.sin(f2);
            double d1 = Mth.cos(f2);
            double d2 = (double) i * 0.35D;
            double d4;
            double d5;
            double d6;
            float f3;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
                double d7 = 960.0D / (double) this.entityRenderDispatcher.options.fov().get().intValue();
                Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * -0.435F, 0.05F);
                vec3 = vec3.scale(d7);
                vec3 = vec3.yRot(f1 * 0.5F);
                vec3 = vec3.xRot(-f1 * 0.7F);
                d4 = Mth.lerp(pPartialTicks, player.xo, player.getX()) + vec3.x;
                d5 = Mth.lerp(pPartialTicks, player.yo, player.getY()) + vec3.y;
                d6 = Mth.lerp(pPartialTicks, player.zo, player.getZ()) + vec3.z;
                f3 = player.getEyeHeight() - 0.2F;
            }
            else {
                d4 = Mth.lerp(pPartialTicks, player.xo, player.getX()) - d1 * d2 - d0 * 0.8D;
                d5 = player.yo + (double) player.getEyeHeight() + (player.getY() - player.yo) * (double) pPartialTicks - 0.45D;
                d6 = Mth.lerp(pPartialTicks, player.zo, player.getZ()) - d0 * d2 + d1 * 0.8D;
                f3 = player.isCrouching() ? -0.1875F : 0.0F;
            }

            double d9 = Mth.lerp(pPartialTicks, pEntity.xo, pEntity.getX());
            double d10 = Mth.lerp(pPartialTicks, pEntity.yo, pEntity.getY()) + 0.25D;
            double d8 = Mth.lerp(pPartialTicks, pEntity.zo, pEntity.getZ());
            float f4 = (float) (d4 - d9);
            float f5 = (float) (d5 - d10) + f3;
            float f6 = (float) (d6 - d8);
            VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose posestack$pose1 = pMatrixStack.last();

            for (int k = 0; k <= 16; ++k) {
                stringVertex(f4, f5, f6, vertexconsumer1, posestack$pose1, fraction(k, 16), fraction(k + 1, 16));
            }

            pMatrixStack.popPose();

            super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }

    private static float fraction(int pNumerator, int pDenominator) {
        return (float)pNumerator / (float)pDenominator;
    }

    private static void stringVertex(float p_174119_, float p_174120_, float p_174121_, VertexConsumer p_174122_, PoseStack.Pose p_174123_, float p_174124_, float p_174125_) {
        float f = p_174119_ * p_174124_;
        float f1 = p_174120_ * (p_174124_ * p_174124_ + p_174124_) * 0.5F + 0.25F;
        float f2 = p_174121_ * p_174124_;
        float f3 = p_174119_ * p_174125_ - f;
        float f4 = p_174120_ * (p_174125_ * p_174125_ + p_174125_) * 0.5F + 0.25F - f1;
        float f5 = p_174121_ * p_174125_ - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        p_174122_.vertex(p_174123_.pose(), f, f1, f2).color(0, 0, 0, 255).normal(p_174123_.normal(), f3, f4, f5).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownYoyoEntity pEntity) {
        System.out.println("amogus");
        return null;
    }
}
