package com.magiology.handlers.animationhandlers;

import com.magiology.core.init.MItems;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.SimpleCounter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WingsFromTheBlackFireHandler{
	static SimpleCounter counter=new SimpleCounter();
	public static enum Positions{
		ErrorPos(false,counter.getAndAdd(),new float[][]{{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0},{0,0,0}}),
		FlapBackwardsPos(false,counter.getAndAdd(),new float[][]{{0,-1.5F,-2.5F},{0,-1,-3},{0,-1.5F,-3.5F},{0,-1,-4},{0,-1.5F,-1},{0,-0.5F,-1},{0,-0.5F,-1}}),
		FlapDownPos(false,counter.getAndAdd(),new float[][]{{-17,-3,-3},{-13,0,-2},{-9,0,0},{8,0,0},{7,0,0},{6,0,0},{5,0,0}}),
		FlapForwardPos(false,counter.getAndAdd(),new float[][]{{0,2,2.5F},{0,2.5F,3},{0,3.5F,3.5F},{0,1,4},{0,1.5F,1},{0,0.5F,1},{0,0.5F,1}}),
		FlapUpPos(false,counter.getAndAdd(),new float[][]{{17,3,3},{15,0,2},{12,0,1},{-8,0,0},{-7,0,0},{-6,0,0},{-5,0,0}}),
		FlyBackvardPos(true,counter.getAndAdd(),new float[][]{{10,20,-10},{-5,10,-5},{-5,-10,-5},{0,-10,-5},{-5,-10,-5},{-5,-0,0},{-5,0,0}}),
		FlyForvardPos(true,counter.getAndAdd(),new float[][]{{10,10,20},{0,10,0},{0,10,0},{0,-10,0},{0,-10,0},{-2,-4,0},{-3,-6,0}}),
		FlyStationarPos(true,counter.getAndAdd(),new float[][]{{10,10,0},{0,10,0},{0,-10,0},{0,-10,0},{0,-10,0},{-2,-4,0},{-3,-6,0}}),
		HighSpeedPos(true,counter.getAndAdd(),new float[][]{{-10,-20,0},{-10,-20,0},{10,-15,0},{0,-20,20},{-5,0,0},{-5,0,0},{-10,0,0}}),
		HoverPos(true,counter.getAndAdd(),new float[][]{{10,20,-10},{-10,10,0},{-10,-10,0},{0,-10,0},{-10,-10,0},{-10,-10,0},{-10,-5,0}}),
		NormalPos(true,counter.getAndAdd(),new float[][]{{-30,10,-30},{-20,-20,-5},{-10,-20,-5},{-10,-20,-5},{-10,-30,-5},{-10,-30,-5},{-10,-30,-5}}),
		ProtectivePos(true,counter.getAndAdd(),new float[][]{{6,5,-30},{-40,10,-10},{-55,0,-5},{-45,30,-5},{-40,20,0},{-20,10,10},{-20,-10,0}});
		public static Positions get(int id){
			if(id>-1&&id<values().length)return values()[id]; 
			return ErrorPos;
		}
		public int id;
		public boolean wanted;
		public float[][] wantedRotationAnglesBase;
		private Positions(boolean wanted,int id,float[][] wantedRotationAnglesBase){
			this.wantedRotationAnglesBase=wantedRotationAnglesBase;
			this.id=id;this.wanted=wanted;
		}
	}
	public static WingsFromTheBlackFireHandler instaince=new WingsFromTheBlackFireHandler();
	
	static float p=1F/16F;
	
	@SideOnly(Side.CLIENT)
	public static void animateModel(EntityPlayer player){
		
	}
	public static boolean getIsActive(EntityPlayer player){
		if(player==null)return false;
		if(!UtilM.isItemInStack(MItems.WingsFTBFI, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)))return false;
		return player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).hasTagCompound();
	}
	public static int getPosId(EntityPlayer player){
		int id=-1;
		ItemStack wings=player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if(getIsActive(player))id=wings.getTagCompound().getInteger("WMID");
		return id;
	}
}
