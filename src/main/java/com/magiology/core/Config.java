package com.magiology.core;

public class Config{
	private static float PARTICLE_AMOUNT=1;
	private static boolean shadersEnabled=true;
	private static boolean wingsThick=true;
	private static int PARTICLE_COUNT=2500;

	public static float getMaxParticleCount(){return PARTICLE_COUNT;}
	public static float getParticleAmount(){return PARTICLE_AMOUNT;}
	public static boolean isShadersEnabled(){return shadersEnabled;}

	public static void setParticleAmount(float particleAmount){PARTICLE_AMOUNT=particleAmount;}
	public static void setShadersEnabled(boolean shadersEnabled){Config.shadersEnabled = shadersEnabled;}
}
