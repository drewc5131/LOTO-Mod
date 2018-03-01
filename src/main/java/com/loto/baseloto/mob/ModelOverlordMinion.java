package com.loto.baseloto.mob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

public class ModelOverlordMinion extends ModelBiped {
	@Override
    protected EnumHandSide getMainHand(Entity entityIn)
    {
        return EnumHandSide.RIGHT;
   
    }
}
