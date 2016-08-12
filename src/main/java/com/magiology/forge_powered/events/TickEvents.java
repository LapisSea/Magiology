package com.magiology.forge_powered.events;

import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.handlers.particle.Particles;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.RandUtil;
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
				Vec3M pos=Vec3M.conv(UtilC.getThePlayer().getPositionEyes(0));
				
				Particles.MISTY_ENERGY.spawn(pos.add(new Vec3M(RandUtil.CRF(10), RandUtil.CRF(10), RandUtil.CRF(10))), new Vec3M(), 0.1F, 0, new ColorF(0.7+RandUtil.RF(0.3),0.4+RandUtil.RF(0.4),0.2+RandUtil.RF(0.1),0.3+RandUtil.CRF(0.7)));
				//if(UtilC.getThePlayer().isSneaking())Particles.MISTY_ENERGY.compileDisplayList();
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
