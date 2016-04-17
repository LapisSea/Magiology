package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.core.init.MGui;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.util.utilobjects.NBTUtil;
import com.magiology.util.utilobjects.m_extension.ItemM;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IPowerSidenessInstructor extends ItemM{
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		list.add(ChatFormatting.BLUE+"Right click on a block to configure the power interaction on a side.");
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		NBTUtil.createNBT(itemStack);
	}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		NBTUtil.createNBT(itemstack);
		boolean isIt=false;
		if(world.getTileEntity(pos) instanceof ISidedPower){
			isIt=true;
			GuiHandlerM.openGui(player, MGui.GuiISidedPowerInstructor, pos);
		}
		
		
		return isIt;
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		NBTUtil.createNBT(itemstack);
	}

	
}
