package com.magiology.registry.events;

import static net.minecraftforge.fml.common.eventhandler.Event.Result.*;

import com.magiology.registry.WrenchRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerWrenchEvent extends Event{
	
	public static PlayerWrenchEvent create(EntityPlayer player, BlockPos pos, EnumFacing face){
		ItemStack wrench;
		wrench=player.getHeldItemMainhand();
		if(WrenchRegistry.isWrench(wrench)){
			PlayerWrenchEvent event=new PlayerWrenchEvent(player, pos, face, player.worldObj,wrench);
			MinecraftForge.EVENT_BUS.post(event);
			return event;
		}
		return null;
	}
	public EntityPlayer entityPlayer;
	public EnumFacing face;
	public BlockPos pos;
	public Result useBlock = DEFAULT,useItem = DEFAULT;
	public World world;
	
	public ItemStack wrenchItem;
	private PlayerWrenchEvent(EntityPlayer player, BlockPos pos, EnumFacing face, World world, ItemStack wrench){
		entityPlayer=player;
		this.pos=pos;
		this.face=face;
		if(face==null)useBlock=DENY;
		this.world=world;
		wrenchItem=wrench;
	}
	@Override
	public boolean isCancelable(){
		return false;
	}

	@Override
	public void setCanceled(boolean cancel)
	{
		super.setCanceled(cancel);
		useBlock = (cancel ? DENY : useBlock == DENY ? DEFAULT : useBlock);
		useItem = (cancel ? DENY : useItem == DENY ? DEFAULT : useItem);
	}
}
