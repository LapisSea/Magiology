package com.magiology.core.registry.init;

import com.magiology.core.registry.imp.AutoReferencedRegistry;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleHandler;
//<GEN:	IMPORTS START>
import com.magiology.mc_objects.particles.ParticleBubbleFactory;
import com.magiology.mc_objects.particles.ParticleCubeFactory;
import com.magiology.mc_objects.particles.ParticleMessageFactory;
import com.magiology.mc_objects.particles.ParticleMistBubbleFactory;
import com.magiology.mc_objects.particles.ParticleMistyEnergyFactory;
//<GEN:	IMPORTS END>
import com.magiology.util.statics.UtilM;

public class ParticlesM extends AutoReferencedRegistry<ParticleFactory>{
	
	private static final ParticlesM instance=new ParticlesM();
	public static ParticlesM get(){return instance;}
	
	//<GEN:	REFERENCE START>
	public static ParticleBubbleFactory      BUBBLE;
	public static ParticleCubeFactory        CUBE;
	public static ParticleMessageFactory     MESSAGE;
	public static ParticleMistBubbleFactory  MIST_BUBBLE;
	public static ParticleMistyEnergyFactory MISTY_ENERGY;
	//<GEN:	REFERENCE END>
	
	
	private ParticlesM(){
		super(ParticleFactory.class);
	}
	
	@Override
	public void registerObj(ParticleFactory factory){
		ParticleHandler.get().registerParticle(factory);
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(BUBBLE      =new ParticleBubbleFactory());
		add(CUBE        =new ParticleCubeFactory());
		add(MESSAGE     =new ParticleMessageFactory());
		add(MIST_BUBBLE =new ParticleMistBubbleFactory());
		add(MISTY_ENERGY=new ParticleMistyEnergyFactory());
		//<GEN:	INIT END>
	}
	
	@Override
	protected String classNameToCutName(String className){
		if(className.startsWith("Particle"))className=className.substring("Particle".length());
		if(className.endsWith("Factory"))className=className.substring(0,className.length()-"Factory".length());
		return UtilM.standardizeName(className);
	}
}
