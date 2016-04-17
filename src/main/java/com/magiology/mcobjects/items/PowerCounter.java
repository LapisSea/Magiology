package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.util.utilobjects.m_extension.ItemM;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
public class PowerCounter extends ItemM{
	public PowerCounter(){
		   super();
	   }
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean is){
	
		
	list.add("Use for checking how much fire units (FU) is in block.");
	
	}
	@Override
	public boolean isFull3D(){return true;}
	
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		boolean state=itemstack.getTagCompound().getBoolean("state");
		boolean notDone=true;
		RenderEvents.disabledEquippItemAnimationTime=2;
		if(player.isSneaking()&&world.isRemote){
			if(state==false&&notDone==true){
				state=true;
				notDone=false;
			}
			if(state==true&&notDone==true){
				state=false;
				notDone=false;
			}
		}
		itemstack.getTagCompound().setBoolean("state", state);
		return itemstack;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {}
	
}
