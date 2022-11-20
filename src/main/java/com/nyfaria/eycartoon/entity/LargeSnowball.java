package com.nyfaria.eycartoon.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class LargeSnowball extends Snowball {

    public LargeSnowball(EntityType<? extends LargeSnowball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        if (!entity.level.isClientSide) {
            entity.hurt(DamageSource.thrown(this, this.getOwner()), 5F);
            createIceCage(entity.level, entity.getOnPos());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level.isClientSide) {
            createIceCage(this.level, pResult.getBlockPos());
        }
    }

    private static void createIceCage(Level level, BlockPos pos) {
        int xPos = pos.getX();
        int yPos = pos.getY() + 1;
        int zPos = pos.getZ();
        for (int x = xPos; x < xPos + 3; ++x) {
            for (int y = yPos; y < yPos + 3; ++y) {
                for (int z = zPos; z < zPos + 3; ++z) {
                    level.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.ICE.defaultBlockState());
                }
            }
        }
    }
}
