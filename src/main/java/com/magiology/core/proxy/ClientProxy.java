package com.magiology.forgepowered.proxy;

import static com.magiology.core.MReference.*;

import com.magiology.client.gui.gui.GuiJSProgramEditor;
import com.magiology.client.render.Textures;
import com.magiology.core.init.MEntitys;
import com.magiology.core.init.MGui;
import com.magiology.core.init.MTileEntitys;
import com.magiology.handlers.EnhancedRobot;
import com.magiology.handlers.KeyHandler;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.handlers.particle.Particles;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilobjects.m_extension.BlockM;
import com.magiology.util.utilobjects.m_extension.ItemM;

import net.minecraftforge.common.MinecraftForge;
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
		}catch(Exception e){
			throw new NullPointerException((RandUtil.RB(0.1)?"CRAP! :(":"")+" "+NAME+" has encountered with a problem while trying to initialize the input robot! This might be the result of incompatible hardware or java version.");
		}
		ClientProxy.ROBOT=robotH;
		
		MGui.preInit();
		
	}
	
	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new KeyHandler());
		MTileEntitys.initRenders();
		MGui.registerGuis();
		MTileEntitys.initCustomRenderers();
		Particles.registerParticles();
		BlockM.registerBlockModels();
		ItemM.registerItemModels();
		MEntitys.initRenders();
	}
	
	@Override
	public void postInit(){
		GuiJSProgramEditor.loadClass();
		Textures.postInit();
		TheHandHandler.init();
	}
	
	@Override
	public void onExit(){
		
	}
}

