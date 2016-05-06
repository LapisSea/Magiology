package com.magiology.mcobjects.particles;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class ParticleMistBubbleFactory extends ParticleFactory<ParticleMistBubble>{
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorF color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		if(!shouldSpawn(pos)||!UtilM.isRemote())return;
		addParticle(new ParticleMistBubble(pos, speed, size, lifeTime, gravity, color));
	}
	
	@Override
	public float getSpawnDistanceInBlocks(){
		return defultSpawnDistance;
	}
	
	@Override
	public boolean hasDistanceLimit(){
		return true;
	}
	
	@Override
	public boolean hasStaticModel(){
		return false;
	}
	
}
