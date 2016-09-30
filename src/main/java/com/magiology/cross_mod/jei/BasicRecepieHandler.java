package com.magiology.cross_mod.jei;

import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.*;

public abstract class BasicRecepieHandler<T extends IRecipeWrapper> implements IRecipeHandler<T>{
	@Nonnull
	protected final Class<T> clazz;
	@Nonnull
	protected final String uid;
	
	public BasicRecepieHandler(@Nonnull Class<T> clazz,@Nonnull String uid){
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
	public String getRecipeCategoryUid(){
		return uid;
	}
	@Nonnull
	@Override
	public String getRecipeCategoryUid(T recipe){
		return uid;
	}

	@Override
	public boolean isRecipeValid(@Nonnull T recipe){
		return(
			listOk(		recipe.getInputs())||
			listOk(recipe.getFluidInputs())
		)&&(
			listOk(		recipe.getOutputs())||
			listOk(recipe.getFluidOutputs())
		);
	}
	private boolean listOk(List l){
		return l!=null&&!l.isEmpty();
	}
}
