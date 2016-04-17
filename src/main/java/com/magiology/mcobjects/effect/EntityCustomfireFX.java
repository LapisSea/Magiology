package com.magiology.mcobjects.effect;

import com.magiology.client.render.Textures;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.m_extension.effect.EntityCloudFXM;
import com.magiology.util.utilobjects.m_extension.effect.EntityLavaFXM;
import com.magiology.util.utilobjects.m_extension.effect.EntitySmokeFXM;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityCustomfireFX extends EntityFXM{
	public boolean active=false;
	int optimizer;
	boolean random;
	float[] roration=new float[3],prevRoration=new float[3];
	float[] rorationSize=new float[3];
	
	public EntityCustomfireFX(World w, double xp, double yp, double zp, double xs, double ys, double zs, boolean l, float sc){
		super(w, xp, yp, zp, xs, ys, zs);
		for(int i=0;i<roration.length;i++){
			roration[i]=worldObj.rand.nextInt(360);
		}
		this.xSpeed =xs;
		this.ySpeed =ys;
		this.zSpeed =zs;
		this.particleMaxAge=1;
		this.active=l;
		this.particleScale=sc;
		this.particleAge=0;
	}
	
	
	@Override
	public int getFXLayer(){return 3;}
	
	@Override
	public void motionHandler(){
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		for(int i=0;i<roration.length;i++){
			rorationSize[i]+=RandUtil.CRD(10);
			rorationSize[i]*=0.9;
			roration[i]+=rorationSize[i];
			if(roration[i]<0){
				if(roration[i]<360)roration[i]+=360;
			}else{
				if(roration[i]>360)roration[i]-=360;
			}
		}
		this.ySpeed +=-0.009;
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
		
	}
	
	public void onCollided(){
		if(this.isCollided){
			//-----------------------------------------
			
			for(int i=0;i<roration.length;i++){
				roration[i]=0;
			}
			
			if(this.particleScale>=15){
				this.particleMaxAge=10;
				if(this.particleAge++>=this.particleMaxAge){
					this.setExpired();
					

				}
				if(U.getMC().gameSettings.particleSetting>=1){
//					for(int i=0;i<10;i++)
						UtilM.spawnEntityFX(new EntityLavaFXM(worldObj, this.posX+worldObj.rand.nextFloat()*10, this.posY+1+worldObj.rand.nextFloat()*10, this.posZ+worldObj.rand.nextFloat()*10));
				}
			}
			else if(this.active==true){
				this.particleMaxAge=10;
				if(this.particleAge==0)random=rand.nextBoolean();
				if(random){
					
					this.xSpeed*=0.96;
					this.ySpeed*=0.96;
					this.zSpeed*=0.96;
					
					if(this.particleAge++>=this.particleMaxAge){
						this.setExpired();
						this.spda();
					}
				}
				else{
					this.setExpired();
					this.spda();
				}
			}
			else this.setExpired();
			//-----------------------------------------
		}
	}
	
	@Override
	public void onUpdate(){
		if(Integer.valueOf(Get.Render.ER().getStatistics())>2500)if(RandUtil.RI(10)==0)this.setExpired();
		if(U.getMC().gameSettings.particleSetting==2)this.setExpired();
		prevRoration=roration.clone();
		this.particleWithSize15andSpeed35();
		this.spawningParticleHandler();
		this.onCollided();
		this.motionHandler();
	}
	
	public void particleWithSize15andSpeed35(){
		if(this.ySpeed<-3.5&&this.particleScale>=15){
			if(worldObj.rand.nextInt(2)==0)UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, this.xSpeed/2+0.05-0.1*worldObj.rand.nextFloat(),this.ySpeed/2, this.xSpeed/2+0.05-0.1*worldObj.rand.nextFloat(), worldObj.rand.nextBoolean(),1+worldObj.rand.nextInt(3)));
			for(int grde=0;grde<10;grde++){
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat(),-this.ySpeed/3, this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat()));
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat(),-this.ySpeed/3, this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat()));
				UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat(),-this.ySpeed/3, this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat()));
				UtilM.spawnEntityFX(new EntityCloudFXM(worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat(), this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat(),-this.ySpeed/3, this.xSpeed/2+0.25-0.5*worldObj.rand.nextFloat()));
				UtilM.spawnEntityFX(new EntityLavaFXM( worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat()));
				UtilM.spawnEntityFX(new EntityLavaFXM( worldObj, this.posX+worldObj.rand.nextFloat(), this.posY+worldObj.rand.nextFloat(), this.posZ+worldObj.rand.nextFloat()));
			}
		}
	}
	
	@Override
	public void render(){
		OpenGLM.disableLighting();
//		OpenGLM.disableDepth();
		OpenGLM.depthMask(false);
//		GL11U.allOpacityIs(true);
		float p=1F/16F;
		double ps=this.particleScale*1.5;
		float x=(float)(prevPosX + (posX-prevPosX)* par2 - interpPosX);
		float y=(float)(prevPosY + (posY-prevPosY)* par2 - interpPosY);
		float z=(float)(prevPosZ + (posZ-prevPosZ)* par2 - interpPosZ);
		
		U.getMC().renderEngine.bindTexture(Textures.SmoothBuble1);
		
		GL11U.setUpOpaqueRendering(2);
		Renderer.POS_UV_COLOR.beginQuads();
		Renderer.POS_UV_COLOR.addVertex((x-par3*ps/1.5-par6*ps/1.5), (y-par4*ps/1.5), (z-par5*ps/1.5-par7*ps/1.5), 0, 0,  1, 0, 0, 0.5F);
		Renderer.POS_UV_COLOR.addVertex((x-par3*ps/1.5+par6*ps/1.5), (y+par4*ps/1.5), (z-par5*ps/1.5+par7*ps/1.5), 1, 0,  1, 0, 0, 0.5F);
		Renderer.POS_UV_COLOR.addVertex((x+par3*ps/1.5+par6*ps/1.5), (y+par4*ps/1.5), (z+par5*ps/1.5+par7*ps/1.5), 1, 1,  1, 0, 0, 0.5F);
		Renderer.POS_UV_COLOR.addVertex((x+par3*ps/1.5-par6*ps/1.5), (y-par4*ps/1.5), (z+par5*ps/1.5-par7*ps/1.5), 0, 1,  1, 0, 0, 0.5F);
		
		
		OpenGLM.disableBlend();
		OpenGLM.depthMask(true);
		GL11U.allOpacityIs(true);
		
		TessUtil.bindTexture(Textures.FireHD);
		
//		OpenGLM.pushMatrix();
//		OpenGLM.enableLighting();
//		OpenGLM.translate(x,y,z);
//		OpenGLM.rotate(roration[0], 1, 0, 0);
//		OpenGLM.rotate(roration[1], 0, 1, 0);
//		OpenGLM.rotate(roration[2], 0, 0, 1);
//
//		cube.render(this, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//		OpenGLM.popMatrix();
		
		RenderHelper.enableStandardItemLighting();
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		GL11U.glRotate(PartialTicksUtil.calculatePos(prevRoration, roration));
		OpenGLM.translate(-x, -y, -z);
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		double lol=2.3;
		
		
		
		GL11U.setUpOpaqueRendering(1);
		if(particleScale>0.8)TessUtil.drawBlurredCube(0, 0, 0, -p*ps*lol, -p*ps*lol, -p*ps*lol, p*ps*lol, p*ps*lol, p*ps*lol, 20, 0.07,  1, 1, 1, 0.7);
		GL11U.endOpaqueRendering();
		OpenGLM.popMatrix();
		
		VertexRenderer a=TessUtil.getVB();
		
		
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 0);
		
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 0, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 0, 1);
		
		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 0, 0);

		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 0, 0);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		
		a.addVertexWithUV(x+p*ps, y+p*ps, z+p*ps, 0, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x+p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x+p*ps, y+p*ps, z-p*ps, 0, 0);

		a.addVertexWithUV(x-p*ps, y+p*ps, z-p*ps, 0, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z-p*ps, 1, 0);
		a.addVertexWithUV(x-p*ps, y-p*ps, z+p*ps, 1, 1);
		a.addVertexWithUV(x-p*ps, y+p*ps, z+p*ps, 0, 1);
		
		a.draw();
		OpenGLM.popMatrix();
		
		OpenGLM.disableBlend();
		OpenGLM.depthMask(true);
		GL11U.allOpacityIs(true);
