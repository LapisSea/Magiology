package com.magiology.util.m_extensions;

import com.magiology.util.interf.Locateable.LocateableVec3M;
import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.vec.Vec3M;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

public abstract class EntityAgeableM extends EntityAgeable implements Worldabale, LocateableVec3M{
	
	public EntityAgeableM(World worldIn){
		super(worldIn);
	}
	
	@Override
	public World getWorld(){
		return world;
	}
	
	public Vec3M getPos(){
		return new Vec3M(posX, posY, posZ);
	}
	
}
