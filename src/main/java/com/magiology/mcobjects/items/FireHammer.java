package com.magiology.mcobjects.items;

import java.util.List;

import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.PowerCore;
import com.magiology.registry.WrenchRegistry;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.ItemM;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireHammer extends ItemM{
	
	public FireHammer(){
		WrenchRegistry.registerWrench(this);
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		if(itemStack.getTagCompound() != null){
				list.add(ChatFormatting.BLUE+" pos.getX()="+ChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("xC"))+
					 ChatFormatting.BLUE+" pos.getY()="+ChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("yC"))+
					 ChatFormatting.BLUE+" pos.getZ()="+ChatFormatting.AQUA+Integer.toString(itemStack.getTagCompound().getInteger("zC"))
					 );
		}
		
	}
	
	@Override
	public boolean isFull3D(){return true;}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		itemStack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack itemstack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float x, float y, float z){
		if(itemstack.getTagCompound()==null)itemstack.setTagCompound(new NBTTagCompound());
		boolean isit=false;
		boolean isit2=false;
		TileEntity tile1=world.getTileEntity(pos);
		
		if(player.isSneaking()){
			UtilM.getBlock(world, pos);
			if(tile1 instanceof PowerCore){
				isit2=true;
			}
		}
		
		int inorout=-1;
		TileEntity tile;
		tile=world.getTileEntity(pos);
		
		if(tile instanceof ISidedPower){
			ISidedPower isTile=(ISidedPower)tile;
			
			int var1=side.getIndex();
			switch (side.getIndex()){
			case 4:side=EnumFacing.getFront(5);break;
			case 3:side=EnumFacing.getFront(4);break;
			case 5:side=EnumFacing.getFront(3);break;
			case 0:side=EnumFacing.getFront(1);break;
			case 1:side=EnumFacing.getFront(0);break;
			}
			
			
			PowerUtil.cricleSideInteraction(isTile, side.getIndex());
			side=EnumFacing.getFront(var1);
		}
		
		return UtilM.booleanToActionResult(isit2);
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5){
		if(itemstack.getTagCompound()==null)itemstack.setTagCompound(new NBTTagCompound());
		
//		if(!H.isRemote(entity)||world.getTotalWorldTime()%15!=0)return;
//		RayTraceResult objectPosition=((EntityPlayer)entity).rayTrace(100, 0);
//		if(objectPosition!=null&&objectPosition.hitVec!=null){
//			float pos.getX()=(float) objectPosition.hitVec.xCoord;
//			float pos.getY()=(float) objectPosition.hitVec.yCoord;
//			float pos.getZ()=(float) objectPosition.hitVec.zCoord;
//			EntitySparkFX en=new EntitySparkFX(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 0.5F, 4F, 1, 4, 130, new Vec3M(0, -2F, 0));
////			EntitySparkFX en=new EntitySparkFX(world, pos, 0.1F, 0.2F, 1, 4, 100, new Vec3M(0, 0.1F, 0));
//			H.spawnEntityFX(en);
//
//			en.endColor.a=0.3F;
//		}
		
	}

	
}
