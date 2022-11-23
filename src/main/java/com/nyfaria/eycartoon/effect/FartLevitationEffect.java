package com.nyfaria.eycartoon.effect;

import com.nyfaria.eycartoon.init.SoundInit;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FartLevitationEffect extends MobEffect {

    public FartLevitationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    int counter = 0;

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level instanceof ServerLevel level) {
            if (!pLivingEntity.hasEffect(MobEffects.LEVITATION)) {
                pLivingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, Integer.MAX_VALUE, 0, false, false));
            }
            if (++counter % 40 == 0) {
                level.playSound(null, pLivingEntity.blockPosition(), SoundInit.KRABBY_PATTY_FART.get(), SoundSource.PLAYERS, 1F, 1.0F);
            }
            double dy = pLivingEntity.getY() - 0.266666;
            if (counter % 2 == 0) {
                level.sendParticles(ParticleTypes.BUBBLE, pLivingEntity.getX(), dy, pLivingEntity.getZ(), 1, 0.05, 0, 0.05, 0.15);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
