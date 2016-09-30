package com.magiology.util.m_extensions;

import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilM;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

public abstract class EntityAgeableM extends EntityAgeable implements Worldabale{

	public EntityAgeableM(World worldIn){
		super(worldIn);
	}

	@Override
	public World getWorld(){
		return worldObj;
	}

	@Override
	public boolean isRemote(){
		return UtilM.isRemote(this);
	}

	@Override
	public boolean server(){
		return !isRemote();
	}

	@Override
	public boolean client(){
		return isRemote();
	}
	
	public Vec3M getPos(){
		return new Vec3M(this.posX, this.posY, this.posZ);
	}
	
}
