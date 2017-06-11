package com.magiology.util.m_extensions;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemM extends Item{
	
	public ItemM(){}
	
	public boolean isInStack(ItemStack stack){
		return stack!=null&&stack.getItem()==this;
	}
}
