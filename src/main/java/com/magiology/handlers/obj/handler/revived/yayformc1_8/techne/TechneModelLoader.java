package com.magiology.handlers.obj.handler.revived.yayformc1_8.techne;

import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustom;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustomLoader;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.ModelFormatException;

import net.minecraft.util.ResourceLocation;

public class TechneModelLoader implements IModelCustomLoader {

  private static final String[] types = { "tcn" };

  @Override
  public String[] getSuffixes()
  {
	return types;
  }
  @Override
  public String getType()
  {
	return "Techne model";
  }

  @Override
  public IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException
  {
	return new TechneModel(resource);
  }

}