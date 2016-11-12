package com.magiology.client.shaders.programs;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.world.World;

public class MatterJumperShader extends ShaderProgram{
	
	protected int			screenSize,tim,prevFrameColor,wobleRadius,noiseRadius,texUnit0,backFrame;
	protected float			time,wobleRad,noiseRad;
	protected ColorF		color;
	protected Framebuffer	fBuf;
	
	@Override
	protected CharSequence getVertexShaderSrc(){
		return getShaderFile("MatterJumper.vs");
	}
	
	@Override
	protected CharSequence getFragmentShaderSrc(){
		return getShaderFile("MatterJumper.fs");
	}
	
	@Override
	protected void bindAtributes(){
		
	}
	
	@Override
	public void initUniforms(){
		upload(screenSize, fBuf.framebufferTextureWidth, fBuf.framebufferTextureHeight);
		upload(prevFrameColor, color);
		upload(tim, time);
		upload(texUnit0, 0);
		upload(backFrame, 1);
		upload(wobleRadius, wobleRad);
		upload(noiseRadius, noiseRad);
	}

	public void activate(TemporaryFrame fBuf, ColorF color, double time, float wobleRad, float noiseRad){
		activate(fBuf.frameBuffer, color, time, wobleRad, noiseRad);
	}
	public void activate(Framebuffer fBuf, ColorF color, double time, float wobleRad, float noiseRad){
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fBuf.framebufferTexture);
		this.fBuf=fBuf;
		this.time=(float)(time%(Math.PI*2*1000));
		this.color=color;
		this.wobleRad=wobleRad;
		this.noiseRad=noiseRad;
		bind();
		initUniforms();
	}
	
	@Override
	protected void initUniformLocations(){
		prevFrameColor=getUniformLocation("prevFrameColor");
		screenSize=getUniformLocation("screenSize");
		tim=getUniformLocation("tim");
		wobleRadius=getUniformLocation("wobleRad");
		noiseRadius=getUniformLocation("noiseRad");
		texUnit0=getUniformLocation("texUnit0");
		backFrame=getUniformLocation("backFrame");
	}
	
}
