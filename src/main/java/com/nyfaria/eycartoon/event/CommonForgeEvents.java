package com.nyfaria.eycartoon.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.cap.PlayerHolder;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.config.EYCartoonConfig;
import com.nyfaria.eycartoon.entity.BossBabyEntity;
import com.nyfaria.eycartoon.entity.LightningMcQueenEntity;
import com.nyfaria.eycartoon.entity.MegaSnowGolemEntity;
import com.nyfaria.eycartoon.init.*;
import dev._100media.hundredmediamorphs.capability.MorphHolderAttacher;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.DragonFireballRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
            var effectInstance = player.getEffect(MobEffects.LEVITATION);
            if (effectInstance != null && !player.hasEffect(MobEffectInit.FART_LEVITATION_EFFECT.get())) {
                if (effectInstance.getDuration() > 2_000_000) {
                    player.removeEffect(MobEffects.LEVITATION);
                }
            }
            GlobalPos redstonePos = configInst().enableIceAbilityRedstonePos.apply(player.level);
            if (redstonePos != null && player.level.getServer() != null) {
                ServerLevel otherDimLevel = player.level.getServer().getLevel(redstonePos.dimension());
                if (otherDimLevel != null) {
                    if (player.level.getBlockState(redstonePos.pos()).is(Blocks.REDSTONE_BLOCK)) {
                        PlayerHolderAttacher.getPlayerHolder(player).ifPresent(PlayerHolder::enableIceAbility);
                    }
                    else {
                        PlayerHolderAttacher.getPlayerHolder(player).ifPresent(PlayerHolder::disableIceAbility);
                    }
                }
            }
            GlobalPos megaGolemPos = configInst().megaSnowGolemSpawnPos.apply(player.level);
            if (megaGolemPos != null && player.level.getServer() != null) {
                ServerLevel otherDimLevel = player.level.getServer().getLevel(megaGolemPos.dimension());
                if (otherDimLevel != null) {
                    boolean spawnedGolem = PlayerHolderAttacher.getPlayerHolder(player).map(PlayerHolder::isHasMegaGolemSpawned).orElse(false);
                    if (megaGolemPos.pos().closerToCenterThan(player.position(), 2D) && !spawnedGolem) {
                        PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> p.setHasMegaGolemSpawned(true));
                        MegaSnowGolemEntity megaSnowGolem = new MegaSnowGolemEntity(EntityInit.MEGA_SNOW_GOLEM.get(), player.level);
                        megaSnowGolem.setPos(new Vec3(megaGolemPos.pos().getX(), megaGolemPos.pos().getY(), megaGolemPos.pos().getZ()));
                        player.level.addFreshEntity(megaSnowGolem);
                    }
                }
            }
            if (player.getInventory().findSlotMatchingItem(new ItemStack(ItemInit.LEMUR_FEET.get())) != -1) {
                player.getInventory().getItem(player.getInventory().findSlotMatchingItem(new ItemStack(ItemInit.LEMUR_FEET.get()))).shrink(1);
            }
            if (!player.getItemBySlot(EquipmentSlot.HEAD).is(ItemInit.JULIENS_CROWN.get())) {
                player.removeEffect(MobEffects.JUMP);
                if (player.getItemBySlot(EquipmentSlot.FEET).is(ItemInit.LEMUR_FEET.get())) {
                    player.setItemSlot(EquipmentSlot.FEET, PlayerHolderAttacher.getPlayerHolder(player).map(PlayerHolder::getPreviousBoots).orElse(ItemStack.EMPTY));
                    PlayerHolderAttacher.getPlayerHolder(player).ifPresent(p -> p.setPreviousBoots(ItemStack.EMPTY));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player && !player.level.isClientSide && player.getVehicle() instanceof LightningMcQueenEntity) {
            if (event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.IN_FIRE) {
                player.setSecondsOnFire(0);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity() != null) {
            ItemStack stack = event.getStack();
            doItemNameChange(stack);
        }
    }

    @SubscribeEvent
    public static void onToolTipHover(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getEntity() != null) {
            doItemNameChange(stack);
        }
    }

    // PLEASE forgive me for my sins
    private static void doItemNameChange(ItemStack stack) {
        if (stack.is(Blocks.ICE.asItem())) {
            stack.setHoverName(Component.literal("Elsa's Ice"));
        }
        else if (stack.is(Blocks.SLIME_BLOCK.asItem())) {
            stack.setHoverName(Component.literal("Shrek's Boogers"));
        }
        else if (stack.is(Blocks.SPONGE.asItem())) {
            stack.setHoverName(Component.literal("Spongebob's Mum"));
        }
        else if (stack.is(Blocks.RED_CONCRETE.asItem())) {
            stack.setHoverName(Component.literal("Lightning McQueen's Paint"));
        }
        else if (stack.is(Blocks.BLUE_CONCRETE.asItem())) {
            stack.setHoverName(Component.literal("Squidward's Home"));
        }
        else if (stack.is(Blocks.WHITE_CONCRETE.asItem()) || stack.is(Blocks.WHITE_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Mickey Mouse's Glove Fragment"));
        }
        else if (stack.is(Blocks.ORANGE_CONCRETE.asItem()) || stack.is(Blocks.ORANGE_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Garfield's Hairball"));
        }
        else if (stack.is(Blocks.MAGENTA_CONCRETE.asItem()) || stack.is(Blocks.MAGENTA_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Tinky Winky's Father"));
        }
        else if (stack.is(Blocks.LIGHT_BLUE_CONCRETE.asItem()) || stack.is(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Sonic's Kid"));
        }
        else if (stack.is(Blocks.YELLOW_CONCRETE.asItem()) || stack.is(Blocks.YELLOW_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Jake the Dog"));
        }
        else if (stack.is(Blocks.LIME_CONCRETE.asItem()) || stack.is(Blocks.LIME_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Plankton's Hideout"));
        }
        else if (stack.is(Blocks.PINK_CONCRETE.asItem()) || stack.is(Blocks.PINK_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Amy's Skin"));
        }
        else if (stack.is(Blocks.GRAY_CONCRETE.asItem()) || stack.is(Blocks.GRAY_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Tom's Hairball"));
        }
        else if (stack.is(Blocks.LIGHT_GRAY_CONCRETE.asItem()) || stack.is(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Tom's Hair"));
        }
        else if (stack.is(Blocks.CYAN_CONCRETE.asItem()) || stack.is(Blocks.CYAN_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Cyan Innocent's Remains"));
        }
        else if (stack.is(Blocks.PURPLE_CONCRETE.asItem()) || stack.is(Blocks.PURPLE_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Purple Guy's Phone"));
        }
        else if (stack.is(Blocks.BROWN_CONCRETE.asItem()) || stack.is(Blocks.BROWN_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Poop"));
        }
        else if (stack.is(Blocks.GREEN_CONCRETE.asItem()) || stack.is(Blocks.GREEN_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Shrek's Poop"));
        }
        else if (stack.is(Blocks.BLACK_CONCRETE.asItem()) || stack.is(Blocks.BLACK_GLAZED_TERRACOTTA.asItem())) {
            stack.setHoverName(Component.literal("Ladybug's Yoyo except its all black and its a cube"));
        }
        else if (stack.is(Items.CARROT)) {
            stack.setHoverName(Component.literal("Olaf's Nose"));
        }
        else if (stack.is(Items.GOLDEN_APPLE)) {
            stack.setHoverName(Component.literal("Spongebob Infused Apple"));
        }
        else if (stack.is(Items.TOTEM_OF_UNDYING)) {
            stack.setHoverName(Component.literal("Ugly Minion"));
        }
        else if (stack.is(Items.STICK)) {
            stack.setHoverName(Component.literal("Spongebob's Legs"));
        }
        else if (stack.is(Items.LEATHER_BOOTS) && stack.getItem() instanceof DyeableLeatherItem item && item.getColor(stack) == 0x0000FF) {
            stack.setHoverName(Component.literal("Sonic's Boots"));
        }
        else if (stack.is(Items.POTION)) {
            var potion = PotionUtils.getPotion(stack);
            if (!potion.getEffects().isEmpty()) {
                if (potion.getEffects().get(0).getEffect() == MobEffects.MOVEMENT_SPEED && potion.getEffects().get(0).getAmplifier() == 1) {
                    stack.setHoverName(Component.literal("Sonic's Pee"));
                }
            }
        }
        else if (stack.is(Items.INK_SAC)) {
            stack.setHoverName(Component.literal("Squidward's Sac"));
        }
    }

    private static final List<Item> RANDOM_VANILLA_ITEMS = List.of(Blocks.ICE.asItem(), Blocks.SLIME_BLOCK.asItem(), Blocks.SPONGE.asItem(), Blocks.RED_CONCRETE.asItem(),
            Blocks.BLUE_CONCRETE.asItem(), Blocks.WHITE_CONCRETE.asItem(), Blocks.ORANGE_CONCRETE.asItem(), Blocks.MAGENTA_CONCRETE.asItem(), Blocks.LIGHT_BLUE_CONCRETE.asItem(),
            Blocks.LIME_CONCRETE.asItem(), Blocks.PINK_CONCRETE.asItem(), Blocks.GRAY_CONCRETE.asItem(), Blocks.LIGHT_GRAY_CONCRETE.asItem(), Blocks.CYAN_CONCRETE.asItem(),
            Blocks.PURPLE_CONCRETE.asItem(), Blocks.BROWN_CONCRETE.asItem(), Blocks.GREEN_CONCRETE.asItem(), Blocks.BLACK_CONCRETE.asItem(), Items.CARROT, Items.GOLDEN_APPLE,
            Items.TOTEM_OF_UNDYING, Items.STICK, Items.LEATHER_BOOTS.asItem(), Items.POTION, Items.INK_SAC, Blocks.YELLOW_CONCRETE.asItem());


    private static final Map<Integer, Item> ITEMS = new HashMap<>();
    private static final Map<Integer, LivingEntity> ENTITIES = new HashMap<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().level.isClientSide) {
            Player player = event.getPlayer();
            BlockPos pos = event.getPos();
            Level level = player.level;
            if (player.getMainHandItem().is(ItemInit.SHREKS_FIST.get())) {
                level.explode(player, pos.getX(), pos.getY(), pos.getZ(), 4.0F, Explosion.BlockInteraction.NONE);
            }
            // From Cartoon block originally
            if (level.getBlockState(pos).is(BlockInit.CARTOON_BLOCK.get())) {
                if (!player.getAbilities().instabuild) {
                    if (!level.isClientSide) {
                        initializeMaps(player, level); // Assign values to maps
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
                                if (configInst().pigWolfHorseDropOrder.get() == p.getBlocksMinedCount()) {
                                    spawnEntitiesOfType(EntityType.PIG.create(level), level, Vec3.atBottomCenterOf(pos.above()), 1);
                                    spawnEntitiesOfType(EntityType.WOLF.create(level), level, Vec3.atBottomCenterOf(pos.above()), 1);
                                    spawnEntitiesOfType(EntityType.HORSE.create(level), level, Vec3.atBottomCenterOf(pos.above()), 1);
                                }
                                else {
                                    spawnEntitiesOfType(entity, level, Vec3.atBottomCenterOf(pos.above()), itemOrEntityCount);
                                }
                            }
                            else {
                                Item randomVanillaDrop = getRandomVanillaDrop(player.getRandom());
                                ItemStack stack = new ItemStack(randomVanillaDrop);
                                if (randomVanillaDrop.asItem() instanceof DyeableLeatherItem dyableItem) {
                                    dyableItem.setColor(stack, 0x0000FF);
                                }
                                else if (randomVanillaDrop instanceof PotionItem) {
                                    PotionUtils.setPotion(stack, Potions.STRONG_SWIFTNESS);
                                }
                                setupItemPosition(stack, level, Vec3.atCenterOf(pos));
                            }
                        });
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
        if (configInst().krabbyPattyDropOrder.get() == blocksMinedCount) {
            count = 10;
        }
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

    private static void initializeMaps(Player owner, Level level) {
        if (ITEMS.isEmpty() && ENTITIES.isEmpty()) {
            ITEMS.put(configInst().minionLaunchedDropOrder.get(), ItemInit.MINION_LAUNCHER.get());
            ITEMS.put(configInst().krabbyPattyDropOrder.get(), ItemInit.KRABBY_PATTY.get());
            ITEMS.put(configInst().cobbleStoneDropOrder.get(), Items.COBBLESTONE);
            ITEMS.put(configInst().shreksFistsDropOrder.get(), ItemInit.SHREKS_FIST.get());
            ITEMS.put(configInst().cartoonRayDropOrder.get(), ItemInit.CARTOON_RAY.get());
            ITEMS.put(configInst().buzzControlPanelDropOrder.get(), ItemInit.BUZZ_CONTROL_PANEL.get());
            ITEMS.put(configInst().ladybugYoYoDropOrder.get(), ItemInit.LADYBUG_YOYO.get());
            ITEMS.put(configInst().sonicCoinsDropOrder.get(), ItemInit.SONIC_COIN.get());
            ITEMS.put(configInst().sonicBootsDropOrder.get(), ItemInit.SONIC_BOOTS.get());
            ITEMS.put(configInst().kingJulienCrownDropOrder.get(), ItemInit.JULIENS_CROWN.get());
            ITEMS.put(configInst().diamondsDropOrder.get(), Items.DIAMOND);

            BossBabyEntity baby = new BossBabyEntity(EntityInit.BOSS_BABY.get(), level, owner);
            ENTITIES.put(configInst().bossBabyDropOrder.get(), baby);
            ENTITIES.put(configInst().zombieCowDropOrder.get(), EntityType.ZOMBIE.create(level));
            ENTITIES.put(configInst().squidwardTraderDropOrder.get(), EntityInit.SQUIDWARD_TRADER_ENTITY.get().create(level));
            ENTITIES.put(configInst().pigWolfHorseDropOrder.get(), EntityType.PIG.create(level));
            ENTITIES.put(configInst().mcqueenMobileDropOrder.get(), EntityInit.LIGHTNING_MCQUEEN.get().create(level));
            ENTITIES.put(configInst().skeletonsDropOrder.get(), EntityType.SKELETON.create(level));
            ENTITIES.put(configInst().petToothlessDropOrder.get(), EntityInit.TOOTHLESS_ENTITY.get().create(level));
        }
    }

    private static EYCartoonConfig configInst() {
        return EYCartoonConfig.INSTANCE;
    }
}
