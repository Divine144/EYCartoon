package com.nyfaria.eycartoon.event;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.ToothlessEntity;
import com.nyfaria.eycartoon.network.NetworkHandler;
import com.nyfaria.eycartoon.network.packets.serverbound.DragonFlyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onInputUpdate(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof ToothlessEntity entity) {
                if (player.input.jumping) {
                    NetworkHandler.INSTANCE.sendToServer(new DragonFlyPacket(true));
                }
                else if (player.input.shiftKeyDown) {
                    if (!entity.isOnGround()) {
                        NetworkHandler.INSTANCE.sendToServer(new DragonFlyPacket(false));
                        player.input.shiftKeyDown = false;
                    }
                }
                else {
                    entity.setYya(0);
                }
            }
        }
    }
}
