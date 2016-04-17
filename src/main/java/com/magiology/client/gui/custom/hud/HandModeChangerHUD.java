package com.magiology.client.gui.custom.hud;

import java.awt.Color;

import com.magiology.core.init.MItems;
import com.magiology.handlers.animationhandlers.thehand.HandPosition;
import com.magiology.handlers.animationhandlers.thehand.TheHandHandler;
import com.magiology.mcobjects.entitys.ExtendedPlayerData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.gui.FontRenderer;

public class HandModeChangerHUD extends HUD{
	public static HandModeChangerHUD instance=new HandModeChangerHUD();
	private ExtendedPlayerData data;
	public float handAlpha=0,lastHandAlpha;
	
	private HandModeChangerHUD(){}
	
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		if(data==null)data=ExtendedPlayerData.get(player);
		if(data==null)return;
		if(data.player!=player){
			data=ExtendedPlayerData.get(player);
		}if(data==null)return;
		FontRenderer fr=TessUtil.getFontRenderer();
		if(UtilM.isNull(player,fr))return;
		if(handAlpha>0&&UtilM.isItemInStack(MItems.theHand, player.getHeldItemMainhand())){
			float HandAlpha=PartialTicksUtil.calculatePos(lastHandAlpha,handAlpha);
			int slot=player.inventory.currentItem;
			OpenGLM.pushMatrix();
			
			int posId=0;
			for(int b=0;b<HandPosition.values().length;b++){
				if(HandPosition.values()[b].equals(TheHandHandler.getActivePosition(player))){
					posId=b;
					continue;
				}
			}
			int a1=-1,a2=-1,a3=-1;
			if(posId==1){
				a1=posId-1;
				a2=posId;
				a3=posId+1;
			}else if(posId==0){
				a1=2;
				a2=posId;
				a3=posId+1;
			}else{
				a1=1;
				a2=posId;
				a3=0;
			}
			String up="string "+a1,now="string "+a2,down="string "+a3;
			
			GL11U.setUpOpaqueRendering(1);
			OpenGLM.translate(slot*20+xScreen/2-95, yScreen-38, 0);
			
			OpenGLM.translate(0, -HandAlpha*20+20, 0);
			OpenGLM.translate(13, 0, 0);
			OpenGLM.scale(HandAlpha, HandAlpha, HandAlpha);
			OpenGLM.rotate(HandAlpha*90-90, 0, 0, 1);
			OpenGLM.translate(-13, 0, 0);
			
			OpenGLM.translate(-10+HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(up, 0,-9, Color.WHITE.hashCode());
			OpenGLM.translate(10-HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(now, 0, 0, Color.WHITE.hashCode());
			OpenGLM.translate(10-HandAlpha*10, 0, 0);
			fr.drawStringWithShadow(down, 0, 9, Color.WHITE.hashCode());
			
			
			GL11U.endOpaqueRendering();
			OpenGLM.popMatrix();
		}
		
	}
	@Override
	public void update(){
		if(UtilM.isNull(UtilM.getThePlayer()))return;
		lastHandAlpha=handAlpha;
		handAlpha+=UtilM.getThePlayer().isSneaking()?0.25:-0.25;
		handAlpha=MathUtil.snap(handAlpha, 0, 1);
	}
}
