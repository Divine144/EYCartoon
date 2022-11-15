package com.nyfaria.eycartoon.ability;

import com.nyfaria.eycartoon.cap.PlayerHolder;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.hmutility.utils.HMUVectorUtils;
import dev._100media.hundredmediaabilities.ability.Ability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public class IceAbility extends Ability {

    @Override
    public boolean isHiddenAbility() {
        return true;
    }

    @Override
    public boolean isHeldAbility() {
        return true;
    }

    @Override
    public void executeHeld(ServerLevel level, ServerPlayer player, int tick) {
        BlockHitResult result = HMUVectorUtils.blockTrace(player, ClipContext.Fluid.NONE, 7, false);
        boolean unlockedAbility = PlayerHolderAttacher.getPlayerHolder(player).map(PlayerHolder::hasUnlockedIceAbility).orElse(false);
        if (unlockedAbility) {
            if (result != null) {
                if (!level.getBlockState(result.getBlockPos()).is(Blocks.ICE)) {
                    level.setBlockAndUpdate(result.getBlockPos(), Blocks.ICE.defaultBlockState());
                }
            }
        }
        super.executeHeld(level, player, tick);
    }
}
