package com.magiology.cross_mod.jei.shaker;

import java.util.ArrayList;
import java.util.List;

import com.magiology.mc_objects.recepies.ShakerRecepies;
import com.magiology.mc_objects.recepies.ShakerRecepies.Recepie;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;

public class ShakingRecipeMaker{
	
	public static List<ShakingRecipe> getShakerRecipes(IJeiHelpers helpers){
		IStackHelper stackHelper=helpers.getStackHelper();
		List<Recepie> smeltingMap=ShakerRecepies.get().getAll();
		
		List<ShakingRecipe> recipes=new ArrayList<ShakingRecipe>();
		
		for(Recepie rec:smeltingMap){
			
			ShakingRecipe recipe=new ShakingRecipe(rec.input,rec.output);
			recipes.add(recipe);
		}
		
		return recipes;
	}
	
}