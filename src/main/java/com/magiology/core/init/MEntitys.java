package com.magiology.core.init;

import com.magiology.client.render.entityrender.BallOfEnergyRenderer;
import com.magiology.client.render.models.entitys.BallOfEnergyModel;
import com.magiology.core.Magiology;
import com.magiology.mcobjects.entitys.EntityBallOfEnergy;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MEntitys{

	private static int id=1;

	public static void init(){
		reg(EntityBallOfEnergy.class, 100, 1, true,new BallOfEnergyRenderer(new BallOfEnergyModel(),1));
	}

	public static void reg(Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates){
		EntityRegistry.registerModEntity(entityClass, entityClass.getClass().getCanonicalName(), id, Magiology.getMagiology(),  trackingRange, updateFrequency, sendsVelocityUpdates);
		id++;
	}

	public static void reg(Class<? extends Entity> entityClass, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates,Render model){
		EntityRegistry.registerModEntity(entityClass, entityClass.getClass().getCanonicalName(), id, Magiology.getMagiology(),  trackingRange, updateFrequency, sendsVelocityUpdates);
		RenderingRegistry.registerEntityRenderingHandler(entityClass, model);
		id++;
	}

}
