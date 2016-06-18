package com.magiology.handlers.particle;

import com.magiology.mc_objects.particles.ParticleMistBubbleFactory;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Particles{
	public static final ParticleMistBubbleFactory MIST_BUBBLE=new ParticleMistBubbleFactory();
	
	
	@SideOnly(Side.CLIENT)
	public static void registerParticles(){
		ParticleHandler.instance.registerParticle(MIST_BUBBLE);
	}
}
