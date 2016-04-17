package com.magiology.core.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MRecepies{

	public static void init(){
		GameRegistry.addSmelting(Items.diamond_axe,new ItemStack(Items.diamond, 3), 10);
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.farmland, 8), "AAA", "ABA", "AAA", 'A', Blocks.dirt, 'B', Items.wooden_hoe);
		GameRegistry.addShapedRecipe(new ItemStack(Items.repeater),"   ", "ABA", "CCC", 'A', Blocks.redstone_torch, 'B', Items.redstone , 'C', Blocks.stone_slab);
	}
	
}
