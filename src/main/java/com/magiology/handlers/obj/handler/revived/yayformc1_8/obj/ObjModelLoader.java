package com.magiology.handlers.obj.handler.revived.yayformc1_8.obj;

import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustom;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustomLoader;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.ModelFormatException;

import net.minecraft.util.ResourceLocation;

public class ObjModelLoader implements IModelCustomLoader
{

  private static final String[] types = { "obj" };

  @Override
  public String[] getSuffixes()
  {
	return types;
  }
  @Override
  public String getType()
  {
	return "OBJ model";
  }

  @Override
  public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException
  {
	return new WavefrontObject(resource);
  }
}