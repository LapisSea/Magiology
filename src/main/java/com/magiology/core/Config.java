package com.magiology.core;

import com.magiology.client.render.models.ModelWingsFromTheBlackFire;

public class Config{
	private static float PARTICLE_AMOUNT=1;
	private static boolean shadersEnabled=true;
	private static boolean wingsThick=true;

	public static float getParticleAmount(){return PARTICLE_AMOUNT;}
	public static boolean isShadersEnabled(){return shadersEnabled;}
	public static boolean isWingsThick(){return wingsThick;}

	public static void setParticleAmount(float particleAmount){PARTICLE_AMOUNT=particleAmount;}
	public static void setShadersEnabled(boolean shadersEnabled){Config.shadersEnabled = shadersEnabled;}
	public static void setWingsThick(boolean wingsThick){ModelWingsFromTheBlackFire.wings3D(Config.wingsThick=wingsThick);}
}
