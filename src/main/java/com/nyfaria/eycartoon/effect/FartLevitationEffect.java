package com.nyfaria.eycartoon.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class FartLevitationEffect extends MobEffect {

    private boolean isInfinite;
    private MobEffectInstance levitation;

    public FartLevitationEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
        levitation = new MobEffectInstance(MobEffects.LEVITATION, 200, 0, false, false);
    }

    int counter = 0;
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        if (pLivingEntity.level instanceof ServerLevel level) {
            if (!pLivingEntity.hasEffect(MobEffects.LEVITATION)) {
                if (this.isInfinite) {
                    levitation = new MobEffectInstance(MobEffects.LEVITATION, Integer.MAX_VALUE, 0, false, false);
                }
                pLivingEntity.addEffect(levitation);
            }

            double dy = pLivingEntity.getY() - 0.3333;
            if (++counter % 10 == 0) {
                level.sendParticles(ParticleTypes.BUBBLE, pLivingEntity.getX(), dy, pLivingEntity.getZ(), 6, 0.2, 0.1, 0.2, 0.1);
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public void setIsInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
    }
}
