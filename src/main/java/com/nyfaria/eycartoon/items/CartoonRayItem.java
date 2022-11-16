package com.nyfaria.eycartoon.items;

import com.mojang.math.Vector3f;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class CartoonRayItem extends Item {

    public CartoonRayItem(Properties pProperties) {
        super(pProperties);
    }

    private static final DustParticleOptions RED = new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 1.2F);
    private static final DustParticleOptions YELLOW = new DustParticleOptions(new Vector3f(Vec3.fromRGB24(14737920)), 1.2F);
    private static final DustParticleOptions ORANGE = new DustParticleOptions(new Vector3f(Vec3.fromRGB24(16753920)), 1.2F);

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Vec3 look = pPlayer.getViewVector(0);
            Vec3 eyepos = pPlayer.getEyePosition(0);
            EntityHitResult result = HMUVectorUtils.rayTraceEntities(pLevel, pPlayer, 20, p -> p instanceof LivingEntity);
            if (result != null) {

            }

            for (double i = 0; i <= 20d; i += 0.1d) {
                Vec3 traceVec2 = eyepos.add(look.x * i, look.y * i, look.z * i);
                Vec3 b = new Vec3(traceVec2.x, traceVec2.y, traceVec2.z);
                for (int j = 0; j < 3; ++j) {
                    double d2 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d3 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d4 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d6 = b.x();// + pLevel.getRandom().nextDouble() ;
                    double d7 = b.y();// + pLevel.getRandom().nextDouble() * d1;
                    double d8 = b.z();// + pLevel.getRandom().nextDouble() ;
                    ((ServerLevel) pLevel).sendParticles(RED, d6, d7, d8, 3, d2, d3, d4, 0);
                    ((ServerLevel) pLevel).sendParticles(YELLOW, d6, d7, d8, 3, d2, d3, d4, 0);
                    ((ServerLevel) pLevel).sendParticles(ORANGE, d6, d7, d8, 3, d2, d3, d4, 0);
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