//		OpenGLM.enableLighting();
//		OpenGLM.enableDepth();
	}
	
	public void spawningParticleHandler(){
		if(this.active==true){
			if(optimizer++>=1&&U.getMC().gameSettings.particleSetting==0){
				optimizer=0;
				EntitySmoothBubleFX sb=new EntitySmoothBubleFX(worldObj,this.posX, this.posY, this.posZ,this.xSpeed/2+RandUtil.CRF(0.1)*particleScale, this.ySpeed/2+RandUtil.CRF(0.1)*particleScale, this.zSpeed/2+RandUtil.CRF(0.1)*particleScale,(int) (600*particleScale), 1.5,this.ySpeed/2, false,1,"tx1",1,0,0, 0.3, 0.96);
				UtilM.spawnEntityFX(sb);
				sb.noClip=true;
				if(particleScale>0.8)UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, this.posX, this.posY, this.posZ, RandUtil.CRF(0.1), RandUtil.CRF(0.1), RandUtil.CRF(0.1)));
				
				
			}
		}
		if(particleScale<0.8)return;
		
		if(worldObj.rand.nextInt(500)<=1){
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0, 0);
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0, 0);
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.15-0.3*worldObj.rand.nextFloat(), 0.1+this.ySpeed*2, 0.15-0.3*worldObj.rand.nextFloat(), true,particleScale));
		}
	}
	
	public void spda(){
		if(particleScale<0.8)return;
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0, 0);
		if(worldObj.rand.nextInt(40)==1){
			if(U.getMC().gameSettings.particleSetting==0)worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0.1, 0);
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.xSpeed/2,	-this.ySpeed, 0+this.zSpeed/2, true,particleScale));
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.1+this.xSpeed/2,  -this.ySpeed, 0+this.zSpeed/2, true,particleScale));
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.1+this.xSpeed/2,  -this.ySpeed, 0.1+this.zSpeed/2, true,particleScale));
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.xSpeed/2, -this.ySpeed, 0+this.zSpeed/2, true,particleScale));
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.xSpeed/2, -this.ySpeed, 0.1+this.zSpeed/2, true,particleScale));
			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, -0.1+this.xSpeed/2, -this.ySpeed, -0.1+this.zSpeed/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.xSpeed/2,	-this.ySpeed, 0.1+this.zSpeed/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0+this.xSpeed/2,	-this.ySpeed, -0.1+this.zSpeed/2, true,particleScale));
   			if(worldObj.rand.nextBoolean())UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY+1, this.posZ, 0.1+this.xSpeed/2, -this.ySpeed, -0.1+this.zSpeed/2, true,particleScale));
   			if(worldObj.rand.nextInt(80)==0)UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, 356, this.posZ, 0.25-0.5*worldObj.rand.nextFloat(), -3.5, 0.25-0.5*worldObj.rand.nextFloat(), true,15));
		}
		else if(worldObj.rand.nextInt(2)==0){
			UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, 0.05-0.1*worldObj.rand.nextFloat()+this.xSpeed/2, worldObj.rand.nextFloat()*0.3-this.ySpeed/3, 0.05-0.1*worldObj.rand.nextFloat()+this.zSpeed/2, true,1));
			if(U.getMC().gameSettings.particleSetting==0){
				for(int i=0;i<10;i++)UtilM.spawnEntityFX(new EntitySmokeFXM(worldObj, this.posX, this.posY, this.posZ, 0.05-0.1*worldObj.rand.nextFloat(),0.05-0.1*worldObj.rand.nextFloat(),0.05-0.1*worldObj.rand.nextFloat()));
			}
		}
		else if(U.getMC().gameSettings.particleSetting==0){
			for(int i=0;i<2;i++)UtilM.spawnEntityFX(new EntityLavaFXM(worldObj, this.posX, this.posY, this.posZ));
			if(U.getFPS()>20)for(int i=0;i<2+RandUtil.RI(10);i++)UtilM.spawnEntityFX(new EntityCustomfireFX(worldObj, this.posX, this.posY, this.posZ, RandUtil.CRF(0.1)+xSpeed,	0.15+RandUtil.CRF(0.1), RandUtil.CRF(0.1)+zSpeed, true,0.2F));
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0);
			UtilM.spawnEntityFX(new EntityCloudFXM(worldObj, this.posX, this.posY, this.posZ, 0,0.1,0));
		}
	}
	
	
	
}
