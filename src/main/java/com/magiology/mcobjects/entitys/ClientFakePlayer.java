package com.magiology.mcobjects.entitys;

import java.lang.reflect.Field;

import com.magiology.util.utilclasses.UtilM;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ClientFakePlayer extends EntityPlayer {
	
	private static GameProfile MC;
	static{
		Field uuddThingy=ReflectionHelper.findField(FakePlayerFactory.class, "MINECRAFT");
		try{
			MC=(GameProfile)uuddThingy.get(null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ClientFakePlayer(){
		super(UtilM.getTheWorld(), MC);
	}

	@Override
	public boolean isSpectator(){
		return true;
	}
	
	public void setPitch(float pitch){
		cameraPitch=pitch;
		rotationPitch=pitch;
	}
	public void setPos(double x,double y,double z){
		posX=x;
		posY=y;
		posZ=z;
	}
	
	public void setPrevPitch(float pitch){
		prevCameraPitch=pitch;
		prevRotationPitch=pitch;
	}
	public void setPrevPos(double x,double y,double z){
		prevPosX=x;
		prevPosY=y;
		prevPosZ=z;
		
	}
	
	public void setPrevYaw(float yaw){
		prevCameraYaw=yaw;
		prevRotationYawHead=yaw;
	}
	public void setYaw(float yaw){
		cameraYaw=yaw;
		rotationYawHead=yaw;
	}
}
