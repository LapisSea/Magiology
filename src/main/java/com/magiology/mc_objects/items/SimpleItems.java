package com.magiology.mc_objects.items;

import com.magiology.util.m_extensions.ItemM;

public class SimpleItems{
	
	public static class ItemMistyPowder extends ItemM{
		private static ItemMistyPowder instance;
		public ItemMistyPowder(){
			instance=this;
		}
		public static ItemMistyPowder get(){return instance;}
	}
	
}