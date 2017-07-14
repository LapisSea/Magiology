package com.magiology.mc_objects.entitys.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWatchClosestM extends EntityAIBase{
	
	protected     EntityLiving            theWatcher;
	/** The closest entity which is being watched by this one. */
	public        Entity                  closestEntity;
	/** This is the Maximum distance that the AI will look for the Entity */
	protected     float                   maxDistanceForPlayer;
	public        int                     lookTime;
	private final float                   chance;
	protected     Class<? extends Entity> watchedClass;

	public EntityAIWatchClosestM(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance){
		this.theWatcher=entitylivingIn;
		this.watchedClass=watchTargetClass;
		this.maxDistanceForPlayer=maxDistance;
		this.chance=0.02F;
		this.setMutexBits(2);
	}

	public EntityAIWatchClosestM(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn){
		this.theWatcher=entitylivingIn;
		this.watchedClass=watchTargetClass;
		this.maxDistanceForPlayer=maxDistance;
		this.chance=chanceIn;
		this.setMutexBits(2);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute(){
		if(this.theWatcher.getRNG().nextFloat()>=this.chance){
			return false;
		}else{
			if(this.theWatcher.getAttackTarget()!=null){
				this.closestEntity=this.theWatcher.getAttackTarget();
			}

			if(this.watchedClass==EntityPlayer.class){
				this.closestEntity=this.theWatcher.world.getClosestPlayerToEntity(this.theWatcher, this.maxDistanceForPlayer);
			}else{
				this.closestEntity=this.theWatcher.world.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0D, this.maxDistanceForPlayer), this.theWatcher);
			}

			return this.closestEntity!=null;
		}
	}
	
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting(){
		return !this.closestEntity.isEntityAlive()?false:(this.theWatcher.getDistanceSqToEntity(this.closestEntity)>this.maxDistanceForPlayer*this.maxDistanceForPlayer?false:this.lookTime>0);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting(){
		this.lookTime=40+this.theWatcher.getRNG().nextInt(40);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask(){
		this.closestEntity=null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask(){
		this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY+this.closestEntity.getEyeHeight(), this.closestEntity.posZ, this.theWatcher.getHorizontalFaceSpeed(), this.theWatcher.getVerticalFaceSpeed());
		--this.lookTime;
	}
}
