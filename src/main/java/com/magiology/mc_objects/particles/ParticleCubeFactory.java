package com.magiology.mc_objects.particles;

import com.magiology.handlers.particle.ParticleFactory;

public class ParticleCubeFactory extends ParticleFactory{
	
	
	@Override
	public float getSpawnDistanceInBlocks(){
		return 0;
	}
	
	@Override
	public boolean hasDistanceLimit(){
		return false;
	}
	
	@Override
	public boolean hasStaticModel(){
		return false;
	}
	
	@Override
	public void setUpOpenGl(){
	}
	
	@Override
	public void resetOpenGl(){
	}
	
	@Override
	public void compileDisplayList(){
		
	}
}
