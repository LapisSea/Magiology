package com.magiology.cross_mod.jei;

import com.magiology.cross_mod.ExtensionLoader;
import com.magiology.cross_mod.jei.shaker.*;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.*;

@JEIPlugin
public class MagiologyPlugin extends BlankModPlugin implements ExtensionLoader{
	
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
	public void register(IModRegistry registry) {
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		jeiHelpers = registry.getJeiHelpers();
		
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		registry.addRecipeCategories(
				new ShakerCategory(guiHelper)
		);
		
		registry.addRecipeHandlers(
				new ShakingRecipeHandler()
		);
		
//		registry.addRecipeClickArea(GuiCrafting.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
//		registry.addRecipeClickArea(GuiBrewingStand.class, 97, 16, 14, 30, VanillaRecipeCategoryUid.BREWING);
//		registry.addRecipeClickArea(GuiFurnace.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
//		
//		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
//
//		recipeTransferRegistry.addRecipeTransferHandler(ContainerWorkbench.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
//		recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnace.class, VanillaRecipeCategoryUid.SMELTING, 0, 1, 3, 36);
//		recipeTransferRegistry.addRecipeTransferHandler(ContainerFurnace.class, VanillaRecipeCategoryUid.FUEL, 1, 1, 3, 36);
//		recipeTransferRegistry.addRecipeTransferHandler(ContainerBrewingStand.class, VanillaRecipeCategoryUid.BREWING, 0, 4, 5, 36);
//
//		registry.addRecipeCategoryCraftingItem(new ItemStack(Blocks.CRAFTING_TABLE), VanillaRecipeCategoryUid.CRAFTING);
//		registry.addRecipeCategoryCraftingItem(new ItemStack(Blocks.FURNACE), VanillaRecipeCategoryUid.SMELTING, VanillaRecipeCategoryUid.FUEL);
//		registry.addRecipeCategoryCraftingItem(new ItemStack(Items.BREWING_STAND), VanillaRecipeCategoryUid.BREWING);
//
		registry.addRecipes(ShakingRecipeMaker.getShakerRecipes(jeiHelpers));
	}
	
}
