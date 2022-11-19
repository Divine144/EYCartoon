package com.nyfaria.eycartoon.items;

import com.nyfaria.eycartoon.init.EntityInit;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class MinionLauncherItem extends Item {

    public MinionLauncherItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            Vec3 look = pPlayer.getViewVector(0);
            Vec3 eyepos = pPlayer.getEyePosition(0);
            EntityHitResult result = HMUVectorUtils.rayTraceEntities(pLevel, pPlayer, 20, p -> p instanceof LivingEntity);
            if (result != null) {
                Entity entity = result.getEntity();
                if (entity instanceof Pig pig)
                    pig.convertTo(EntityInit.FLAT_PIG_ENTITY.get(), false);
                else if (entity instanceof Wolf wolf)
                    wolf.convertTo(EntityInit.FLAT_WOLF_ENTITY.get(), false);
                else if (entity instanceof Horse horse)
                    horse.convertTo(EntityInit.FLAT_HORSE_ENTITY.get(), false);
            }
            for (double i = 0; i <= 20d; i += 0.1d) {
                Vec3 traceVec2 = eyepos.add(look.x * i, look.y * i, look.z * i);
                Vec3 b = new Vec3(traceVec2.x, traceVec2.y, traceVec2.z);
                for (int j = 0; j < 3; ++j) {
                    double d2 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d3 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d4 = pLevel.getRandom().nextGaussian() * 0.02D;
                    double d6 = b.x();
                    double d7 = b.y();
                    double d8 = b.z();

                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
