package com.magiology.mcobjects.effect;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityFollowingBubleFX extends EntitySmoothBubleFX{
	
	Entity entity;
	public boolean isGL_DEPTHDisabled=false,isChangingPos=true;
	double spotX,spotY,spotZ,startAngle;
	
	public EntityFollowingBubleFX(World w,double xp, double yp, double zp, double xs, double ys, double zs,Entity entity1,double sAngle, double sX, double sY, double sZ, int siz, double lengt, double Ra,double Ga,double Ba,double opacita){
		super(w,xp, yp, zp, xs, ys, zs, siz, lengt, 0, false,1,"tx1" ,Ra,Ga,Ba,opacita,0.99);
		this.xSpeed =xs;
		this.ySpeed =ys;
		this.zSpeed =zs;
		for(int a=0;a<sideOpacity.length;a++){
			sideOpacityChange[a]=worldObj.rand.nextInt(3)-1;
			sideOpacity[a]=worldObj.rand.nextDouble();
		}
		this.particleMaxAge=siz;
		this.particleScale=siz/10;
		this.length=lengt;
		this.r_e=Ra;
		this.g_e=Ga;
		this.b_e=Ba;
		this.opacity_e=opacita;
		spotX=sX;
		spotY=sY;
		spotZ=sZ;
		entity=entity1;
		startAngle=sAngle;
	}
	
	@Override
	public void motionHandler(){
		friction=0.7+worldObj.rand.nextFloat()*0.2;
		this.xSpeed*=friction;
		this.ySpeed*=friction;
		this.zSpeed*=friction;
		this.ySpeed+=this.gravity;
		
		double addX=0,addY=0,addZ=0;
		if(this.entity!=null){
			addX=entity.posX;
			addY=entity.posY;
			addZ=entity.posZ;
		}
		
		this.xSpeed+=((this.spotX+addX)-this.posX)/5;
		this.ySpeed+=((this.spotY+addY)-this.posY)/5;
		this.zSpeed+=((this.spotZ+addZ)-this.posZ)/5;
		
		this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
		
	}
	
	@Override
	public void onUpdate(){
		super.onUpdate();
		this.opacityHandler();
		
//		if(entity==H.getMC().thePlayer){
////			UtilM.printlnln(sideOpacity[0]);
//		}
		
		this.particleScale-=0.001*particleMaxAge/(length/10);
		if(particleScale<0)this.setExpired();
		if(isChangingPos&&entity!=null){
			if(worldObj.getTotalWorldTime()%100==0)spotX*=-1;
			if(worldObj.getTotalWorldTime()%200==0)spotZ*=-1;
			if(worldObj.getTotalWorldTime()%500==0){
				spotX*=-1;
				spotZ*=-1;
			}
		}
	}
}
