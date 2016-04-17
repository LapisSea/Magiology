package com.magiology.client.gui.custom;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import com.magiology.client.gui.custom.hud.HUD;
import com.magiology.core.MReference;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.util.ResourceLocation;

public class DownloadingIcon {
	private static class Drop{
		public float alpha,scale,speed;
		public boolean dead=false;
		public Vector2f pos,prevPos;
		public Drop(Vector2f pos, float scale){
			this.pos = pos;
			this.scale = scale;
		}
		
	}
	
	private static ArrayList<Drop> drops=new ArrayList<Drop>();
	private static ResourceLocation main=new ResourceLocation(MReference.MODID,"/textures/gui/downloading.png");
	
	private static int timeout=0;
	public static void draw(ColorF color){
		timeout=40;
		OpenGLM.pushMatrix();
		TessUtil.bindTexture(main);
		
		for(Drop drop:drops){
			OpenGLM.pushMatrix();
			OpenGLM.color(1, 1, 1, drop.alpha);
			OpenGLM.translate(PartialTicksUtil.calculatePos(drop.prevPos.x, drop.pos.x), PartialTicksUtil.calculatePos(drop.prevPos.y, drop.pos.y), 0);
			GL11U.glRotate(0, 0, -drop.scale*drop.alpha*180, 14, 25, 0);
			
			HUD.drawRect(1F/368, 1F/399, 0, 0, 340, 0, 28, 51);
			
			OpenGLM.popMatrix();
		}
		GL11U.glColor(color);
		HUD.drawRect(1F/368, 1F/399, 0, 0, 170, 0, 170, 399);
		OpenGLM.color(1, 1, 1, 1);
		HUD.drawRect(1F/368, 1F/399, 0, 0, 0, 0, 170, 399);
		OpenGLM.popMatrix();
	}
	public static void update(){
		if(timeout>0)timeout--;
		else return;
		if(RandUtil.RB(0.5))drops.add(new Drop(new Vector2f(RandUtil.RF(130)+14, 60),RandUtil.RF(0.25)+0.75F));
		for(int i=0;i<drops.size();i++){
			Drop drop=drops.get(i);
			if(!drop.dead){
				drop.speed++;
				drop.prevPos=drop.pos;
				
				drop.pos.y+=drop.speed;
				drop.alpha=(260F-drop.pos.y)/260*2;
				if(drop.alpha<0)drop.dead=true;
			}else drops.remove(i);
		}
	}
}
