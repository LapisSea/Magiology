package com.magiology.mc_objects.items;

import com.magiology.client.rendering.item.renderers.ItemMatterJumperRenderer;
import com.magiology.util.interf.Renderable;
import com.magiology.util.interf.SpecialRender;
import com.magiology.util.objs.NBTUtil;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMatterJumper extends ItemOwnable implements SpecialRender{
	
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
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flagIn){
		super.addInformation(stack, world, tooltip, flagIn);
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public Renderable getRenderer(){
		return ItemMatterJumperRenderer.get();
	}
}
