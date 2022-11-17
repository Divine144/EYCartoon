package com.nyfaria.eycartoon.block;

import com.nyfaria.eycartoon.entity.CoinProjectileEntity;
import com.nyfaria.eycartoon.init.BlockEntityInit;
import com.nyfaria.eycartoon.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CashRegisterBlock extends BaseEntityBlock {

    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CashRegisterBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    int timer = 0;

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (++timer % 5 == 0) {
                BlockPos relative = blockPos.relative(state.getValue(FACING), 1);
                Vec3 relativeVec = new Vec3(relative.getX(), relative.getY(), relative.getZ());
                CoinProjectileEntity entity = new CoinProjectileEntity(EntityInit.COIN_PROJECTILE.get(), level);
                entity.setInitialPosition(relativeVec);
                entity.setPos(relativeVec);
                switch (state.getValue(FACING)) {
                    case EAST: entity.shoot(10, 0, 0,1.6F, 0);
                    case WEST: entity.shoot(-10, 0, 0, 1.6F, 0);
                    case SOUTH: entity.shoot(0, 0, 10, 1.6F, 0);
                    case NORTH: entity.shoot(0, 0, -10, 1.6F, 0);
                    case UP: entity.shoot(0, 10, 0, 1.6F, 0);
                    case DOWN: entity.shoot(0, -10, 0, 1.6F, 0);
                }
                level.addFreshEntity(entity);
            }
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return !pLevel.isClientSide ? createTickerHelper(pBlockEntityType, BlockEntityInit.REGISTER_BLOCK_ENTITY.get(), this::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CashRegisterBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
