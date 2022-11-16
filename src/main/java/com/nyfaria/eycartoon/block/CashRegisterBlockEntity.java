package com.nyfaria.eycartoon.block;

import com.nyfaria.eycartoon.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CashRegisterBlockEntity extends BlockEntity {

    public CashRegisterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.REGISTER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
    }
}