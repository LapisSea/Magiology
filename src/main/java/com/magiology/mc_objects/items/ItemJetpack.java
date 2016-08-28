package com.magiology.mc_objects.items;

import java.util.ArrayList;

import com.magiology.mc_objects.entitys.EntityPenguin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemJetpack extends Item{
	
	
	private static final ItemJetpack instance=new ItemJetpack();
	public static ItemJetpack get(){
		return instance;
	}
	
	private final ArrayList<Class<? extends Entity>> allowedEntitys=new ArrayList<>();
	
	public ItemJetpack(){
		allowedEntitys.add(EntityPlayer.class);
		allowedEntitys.add(EntityPenguin.class);
	}
	
	@Override
	public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity){
		return armorType==EntityEquipmentSlot.CHEST&&allowedEntitys.contains(entity.getClass());
	}
	
}
