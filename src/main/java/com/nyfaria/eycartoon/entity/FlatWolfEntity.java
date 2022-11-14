package com.nyfaria.eycartoon.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

public class FlatWolfEntity extends Wolf {
    // Just gotta change the model ig
    public FlatWolfEntity(EntityType<? extends Wolf> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
