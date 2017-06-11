package com.magiology.cross_mod;

import com.magiology.cross_mod.jei.MagiologyPlugin_JEI;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class ModChecker{
	
	private static final ModChecker instance=new ModChecker();
	
	public static ModChecker instance(){
		return instance;
	}
	
	private ModChecker(){}
	
	private final List<ExtensionLoader> loaders=new ArrayList<>();
	
	//using wrapped boolean for easy null pointer
	private Boolean jeiLoaded;
	
	public void detectMods(){
		
		jeiLoaded=Loader.isModLoaded("JEI")||Loader.isModLoaded("jei")||Loader.isModLoaded("Jei");
		if(jeiLoaded) loaders.add(new MagiologyPlugin_JEI());
		
	}
	
	public boolean isJeiLoaded(){
		return jeiLoaded.booleanValue();
	}
	
	public void init(){
		loaders.forEach(ExtensionLoader::init);
	}
	
}
