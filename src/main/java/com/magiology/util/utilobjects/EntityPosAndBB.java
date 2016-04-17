package com.magiology.util.utilobjects;

import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityPosAndBB{
	public double[] BB;
	public Entity entity;
	private boolean isDead,isRendering;
	
	public EntityPosAndBB(Entity entity){
		this.entity=entity;
		this.BB=new double[]{entity.getEntityBoundingBox().minX,entity.getEntityBoundingBox().minY,entity.getEntityBoundingBox().minZ,entity.getEntityBoundingBox().maxX,entity.getEntityBoundingBox().maxY,entity.getEntityBoundingBox().maxZ};
	}
	
	public double[] getMiddleOfBB(){
		double[] result={0,0,0};
		result[0]=/*(BB[3]-BB[0])**/0;
		result[1]=(BB[4]-BB[1])/(entity instanceof EntityPlayer?-2:2);
		result[2]=/*(BB[5]-BB[2])**/0;
		
		return result;
	}
	public double[] getRandDotInBB(double randomness){
		double[] result={0,0,0};
		result[0]=entity.posX+RandUtil.CRF(randomness);
		result[1]=entity.posY+RandUtil.CRF(randomness);
		result[2]=entity.posZ+RandUtil.CRF(randomness);
		
		return result;
	}
	
	public boolean isDead(){
		isDead=entity.isDead;
		return isDead;
		}
	public boolean isRendering(){
		EntityPlayer p=U.getMC().thePlayer;
		isRendering=entity.isInRangeToRender3d(p.posX, p.posY, p.posZ);
		return isRendering;
		}
	
//	public void update(Entity entity){
//		EntityClientPlayerMP p=H.getMC().thePlayer;
//		isRendering=entity.isInRangeToRender3d(p.posX, p.posY, p.posZ);
//		isDead=entity.isDead;
//	}
	
}
