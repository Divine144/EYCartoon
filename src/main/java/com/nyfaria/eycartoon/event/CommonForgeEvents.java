package com.nyfaria.eycartoon.event;

import com.nyfaria.eycartoon.EYCartoon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().level.isClientSide) {
            Player player = event.getPlayer();
            Block.getDrops(player.level.getBlockState(event.getPos()), (ServerLevel) player.level,event.getPos(), null).add(Items.STICK.getDefaultInstance());
        }
    }
}
