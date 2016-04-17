package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.forgepowered.events.client.CustomRenderedItem;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.animationhandlers.thehand.animation.CommonHand;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TheHand extends Item implements CustomRenderedItem{
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
//		if(itemStack.getTagCompound() != null){
//				list.add(EnumChatFormatting.BLUE+" X="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("xC"))+
//					 EnumChatFormatting.BLUE+" Y="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("yC"))+
//					 EnumChatFormatting.BLUE+" Z="+EnumChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("zC"))
//					 );
//		}
//		list.add("hold ");
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_){return 100000;}
	
	@Override
	public CustomRenderedItemRenderer getRenderer(ItemStack stack){
		return TheHandHandler.getRenderer();
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		NBTUtil.createNBT(itemStack);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player){
		
		HandPosition ap=TheHandHandler.getActivePosition(player);
		if(ap==CommonHand.weaponHolder){
			TheHandHandler.actionAnimation(player);
			player.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}else if(ap==CommonHand.naturalPosition){
			TheHandHandler.handUseAnimation(player);
		}
		return itemstack;
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ){
		TheHandHandler.handUseAnimation(player);
		HandPosition ap=TheHandHandler.getActivePosition(player);
		if(ap==CommonHand.weaponHolder){
			TheHandHandler.actionAnimation(player);
			player.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}else if(ap==CommonHand.naturalPosition){
			TheHandHandler.handUseAnimation(player);
		}
		return false;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity){
		if(TheHandHandler.getActivePosition(player)==CommonHand.closedFist){
			
			player.worldObj.createExplosion(player, entity.posX, entity.posY-0.0005, entity.posZ, 0.01F, false);
			return true;
		}
		return false;
	}
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int timeHeld){
		timeHeld-=100000;
		timeHeld*=-1;
		if(timeHeld<10)return;
//		if(!world.isRemote){
////			HandComonPositions ap=TheHandHandler.getActivePosition(player);
////			if(ap==HandComonPositions.ReadyForAction)TheHandHandler.addANewEvent(player, player.worldObj.getTotalWorldTime()+5, "spawnProjectile", timeHeld);
////			else if(ap==HandComonPositions.WeaponHolder)TheHandHandler.addANewEvent(player, player.worldObj.getTotalWorldTime()+5, "spawnEntitySubatomicWorldDeconstructor", timeHeld);
//		}
//		else TheHandHandler.actionAnimation(player);
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		NBTUtil.createNBT(itemstack);
		if(entity instanceof EntityPlayer){
//			((EntityPlayer)entity).setItemInUse(itemstack, 100);
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged;
	}
}
