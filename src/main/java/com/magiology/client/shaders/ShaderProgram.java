package com.magiology.client.shaders;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.nio.file.Files;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import com.google.common.base.Joiner;
import com.magiology.core.ConfigM;
import com.magiology.core.Magiology;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.FileUtil;
import com.magiology.util.statics.LogUtil;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderManager;

public abstract class ShaderProgram{
	
	private int programID=-1, vertexShaderID, fragmentShaderID,bindUnbindSafety=0;
	private boolean vertexEnabled,fragmentEnabled;
	
	public static FloatBuffer matrixBuffer=BufferUtils.createFloatBuffer(16);
	
	protected abstract CharSequence getVertexShaderSrc();
	protected abstract CharSequence getFragmentShaderSrc();
	protected abstract void bindAtributes();
	protected abstract void initUniformLocations();
	public abstract void initUniforms();
	
	public void compile(){
		if(!ConfigM.shadersEnabled())return;
		deleteShader();
		CharSequence vertex=getVertexShaderSrc(),fragment=getFragmentShaderSrc();
		fragmentEnabled=fragment!=null&&fragment.length()>0;
		vertexEnabled=vertex!=null&&vertex.length()>0;
		
		if(vertexEnabled){
			try{
				vertexShaderID=loadShader(injectModules(vertex.toString()), OpenGlHelper.GL_VERTEX_SHADER);
			}catch(Exception e){
				e.printStackTrace();
				vertexEnabled=false;
				LogUtil.println("Failed to compile vertex shader!");
			}
		}
		if(fragmentEnabled){
			try{
				fragmentShaderID=loadShader(injectModules(fragment.toString()), OpenGlHelper.GL_FRAGMENT_SHADER);
			}catch(Exception e){
				fragmentEnabled=false;
				LogUtil.println("Failed to compile fragment shader!");
			}
		}
		vertexEnabled=vertexEnabled&&vertexShaderID!=-1;
		fragmentEnabled=fragmentEnabled&&fragmentShaderID!=-1;
		
		programID=OpenGlHelper.glCreateProgram();
		if(vertexEnabled)OpenGlHelper.glAttachShader(programID, vertexShaderID);
		if(fragmentEnabled)OpenGlHelper.glAttachShader(programID, fragmentShaderID);
		bindAtributes();
		OpenGlHelper.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		initUniformLocations();
	}
	private CharSequence injectModules(String shader){
		int modulePos,off="#module".length();
		
		while((modulePos=shader.indexOf("#module"))!=-1){
			
			int startMarker=modulePos+off;
			char startChar=' ',endChar=' ';
			while(Character.isWhitespace(startChar=shader.charAt(startMarker))){
				if(startMarker==shader.length()){
					LogUtil.println("No module module name opening: line=",shader.substring(0, startMarker).split("\n").length);
					throw new RuntimeException();
				}
				startMarker++;
			}
			
			if(startChar!='<'){
				LogUtil.println("Invalid module: line=",shader.substring(0, startMarker).split("\n").length);
				throw new RuntimeException();
			}
			
			int endMarker=startMarker+1;
			
			while((endChar=shader.charAt(endMarker))!='>'){
				if(!Character.isJavaIdentifierPart(endChar)&&!Character.isWhitespace(endChar)){
					LogUtil.println("Invalid module name: line=",shader.substring(0, startMarker).split("\n").length);
					throw new RuntimeException();
				}
				if(endMarker==shader.length()){
					LogUtil.println("No module module name closing: line=",shader.substring(0, startMarker).split("\n").length);
					throw new RuntimeException();
				}
				endMarker++;
			}
			
			String moduleName=shader.substring(startMarker+1, endMarker).trim();
			
			boolean comressTo1Line=moduleName.endsWith("=1line");
			if(comressTo1Line)moduleName=moduleName.substring(0, moduleName.length()-"=1line".length());
			File file=new File(Magiology.EXTRA_FILES.getRoot()+"shaders/modules/"+moduleName+".sm");
			String moduleSrc;
			try{
				moduleSrc=new String(Files.readAllBytes(file.toPath()));
			}catch(Exception e){
				LogUtil.println("Can't find shader module src:",file);
				throw new RuntimeException();
			}
			if(comressTo1Line){
				String[] lines=moduleSrc.split("\n");
				for(int i=0;i<lines.length;i++)lines[i]=lines[i].trim();
				moduleSrc=Joiner.on(' ').join(lines);
			}
			String shaderStart=shader.substring(0, modulePos),shaderEnd=shader.substring(endMarker+1, shader.length());
			
			shader=shaderStart+moduleSrc+shaderEnd;
		}
		
		return shader;
	}
	private static int loadShader(CharSequence src, int type){
		int shaderID=GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, src);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 5000));
			GL20.glDeleteShader(shaderID);
			return -1;
		}
		return shaderID;
	}
	public int getUniformLocation(String uniformName){
		return OpenGlHelper.glGetUniformLocation(programID, uniformName);
	}
	public void bindAttribute(int attributeID,String name){
		GL20.glBindAttribLocation(programID, attributeID, name);
	}

	public void upload(int location, int value){
		OpenGlHelper.glUniform1i(location, value);
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
	public void upload(int location, ColorM color){
		upload(location, color.r(), color.g(), color.b(), color.a());
	}
	public void upload(int location, Vec3M vec){
		upload(location, vec.getX(),vec.getY(),vec.getZ());
	}
	
	public void upload(int location, Matrix4f value){
		value.store(matrixBuffer);
		matrixBuffer.flip();
		OpenGlHelper.glUniformMatrix4(location, false, matrixBuffer);
	}

	public void bind(){
		if(bindUnbindSafety>100){
			deactivate();
			bindUnbindSafety=0;
			throw new StackOverflowError("Shader not deactivated at some point");
		}
		OpenGlHelper.glUseProgram(programID);
		bindUnbindSafety++;
	}
	public void deactivate(){
		OpenGlHelper.glUseProgram(0);
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
			OpenGlHelper.glDeleteShader(vertexShaderID);
			vertexShaderID=-1;
		}
		if(fragmentEnabled){
			GL20.glDetachShader(programID, fragmentShaderID);
			OpenGlHelper.glDeleteShader(fragmentShaderID);
			fragmentShaderID=-1;
		}
		OpenGlHelper.glDeleteProgram(programID);
		programID=-1;
	}
	
	protected CharSequence getShaderFile(String path){
		return FileUtil.getFileTxt(new File(Magiology.EXTRA_FILES.getRoot()+"shaders/"+path));
	}
}
