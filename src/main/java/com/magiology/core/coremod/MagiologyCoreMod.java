package com.magiology.core.coremod;

import java.io.File;
import java.util.Map;

import com.magiology.core.MReference;
import com.magiology.core.init.classload.ClassList;
import com.magiology.util.utilclasses.CollectionConverter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name(MReference.NAME+"core")
@MCVersion(MReference.MC_VERSION)
@TransformerExclusions(MReference.BASE_PATH)
@SortingIndex(Integer.MAX_VALUE)
public class MagiologyCoreMod implements IFMLLoadingPlugin{
	
	public static File location;
	
	public MagiologyCoreMod(){
		
	}
	
	@Override
	public String getAccessTransformerClass(){
		return null;
	}
	
	@Override
	public String[] getASMTransformerClass(){
		return CollectionConverter.convAr(ClassList.getImplementations(IClassTransformer.class), String.class, c->c.getName());
	}

	@Override
	public String getModContainerClass(){
		return null;
	}

	@Override
	public String getSetupClass(){
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data){
		location=(File)data.get("coremodLocation");
	}

}
