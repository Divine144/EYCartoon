package com.nyfaria.eycartoon.init;

import com.nyfaria.eycartoon.block.CartoonBlock;
import com.nyfaria.eycartoon.block.CashRegisterBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.nyfaria.eycartoon.EYCartoon.MODID;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<CartoonBlock> CARTOON_BLOCK = registerBlock("cartoon_block",
            () -> new CartoonBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.2F)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<CashRegisterBlock> CASH_REGISTER_BLOCK = registerBlock("squidwards_cash_register", () -> new CashRegisterBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).noOcclusion().strength(2F, 60.0F)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
