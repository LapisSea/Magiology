package com.magiology.handlers.obj.handler.revived.yayformc1_8;

import net.minecraft.util.ResourceLocation;

/**
 * Instances of this class act as factories for their model type
 *
 * @author cpw
 *
 */
public interface IModelCustomLoader {
  /**
   * Get resource suffixes this model loader recognizes
   * @return a list of suffixes
   */
  String[] getSuffixes();
  /**
   * Get the main type name for this loader
   * @return the type name
   */
  String getType();
  /**
   * Load a model instance from the supplied path
   * @param resource The ResourceLocation of the model
   * @return A model instance
   * @throws ModelFormatException if the model format is not correct
   */
  IModelCustom loadInstance(ResourceLocation resource) throws ModelFormatException;
}