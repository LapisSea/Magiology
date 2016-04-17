package com.magiology.mcobjects.effect;

import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@Deprecated
public abstract class EntityFXMDeprecated extends EntityFX{
	public static final float p=1F/16F;
	protected static WorldRenderer tess=TessUtil.getWR();
	public EntityFXMDeprecated(World world, double x, double y, double z, double xSpeed, double ySpeed,double zSpeed){
		super(world, x,y,z, xSpeed, ySpeed, zSpeed);
	}
	
	public abstract int getRenderPass();
	public void renderParticle(VertexRenderer buf, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
	{
		float f6 = particleTextureIndexX / 16.0F;
		float f7 = f6 + 0.0624375F;
		float f8 = particleTextureIndexY / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * particleScale;

		if (particleIcon != null)
		{
			f6 = particleIcon.getMinU();
			f7 = particleIcon.getMaxU();
			f8 = particleIcon.getMinV();
			f9 = particleIcon.getMaxV();
		}

		float f11 = (float)(prevPosX + (posX - prevPosX) * p_70539_2_ - interpPosX);
		float f12 = (float)(prevPosY + (posY - prevPosY) * p_70539_2_ - interpPosY);
		float f13 = (float)(prevPosZ + (posZ - prevPosZ) * p_70539_2_ - interpPosZ);
		tess.color(particleRed, particleGreen, particleBlue, particleAlpha);
		buf.addVertexWithUV(f11 - p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 - p_70539_5_ * f10 - p_70539_7_ * f10, f7, f9);
		buf.addVertexWithUV(f11 - p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 - p_70539_5_ * f10 + p_70539_7_ * f10, f7, f8);
		buf.addVertexWithUV(f11 + p_70539_3_ * f10 + p_70539_6_ * f10, f12 + p_70539_4_ * f10, f13 + p_70539_5_ * f10 + p_70539_7_ * f10, f6, f8);
		buf.addVertexWithUV(f11 + p_70539_3_ * f10 - p_70539_6_ * f10, f12 - p_70539_4_ * f10, f13 + p_70539_5_ * f10 - p_70539_7_ * f10, f6, f9);
	}
}
