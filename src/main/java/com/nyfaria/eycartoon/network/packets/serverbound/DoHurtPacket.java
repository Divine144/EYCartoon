package com.nyfaria.eycartoon.network.packets.serverbound;

import dev._100media.capabilitysyncer.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class DoHurtPacket implements IPacket {

    public float damage;

    public DoHurtPacket(float damage) {
        super();
        this.damage = damage;

    }

    public static DoHurtPacket read(FriendlyByteBuf packetBuf) {
        return new DoHurtPacket(packetBuf.readFloat());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, DoHurtPacket.class, DoHurtPacket::read);
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        if (context.getSender() != null) {
            context.getSender().level.getEntitiesOfClass(LivingEntity.class, context.getSender().getBoundingBox().inflate(2), e -> e != context.getSender()).forEach(le -> le.hurt(DamageSource.playerAttack(context.getSender()), damage));
        }
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeFloat(damage);
    }
}
