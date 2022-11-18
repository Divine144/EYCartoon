package com.nyfaria.eycartoon.client.renderer;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.BossBabyEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BossBabyRenderer extends GeoEntityRenderer<BossBabyEntity> {

    public BossBabyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoEntityModel<>(EYCartoon.MODID, "boss_baby"));
    }

}
