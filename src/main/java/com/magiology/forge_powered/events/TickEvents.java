package com.magiology.forge_powered.events;

import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TickEvents{
	
	
	public static TickEvents instance=new TickEvents();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event){
		if(event.phase!=Phase.START){
			if(!UtilC.getMC().isGamePaused()&&UtilC.isWorldOpen()){
//				Vec3M pos=Vec3M.conv(UtilC.getThePlayer().getLook(0).add(UtilC.getThePlayer().getPositionEyes(0)));
//				for(int i=0;i<1;i++)Particles.MIST_BUBBLE.spawn(pos,
//						new Vec3M(RandUtil.CRF(0.01), RandUtil.CRF(0.01), RandUtil.CRF(0.01)),
//						2F+RandUtil.CRF(1),
//						60,
//						new Vec3M(RandUtil.CRF(0.001), RandUtil.CRF(0.001), RandUtil.CRF(0.001)),
//						new ColorF(RandUtil.RF(),RandUtil.RF(),RandUtil.RF(),0.05));
				ParticleHandler.instance.updateParticles();
				//				if(UtilC.getWorldTime()%60==0){
				//					ShaderHandler.get().load();
				//					PrintUtil.println("shaders reloaded");
				//				}
			}
			return;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		PartialTicksUtil.partialTicks=event.renderTickTime;
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		
	}
}
