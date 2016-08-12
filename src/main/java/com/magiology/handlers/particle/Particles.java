package com.magiology.handlers.particle;

import com.magiology.mc_objects.particles.ParticleBubbleFactory;
import com.magiology.mc_objects.particles.ParticleCubeFactory;
import com.magiology.mc_objects.particles.ParticleMistBubbleFactory;
import com.magiology.mc_objects.particles.ParticleMistyEnergyFactory;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Particles{
	public static final ParticleMistBubbleFactory MIST_BUBBLE=new ParticleMistBubbleFactory();
	public static final ParticleCubeFactory CUBE=new ParticleCubeFactory();
	public static final ParticleMistyEnergyFactory MISTY_ENERGY=new ParticleMistyEnergyFactory();
	public static final ParticleBubbleFactory MISTY_BUBBLE=new ParticleBubbleFactory();
	
	
	@SideOnly(Side.CLIENT)
	public static void registerParticles(){
		ParticleHandler.instance.registerParticle(MIST_BUBBLE);
		ParticleHandler.instance.registerParticle(CUBE);
		ParticleHandler.instance.registerParticle(MISTY_ENERGY);
		ParticleHandler.instance.registerParticle(MISTY_BUBBLE);
		
	}
}
