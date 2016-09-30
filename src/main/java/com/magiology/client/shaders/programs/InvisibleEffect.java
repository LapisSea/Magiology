package com.magiology.client.shaders.programs;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.effects.SoftEffectsShader;
import com.magiology.client.shaders.upload.UniformUploaderF1;
import com.magiology.client.shaders.upload.UniformUploaderF2;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.statics.FileUtil;

import org.lwjgl.opengl.Display;

public class InvisibleEffect extends ShaderProgram{
	
	public static InvisibleEffect instance;
	public static float screenSizeF;

	private UniformUploaderF2 screenDim;
	private UniformUploaderF1 screenSize;
	
//	private UniformUploaderCustom<T>
	
	private Queue<PositionAwareEffect> effects=new ArrayDeque<>();
	
	public InvisibleEffect(){
		instance=this;
		effects.add(SoftEffectsShader.instance);
	}
	
	@Override
	protected CharSequence getVertexShaderSrc(){
//		return FileUtil.getFileTxt(new File("vertex.vs"));
		return null;
	}
	    
	
	@Override
	protected CharSequence getFragmentShaderSrc(){
		return FileUtil.getFileTxt(new File("fragmentShader.fs"));
	}
	
	@Override
	protected void bindAtributes(){
		
	}
	
	@Override
	public void initUniforms(){
		screenDim.upload(Display.getWidth(),Display.getHeight());
		screenSizeF=new Vec2FM(Display.getWidth(),Display.getHeight()).length()/2;
		screenSize.upload(screenSizeF);
		effects.forEach(fx->fx.upload());
		
	}
	
	public void addEffect(PositionAwareEffect effect){
		effects.add(effect);
	}
	
	public void activate(){
		bind();
		initUniforms();
	}
	
	@Override
	protected void initUniformLocations(){
		screenDim=new UniformUploaderF2(this,"screenDim");
		screenSize=new UniformUploaderF1(this, "screenSize");
		effects.forEach(fx->fx.initUniformLocations());
		
	}

	public boolean shouldRender(){
		return effects.stream().anyMatch(effect->effect.shouldRender());
	}
}
