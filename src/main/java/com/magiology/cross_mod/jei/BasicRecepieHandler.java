package com.magiology.cross_mod.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public abstract class BasicRecepieHandler<T extends IRecipeWrapper> implements IRecipeHandler<T>{
	
	@Nonnull
	protected final Class<T> clazz;
	@Nonnull
	protected final String   uid;
	
	public BasicRecepieHandler(@Nonnull Class<T> clazz, @Nonnull String uid){
		this.clazz=clazz;
		this.uid=uid;
	}
	
	@Nonnull
	@Override
	public Class<T> getRecipeClass(){
		return clazz;
	}
	
	@Nonnull
	@Override
	public String getRecipeCategoryUid(T recipe){
		return uid;
	}
	
}
