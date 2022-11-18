package com.nyfaria.eycartoon.event;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.client.renderer.*;
import com.nyfaria.eycartoon.entity.LargeSnowball;
import com.nyfaria.eycartoon.entity.RocketEntity;
import com.nyfaria.eycartoon.entity.ThrownYoyoEntity;
import com.nyfaria.eycartoon.init.EntityInit;
import dev._100media.hundredmediageckolib.client.model.SimpleAnimatedGeoModel;
import dev._100media.hundredmediamorphs.HundredMediaMorphsMod;
import dev._100media.hundredmediamorphs.client.renderer.MorphRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void init(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.THROWN_YOYO_ENTITY.get(), ThrownYoyoRenderer::new);
        event.registerEntityRenderer(EntityInit.SQUIDWARD_TRADER_ENTITY.get(), SquidwardRenderer::new);
        event.registerEntityRenderer(EntityInit.ELSA_ENTITY.get(), ElsaRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_PIG_ENTITY.get(), FlatPigRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_HORSE_ENTITY.get(), FlatHorseRenderer::new);
        event.registerEntityRenderer(EntityInit.FLAT_WOLF_ENTITY.get(), FlatWolfRenderer::new);
        event.registerEntityRenderer(EntityInit.ROCKET_ENTITY.get(), RocketRenderer::new);
        event.registerEntityRenderer(EntityInit.LARGE_SNOWBALL.get(), context -> new ThrownItemRenderer<>(context, 3F, false));
        event.registerEntityRenderer(EntityInit.COIN_PROJECTILE.get(), CoinRenderer::new);
        event.registerEntityRenderer(EntityInit.LIGHTNING_MCQUEEN.get(), LightningMCQueenRenderer::new);
        event.registerEntityRenderer(EntityInit.BOSS_BABY.get(), BossBabyRenderer::new);
        event.registerEntityRenderer(EntityInit.SPONGEBOB.get(), SpongebobRenderer::new);
    }
}
