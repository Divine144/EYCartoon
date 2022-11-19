package com.nyfaria.eycartoon.client.renderer;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.MinionProjectileEntity;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class MinionProjectileRenderer extends GeoProjectilesRenderer<MinionProjectileEntity> {
    public MinionProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SimpleGeoEntityModel<>(EYCartoon.MODID, "minion"));
    }
}
