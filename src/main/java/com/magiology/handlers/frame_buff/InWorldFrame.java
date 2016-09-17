package com.magiology.handlers.frame_buff;

import com.magiology.util.interf.ObjectSimpleCallback;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.PrintUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InWorldFrame{
	
	Framebuffer frameBuffer=new Framebuffer(1, 1, false);
	boolean dirty=true,gcPossible=false;
	long lastTimeUsed=System.currentTimeMillis();
	
	private int width,height;
	private boolean useDepth,willBeRendered=false;
	private ObjectSimpleCallback<InWorldFrame> rednerHook;
	
	public InWorldFrame(int width, int height, boolean useDepth){
		this.width=width;
		this.height=height;
		this.useDepth=useDepth;
		InWorldFrameBufferHandler.instance.allBuffers.add(this);
	}
	public InWorldFrame setSize(int width, int height){
		if(!dirty)dirty=this.width!=width||this.height!=height;
		this.width=width;
		this.height=height;
		lastTimeUsed=System.currentTimeMillis();
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
		if(willBeRendered)return;
		willBeRendered=true;
		InWorldFrameBufferHandler.instance.requestRender(this);
	}
	public void bindTexture(){
		frameBuffer.bindFramebufferTexture();
		lastTimeUsed=System.currentTimeMillis();
	}
	
	
	public void render(){
		OpenGLM.pushMatrix();
		if(dirty){
			dirty=false;
			frameBuffer.deleteFramebuffer();
			frameBuffer.useDepth=useDepth;
			frameBuffer.setFramebufferColor(0, 0, 0, 0);
			frameBuffer.createBindFramebuffer(width,height);
			onBufferInit();
			PrintUtil.println("Created InWorldFrame id=",frameBuffer.framebufferObject);
			requestRender();
		}
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
	protected void onBufferInit(){}
	public void setRednerHook(ObjectSimpleCallback<InWorldFrame> rednerHook){
		this.rednerHook = rednerHook;
		lastTimeUsed=System.currentTimeMillis();
	}
}
