package com.nyfaria.eycartoon.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FartLevitationEffect extends MobEffect {

    public FartLevitationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    private static final MobEffectInstance LEVITATION = new MobEffectInstance(MobEffects.LEVITATION, 2, 1, false, false);
    int counter = 0;

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level instanceof ServerLevel level) {
            pLivingEntity.addEffect(LEVITATION);
            double dy = pLivingEntity.getY() - 0.3333;
            if (++counter % 20 == 0) {
                level.sendParticles(ParticleTypes.BUBBLE, pLivingEntity.getX(), dy, pLivingEntity.getZ(), 6, 0.2, 0.1, 0.2, 0.1);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
