package com.magiology.forge.events;

import java.util.ArrayList;
import java.util.List;

import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.handlers.frame_buff.TemporaryFrameBufferHandler;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.mc_objects.particles.Particles;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickEvents{
	
	
	public static TickEvents instance=new TickEvents();
	static boolean worldRendering;
	
	private final List<Runnable> serverTickQueue=new ArrayList<>(),clientTickQueue=new ArrayList<>();

	public static void nextTick(Runnable hook){
		nextTick(UtilM.isRemote(),hook);
	}
	public static void nextTick(boolean isRemote,Runnable hook){
		(isRemote?instance.clientTickQueue:instance.serverTickQueue).add(hook);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START){
			if(UtilC.isWorldOpen()){
				if(UtilC.getWorldTime()%50==0)TemporaryFrameBufferHandler.instance.bufferGC();
				if(!UtilC.getMC().isGamePaused()){
					tick(clientTickQueue);
					
					Vec3M pos=new Vec3M(UtilC.getThePlayer().getPositionEyes(0));
					
					Particles.MISTY_ENERGY.spawn(pos.add(new Vec3M(RandUtil.CRF(10), RandUtil.CRF(10), RandUtil.CRF(10))), new Vec3M(), 0.1F, 0, new ColorM(0.7+RandUtil.RF(0.3),0.4+RandUtil.RF(0.4),0.2+RandUtil.RF(0.1),0.3+RandUtil.CRF(0.7)));
					ParticleHandler.get().updateParticles();
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		if(event.phase==Phase.START)worldRendering=true;
		PartialTicksUtil.partialTicks=event.renderTickTime;
		TemporaryFrameBufferHandler.instance.renderFrames();
		if(event.phase==Phase.END)worldRendering=false;
	}
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		if(event.phase==Phase.START)return;
		tick(serverTickQueue);
		UpdateTileNBTPacket.upload();
	}
	private void tick(List<Runnable> queue){
		if(queue.isEmpty())return;
		List<Runnable> r=new ArrayList<>(queue);
		queue.clear();
		r.forEach(Runnable::run);
	}
	public static boolean isWorldRendering(){
		return worldRendering;
	}
}
