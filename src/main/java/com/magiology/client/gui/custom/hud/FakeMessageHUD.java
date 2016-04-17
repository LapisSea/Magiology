package com.magiology.client.gui.custom.hud;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.updateable.Updater;
import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

public class FakeMessageHUD extends HUD{
	
	public static class Message implements Updateable{
		private int age;
		private ColorF color,prevColor;
		private boolean isDead=false;
		protected PhysicsFloat pos;
		private String text,id;
		public Message(ColorF color, String text, String id){
			this.color=this.prevColor=color;
			this.text=text;
			this.id=id;
			
		}
		public boolean isDead(){
			return isDead;
		}
		public boolean isSameId(Message msg){
			if(id==null)return false;
			return this.id.equals(msg.id);
		}
		public void redner(){
			ColorF color=UtilM.calculateRenderColor(prevColor, this.color);
			int sw=Font.FR().getStringWidth(text);
			float animation=(color.a-1);
			OpenGLM.pushMatrix();
			if(animation<0){
				GL11U.glRotate(0, animation*40,0, -sw/2F, Font.FR().FONT_HEIGHT/2F, 0);
				GL11U.glRotate(animation*90, 0,0, -sw/2F, Font.FR().FONT_HEIGHT/2F, 0);
			}
			OpenGLM.color(0, 0, 0, 0.3F*color.a);
			GL11U.texture(false);
			drawRect(1, 1, -sw-1, -1, 0, 0, sw+2, Font.FR().FONT_HEIGHT+1);
			GL11U.texture(true);
			Font.FR().drawStringWithShadow(text, -sw, 0, color.toCode());
			OpenGLM.popMatrix();
		}
		@Override
		public void update(){
			prevColor=color;
			if(age>80)color.a-=0.1;
			color.a=MathUtil.snap(color.a, 0, 1);
			if(color.a<=0)isDead=true;
			age++;
		}
	}

	private static FakeMessageHUD instance;
	private static List<Message> messages=new ArrayList<Message>();
	private static int timeout;
	public static void addMessage(Message msg){
		int mach=-1;
		for(int i=0;i<messages.size();i++){
			if(messages.get(i).isSameId(msg)){
				mach=i;
				break;
			}
		}
		if(mach!=-1){
			messages.get(mach).age=80;
		}
		messages.add(0, msg);
	}
	
	public static FakeMessageHUD get(){
		if(instance==null)instance=new FakeMessageHUD();
		return instance;
	}
	private FakeMessageHUD(){}
	
	@Override
	public void render(int xScreen, int yScreen, float partialTicks){
		timeout=10;
		OpenGLM.pushMatrix();
		OpenGLM.translate(xScreen-1, yScreen, 0);
		GL11U.setUpOpaqueRendering(1);
		for(Message msg:messages)if(!msg.isDead()){
			OpenGLM.translate(0, -Font.FR().FONT_HEIGHT-1, 0);
			msg.redner();
		}
		GL11U.endOpaqueRendering();
		OpenGLM.popMatrix();
	}
	@Override
	public void update(){
		if(timeout>0)timeout--;
		else return;
		for(int i=0;i<messages.size();i++){
			if(messages.get(i).isDead())messages.remove(i);
		}
		Updater.update(messages);
	}
}
