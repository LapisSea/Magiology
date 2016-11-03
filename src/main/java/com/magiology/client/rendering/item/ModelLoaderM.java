package com.magiology.client.rendering.item;

import com.magiology.util.statics.LogUtil;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class ModelLoaderM implements ICustomModelLoader{
	
	@Override
	public boolean accepts(ResourceLocation modelLocation){
		return SIRRegistry.isIndexed(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception{
		return new ItemRendererModelDummy(modelLocation);
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager){
	}
	
	@Override
	public String toString(){
		return "dummy-for-item-redering-model";
	}
}
