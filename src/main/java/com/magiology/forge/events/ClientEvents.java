package com.magiology.forge.events;

import com.magiology.core.registry.init.ShadersM;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ClientEvents{
	
	@SubscribeEvent
	public static void textureStitchPre(TextureStitchEvent.Pre e){
		
	}
	
	@SubscribeEvent
	public static void textureStitchPost(TextureStitchEvent.Post e){
		
	}
	
	@SubscribeEvent
	public static void modelBake(ModelBakeEvent e){}
	
	@SubscribeEvent
	public static void playerjoin(ModelBakeEvent e){
		ShadersM.reload();
	}
	
}
