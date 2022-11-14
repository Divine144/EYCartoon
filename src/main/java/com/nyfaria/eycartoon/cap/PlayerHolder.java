package com.nyfaria.eycartoon.cap;

import com.nyfaria.eycartoon.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.EntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.simple.SimpleChannel;

public class PlayerHolder extends EntityCapability {

    private int blocksMinedCount;

    protected PlayerHolder(Entity entity) {
        super(entity);
    }

    public int getBlocksMinedCount() {
        return blocksMinedCount;
    }

    public void addBlocksMined() {
        ++this.blocksMinedCount;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("blocksMinedCount", this.blocksMinedCount);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.blocksMinedCount = nbt.getInt("blocksMinedCount");
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.entity.getId(), PlayerHolderAttacher.PLAYER_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return NetworkHandler.INSTANCE;
    }
}
