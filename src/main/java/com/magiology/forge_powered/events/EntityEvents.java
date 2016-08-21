package com.magiology.forge_powered.events;

import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEvents{
	
	public static final EntityEvents instance=new EntityEvents();
	private EntityEvents(){}

	@SubscribeEvent
	public void entityInteract(EntityInteract e){
		e.getEntityPlayer().startRiding(e.getTarget(),true);
	}
	
}
