package com.nyfaria.eycartoon.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.cap.PlayerHolder;
import com.nyfaria.eycartoon.cap.PlayerHolderAttacher;
import com.nyfaria.eycartoon.config.EYCartoonConfig;
import com.nyfaria.eycartoon.init.ItemInit;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
}
