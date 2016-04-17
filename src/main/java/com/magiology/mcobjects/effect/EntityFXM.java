package com.magiology.mcobjects.effect;

import java.util.ArrayDeque;
import java.util.Queue;

import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityFXM extends EntityFX{
	
	public static Queue<EntityFXM> queuedRenders = new ArrayDeque();
	public static void renderBufferedParticle(){
		for(EntityFXM particle:queuedRenders)particle.render();
		queuedRenders.clear();
	}
	public ColorF color;
	public double gravity,friction;
	public float par2,par3,par4,par5,par6,par7;
	public int state;
	public EntityFXM(World w, double xp, double yp, double zp){
		super(w, xp, yp, zp);
	}
	public EntityFXM(World w, double xp, double yp, double zp, double xs, double ys, double zs){
		super(w, xp, yp, zp, xs, ys, zs);
		this.xSpeed =xs;
		this.ySpeed =ys;
		this.zSpeed =zs;
	}
	@Override
	public int getFXLayer(){return 3;}
	public void motionHandler(){
		this.xSpeed*=friction;
		this.ySpeed*=friction;
		this.zSpeed*=friction;
		this.ySpeed +=this.gravity;
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
	}
	@Override
	public void onUpdate(){
		this.prevPosX=this.posX;
		this.prevPosY=this.posY;
		this.prevPosZ=this.posZ;
		this.particleAge++;
		if(Integer.valueOf(Get.Render.ER().getStatistics())>2500&&worldObj.rand.nextBoolean())this.setExpired();
		if(U.getMC().gameSettings.particleSetting==2)this.setExpired();
		this.motionHandler();
	}
	public void render(){
		renderStandardParticle(par2,par3,par4,par5,par6,par7,0.01F*this.particleScale,true);
	}
	@Override
	public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float pa2, float pa3, float pa4, float pa5, float pa6, float pa7){
		par2=pa2;par3=pa3;par4=pa4;par5=pa5;par6=pa6;par7=pa7;
		queuedRenders.add(this);
//		Renderer.begin(7,DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
//		Renderer.addVertexData((double)(f5 - p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 - p_180434_8_ * f4), (double)f1, (double)f3).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//		Renderer.addVertexData((double)(f5 - p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 - p_180434_6_ * f4 + p_180434_8_ * f4), (double)f1, (double)f2).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//		Renderer.addVertexData((double)(f5 + p_180434_4_ * f4 + p_180434_7_ * f4), (double)(f6 + p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 + p_180434_8_ * f4), (double)f, (double)f2).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//		Renderer.addVertexData((double)(f5 + p_180434_4_ * f4 - p_180434_7_ * f4), (double)(f6 - p_180434_5_ * f4), (double)(f7 + p_180434_6_ * f4 - p_180434_8_ * f4), (double)f, (double)f3).setColor(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//		Renderer.draw();
	}
	
	public void renderStandardParticle(float par2, float par3, float par4, float par5, float par6, float par7, float Scale,boolean draw){
		
		float PScale = 0.01F*this.particleScale;
		float x=(float)(this.prevPosX+(this.posX-this.prevPosX)*par2-interpPosX);
		float y=(float)(this.prevPosY+(this.posY-this.prevPosY)*par2-interpPosY);
		float z=(float)(this.prevPosZ+(this.posZ-this.prevPosZ)*par2-interpPosZ);
		int i = this.getBrightnessForRender(par2);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		
		ColorF color=this.color==null?new ColorF(particleRed, particleGreen, particleBlue, particleAlpha):this.color;
		if(draw){
			Renderer.PARTICLE.beginQuads();
			Renderer.PARTICLE.addVertex((x-par3*PScale-par6*PScale), (y-par4*PScale), (z-par5*PScale-par7*PScale), 0, 0, color, j, k);
			Renderer.PARTICLE.addVertex((x-par3*PScale+par6*PScale), (y+par4*PScale), (z-par5*PScale+par7*PScale), 1, 0, color, j, k);
			Renderer.PARTICLE.addVertex((x+par3*PScale+par6*PScale), (y+par4*PScale), (z+par5*PScale+par7*PScale), 1, 1, color, j, k);
			Renderer.PARTICLE.addVertex((x+par3*PScale-par6*PScale), (y-par4*PScale), (z+par5*PScale-par7*PScale), 0, 1, color, j, k);
			Renderer.PARTICLE.draw();
		}else{
			Renderer.PARTICLE.addVertex((x-par3*PScale-par6*PScale), (y-par4*PScale), (z-par5*PScale-par7*PScale), 0, 0, color, j, k);
			Renderer.PARTICLE.addVertex((x-par3*PScale+par6*PScale), (y+par4*PScale), (z-par5*PScale+par7*PScale), 1, 0, color, j, k);
			Renderer.PARTICLE.addVertex((x+par3*PScale+par6*PScale), (y+par4*PScale), (z+par5*PScale+par7*PScale), 1, 1, color, j, k);
			Renderer.PARTICLE.addVertex((x+par3*PScale-par6*PScale), (y-par4*PScale), (z+par5*PScale-par7*PScale), 0, 1, color, j, k);
		}
	}
}
