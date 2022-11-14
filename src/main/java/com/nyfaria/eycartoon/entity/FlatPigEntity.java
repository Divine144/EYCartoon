package com.nyfaria.eycartoon.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

public class FlatPigEntity extends Pig {
    // Just gotta change the model ig
    public FlatPigEntity(EntityType<? extends Pig> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
