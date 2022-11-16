package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.entity.RocketEntity;
import com.nyfaria.eycartoon.init.EntityInit;
import com.nyfaria.eycartoon.init.ItemInit;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AirStrikeAbility extends Ability {

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public void executePressed(ServerLevel level, ServerPlayer player) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemInit.BUZZ_CONTROL_PANEL.get())) {
            BlockHitResult result = HMUVectorUtils.blockTrace(player, ClipContext.Fluid.NONE, 30, false);
            if (result != null) {
                BlockPos pos = result.getBlockPos();
                for (int i = 0; i < 3; i++) {
                    BlockPos randPos = pos.offset(player.getRandom().nextInt(3) + i, 0, player.getRandom().nextInt(3) + i);
                    RocketEntity entity = new RocketEntity(EntityInit.ROCKET_ENTITY.get(), level);
                    entity.setPos(new Vec3(randPos.getX(), 75, randPos.getZ()));
                    level.addFreshEntity(entity);
                }
            }
        }
        super.executePressed(level, player);
    }
}
