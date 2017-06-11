package com.magiology.forge.events;

import com.magiology.core.registry.init.ParticlesM;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.handlers.frame_buff.TemporaryFrameBufferHandler;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@EventBusSubscriber
public class TickEvents{
	
	public TickEvents(){
		throw new UnsupportedOperationException();
	}
	
	static boolean worldRendering;
	
	private static final List<Runnable>			serverTickQueue	=new ArrayList<>(),clientTickQueue=new ArrayList<>();
	private static final List<PairM<BooleanSupplier,Runnable>>		serverWaitQueue=new ArrayList<>(),clientWaitQueue=new ArrayList<>();
	
	public static void nextTick(Runnable hook){
		nextTick(UtilM.isRemote(), hook);
	}
	
	public static void nextTick(Worldabale worldContainer, Runnable hook){
		nextTick(worldContainer.getWorld(), hook);
	}
	
	public static void nextTick(World worldContainer, Runnable hook){
		nextTick(worldContainer.isRemote, hook);
	}
	
	public static void nextTick(boolean isRemote, Runnable hook){
		(isRemote?clientTickQueue:serverTickQueue).add(hook);
	}
	
	public static void waitRunUntil(Worldabale worldContainer, BooleanSupplier condition, Runnable action){
		waitRunUntil(worldContainer.getWorld(), condition, action);
	}
	
	public static void waitRunUntil(World worldContainer, BooleanSupplier condition, Runnable action){
		waitRunUntil(worldContainer.isRemote, condition, action);
	}
	
	public static void waitRunUntil(BooleanSupplier condition, Runnable action){
		waitRunUntil(UtilM.isRemote(), condition, action);
	}
	
	public static void waitRunUntil(boolean isRemote, BooleanSupplier condition, Runnable action){
		if(condition.getAsBoolean())(isRemote?clientWaitQueue:serverWaitQueue).add(new PairM<>(condition, action));
		else action.run();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START){
			if(UtilC.isWorldOpen()){
				if(UtilC.getWorldTime()%50==0) TemporaryFrameBufferHandler.instance.bufferGC();
				if(!UtilC.getMC().isGamePaused()){
					tick(clientTickQueue);
					waitTick(clientWaitQueue);
					
					Vec3M pos=new Vec3M(UtilC.getThePlayer().getPositionEyes(0));
					
					ParticlesM.MISTY_ENERGY.spawn(pos.add(new Vec3M(RandUtil.CRF(10), RandUtil.CRF(10), RandUtil.CRF(10))), new Vec3M(), 0.1F, 0, new ColorM(0.7+RandUtil.RF(0.3), 0.4+RandUtil.RF(0.4), 0.2+RandUtil.RF(0.1), 0.3+RandUtil.CRF(0.7)));
					ParticleHandler.get().updateParticles();
				}
			}
		}
		UtilM.timerSafety();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderTick(TickEvent.RenderTickEvent event){
		if(event.phase==Phase.START) worldRendering=true;
		PartialTicksUtil.partialTicks=event.renderTickTime;
		TemporaryFrameBufferHandler.instance.renderFrames();
		if(event.phase==Phase.END) worldRendering=false;
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event){
		UtilM.timerSafety();
	}
	
	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event){
		if(event.phase==Phase.START) return;
		tick(serverTickQueue);
		waitTick(serverWaitQueue);
		UpdateTileNBTPacket.upload();
	}
	
	private static void tick(List<Runnable> queue){
		if(queue.isEmpty()) return;
		List<Runnable> r=new ArrayList<>(queue);
		queue.clear();
		r.forEach(Runnable::run);
	}
	
	private static void waitTick(List<PairM<BooleanSupplier,Runnable>> queue){
		if(queue.isEmpty()) return;
		queue.removeIf(b->{
			if(!b.obj1.getAsBoolean()){
				b.obj2.run();
				return true;
			}
			return false;
		});
	}
	
	public static boolean isWorldRendering(){
		return worldRendering;
	}
}
