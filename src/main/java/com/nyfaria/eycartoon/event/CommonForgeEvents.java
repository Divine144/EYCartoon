package com.nyfaria.eycartoon.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.cap.PlayerHolder;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.config.EYCartoonConfig;
import com.nyfaria.eycartoon.init.BlockInit;
import com.nyfaria.eycartoon.init.ItemInit;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("eycartoon")
                .then(Commands.literal("cartoonBlocksMined")
                        .then(Commands.literal("set")
                                .then(Commands.argument("blocksMinedAmount", IntegerArgumentType.integer())
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    Player player = EntityArgument.getPlayer(context, "player");
                                                    int amount = IntegerArgumentType.getInteger(context, "blocksMinedAmount");
                                                    PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> p.setBlocksMined(amount));
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                        )
                                )
                        )
                )
        );
    }

    @SubscribeEvent
    public static void onStartEating(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player && !player.level.isClientSide) {
            if (event.getItem().getItem() == ItemInit.SONIC_COIN.get() && !player.isOnGround()) {
                event.setDuration(0); // Cancel eating
            }
        }
    }

    @SubscribeEvent
    public static void onFinishEating(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player && !player.level.isClientSide) {
            boolean canJump = PlayerHolderAttacher.getPlayerHolder(player).map(PlayerHolder::isCanDoubleJump).orElse(false);
            if (event.getResultStack().getItem() == ItemInit.SONIC_COIN.get() && !canJump) {
                PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> p.setCanDoubleJump(true));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player != null && !event.player.level.isClientSide && event.phase == TickEvent.Phase.END) {
            Player player = event.player;
            GlobalPos redstonePos = EYCartoonConfig.INSTANCE.enableIceAbilityRedstonePos.apply(player.level);
            if (redstonePos != null && player.level.getServer() != null) {
                ServerLevel otherDimLevel = player.level.getServer().getLevel(redstonePos.dimension());
                if (otherDimLevel != null) {
                    boolean unlockedIce = PlayerHolderAttacher.getPlayerHolder(player).map(PlayerHolder::hasUnlockedIceAbility).orElse(false);
                    if (player.level.getBlockState(redstonePos.pos()).is(Blocks.REDSTONE_BLOCK) && !unlockedIce) {
                        PlayerHolderAttacher.getPlayerHolder(player).ifPresent(PlayerHolder::enableIceAbility);
                    }
                }
            }
        }
    }

    private static final List<Item> RANDOM_VANILLA_ITEMS = ForgeRegistries.ITEMS.getValues().stream()
            .filter(p -> !Objects.equals(p.getCreatorModId(p.getDefaultInstance()), EYCartoon.MODID))
            .filter(p -> p != Items.AIR)
            .collect(Collectors.toList());

    private static final Map<Integer, Item> ITEMS = new HashMap<>();
    private static final Map<Integer, LivingEntity> ENTITIES = new HashMap<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().level.isClientSide) {
            Player player = event.getPlayer();
            BlockPos pos = event.getPos();
            Level level = player.level;
            if (player.getMainHandItem().is(ItemInit.SHREKS_FIST.get())) {
                if (level.getBlockState(event.getPos()).is(Blocks.BARRIER) && event.isCancelable()) event.setCanceled(true);
                level.explode(player, pos.getX(), pos.getY(), pos.getZ(), 4.0F, Explosion.BlockInteraction.NONE);
            }
            // From Cartoon block originally
            if (level.getBlockState(pos).is(BlockInit.CARTOON_BLOCK.get())) {
                if (!player.getAbilities().instabuild) {
                    if (!level.isClientSide) {
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
                    event.setCanceled(true);
                }
            }
        }
    }

    private static Item getRandomVanillaDrop(RandomSource source) {
        return Util.getRandom(RANDOM_VANILLA_ITEMS, source);
    }

    private static void setupItemPosition(ItemStack stack, Level level, Vec3 at) {
        ItemEntity itementity = new ItemEntity(level, at.x(), at.y() + 0.6, at.z(), stack);
        itementity.setDefaultPickUpDelay();
        itementity.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(itementity);
    }

    private static void spawnEntitiesOfType(LivingEntity entity, Level level, Vec3 at, int count) {
        for (int i = 0; i < count; i++) {
            var type = entity.getType().create(level);
            if (type != null) {
                type.setPos(at.x, at.y, at.z);
                level.addFreshEntity(type);
            }
        }
    }

    public static int getCount(int blocksMinedCount) {
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
            ITEMS.put(configInst().shreksFistsDropOrder.get(), ItemInit.SHREKS_FIST.get());
            ITEMS.put(configInst().cartoonRayDropOrder.get(), ItemInit.CARTOON_RAY.get());
            ITEMS.put(configInst().buzzControlPanelDropOrder.get(), ItemInit.BUZZ_CONTROL_PANEL.get());
            ITEMS.put(configInst().ladybugYoYoDropOrder.get(), ItemInit.LADYBUG_YOYO.get());
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
