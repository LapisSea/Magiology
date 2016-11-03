package com.magiology.client.shaders.programs;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

public class MatterJumperShader extends ShaderProgram{
	
	
	protected int mulColor;
	
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
		upload(mulColor, Display.getWidth(),Display.getHeight());
		upload(getUniformLocation("texUnit0"), 0);
		upload(getUniformLocation("texUnit1"), 1);
		upload(getUniformLocation("prevFrameColor"), new ColorF(0.5,0.7,1,1));
		upload(getUniformLocation("tim"), (float)((System.currentTimeMillis()/200D)%(Math.PI*2*1000)));
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, UtilC.getMC().getFramebuffer().framebufferTexture);
	}
	
	public void activate(){
		bind();
		initUniforms();
	}
	
	@Override
	protected void initUniformLocations(){
		mulColor=getUniformLocation("screenSize");
	}

	
}
