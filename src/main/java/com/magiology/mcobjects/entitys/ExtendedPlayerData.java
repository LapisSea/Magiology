package com.magiology.mcobjects.entitys;

import com.magiology.forgepowered.packets.packets.SendPlayerDataPacket;
import com.magiology.forgepowered.packets.packets.UploadPlayerDataPacket;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

//props to coolAlias on mc forums for teaching me this! :D
public class ExtendedPlayerData implements IExtendedEntityProperties{
	public final static String EXT_PROP_NAME = "ExtendedPlayerData";
	public static void enshure(EntityPlayer player){if(get(player)==null)register(player);}
	public static ExtendedPlayerData get(EntityPlayer player){return player==null?null:(ExtendedPlayerData)player.getExtendedProperties(EXT_PROP_NAME);}
	public static ExtendedPlayerData register(EntityPlayer player){
		player.registerExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME, new ExtendedPlayerData(player));
		return ExtendedPlayerData.get(player);
	}
	public boolean isFlappingDown;
	private int jumpCount;
	public boolean[] keys=new boolean[6];
	
	public final EntityPlayer player;
	
	private float reducedFallDamage;

	public int soulFlame,maxSoulFlame;
	public ExtendedPlayerData(EntityPlayer player){
		this.player=player;
		this.maxSoulFlame=5000;
	}
	public int getJupmCount(){return jumpCount;}
	
	public int getKeysX(){
		int x=0;if(keys[0])x++;
		if(keys[1])x--;return x;
	}
	public int getKeysY(){
		int y=0;if(keys[2])y++;
		if(keys[3])y--;return y;
	}

	public int getKeysZ(){
		int z=0;if(keys[4])z++;
		if(keys[5])z--;return z;
	}
	public float getReducedFallDamage(){return reducedFallDamage;}
	//------------------------
	@Override public void init(Entity entity, World world){}
	@Override
	public void loadNBTData(NBTTagCompound Nbt){
		NBTTagCompound NBT=(NBTTagCompound)Nbt.getTag(EXT_PROP_NAME);
		this.soulFlame=NBT.getInteger("SF");
		this.maxSoulFlame=NBT.getInteger("MSF");
		this.jumpCount=NBT.getInteger("JC");
		this.reducedFallDamage=NBT.getFloat("RFD");
	}
	
	public boolean onJump(){
		boolean change=false;
		int prevJupmCount=jumpCount;
		jumpCount++;
		change=prevJupmCount!=jumpCount;
		if(change||true)sendData();
		return change;
	}public boolean onLand(boolean isSurvival){
		boolean change=false;
		int prevJupmCount=jumpCount;
		jumpCount=0;
		change=prevJupmCount!=jumpCount;
		reducedFallDamage=0;
		if(change)sendData();
		return change;
	}@Override
	public void saveNBTData(NBTTagCompound Nbt){
		NBTTagCompound NBT=new NBTTagCompound();
		NBT.setInteger("SF", soulFlame);
		NBT.setInteger("MSF", maxSoulFlame);
		NBT.setInteger("JC", jumpCount);
		NBT.setFloat("RFD", reducedFallDamage);
		Nbt.setTag(EXT_PROP_NAME, NBT);
	}

	public void sendData(){
		if(player.worldObj.isRemote||player==null)return;
		try{UtilM.sendMessage(new SendPlayerDataPacket(player));}catch(Exception e){e.printStackTrace();}
	}
	public void setJupmCount(int count){jumpCount=count;}
	public boolean setReducedFallDamage(float ReducedFallDamage){
		float prevReducedFallDamage=reducedFallDamage;
		reducedFallDamage=ReducedFallDamage;
		return prevReducedFallDamage!=reducedFallDamage;
	}
	public void uploadData(){
		if(!player.worldObj.isRemote||player==null)return;
		try{UtilM.sendMessage(new UploadPlayerDataPacket(player));}catch(Exception e){e.printStackTrace();}
	}
}
