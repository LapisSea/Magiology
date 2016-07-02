package com.magiology.forge_powered.proxy;

import com.magiology.client.rendering.tile_render.DummyTileEntityRenderer;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.forge_powered.events.RenderEvents;
import com.magiology.handlers.particle.Particles;
import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.EnhancedRobot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
		ShaderHandler.get().load();
		
		
	}
	
	@Override
	public void init(){
		MinecraftForge.EVENT_BUS.register(new RenderEvents());
		registerTileRedners();
		Particles.registerParticles();
	}
	
	@Override
	public void postInit(){
	}
	
	@Override
	public void onExit(){
		
	}
	
	private void registerTileRedners(){
		bindTileWithRenderer(DummyTileEntity.class, new DummyTileEntityRenderer());
	}
	
	public static void bindTileWithRenderer(Class<?extends TileEntityM>tileEntityClass, TileEntitySpecialRendererM specialRenderer){
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass,specialRenderer);
	}
}

