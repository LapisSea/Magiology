package com.magiology.util.m_extensions;

import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public abstract class EntityCreatureM extends EntityCreature implements Worldabale{
	
	
	public EntityCreatureM(World worldIn){
		super(worldIn);
	}
	
	public Vec3M getPos(){
		return new Vec3M(posX, posY, posZ);
	}
}
