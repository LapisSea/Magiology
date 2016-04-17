package com.magiology.mcobjects.items;

import com.magiology.util.utilobjects.m_extension.BlockPosM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkPointer extends Item{
	
	public static BlockPosM getTarget(ItemStack stack){
		NBTTagCompound nbt=stack.getTagCompound();
		if(nbt==null)return BlockPosM.ORIGIN;
		return new BlockPosM(nbt.getInteger("xLink"),nbt.getInteger("yLink"),nbt.getInteger("zLink"));
	}
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!stack.hasTagCompound())stack.setTagCompound(new NBTTagCompound());
		if(player.isSneaking()){
			NBTTagCompound NBT=stack.getTagCompound();
			NBT.setInteger("xLink", pos.getX());
			NBT.setInteger("yLink", pos.getY());
			NBT.setInteger("zLink", pos.getZ());
			return true;
		}
		return false;
	}
}
