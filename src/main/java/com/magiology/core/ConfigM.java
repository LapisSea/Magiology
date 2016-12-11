package com.magiology.core;

import static com.magiology.core.MReference.*;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class ConfigM extends Configuration{
	private static float PARTICLE_MULTIPLIER=1;
	private static boolean SHADERS_ENABLED=true;
	private static int PARTICLE_COUNT=2500;

	public static float getMaxParticleCount(){return PARTICLE_COUNT;}
	public static float getParticleMultiplier(){return PARTICLE_MULTIPLIER;}
	public static boolean shadersEnabled(){return SHADERS_ENABLED;}

	public static void setParticleAmount(float particleAmount){PARTICLE_MULTIPLIER=particleAmount;}
	public static void setShadersEnabled(boolean shadersEnabled){ConfigM.SHADERS_ENABLED = shadersEnabled;}
	
	public ConfigM(){
		super(new File(Loader.instance().getConfigDir(), MODID+".cfg"));
		load();
	}
	
	@Override
	public void load(){
		super.load();
		Property prop;

		prop=get(Configuration.CATEGORY_CLIENT, "particleMultiplier", PARTICLE_MULTIPLIER);
		PARTICLE_MULTIPLIER=(float)prop.getDouble();
		prop=get(Configuration.CATEGORY_CLIENT, "shadersEnabled", SHADERS_ENABLED);
		SHADERS_ENABLED=prop.getBoolean();
		
		
		if(hasChanged())save();
	}
}
