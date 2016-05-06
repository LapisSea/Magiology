package com.magiology.mcobjects.effect;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.world.World;

public class EntityMovingParticleFX extends EntitySmoothBubleFX{
	
	
	double opacity_e,x1,y1,z1;
	double r_e,g_e,b_e;
	double[] sideOpacity=new double[8];
	int[] sideOpacityChange=new int[8];
	int type,state,xf,yf,zf,blinktime=0;
	
	public EntityMovingParticleFX(World w, double xp1, double yp1, double zp1, double xp2, double yp2, double zp2, int siz, double Ra,double Ga,double Ba,double opacita){
		super(w, xp1, yp1, zp1, 0, 0, 0, 0, 0, 1, Ra, Ga, Ba, opacita);
		
		for(int a=0;a<sideOpacity.length;a++){
			sideOpacityChange[a]=worldObj.rand.nextInt(3)-1;
			sideOpacity[a]=worldObj.rand.nextDouble();
		}
		particleMaxAge=84;
		blinktime=1+worldObj.rand.nextInt(84);
		particleScale=siz/10;
		r_e=Ra;
		g_e=Ga;
		b_e=Ba;
		opacity_e=opacita;
		xf=(int) xp1;
		yf=(int) yp1;
		zf=(int) zp1;
		x1=xp2-xp1;
		y1=yp2-yp1;
		z1=zp2-zp1;
		noClip=true;
	}
	
	@Override
	public void motionHandler(){
		xSpeed*=0.8;
		ySpeed*=0.8;
		zSpeed*=0.8;
		xSpeed+=x1/400;
		ySpeed+=y1/400;
		zSpeed+=z1/400;
		moveEntity(xSpeed, ySpeed, zSpeed);
	}
	
	@Override
	public void onUpdate(){
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		particleAge++;
		
		
		if(U.getMC().gameSettings.particleSetting==2)setExpired();
		if(particleAge>particleMaxAge)setExpired();
		motionHandler();
		opacityHandler();
		if(type==1){
			if(particleAge==blinktime){
				for(int a=0;a<1+worldObj.rand.nextInt(3);a++)UtilM.spawnEntityFX(new EntitySmoothBubleFX(worldObj,posX, posY, posZ, 0.025-0.05*worldObj.rand.nextFloat()-xSpeed,0.025-0.05*worldObj.rand.nextFloat()-ySpeed, 0.025-0.05*worldObj.rand.nextFloat()-zSpeed, (int) (10+particleScale*7), 3,-10, false,1,"tx1",r_e,g_e,b_e, this.opacity_e, 0.99));
			}
			if(particleAge>78&&particleScale>15){
				particleScale/=1.2;
			}
			
		}
		else if(type==2){
			
		}
		else type=1;
	}
}
