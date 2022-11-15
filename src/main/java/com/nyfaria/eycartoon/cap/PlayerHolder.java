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
    private boolean hasUnlockedIceAbility;
    private boolean canDoubleJump;

    protected PlayerHolder(Entity entity) {
        super(entity);
        this.hasUnlockedIceAbility = false;
        this.canDoubleJump = false;
    }

    public int getBlocksMinedCount() {
        return blocksMinedCount;
    }

    public void addBlocksMined() {
        ++this.blocksMinedCount;
    }

    public void setBlocksMined(int blocksMinedCount) {
        this.blocksMinedCount = blocksMinedCount;
    }

    public boolean hasUnlockedIceAbility() {
        return this.hasUnlockedIceAbility;
    }

    public void enableIceAbility() {
        this.hasUnlockedIceAbility = true;
    }

    public boolean isCanDoubleJump() {
        return canDoubleJump;
    }

    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("blocksMinedCount", this.blocksMinedCount);
        tag.putBoolean("hasUnlockedIceAbility", this.hasUnlockedIceAbility);
        tag.putBoolean("canDoubleJump", this.canDoubleJump);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.blocksMinedCount = nbt.getInt("blocksMinedCount");
        this.hasUnlockedIceAbility = nbt.getBoolean("hasUnlockedIceAbility");
        this.canDoubleJump = nbt.getBoolean("canDoubleJump");
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
