package com.nyfaria.eycartoon.cap;

import com.nyfaria.eycartoon.network.NetworkHandler;
import dev._100media.capabilitysyncer.core.EntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

public class PlayerHolder extends EntityCapability {

    private int blocksMinedCount;
    private boolean hasUnlockedIceAbility;
    private boolean canDoubleJump;
    private boolean hasMegaGolemSpawned;
    private float baseSpeed;
    private ItemStack previousBoots;

    protected PlayerHolder(Entity entity) {
        super(entity);
        this.hasUnlockedIceAbility = false;
        this.canDoubleJump = false;
        this.hasMegaGolemSpawned = false;
        this.previousBoots = ItemStack.EMPTY;
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

    public void disableIceAbility() {
        this.hasUnlockedIceAbility = false;
    }

    public boolean isCanDoubleJump() {
        return canDoubleJump;
    }

    public void setCanDoubleJump(boolean canDoubleJump) {
        this.canDoubleJump = canDoubleJump;
    }

    public boolean isHasMegaGolemSpawned() {
        return this.hasMegaGolemSpawned;
    }

    public void setHasMegaGolemSpawned(boolean hasMegaGolemSpawned) {
        this.hasMegaGolemSpawned = hasMegaGolemSpawned;
    }

    public float getBaseSpeed() {
        return this.baseSpeed;
    }

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public @NotNull ItemStack getPreviousBoots() {
        return this.previousBoots;
    }

    public void setPreviousBoots(ItemStack previousBoots) {
        this.previousBoots = previousBoots;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("blocksMinedCount", this.blocksMinedCount);
        tag.putBoolean("hasUnlockedIceAbility", this.hasUnlockedIceAbility);
        tag.putBoolean("canDoubleJump", this.canDoubleJump);
        tag.putBoolean("hasMegaGolemSpawned", this.hasMegaGolemSpawned);
        tag.put("previousBoots", this.previousBoots.save(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.blocksMinedCount = nbt.getInt("blocksMinedCount");
        this.hasUnlockedIceAbility = nbt.getBoolean("hasUnlockedIceAbility");
        this.canDoubleJump = nbt.getBoolean("canDoubleJump");
        this.hasMegaGolemSpawned = nbt.getBoolean("hasMegaGolemSpawned");
        if (nbt.get("previousBoots") instanceof CompoundTag tag) {
            this.previousBoots = ItemStack.of(tag);
        }
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
