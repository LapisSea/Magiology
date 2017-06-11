package com.magiology.forge.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EntityEvents{
	
	public EntityEvents(){throw new UnsupportedOperationException();}
	
	@SubscribeEvent
	public static void entityInteract(EntityInteract e){
		//		e.getEntityPlayer().startRiding(e.getTarget(),true);
	}
	
}
