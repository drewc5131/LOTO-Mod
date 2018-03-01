package com.loto.baseloto.mob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderOverlordMinionMob extends RenderBiped {

	private static final ResourceLocation mobTextures = new ResourceLocation("baseloto:textures/entity/OverlordMinion.png");
	
	public RenderOverlordMinionMob(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelOverlordMinion(), 0.5f);
		
	}

	protected ResourceLocation getEntityTexture(EntityOverlordMinionMob entity)
	{
		return mobTextures;
	}
	
	protected ResourceLocation getEntityTexture(Entity entity){
		return this.getEntityTexture((EntityOverlordMinionMob)entity);
	}

}
