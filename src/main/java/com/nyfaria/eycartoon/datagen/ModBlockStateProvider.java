package com.nyfaria.eycartoon.datagen;

import com.nyfaria.eycartoon.EYCartoon;
import com.nyfaria.eycartoon.init.BlockInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, EYCartoon.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    // Stream.of(
    //
    //         )
    //         .map(Supplier::get)
    //         .forEach(this::simpleCubeBottomTopBlockState);
    //
    // Stream.of(
    //
    // ).map(Supplier::get)
    //         .forEach(this::simpleBlock);
    }

    protected void simpleCubeBottomTopBlockState(Block block) {
        simpleBlock(block, blockCubeTopModel(block));
    }

    protected BlockModelBuilder blockCubeTopModel(Block block) {
        String name = getName(block);
        return models().cubeBottomTop(name, modLoc("block/" + name + "_side"), modLoc("block/" + name + "_bottom"), modLoc("block/" + name + "_top"));
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}
