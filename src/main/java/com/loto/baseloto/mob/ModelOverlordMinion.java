package com.loto.baseloto.mob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;

public class ModelOverlordMinion extends ModelBiped {
	
    public ModelOverlordMinion()
    {
        this(0.0F);
    }

    public ModelOverlordMinion(float modelSize)
    {
        super(modelSize, 0.0F, 64, 64);
    }
	@Override
    protected EnumHandSide getMainHand(Entity entityIn)
    {
        return EnumHandSide.RIGHT;
   
    }
}
