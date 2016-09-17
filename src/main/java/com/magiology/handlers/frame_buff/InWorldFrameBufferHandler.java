package com.magiology.handlers.frame_buff;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class InWorldFrameBufferHandler{
	
	public static final InWorldFrameBufferHandler instance=new InWorldFrameBufferHandler();
	private InWorldFrameBufferHandler(){}

	private final Queue<InWorldFrame> renderQueue=new ArrayDeque<>();
	
	private int bufferSizeRedFlag=12,periodicChecksSize=30;
	
	final List<InWorldFrame> allBuffers=new ArrayList<InWorldFrame>(){
		@Override
		public boolean add(InWorldFrame obj){
			if(size()>=bufferSizeRedFlag)bufferGC();
			return super.add(obj);
		}
	},trashBank=new ArrayList();
	
	public void bufferGC(){
		long time=System.currentTimeMillis();
		Queue<InWorldFrame> unused=new ArrayDeque<>();
		allBuffers.forEach(f->{
			if(f.lastTimeUsed+2000<time){
				unused.add(f);
				f.dirty=true;
				PrintUtil.println("Deleted InWorldFrame id=",f.frameBuffer.framebufferObject);
				f.frameBuffer.deleteFramebuffer();
			}
		});
		PrintUtil.println("Old InWorldFrame list size:",allBuffers.size());
		allBuffers.removeAll(unused);
		PrintUtil.println("New InWorldFrame list size:",allBuffers.size());
	}
	
	void requestRender(InWorldFrame frame){
		renderQueue.add(frame);
		if(frame.gcPossible){
			frame.gcPossible=false;
			allBuffers.add(frame);
		}
	}
	
	
	public void renderFrames(){
		if(renderQueue.isEmpty()){
			
			return;
		}
		OpenGLM.pushMatrix();
		
		renderQueue.forEach(InWorldFrame::render);
		renderQueue.clear();
		
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
}
