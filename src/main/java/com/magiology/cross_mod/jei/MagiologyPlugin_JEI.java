package com.magiology.cross_mod.jei;

import com.magiology.cross_mod.ExtensionLoader;
import com.magiology.cross_mod.jei.shaker.ShakerCategory;
import com.magiology.cross_mod.jei.shaker.ShakingRecipeHandler;
import com.magiology.cross_mod.jei.shaker.ShakingRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;

@JEIPlugin
public class MagiologyPlugin_JEI extends BlankModPlugin implements ExtensionLoader{
	
	@Override
	public void init(){
		
	}
	
	public static IJeiHelpers jeiHelpers;
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry){
		
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration ingredientRegistration){
	}
	
	@Override
	public void register(IModRegistry registry){
		IIngredientRegistry ingredientRegistry=registry.getIngredientRegistry();
		jeiHelpers=registry.getJeiHelpers();
		
		IGuiHelper guiHelper=jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(new ShakerCategory(guiHelper));
		
		registry.addRecipeHandlers(new ShakingRecipeHandler());
		
		registry.addRecipes(ShakingRecipeMaker.getShakerRecipes(jeiHelpers));
		
	}
	
}
