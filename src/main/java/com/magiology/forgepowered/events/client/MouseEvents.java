package com.magiology.forgepowered.events.client;

import com.magiology.client.gui.custom.hud.WingModeChangerHUD;
import com.magiology.handlers.animationhandlers.WingsFromTheBlackFireHandler;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MouseEvents{
	
	public void mouseInput(MouseEvent event, int id){
		if(id==-1)return;
	}
	public void roll(MouseEvent event, int direction){
		//switch scrolling to WingModeChangerGui
		if(direction==1)WingModeChangerHUD.instance.next();
		else WingModeChangerHUD.instance.prev();
		if(!(!GuiScreen.isCtrlKeyDown()||!WingsFromTheBlackFireHandler.getIsActive(UtilM.getThePlayer())))event.setCanceled(true);
//		
//		if(H.RB(0.9))FakeMessageHUD.addMessage(new Message(new ColorF(), "such mesage"+direction, null));
//		else FakeMessageHUD.addMessage(new Message(new ColorF(), "wow such mesage!", "some "));
	}
	
	
	//caller
	@SubscribeEvent
	public void startMouseInput(MouseEvent event){
		mouseInput(event,event.button);
		int roll=event.dwheel;if(roll==0)return;
		int negative=roll<0?-1:1;roll*=negative;
		for(int a=120;a<=roll;a+=120)roll(event,negative);
		
	}
}
