package com.magiology.mc_objects.particles;

import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.UtilM;

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
