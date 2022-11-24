package com.nyfaria.eycartoon.block;

import com.nyfaria.eycartoon.entity.CoinProjectileEntity;
import com.nyfaria.eycartoon.init.BlockEntityInit;
import com.nyfaria.eycartoon.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class CashRegisterBlock extends BaseEntityBlock {

    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CashRegisterBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    int timer = 0;

    private void tick(Level level, BlockPos blockPos, BlockState state, BlockEntity blockEntity) {
        if (!level.isClientSide) {
            if (++timer % 3 == 0) {
                BlockPos relative = blockPos.relative(state.getValue(FACING), 1).above();
                Vec3 relativeVec = new Vec3(relative.getX(), relative.getY(), relative.getZ());
                CoinProjectileEntity entity = new CoinProjectileEntity(EntityInit.COIN_PROJECTILE.get(), level);
                entity.setInitialPosition(blockPos);
                entity.setPos(relativeVec);
                System.out.println(state.getValue(FACING));
                switch (state.getValue(FACING)) {
                    case EAST, WEST -> entity.setPos(relativeVec.x, relativeVec.y - 0.5, relativeVec.z + 0.5);
                    case SOUTH, NORTH -> entity.setPos(relativeVec.x + 0.5, relativeVec.y - 0.5, relativeVec.z);
                }
                level.addFreshEntity(entity);
            }
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
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
    @ParametersAreNonnullByDefault
    @NotNull
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case WEST -> Shapes.join(Block.box(2.0D, 0.0D, 3.0D, 16.0D, 4.0D, 13.0D), Block.box(2D, 0D, 3D, 8D, 11d, 13D), BooleanOp.OR);
            case EAST ->  Shapes.join(Block.box(0.04800000000000004, 0, 2.7920000000000007, 8.232, 3.3926399999999997, 13.207999999999998), Block.box(8.255199999999999, 0, 2.7920000000000007, 13.983999999999998, 11.02608, 13.207999999999998), BooleanOp.OR);
            case SOUTH -> Shapes.join(Block.box(3.0388068398946944, 0, -0.12893667128628206, 13.454806839894692, 3.3926399999999997, 8.055063328713718), Block.box(3.0388068398946944, 0, 8.078263328713717, 13.454806839894692, 11.02608, 13.807063328713717), BooleanOp.OR);
            default -> Shapes.join(Block.box(3.0D, 0.0D, 2.0D, 13.0D, 4.0D, 16.0D), Block.box(3D, 0D, 2D, 13D, 11d, 8D), BooleanOp.OR);
        };
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

        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }
}
