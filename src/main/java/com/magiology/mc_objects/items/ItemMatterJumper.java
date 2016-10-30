package com.magiology.mc_objects.items;

import java.util.List;

import com.magiology.util.objs.NBTUtil;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMatterJumper extends ItemOwnable{
	
	
	public static enum MatterJumperMode{
		WRENCH(0),
		BINDER(1);
		
		private static final String MARKER="mode";
		
		public final int id;
		
		private MatterJumperMode(int id){
			this.id=id;
		}
		
	}
	
	public ItemMatterJumper(){
		super(true);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced){
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(getModeAsString(stack));
	}
	
	public static MatterJumperMode getMode(ItemStack stack){
		return MatterJumperMode.values()[MathUtil.snapToArray(NBTUtil.getInt(stack, MatterJumperMode.MARKER), MatterJumperMode.values())];
	}
	public static String getModeAsString(ItemStack stack){
		return getMode(stack).toString();
	}
	public static void setMode(ItemStack stack, MatterJumperMode mode){
		NBTUtil.setInt(stack, MatterJumperMode.MARKER, mode==null?0:mode.id);
	}
}
