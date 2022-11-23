package com.nyfaria.eycartoon.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BubbleParticle.Provider.class)
public class MixinBubbleParticleProvider {

    @Shadow @Final private SpriteSet sprite;

    @Inject(method = "createParticle(Lnet/minecraft/core/particles/SimpleParticleType;Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    public void createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, CallbackInfoReturnable<Particle> cir) {
        if (pType == ParticleTypes.BUBBLE) {
            BubbleParticle bubbleparticle = new BubbleParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed) {
                @Override
                public void tick() {
                    this.xo = this.x;
                    this.yo = this.y;
                    this.zo = this.z;
                    if (this.lifetime-- <= 0) {
                        this.remove();
                    }
                    else {
                        this.yd += 0.002D;
                        this.move(this.xd, this.yd, this.zd);
                        this.xd *= 0.85F;
                        this.yd *= 0.85F;
                        this.zd *= 0.85F;
                    }
                }
            };
            bubbleparticle.pickSprite(this.sprite);
            bubbleparticle.setLifetime(80);
            cir.setReturnValue(bubbleparticle);
        }
    }
}
