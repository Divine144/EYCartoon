package com.nyfaria.eycartoon.block;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.config.EYCartoonConfig;
import com.nyfaria.eycartoon.init.ItemInit;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CartoonBlock extends Block {

    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final List<Item> RANDOM_VANILLA_ITEMS = ForgeRegistries.ITEMS.getValues().stream()
            .filter(p -> !Objects.equals(p.getCreatorModId(p.getDefaultInstance()), EYCartoon.MODID))
            .filter(p -> p != Items.AIR)
            .collect(Collectors.toList());

    private static final Map<Integer, Item> ITEMS = new HashMap<>();
    private static final Map<Integer, LivingEntity> ENTITIES = new HashMap<>();

    public CartoonBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
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

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player != null && !player.getAbilities().instabuild) {
            if (!player.level.isClientSide) {
                initializeMaps(level); // Assign values to maps
                if (player.getRandom().nextIntBetweenInclusive(0, 100) >= 10) { // 90% chance -> Blocks mined progression++
                    PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> {
                        p.addBlocksMined();
                        Item item = ITEMS.get(p.getBlocksMinedCount());
                        LivingEntity entity = ENTITIES.get(p.getBlocksMinedCount());
                        int itemOrEntityCount = getCount(p.getBlocksMinedCount());
                        if (item != null) {
                            ItemStack stack = new ItemStack(item);
                            stack.setCount(itemOrEntityCount);
                            setupItemPosition(stack, level, Vec3.atCenterOf(pos));
                        }
                        else if (entity != null) {
                            spawnEntitiesOfType(entity, level, Vec3.atBottomCenterOf(pos.above()), itemOrEntityCount);
                        }
                    });
                }
                else {
                    setupItemPosition(new ItemStack(getRandomVanillaDrop(player.getRandom())), level, Vec3.atCenterOf(pos));
                }
            }
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private Item getRandomVanillaDrop(RandomSource source) {
        return Util.getRandom(RANDOM_VANILLA_ITEMS, source);
    }

    private void setupItemPosition(ItemStack stack, Level level, Vec3 at) {
        ItemEntity itementity = new ItemEntity(level, at.x(), at.y() + 0.6, at.z(), stack);
        itementity.setDefaultPickUpDelay();
        itementity.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(itementity);
    }

    private void spawnEntitiesOfType(LivingEntity entity, Level level, Vec3 at, int count) {
        for (int i = 0; i < count; i++) {
            var type = entity.getType().create(level);
            if (type != null) {
                type.setPos(at.x, at.y, at.z);
                level.addFreshEntity(type);
            }
        }
    }

    public int getCount(int blocksMinedCount) {
        int count = 1;
        if (configInst().cobbleStoneDropOrder.get() == blocksMinedCount) {
            count = 32;
        }
        else if (configInst().zombieCowDropOrder.get() == blocksMinedCount) {
            count = 3;
        }
        else if (configInst().skeletonsDropOrder.get() == blocksMinedCount) {
            count = 3;
        }
        else if (configInst().sonicCoinsDropOrder.get() == blocksMinedCount) {
            count = 10;
        }
        else if (configInst().diamondsDropOrder.get() == blocksMinedCount) {
            count = 20;
        }
        return count;
    }

    private static void initializeMaps(Level level) {
        if (ITEMS.isEmpty() && ENTITIES.isEmpty()) {
            // ALL DIRT ITEMS ARE PLACEHOLDERS
            ITEMS.put(configInst().minionLaunchedDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().krabbyPattyDropOrder.get(), ItemInit.KRABBY_PATTY.get());
            ITEMS.put(configInst().cobbleStoneDropOrder.get(), Items.COBBLESTONE);
            ITEMS.put(configInst().shreksFistsDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().cartoonRayDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().buzzControlPanelDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().ladybugYoYoDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().sonicCoinsDropOrder.get(), ItemInit.SONIC_COIN.get());
            ITEMS.put(configInst().sonicBootsDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().kingJulienCrownDropOrder.get(), Items.DIRT);
            ITEMS.put(configInst().diamondsDropOrder.get(), Items.DIAMOND);

            // ALL ZOMBIE ENTITIES ARE PLACEHOLDERS
            ENTITIES.put(configInst().bossBabyDropOrder.get(), EntityType.ZOMBIE.create(level));
            ENTITIES.put(configInst().zombieCowDropOrder.get(), EntityType.ZOMBIE.create(level));
            ENTITIES.put(configInst().squidwardTraderDropOrder.get(), EntityType.ZOMBIE.create(level));
            ENTITIES.put(configInst().pigWolfHorseDropOrder.get(), EntityType.PIG.create(level));
            ENTITIES.put(configInst().mcqueenMobileDropOrder.get(), EntityType.ZOMBIE.create(level));
            ENTITIES.put(configInst().skeletonsDropOrder.get(), EntityType.SKELETON.create(level));
            ENTITIES.put(configInst().petToothlessDropOrder.get(), EntityType.ZOMBIE.create(level));
        }
    }

    private static EYCartoonConfig configInst() {
        return EYCartoonConfig.INSTANCE;
    }
}
