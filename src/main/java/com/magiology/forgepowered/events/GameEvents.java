package com.magiology.forgepowered.events;

import com.magiology.api.power.PowerCore;
import com.magiology.api.power.PowerUpgrades;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GameEvents{
	
	public static EntityItem dropContainerOnDerstroy(World world,BlockPos pos){
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerUpgrades){
			PowerUpgrades Tile=(PowerUpgrades)tile;
			if(Tile.getcontainerItems()!=null)for(ItemStack a:Tile.getcontainerItems())if(a!=null)return UtilM.dropBlockAsItem(world,pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5,a);
		}
		return null;
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e){
		BlockPos pos=e.getPos();
		EntityPlayer player=e.getPlayer();
		World world=e.getWorld();
		world.getTileEntity(pos);
		if(player.capabilities.isCreativeMode){
			
		}else{
			dropContainerOnDerstroy(world, pos);
		}
	}
	@SubscribeEvent
	public void onBlockPlace(BlockEvent.PlaceEvent e){
		BlockPos pos=e.getPos();
		EntityPlayer player=e.getPlayer();
		World world=e.getWorld();
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof PowerCore)PowerCore.SavePowerToItemEvents.onPowerCorePlaced(pos, player, world, tile);
	}
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e){
		
	}
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save e){
		
	}
}
