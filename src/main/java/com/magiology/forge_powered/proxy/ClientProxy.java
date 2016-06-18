package com.magiology.forge_powered.proxy;

import com.magiology.util.objs.EnhancedRobot;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy{
	
	
	public static EnhancedRobot ROBOT;

	@Override
	public void loadModFiles(){
		
	}
	
	@Override
	public void preInit(){
		EnhancedRobot robotH=null;
		try{
			robotH=new EnhancedRobot();
		}catch(Exception e){}
		ROBOT=robotH;
		
		
	}
	
	@Override
	public void init(){
		
	}
	
	@Override
	public void postInit(){
		
	}
	
	@Override
	public void onExit(){
		
	}
}

