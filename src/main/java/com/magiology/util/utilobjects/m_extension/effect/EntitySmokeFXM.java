package com.magiology.util.utilobjects.m_extension.effect;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

public class EntitySmokeFXM extends EntitySmokeFX{

	public EntitySmokeFXM(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed){
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1);
	}
	public EntitySmokeFXM(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale){
		super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, scale);
	}

}
