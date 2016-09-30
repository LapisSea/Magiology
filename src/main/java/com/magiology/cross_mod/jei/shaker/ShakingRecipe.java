package com.magiology.cross_mod.jei.shaker;

import java.util.*;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ShakingRecipe extends BlankRecipeWrapper{
	private final List<List<ItemStack>>	inputs;
	private final List<ItemStack>		outputs;
	
	public ShakingRecipe(List<ItemStack> inputs,List<ItemStack> outputs){
		this.inputs=Collections.singletonList(inputs);
		this.outputs=outputs;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients){
		ingredients.setInputLists(ItemStack.class,inputs);
		ingredients.setOutputs(ItemStack.class,outputs);
	}
	
	@Override
	public List<List<ItemStack>> getInputs(){
		return inputs;
	}
	
	@Override
	public List<ItemStack> getOutputs(){
		return outputs;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft,int recipeWidth,int recipeHeight,int mouseX,int mouseY){
	}
}