package com.magiology.forgepowered.events;

import com.magiology.api.power.ISidedPower;
import com.magiology.client.gui.container.ISidedPowerInstructorContainer;
import com.magiology.handlers.GenericPacketEventHandler;
import com.magiology.handlers.GenericPacketEventHandler.IntegerPacketEvent;
import com.magiology.handlers.GenericPacketEventHandler.PacketEvent;
import com.magiology.handlers.GenericPacketEventHandler.StringPacketEvent;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.UpdateablePipeHandler;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class GenericPacketEvents{
	public static GenericPacketEventHandler callerInstance;
	public void intPacketEvent(IntegerPacketEvent event){
		EntityPlayer player=event.player;
		int integer=event.integer;
		try{switch (event.eventId){
		case 5:{
			//wings has space update
			if(WingsFromTheBlackFireHandler.getIsActive(player))player.getCurrentArmor(2).getTagCompound().setBoolean("HS", integer==1);
		}break;
		case 6:{
			WingsFromTheBlackFireHandler.setPosId(player, integer);
		}break;
		case 7:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			PowerUtil.cricleSideInteraction((ISidedPower)tileEn, integer);
			UpdateablePipeHandler.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		case 8:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			ISidedPower tile=(ISidedPower)tileEn;
			tile.setSendOnSide(integer, !tile.getOut(integer));
			UpdateablePipeHandler.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		case 9:{
			TileEntity tileEn=((ISidedPowerInstructorContainer)player.openContainer).tile;
			ISidedPower tile=(ISidedPower)tileEn;
			tile.setReceaveOnSide(integer, !tile.getIn(integer));
			UpdateablePipeHandler.updatein3by3(player.worldObj, tileEn.getPos());
		}break;
		default:{PrintUtil.println("ERROR! EVENT IntegerPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
	
	public void stringPacketEvent(StringPacketEvent event){
		EntityPlayer player=event.player;
		String string=event.string;
		try{switch (event.eventId){
		case 0:{
			int x=0,y=0,xTest,yTest;
			char[] chars=string.toCharArray();
			xTest=Integer.parseInt(chars[0]+"");
			yTest=Integer.parseInt(chars[2]+"");
			switch(xTest){case 0:x=0;break;case 1:x=1;break;case 2:x=-1;break;case 3:x=0;break;}
			switch(yTest){case 0:y=0;break;case 1:y=1;break;case 2:y=-1;break;case 3:y=0;break;}
			SpecialMovmentEvents.instance.doubleJumpEvent(player,x,y);
		}break;
		case 1:{
			
		}break;
		default:{PrintUtil.println("ERROR! EVENT StringPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
	public void voidPacketEvent(PacketEvent event){
		EntityPlayer player=event.player;
		try{switch (event.eventId){
		case 0:{
			TheHandHandler.nextPosition(player);
		}break;
		default:{PrintUtil.println("ERROR! EVENT voidPacketEvent HAS BEEN RAN WITH A INVALID EVENT ID!","PLEASE ADD THE ID TO THE SWITCH IN THE EVENT HANDLER!");}break;
		}}catch(Exception e){e.printStackTrace();}
	}
}
