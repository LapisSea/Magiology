package com.magiology.client.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;

public abstract class ShaderProgram{
	
	private int programID=-1, vertexShaderID, fragmentShaderID,bindUnbindSafety=0;
	private boolean vertexEnabled,fragmentEnabled;
	
	public static FloatBuffer matrixBuffer=BufferUtils.createFloatBuffer(16);
	
	protected abstract CharSequence getVertexShaderSrc();
	protected abstract CharSequence getFragmentShaderSrc();
	protected abstract void bindAtributes();
	protected abstract void initUniformLocations();
	public abstract void initUniforms();
	
	protected void compile(){
		deleteShader();
		CharSequence vertex=getVertexShaderSrc(),fragment=getFragmentShaderSrc();
		fragmentEnabled=fragment!=null&&fragment.length()>0;
		vertexEnabled=vertex!=null&&vertex.length()>0;
		
		if(vertexEnabled)vertexShaderID=loadShader(vertex, GL20.GL_VERTEX_SHADER);
		if(fragmentEnabled)fragmentShaderID=loadShader(fragment, GL20.GL_FRAGMENT_SHADER);
		vertexEnabled=vertexEnabled&&vertexShaderID!=-1;
		fragmentEnabled=fragmentEnabled&&fragmentShaderID!=-1;
		
		programID=GL20.glCreateProgram();
		if(vertexEnabled)GL20.glAttachShader(programID, vertexShaderID);
		if(fragmentEnabled)GL20.glAttachShader(programID, fragmentShaderID);
		bindAtributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		initUniformLocations();
	}
	private static int loadShader(CharSequence src, int type){
		int shaderID=GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, src);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			GL20.glDeleteShader(shaderID);
			return -1;
		}
		return shaderID;
	}
	public int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	public void bindAtribute(int attributeID,String name){
		GL20.glBindAttribLocation(programID, attributeID, name);
	}
	
	public void upload(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	public void upload(int location, boolean value){
		GL20.glUniform1f(location, value?1:0);
	}

	public void upload(int location, float f1, float f2){
		GL20.glUniform2f(location, f1, f2);
	}
	public void upload(int location, float f1, float f2, float f3){
		GL20.glUniform3f(location, f1, f2, f3);
	}
	public void upload(int location, float f1, float f2, float f3, float f4){
		GL20.glUniform4f(location, f1, f2, f3, f4);
	}
	public void upload(int location, ColorF color){
		upload(location, color.r, color.g, color.b, color.a);
	}
	public void upload(int location, Vec3M vec){
		upload(location, (float)vec.x,(float)vec.y,(float)vec.z);
	}
	
	protected void loadMatrix4F(int location, Matrix4f value){
		value.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}

	public void bind(){
		if(bindUnbindSafety>100){
			deactivate();
			bindUnbindSafety=0;
			throw new StackOverflowError("Shader not deactivated at some point");
		}
		GL20.glUseProgram(programID);
		bindUnbindSafety++;
	}
	public void deactivate(){
		GL20.glUseProgram(0);
		bindUnbindSafety--;
	}
	@Override
	protected void finalize(){
		deleteShader();
	}
	private void deleteShader(){
		if(programID==-1)return;
		if(vertexEnabled){
			GL20.glDetachShader(programID, vertexShaderID);
			GL20.glDeleteShader(vertexShaderID);
			vertexShaderID=-1;
		}
		if(fragmentEnabled){
			GL20.glDetachShader(programID, fragmentShaderID);
			GL20.glDeleteShader(fragmentShaderID);
			fragmentShaderID=-1;
		}
		GL20.glDeleteProgram(programID);
		programID=-1;
	}
}
