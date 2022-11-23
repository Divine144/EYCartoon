package com.nyfaria.eycartoon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.PlasmaProjectileEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.renderers.geo.layer.AbstractLayerGeo;

public class PlasmaRayProjectileDummyRenderer extends GeoProjectilesRenderer<PlasmaProjectileEntity> {
    public PlasmaRayProjectileDummyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoEntityModel<>(EYCartoon.MODID, "minion"));
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, stack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("body")) {
            stack.pushPose();
            stack.translate(0,1.3,-0.5);
            stack.mulPose(Vector3f.ZN.rotationDegrees(90));
            BeaconRenderer.renderBeaconBeam(stack, getCurrentRTB(), BeaconRenderer.BEAM_LOCATION, Minecraft.getInstance().getPartialTick(), 1F,1,0,10, new float[]{0.4f,0,0.7f},0.5f,0.0f);
            stack.popPose();
        }
    }
}
