package com.magiology.client.shaders.programs;

import com.magiology.client.rendering.ShaderMultiTransformModel;
import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.statics.OpenGLM;
import org.lwjgl.util.vector.Matrix4f;

public class MultiTransformShader extends ShaderProgram{
	
	protected int[] matrixIds=new int[64], matrices=new int[16];
	protected ShaderMultiTransformModel model;
	
	@Override
	protected CharSequence getVertexShaderSrc(){
		return getShaderFile("MultiTransform.vs");
	}
	
	@Override
	protected CharSequence getFragmentShaderSrc(){
		return getShaderFile("MultiTransform.fs");
	}
	
	@Override
	protected void bindAtributes(){
		
	}
	
	@Override
	public void initUniforms(){
		int[] matIds=model.getMatrixIds();
		Matrix4f[] mats=model.getMatrices();
		//		LogUtil.println(getUniformLocation("matrices[1]"));
		for(int i=0, j=Math.min(matIds.length, matrixIds.length); i<j; i++){
			upload(matrixIds[i], matIds[i]);
		}
		
		for(int i=0, j=Math.min(mats.length, matrices.length); i<j; i++){
			upload(matrices[i], mats[i]);
		}
	}
	
	public void drawModel(ShaderMultiTransformModel model){
		this.model=model;
		bind();
		initUniforms();
		
		OpenGLM.callList(model.getModelListId());
		
		deactivate();
	}
	
	@Override
	protected void initUniformLocations(){
		for(int i=0; i<matrixIds.length; i++){
			matrixIds[i]=getUniformLocation("matrixIds["+i+"]");
		}
		for(int i=0; i<matrices.length; i++){
			matrices[i]=getUniformLocation("matrices["+i+"]");
		}
		
	}
	
}
