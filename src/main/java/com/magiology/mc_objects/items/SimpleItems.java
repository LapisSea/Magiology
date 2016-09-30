package com.magiology.mc_objects.items;

import net.minecraft.item.Item;

public class SimpleItems{
	
	public static class ItemMistyPowder extends Item{
		private static ItemMistyPowder instance;
		public ItemMistyPowder(){
			instance=this;
		}
		public static ItemMistyPowder get(){return instance;}
	}
	
}