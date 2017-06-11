package com.magiology.core;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

import static com.magiology.core.MReference.MODID;

public class ConfigM extends Configuration{
	
	private static boolean	SHADERS_ENABLED		=true,DEBUG_WIN=false;
	private static int		PARTICLE_COUNT		=2500;
	private static float	PARTICLE_MULTIPLIER	=1;
	
	public static float getMaxParticleCount(){
		return PARTICLE_COUNT;
	}
	
	public static float getParticleMultiplier(){
		return PARTICLE_MULTIPLIER;
	}
	
	public static boolean shadersEnabled(){
		return SHADERS_ENABLED;
	}
	
	public static boolean debugWin(){
		return DEBUG_WIN;
	}
	
	public static void setParticleAmount(float particleAmount){
		PARTICLE_MULTIPLIER=particleAmount;
	}
	
	public static void setShadersEnabled(boolean shadersEnabled){
		ConfigM.SHADERS_ENABLED=shadersEnabled;
	}
	
	public ConfigM(){
		super(new File(Loader.instance().getConfigDir(), MODID+".cfg"));
		load();
	}
	
	@Override
	public void load(){
		super.load();
		
		PARTICLE_MULTIPLIER=(float)get(Configuration.CATEGORY_CLIENT, "particleMultiplier", PARTICLE_MULTIPLIER).getDouble();
		SHADERS_ENABLED=get(Configuration.CATEGORY_CLIENT, "shadersEnabled", SHADERS_ENABLED).getBoolean();
		DEBUG_WIN=get(Configuration.CATEGORY_GENERAL, "debugWin", DEBUG_WIN).getBoolean();
		if(hasChanged()) save();
	}
}
