package com.magiology.util.m_extensions;

import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilM;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public abstract class EntityCreatureM extends EntityCreature implements Worldabale{
	
	
	public EntityCreatureM(World worldIn){
		super(worldIn);
	}
	
	@Override
	public boolean isRemote(){
		return UtilM.isRemote(this);
	}
	@Override
	public boolean client(){
		return isRemote();
	}
	@Override
	public boolean server(){
		return !isRemote();
	}
	
	public Vec3M getPos(){
		return new Vec3M(this.posX, this.posY, this.posZ);
	}
}
