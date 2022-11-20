package com.nyfaria.eycartoon.event;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.entity.ToothlessEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        LocalPlayer player = (LocalPlayer) event.getEntity();
        if (player != null && player.isPassenger() && player.getVehicle() instanceof ToothlessEntity pegasus) {
            if (player.input.jumping) {
                pegasus.setYya(0.8f);
            } else if (player.input.shiftKeyDown) {
                if (!pegasus.isOnGround()) {
                    pegasus.setYya(-0.8f);
                    player.input.shiftKeyDown = false;
                }
            } else {
                pegasus.setYya(0);
            }
        }
    }
}
