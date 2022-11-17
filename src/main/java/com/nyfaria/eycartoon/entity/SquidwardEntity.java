package com.nyfaria.eycartoon.entity;

import com.nyfaria.eycartoon.init.BlockInit;
import com.nyfaria.eycartoon.init.ItemInit;
import com.nyfaria.eycartoon.init.ProfessionsInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SquidwardEntity extends Villager {

    public SquidwardEntity(EntityType<? extends Villager> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void customServerAiStep() {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        var data = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setVillagerData(getVillagerData().setProfession(ProfessionsInit.SQUIDWARD.get()));
        this.setOffers(null);
        return data;
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        offers = new MerchantOffers();
        offers.add(new MerchantOffer(new ItemStack(Items.DIAMOND, 10), new ItemStack(BlockInit.CASH_REGISTER_BLOCK.get().asItem()), 5, 1, 1.0f));
        offers.add(new MerchantOffer(new ItemStack(Items.DIAMOND, 1), new ItemStack(ItemInit.KRABBY_PATTY.get()), 64, 1, 1.0f));
        offers.add(new MerchantOffer(new ItemStack(Items.DIAMOND, 25), new ItemStack(ItemInit.SPONGEBOBS_TIE.get()), 5, 1, 1.0f));
        offers.add(new MerchantOffer(new ItemStack(Items.DIAMOND, 15), new ItemStack(ItemInit.PATRICKS_UNDERWEAR.get()), 5, 1, 1.0f));
        return offers;
    }
}
