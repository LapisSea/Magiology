package com.magiology.mc_objects.particles;

import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.objs.RegistrableDatabaseStorage;

public class Particles extends RegistrableDatabaseStorage<ParticleFactory>{
	
	public static final ParticleMistBubbleFactory MIST_BUBBLE=ParticleMistBubbleFactory.get();
	public static final ParticleCubeFactory CUBE=ParticleCubeFactory.get();
	public static final ParticleMistyEnergyFactory MISTY_ENERGY=ParticleMistyEnergyFactory.get();
	public static final ParticleBubbleFactory MISTY_BUBBLE=ParticleBubbleFactory.get();
	
	
	private static final Particles instance=new Particles(); 
	public static Particles get(){return instance;}
	private Particles(){
		super(ParticleFactory.class);
		
	}
	@Override
	public ParticleFactory[] getDatabase(){
		return new ParticleFactory[]{MIST_BUBBLE,CUBE,MISTY_ENERGY,MISTY_BUBBLE};
	}
	
	@Override
	public void registerObj(ParticleFactory obj){
		ParticleHandler.get().registerParticle(obj);
	}
}