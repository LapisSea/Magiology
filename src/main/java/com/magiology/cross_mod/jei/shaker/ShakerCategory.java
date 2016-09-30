package com.magiology.cross_mod.jei.shaker;

import java.util.List;

import com.magiology.cross_mod.jei.JeiUidsM;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.plugins.vanilla.furnace.FurnaceRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ShakerCategory extends FurnaceRecipeCategory<ShakingRecipe>{
	private final IDrawable	background;
	private final String	localizedName;
	
	public ShakerCategory(IGuiHelper guiHelper){
		super(guiHelper);
		ResourceLocation location=new ResourceLocation("minecraft","textures/gui/container/furnace.png");
		background=guiHelper.createDrawable(location,55,16,82,54);
		localizedName=Translator.translateToLocal(JeiUidsM.SHAKING);
	}
	
	@Override
	public IDrawable getBackground(){
		return background;
	}
	
	@Override
	public void drawAnimations(Minecraft minecraft){
		flame.draw(minecraft,2,20);
		arrow.draw(minecraft,24,18);
	}
	
	@Override
	public String getTitle(){
		return localizedName;
	}
	
	@Override
	public String getUid(){
		return JeiUidsM.SHAKING;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout,ShakingRecipe recipeWrapper){
		IGuiItemStackGroup guiItemStacks=recipeLayout.getItemStacks();
		
		guiItemStacks.init(inputSlot,true,0,0);
		guiItemStacks.init(outputSlot,false,60,18);
		
		List<List<ItemStack>> inputs=recipeWrapper.getInputs();
		guiItemStacks.set(inputSlot,inputs.get(0));
		List<ItemStack> outputs=recipeWrapper.getOutputs();
		guiItemStacks.set(outputSlot,outputs.get(0));
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout,ShakingRecipe recipeWrapper,IIngredients ingredients){
		IGuiItemStackGroup guiItemStacks=recipeLayout.getItemStacks();
		
		guiItemStacks.init(inputSlot,true,0,0);
		guiItemStacks.init(outputSlot,false,60,18);
		
		guiItemStacks.set(ingredients);
	}
}