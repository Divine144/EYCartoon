package com.nyfaria.eycartoon.block;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MinionBlock extends Block {

    public MinionBlock(Properties pProperties) {
        super(pProperties);
    }

    private static final List<Item> RANDOM_VANILLA_ITEMS = ForgeRegistries.ITEMS.getValues().stream()
                                                           .filter(p -> !Objects.equals(p.getCreatorModId(p.getDefaultInstance()), EYCartoon.MODID))
                                                           .filter(p -> p != Items.AIR)
                                                           .collect(Collectors.toList());

    // Need to put items and entities depending on order and config, will do that later
    private static final Map<Integer, Item> ITEMS = new LinkedHashMap<>();
    private static final Map<Integer, LivingEntity> ENTITIES = new LinkedHashMap<>();

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player != null && !level.isClientSide) {
            if (!player.getAbilities().instabuild) {
                if (player.getRandom().nextIntBetweenInclusive(0, 100) >= 10) { // 90% chance -> Blocks mined progression
                    PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> {
                        p.addBlocksMined();
                        Item item = ITEMS.get(p.getBlocksMinedCount());
                        LivingEntity entity = ENTITIES.get(p.getBlocksMinedCount());
                        int itemOrEntityCount = switch (p.getBlocksMinedCount()) {
                            case 3 -> 32;
                            case 6, 13 -> 3;
                            case 14 -> 10;
                            case 18 -> 20;
                            default -> 1;
                        };
                        if (item != null) {
                            ItemStack stack = new ItemStack(ITEMS.get(p.getBlocksMinedCount()));
                            stack.setCount(itemOrEntityCount);
                            setupItemPosition(stack, level, Vec3.atCenterOf(pos));
                        }
                        else if (entity != null) {
                            spawnEntitiesOfType(ENTITIES.get(p.getBlocksMinedCount()), level, Vec3.atBottomCenterOf(pos.above()), itemOrEntityCount);
                        }
                    });
                }
                else {
                    setupItemPosition(new ItemStack(getRandomVanillaDrop(player.getRandom())), level, Vec3.atCenterOf(pos));
                }
            }
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
        var type = entity.getType().create(level);
        for (int i = 0; i < count; i++) {
            if (type != null) {
                type.setPos(at.x, at.y, at.z);
                level.addFreshEntity(type);
            }
        }
    }
}
