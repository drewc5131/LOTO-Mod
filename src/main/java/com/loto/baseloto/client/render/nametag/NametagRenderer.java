package com.loto.baseloto.client.render.nametag;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;

public class NametagRenderer {

	public RenderManager renderManager;

	public NametagRenderer(RenderManager renderMgr) {
		this.renderManager = renderMgr;
	}

	public void renderLivingLabel(Entity entityIn, String name, double x,
			double y, double z, int maxDistance, boolean renderThroughBlocks) {
		double d0 = entityIn
				.getDistanceSqToEntity(this.renderManager.renderViewEntity);
		double dist = entityIn
				.getDistanceToEntity(this.renderManager.renderViewEntity);

		if (dist <= (double) (maxDistance)) {
			double dirx = this.renderManager.renderViewEntity.posX
					- entityIn.posX;
			double diry = this.renderManager.renderViewEntity.posY
					- entityIn.posY - .5;
			double dirz = this.renderManager.renderViewEntity.posZ
					- entityIn.posZ;

			double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

			dirx /= len;
			diry /= len;
			dirz /= len;

			double pitch = Math.asin(diry);
			double yaw = Math.atan2(dirz, dirx);

			pitch = pitch * 180.0 / Math.PI;
			yaw = yaw * 180.0 / Math.PI;

			yaw += 90f;

			float entityHeight = entityIn.height + 0.5F;
			drawNameplate(name, (float) x, (float) y + entityHeight, (float) z,
					(float) yaw, (float) pitch, Math.sqrt(dist), renderThroughBlocks);
		}
	}

	public void drawNameplate(String name, float x, float y, float z,
			float viewerYaw, float viewerPitch, double distance, boolean renderThroughBlocks) {
		double distanceFromPlayer = Math.min(32, distance);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(viewerPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(-0.025F * distanceFromPlayer * .55, -0.025F
				* distanceFromPlayer * .55, 0.025F * distanceFromPlayer * .55);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		
		if(renderThroughBlocks) {
			GlStateManager.disableDepth();
		}

		GlStateManager.enableBlend();
		GlStateManager
				.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
						GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
		int i = this.renderManager.getFontRenderer().getStringWidth(name) / 2;
		GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		bufferbuilder.pos((double) (-i - 1), -1, 0.0D)
				.color(0.1F, 0.1F, 0.1F, 0.3F).endVertex();
		bufferbuilder.pos((double) (-i - 1), 8, 0.0D)
				.color(0.1F, 0.1F, 0.1F, 0.3F).endVertex();
		bufferbuilder.pos((double) (i + 1), 8, 0.0D)
				.color(0.1F, 0.1F, 0.1F, 0.3F).endVertex();
		bufferbuilder.pos((double) (i + 1), -1, 0.0D)
				.color(0.1F, 0.1F, 0.1F, 0.3F).endVertex();
		tessellator.draw();

		GlStateManager.enableTexture2D();

		this.renderManager.getFontRenderer().drawString(name,
				-this.renderManager.getFontRenderer().getStringWidth(name) / 2,
				0, -1);
		
		if(renderThroughBlocks) {
			GlStateManager.enableDepth();
		}

		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}
