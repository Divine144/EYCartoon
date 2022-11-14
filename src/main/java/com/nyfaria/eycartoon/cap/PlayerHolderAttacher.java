package com.nyfaria.eycartoon.cap;

import com.nyfaria.eycartoon.EYCartoon;
import dev._100media.capabilitysyncer.core.CapabilityAttacher;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EYCartoon.MODID)
public class PlayerHolderAttacher extends CapabilityAttacher {
    public static final Capability<PlayerHolder> PLAYER_CAPABILITY = getCapability(new CapabilityToken<>() {});
    public static final ResourceLocation PLAYER_RL = new ResourceLocation(EYCartoon.MODID, "example");
    private static final Class<PlayerHolder> CAPABILITY_CLASS = PlayerHolder.class;

    @SuppressWarnings("ConstantConditions")
    public static PlayerHolder getPlayerHolderUnwrap(Entity player) {
        return getPlayerHolder(player).orElse(null);
    }

    public static LazyOptional<PlayerHolder> getPlayerHolder(Entity player) {
        return player.getCapability(PLAYER_CAPABILITY);
    }

    private static void attach(AttachCapabilitiesEvent<Entity> event, Entity entity) {
        genericAttachCapability(event, new PlayerHolder(entity), PLAYER_CAPABILITY, PLAYER_RL);
    }

    public static void register() {
        CapabilityAttacher.registerCapability(CAPABILITY_CLASS);
        CapabilityAttacher.registerEntityAttacher(LivingEntity.class, PlayerHolderAttacher::attach, PlayerHolderAttacher::getPlayerHolder);
        SimpleEntityCapabilityStatusPacket.registerRetriever(PLAYER_RL, PlayerHolderAttacher::getPlayerHolderUnwrap);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        // So we can copy capabilities
        oldPlayer.revive();

        getPlayerHolder(oldPlayer).ifPresent(oldAbilityHolder -> getPlayerHolder(newPlayer)
                .ifPresent(newAbilityHolder -> newAbilityHolder.deserializeNBT(oldAbilityHolder.serializeNBT(false), false)));
    }
}
