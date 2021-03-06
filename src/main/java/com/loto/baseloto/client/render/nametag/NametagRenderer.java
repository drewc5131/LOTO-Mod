package com.loto.baseloto.client.render.nametag;

import org.lwjgl.opengl.GL11;

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
				.getDistanceSq(this.renderManager.renderViewEntity);
		double dist = entityIn
				.getDistance(this.renderManager.renderViewEntity);

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
					(float) yaw, this.renderManager.renderViewEntity.rotationPitch, Math.sqrt(dist),
					renderThroughBlocks);
		}
	}

	public void drawNameplate(String name, float x, float y, float z,
			float viewerYaw, float viewerPitch, double distance,
			boolean renderThroughBlocks) {
		double distanceFromPlayer = Math.max(Math.min(32, distance), 2);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotated(-viewerYaw, 0.0d, 1.0d, 0.0d);
		GL11.glRotated(viewerPitch, 1.0d, 0.0d, 0.0d);
		GL11.glScaled(-0.025d * distanceFromPlayer * .45, -0.025d
				* distanceFromPlayer * .45, 0.025d * distanceFromPlayer * .55);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);

		if (renderThroughBlocks) {
			GL11.glDisable(GL11.GL_DEPTH);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager
				.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
						GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
		int i = this.renderManager.getFontRenderer().getStringWidth(name) / 2;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0.1F, 0.1F, 0.1F, 0.3F);
		GL11.glVertex2d((-i - 1), -1);
		GL11.glVertex2d((-i - 1), 8);
		GL11.glVertex2d((i + 1), 8);
		GL11.glVertex2d((i + 1), -1);
		GL11.glColor4f(1F, 1F,1F, 1F);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_POLYGON_SMOOTH);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.renderManager.getFontRenderer().drawString(name,
				-this.renderManager.getFontRenderer().getStringWidth(name) / 2,
				0, -1, true);

		if (renderThroughBlocks) {
			GL11.glEnable(GL11.GL_DEPTH);
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

}
