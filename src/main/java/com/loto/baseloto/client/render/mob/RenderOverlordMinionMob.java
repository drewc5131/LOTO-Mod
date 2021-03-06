package com.loto.baseloto.client.render.mob;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.loto.baseloto.client.render.nametag.NametagRenderer;
import com.loto.baseloto.mob.EntityOverlordMinionMob;
import com.loto.baseloto.mob.ModelOverlordMinion;

@SideOnly(Side.CLIENT)
public class RenderOverlordMinionMob extends RenderBiped {
	public NametagRenderer nametagRenderer;
	private static final ResourceLocation mobTextures = new ResourceLocation(
			"baseloto:textures/entity/gsgame_loto_mob_overlordminion.png");

	public RenderOverlordMinionMob(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelOverlordMinion(), 0.4f);
		nametagRenderer = new NametagRenderer(renderManagerIn);

	}

	protected ResourceLocation getEntityTexture(EntityOverlordMinionMob entity) {
		return mobTextures;
	}

	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityOverlordMinionMob) entity);
	}

	@Override
	protected void renderLivingLabel(Entity entityIn, String name, double x,
			double y, double z, int maxDistance) {
		this.nametagRenderer.renderLivingLabel(entityIn, name, x, y, z,
				maxDistance, false);
	}
}
