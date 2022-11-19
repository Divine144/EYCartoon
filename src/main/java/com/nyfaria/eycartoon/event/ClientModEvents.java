package com.nyfaria.eycartoon.event;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.client.animatable.SpinAnimatable;
import com.nyfaria.eycartoon.client.renderer.*;
import com.nyfaria.eycartoon.init.EntityInit;
import com.nyfaria.eycartoon.init.MorphInit;
import com.nyfaria.eycartoon.items.armor.JulienCrownArmorItem;
import com.nyfaria.eycartoon.items.armor.PlayerLemurFeetArmorItem;
import com.nyfaria.eycartoon.items.armor.SonicBootsArmorItem;
import dev._100media.hundredmediageckolib.client.animatable.SimpleAnimatable;
import dev._100media.hundredmediageckolib.client.model.SimpleGeoPlayerModel;
import dev._100media.hundredmediageckolib.client.renderer.GeoPlayerRenderer;
import dev._100media.hundredmediamorphs.client.model.AdvancedGeoPlayerModel;
import dev._100media.hundredmediamorphs.client.renderer.AdvancedGeoPlayerRenderer;
import dev._100media.hundredmediamorphs.client.renderer.MorphRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {

        MorphRenderers.registerPlayerMorphRenderer(MorphInit.PLAYER_SPINNING.get(),
                manager -> new AdvancedGeoPlayerRenderer<>(manager,
                        new AdvancedGeoPlayerModel<>(EYCartoon.MODID, "player_spinning"),
                        new SpinAnimatable()));

        event.registerEntityRenderer(EntityInit.THROWN_YOYO_ENTITY.get(), ThrownYoyoRenderer::new);
        event.registerEntityRenderer(EntityInit.SQUIDWARD_TRADER_ENTITY.get(), SquidwardRenderer::new);
        event.registerEntityRenderer(EntityInit.ELSA_ENTITY.get(), ElsaRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_PIG_ENTITY.get(), FlatPigRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_HORSE_ENTITY.get(), FlatHorseRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_WOLF_ENTITY.get(), FlatWolfRenderer::new);
        event.registerEntityRenderer(EntityInit.ROCKET_ENTITY.get(), RocketRenderer::new);
        event.registerEntityRenderer(EntityInit.MINION_PROJECTILE.get(), MinionProjectileRenderer::new);
        event.registerEntityRenderer(EntityInit.LARGE_SNOWBALL.get(), context -> new ThrownItemRenderer<>(context, 3F, false));
        event.registerEntityRenderer(EntityInit.COIN_PROJECTILE.get(), CoinRenderer::new);
        event.registerEntityRenderer(EntityInit.LIGHTNING_MCQUEEN.get(), LightningMCQueenRenderer::new);
        event.registerEntityRenderer(EntityInit.BOSS_BABY.get(), BossBabyRenderer::new);
        event.registerEntityRenderer(EntityInit.SPONGEBOB.get(), SpongebobRenderer::new);
    }

    @SubscribeEvent
    public static void initLayers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(SonicBootsArmorItem.class, SonicBootsArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(JulienCrownArmorItem.class, JulienCrownArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(PlayerLemurFeetArmorItem.class, PlayerLemurFeetArmorRenderer::new);
    }
}
