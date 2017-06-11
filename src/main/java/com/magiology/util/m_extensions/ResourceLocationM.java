package com.magiology.util.m_extensions;

import com.magiology.core.MReference;
import net.minecraft.util.ResourceLocation;

public class ResourceLocationM extends ResourceLocation{
	
	public ResourceLocationM(String resourcePath){
		super(MReference.MODID, resourcePath);
	}
	
}
