package com.magiology.mcobjects.effect;

import com.magiology.core.MReference;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityBaseFX extends EntityFX{
	
	private static final ResourceLocation texture1 = new ResourceLocation(MReference.MODID+":/textures/particle/SmoothBuble1.png");
	
	double gravity,friction,r_e,g_e,b_e,opacity_e;
	
	public EntityBaseFX(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	public EntityBaseFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, int siz, int lengt){
//		use this if you want to use less customization
		super(w, xp, yp, zp, xs, ys, zs);
		this.xSpeed =xs;
		this.ySpeed =ys;
		this.zSpeed =zs;
		this.particleMaxAge=lengt;
		this.particleScale=siz/10;
		
		this.friction=0.98;
		this.gravity=0;
		this.r_e=1;
		this.g_e=1;
		this.b_e=1;
		this.opacity_e=1;
	}
	
	public EntityBaseFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, int siz, int lengt,double gravit, double Ra,double Ga,double Ba,double opacita,double frictio){
//		delete double Ra,double Ga,double Ba, and set r_e,g_e,b_e to 1 if you want to have a multicolored texture
		super(w, xp, yp, zp, xs, ys, zs);
		this.xSpeed =xs;
		this.ySpeed =ys;
		this.zSpeed =zs;
		this.friction=frictio;
		this.particleMaxAge=lengt;
		this.particleScale=siz/10;
		this.gravity=gravit*0.001;
		this.r_e=Ra;
		this.g_e=Ga;
		this.b_e=Ba;
		this.opacity_e=opacita;
	}
	@Override
	public int getFXLayer(){return 3;}
	public void motionHandler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.xSpeed*=friction;
		this.ySpeed*=friction;
		this.zSpeed*=friction;
		
		
		
		
		
		this.ySpeed +=this.gravity;
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
		
	}
	
	@Override
	public void onUpdate(){
		if(particleAge>particleMaxAge)this.setExpired();
		if(U.getMC().gameSettings.particleSetting==2)this.setExpired();
		
		if(worldObj.isRemote)this.motionHandler();
		
		this.particleScale-=particleMaxAge/10.0;
		
		
		this.particleAge++;
	}
	
	public void renderParticle(float par2, float par3, float par4, float par5, float par6, float par7){
		OpenGLM.disableLighting();
		OpenGLM.depthMask(false);
		OpenGLM.enableBlend();
		GL11U.allOpacityIs(true);
		GL11U.blendFunc(2);
		float PScale = 0.01F*this.particleScale;
		float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
		float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
		float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
		U.getMC().renderEngine.bindTexture(texture1);
		Renderer.POS_UV_COLOR.beginQuads();
		Renderer.POS_UV_COLOR.addVertex(x-par3*PScale-par6*PScale, y-par4*PScale, z-par5*PScale-par7*PScale, 0, 0, (float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
		Renderer.POS_UV_COLOR.addVertex(x-par3*PScale+par6*PScale, y+par4*PScale, z-par5*PScale+par7*PScale, 1, 0, (float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
		Renderer.POS_UV_COLOR.addVertex(x+par3*PScale+par6*PScale, y+par4*PScale, z+par5*PScale+par7*PScale, 1, 1, (float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
		Renderer.POS_UV_COLOR.addVertex(x+par3*PScale-par6*PScale, y-par4*PScale, z+par5*PScale-par7*PScale, 0, 1, (float)this.r_e, (float)this.g_e, (float)this.b_e, (float)this.opacity_e);
		Renderer.POS_UV_COLOR.draw();
		OpenGLM.disableBlend();
		OpenGLM.depthMask(true);
		GL11U.allOpacityIs(true);
		OpenGLM.enableLighting();
	}
	
	@Override
	public void renderParticle(VertexBuffer renderer, Entity entitiy, float par2, float par3, float par4, float par5, float par6, float par7){
		
	}
	
}
