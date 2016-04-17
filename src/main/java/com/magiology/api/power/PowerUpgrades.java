package com.magiology.api.power;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface PowerUpgrades{
	public ItemStack[] getcontainerItems();
	//getters
	public int getNumberOfContainerSlots();
	//random
	public boolean isUpgradeInInv(Item upgradeItem);
	public void setcontainerItems(ItemStack[] containerItems);
	//setters
	public void setNumberOfContainerSlots(int Int);
	
}
