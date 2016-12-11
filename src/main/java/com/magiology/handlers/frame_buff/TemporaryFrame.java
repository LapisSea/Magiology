package com.magiology.handlers.frame_buff;

import org.lwjgl.opengl.GL11;

import com.magiology.util.interf.ObjectSimpleCallback;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TemporaryFrame{
	
	public Framebuffer								frameBuffer	=new Framebuffer(1, 1, false);
	boolean											dirty		=true, gcPossible=false;
	long											lastTimeUsed=System.currentTimeMillis();
	private int										width, height;
	private boolean									useDepth, willBeRendered=false;
	private ObjectSimpleCallback<TemporaryFrame>	rednerHook	=f->{};
	
	public TemporaryFrame(int width, int height, boolean useDepth){
		this.width=width;
		this.height=height;
		this.useDepth=useDepth;
		TemporaryFrameBufferHandler.instance.resurrectList(this);
	}
	
	public TemporaryFrame setSize(int width, int height){
		TemporaryFrameBufferHandler.instance.resurrectList(this);
		if(!dirty&&(this.width!=width||this.height!=height)) dirty=true;
		this.width=width;
		this.height=height;
		lastTimeUsed=System.currentTimeMillis();
		return this;
	}
	
	public TemporaryFrame setUsingDepth(boolean using){
		if(!dirty&&useDepth!=using) dirty=true;
		useDepth=using;
		return this;
	}
	
	public TemporaryFrame setClearColor(ColorM color){
		frameBuffer.setFramebufferColor(color.r(), color.g(), color.b(), color.a());
		return this;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public boolean usingDepth(){
		return useDepth;
	}
	
	public void requestRender(){
		if(willBeRendered) return;
		TemporaryFrameBufferHandler.instance.resurrectList(this);
		willBeRendered=true;
		TemporaryFrameBufferHandler.instance.requestRender(this);
	}
	
	public void bindTexture(){
		TemporaryFrameBufferHandler.instance.resurrectList(this);
		frameBuffer.bindFramebufferTexture();
		lastTimeUsed=System.currentTimeMillis();
	}
	
	public void bindFrame(){
		TemporaryFrameBufferHandler.instance.resurrectList(this);
		frameBuffer.bindFramebuffer(true);
		lastTimeUsed=System.currentTimeMillis();
	}
	
	public void render(){
		TemporaryFrameBufferHandler.instance.resurrectList(this);
		if(dirty){
			dirty=false;
			frameBuffer.deleteFramebuffer();
			frameBuffer.useDepth=useDepth;
			frameBuffer.createBindFramebuffer(width, height);
			onBufferInit();
			LogUtil.println("Created InWorldFrame id="+frameBuffer.framebufferObject);
			forceRender();
			return;
		}
		OpenGLM.pushMatrix();
		frameBuffer.framebufferClear();
		frameBuffer.bindFramebuffer(true);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		OpenGLM.ortho(0.0D, frameBuffer.framebufferTextureWidth, frameBuffer.framebufferTextureHeight, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		rednerHook.process(this);
		OpenGLM.popMatrix();
		willBeRendered=false;
		lastTimeUsed=System.currentTimeMillis();
	}
	
	public void forceRender(){
		GL11.glPushAttrib(GL11.GL_MATRIX_MODE);
		GL11.glPushAttrib(GL11.GL_VIEWPORT);
		
		OpenGLM.pushMatrix();
		OpenGLM.disableLighting();
		render();
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
		
		GL11.glPopAttrib();
		GL11.glPopAttrib();
	}
	
	protected void onBufferInit(){}
	
	public void setRednerHook(ObjectSimpleCallback<TemporaryFrame> rednerHook){
		this.rednerHook=rednerHook;
		lastTimeUsed=System.currentTimeMillis();
	}
}
