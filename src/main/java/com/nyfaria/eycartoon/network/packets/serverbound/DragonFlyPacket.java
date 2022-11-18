package com.nyfaria.eycartoon.network.packets.serverbound;

import com.nyfaria.eycartoon.entity.ToothlessEntity;
import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class DragonFlyPacket implements IPacket {
    public boolean upDown;

    public DragonFlyPacket(boolean upDown) {
        this.upDown = upDown;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player != null) {
            if (player.getVehicle() instanceof ToothlessEntity dragon) {
                if (!player.level.getBlockState(dragon.blockPosition().below()).isAir() && upDown) {
                    dragon.setPos(dragon.getX(), dragon.getY() + 1, dragon.getZ());
                }
                dragon.setDeltaMovement(dragon.getDeltaMovement().x, upDown ? 0.8 : -0.8, dragon.getDeltaMovement().z);
                player.hurtMarked = true;
                dragon.hurtMarked = true;
            }
        }
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeBoolean(upDown);
    }

    public static DragonFlyPacket read(FriendlyByteBuf packetBuf) {
        return new DragonFlyPacket(packetBuf.readBoolean());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, DragonFlyPacket.class, DragonFlyPacket::read);
    }
}
