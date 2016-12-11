package com.magiology.handlers.frame_buff;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class TemporaryFrameBufferHandler{
	
	public static final TemporaryFrameBufferHandler instance=new TemporaryFrameBufferHandler();
	private TemporaryFrameBufferHandler(){}

	private final List<TemporaryFrame> renderQueue=new ArrayList<>();
	
	private int bufferSizeRedFlag=12,periodicChecksSize=30;
	
	final List<TemporaryFrame> allBuffers=new ArrayList<TemporaryFrame>(){
		@Override
		public boolean add(TemporaryFrame obj){
			if(size()>=bufferSizeRedFlag||true)bufferGC();
			return super.add(obj);
		}
	},trashBank=new ArrayList();
	
	public void bufferGC(){
		long time=System.currentTimeMillis();
		Queue<TemporaryFrame> unused=new ArrayDeque<>();
		allBuffers.forEach(f->{
			if(f.lastTimeUsed+14000<time){
				unused.add(f);
				f.dirty=true;
				LogUtil.println("Deleted InWorldFrame id=",f.frameBuffer.framebufferObject);
				f.frameBuffer.deleteFramebuffer();
			}
		});
		int siz=allBuffers.size();
		allBuffers.removeAll(unused);
		if(allBuffers.size()!=siz){
			LogUtil.println("Old InWorldFrame list size:",siz);
			LogUtil.println("New InWorldFrame list size:",allBuffers.size());
		}
	}
	
	void requestRender(TemporaryFrame frame){
		renderQueue.add(frame);
	}
	
	
	public void renderFrames(){
		if(renderQueue.isEmpty())return;
		
		OpenGLM.pushMatrix();

		OpenGLM.disableLighting();
		try{
			synchronized(renderQueue){
				renderQueue.forEach(TemporaryFrame::render);
				renderQueue.clear();
			}
		}catch(Exception e){}
		
		UtilC.getMC().getFramebuffer().bindFramebuffer(true);
		ScaledResolution scaledresolution=new ScaledResolution(UtilC.getMC());
		int i=scaledresolution.getScaleFactor();
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		OpenGLM.popMatrix();
	}
	
	public void resurrectList(TemporaryFrame frame){
		if(frame.gcPossible){
			frame.gcPossible=false;
		}
		if(allBuffers.stream().noneMatch(f->frame==f)){
			allBuffers.add(frame);
			allBuffers.stream().forEach(TemporaryFrame::requestRender);
		}
	}
}
