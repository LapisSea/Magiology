package com.magiology.forge.events;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ClientEvents{

	@SubscribeEvent
	public static void textureStitchPre(TextureStitchEvent.Pre e){
		//e.getMap().registerSprite(new ResourceLocationM(""));
	}
	@SubscribeEvent
	public static void textureStitchPost(TextureStitchEvent.Post e){
		
	}
	@SubscribeEvent
	public static void modelBake(ModelBakeEvent e){
//		e.getModelLoader().
	}
	
}
