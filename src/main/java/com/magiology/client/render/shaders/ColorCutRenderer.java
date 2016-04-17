package com.magiology.client.render.shaders;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.magiology.client.render.shaders.core.ShaderAspectRenderer;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.client.shader.Framebuffer;

public class ColorCutRenderer extends ShaderAspectRenderer{
	public static ColorCutRenderer instance;
	private IntBuffer pixels=BufferUtils.createIntBuffer(1);
	
	public float r=1,g=1,b=1,rPrev=1,gPrev=1,bPrev=1,tolerance=0.3F,intensity=0.9F,prevTolerance=0.3F,prevIntensity=0.9F;
	
	public ColorCutRenderer(){
		super("colorCutM",1,"color","tolerance","intensity");
		instance=this;
	}
	
	@Override
	public boolean getConditionForActivation(){
		return false;
	}
	
	@Override
	public void redner(){
		ShaderAspectRenderer.setUniform(uniforms.get(0), PartialTicksUtil.calculatePos(rPrev, r),PartialTicksUtil.calculatePos(gPrev, g),PartialTicksUtil.calculatePos(bPrev, b));
		ShaderAspectRenderer.setUniform(uniforms.get(1), PartialTicksUtil.calculatePos(prevTolerance, tolerance));
		ShaderAspectRenderer.setUniform(uniforms.get(2), PartialTicksUtil.calculatePos(prevIntensity, intensity));
	}
	
	@Override
	public void update(){
		prevTolerance=tolerance;
		prevIntensity=intensity;
		rPrev=r;
		gPrev=g;
		bPrev=b;
		
		Framebuffer fb=UtilM.getMC().getFramebuffer();
		pixels.clear();
		GL11.glReadPixels(fb.framebufferWidth/2, fb.framebufferHeight/2, 1, 1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
		
		Color center=new Color(pixels.get());
		r=1-(center.getRed()/256F);
		g=1-(center.getGreen()/256F);
		b=1-(center.getBlue()/256F);
		
		tolerance=0.3F;
		intensity=0.15F;
	}
}
