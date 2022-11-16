package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.block.CashRegisterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EYCartoon.MODID);
    public static final RegistryObject<BlockEntityType<CashRegisterBlockEntity>> REGISTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("cash_register_block_entity", () -> BlockEntityType.Builder.of(CashRegisterBlockEntity::new, BlockInit.CASH_REGISTER_BLOCK.get()).build(null));
}
