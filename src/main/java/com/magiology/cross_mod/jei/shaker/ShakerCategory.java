package com.magiology.cross_mod.jei.shaker;

import com.magiology.cross_mod.jei.JeiUidsM;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ShakerCategory extends BlankRecipeCategory<ShakingRecipe>{
	
	private final IDrawable background;
	private final String    localizedName;
	
	protected static final int inputSlot =0;
	protected static final int fuelSlot  =1;
	protected static final int outputSlot=2;
	
	public ShakerCategory(IGuiHelper guiHelper){
		background=guiHelper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/furnace.png"), 55, 16, 82, 54);
		localizedName=Translator.translateToLocal(JeiUidsM.SHAKING);
	}
	
	@Override
	public IDrawable getBackground(){
		return background;
	}
	
	//	@Override
	//	public void drawAnimations(Minecraft minecraft){
	//		OpenGLM.disableTexture2D();
	//		ColorM.BLUE.bind();
	//		Renderer.POS.beginQuads();
	//		Renderer.POS.addVertex(0,20,0);
	//		Renderer.POS.addVertex(20,20,0);
	//		Renderer.POS.addVertex(20,0,0);
	//		Renderer.POS.addVertex(0,0,0);
	//		Renderer.POS.draw();
	//		OpenGLM.enableTexture2D();
	//	}
	
	@Override
	public String getTitle(){
		return localizedName;
	}
	
	@Override
	public String getUid(){
		return JeiUidsM.SHAKING;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ShakingRecipe recipeWrapper, IIngredients ingredients){
		IGuiItemStackGroup guiItemStacks=recipeLayout.getItemStacks();
		
		guiItemStacks.init(inputSlot, true, 0, 0);
		guiItemStacks.init(outputSlot, false, 60, 18);
		guiItemStacks.init(outputSlot+1, false, 68, 18);
		
		List<List<ItemStack>> inputs=recipeWrapper.getInputs();
		guiItemStacks.set(inputSlot, inputs.get(0));
		List<ItemStack> outputs=recipeWrapper.getOutputs();
		guiItemStacks.set(outputSlot, outputs.get(0));
	}
}
