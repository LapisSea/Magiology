package com.magiology.forgepowered.events;

import org.lwjgl.opengl.Display;

import com.magiology.client.gui.custom.DownloadingIcon;
import com.magiology.client.gui.custom.hud.FakeMessageHUD;
import com.magiology.client.gui.custom.hud.HUD;
import com.magiology.client.gui.custom.hud.MainMenuUpdateNotificationHUD;
import com.magiology.core.MUpdater;
import com.magiology.forgepowered.events.client.RenderEvents;
import com.magiology.handlers.particle.ParticleHandler;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.world.World;
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
//				for(int i=0;i<30;i++)Particles.MIST_BUBBLE.spawn(
//						Vec3M.conv(UtilC.getMC().objectMouseOver.hitVec), 
//						new Vec3M(RandUtil.CRF(0.05), RandUtil.CRF(0.05), RandUtil.CRF(0.05)), 0.2F+RandUtil.CRF(0.5), 
//						60, 
//						new Vec3M(RandUtil.CRF(0.003), RandUtil.CRF(0.003), RandUtil.CRF(0.003)), 
//						new ColorF(RandUtil.RF(), RandUtil.RF(), RandUtil.RF(), 1));
				ParticleHandler.instance.updateParticles();
			}
			return;
		}
		
		if(MUpdater.getFoundNew()&&UtilC.getMC().currentScreen instanceof GuiMainMenu)MainMenuUpdateNotificationHUD.instance.update();
		DownloadingIcon.update();
		
		if(UtilC.getMC().isGamePaused())return;
		if(UtilC.getThePlayer()==null)return;
		
		FakeMessageHUD.get().update();
		
		if(RenderEvents.disabledEquippItemAnimationTime>0)RenderEvents.disabledEquippItemAnimationTime--;
		else if(RenderEvents.disabledEquippItemAnimationTime<0)RenderEvents.disabledEquippItemAnimationTime=0;
		
		RenderEvents.FPGui.forEach(HUD::update);
		
//		PlayerClothPhysiscHandeler.updateInstances();
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event){
		PartialTicksUtil.partialTicks=event.renderTickTime;
		
		if(event.phase!=Phase.END)return;
		
		if(MUpdater.getFoundNew()&&UtilC.getMC().currentScreen instanceof GuiMainMenu)MainMenuUpdateNotificationHUD.instance.render(Display.getWidth(), Display.getHeight(),event.renderTickTime);
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		
	}
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
		//checking if the tick should be activated: start--|
		World world=event.world;//-------------------------|
		if(UtilM.isNull(world))return;//------------------|
		if(event.phase==Phase.END)return;
//		if(event.type!=Type.WORLD)return;
		//checking if the tick should be activated: end----|
//		Helper.println((wTime==lastTick)+"\t"+event.type+" "+event.side+" "+event.phase);
		/**Caling for my gui registry for updating**/
//		lastTick=wTime;
	}
}
