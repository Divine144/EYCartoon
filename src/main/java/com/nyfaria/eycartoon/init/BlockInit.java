package com.nyfaria.eycartoon.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import static com.nyfaria.eycartoon.EYCartoon.MODID;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public class clazz extends Block {

        public clazz(Properties pProperties) {
            super(pProperties);
        }

        @Override
        public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
            return super.getDrops(pState, pBuilder);
        }
    }
}
