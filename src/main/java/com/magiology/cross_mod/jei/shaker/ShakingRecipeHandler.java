package com.magiology.cross_mod.jei.shaker;

import com.magiology.cross_mod.jei.JeiUidsM;
import com.magiology.util.statics.LogUtil;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.ErrorUtil;

public class ShakingRecipeHandler implements IRecipeHandler<ShakingRecipe>{
	
	@Override
	public Class<ShakingRecipe> getRecipeClass(){
		return ShakingRecipe.class;
	}
	
	@Override
	public String getRecipeCategoryUid(){
		return JeiUidsM.SHAKING;
	}
	
	@Override
	public String getRecipeCategoryUid(ShakingRecipe recipe){
		return getRecipeCategoryUid();
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(ShakingRecipe recipe){
		return recipe;
	}
	
	@Override
	public boolean isRecipeValid(ShakingRecipe recipe){
		if(recipe.getInputs().isEmpty()){
			LogUtil.println("Recipe has no inputs.",ErrorUtil.getInfoFromBrokenRecipe(recipe,this));
			return false;
		}
		if(recipe.getOutputs().isEmpty()){
			LogUtil.println("Recipe has no outputs.",ErrorUtil.getInfoFromBrokenRecipe(recipe,this));
			return false;
		}
		return true;
	}
	
}