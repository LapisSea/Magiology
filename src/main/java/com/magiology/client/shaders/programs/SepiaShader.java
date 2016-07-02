package com.magiology.client.shaders.programs;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.util.objs.ColorF;

public class SepiaShader extends ShaderProgram{
	
	public ColorF color=new ColorF();
	
	protected int mulColor;
	
	@Override
	protected CharSequence getVertexShaderSrc(){
		return null;
	}
	    
	
	@Override
	protected CharSequence getFragmentShaderSrc(){
		return String.join("\n",
			"",
			"uniform sampler2D texUnit0;"+
			"uniform vec4 mulColor;"+
			"void main (void){"+
			"    vec4 color=gl_Color;"+
			"    color *= texture2D(texUnit0, gl_TexCoord[0].xy);"+
			"    float brightness=(color.r+color.g+color.b)/3;"+
			"    gl_FragColor =mulColor*vec4(brightness,brightness,brightness,color.a);"+
			"}"
		);
//		return FileUtil.getFileTxt(new File("fragmentShader.fs"));
	}
	
	@Override
	protected void bindAtributes(){
		
	}
	
	@Override
	public void initUniforms(){
		upload(mulColor, color);
	}
	
	public void activate(ColorF color){
		this.color=color;
		bind();
		initUniforms();
	}
	
	@Override
	protected void initUniformLocations(){
		mulColor=getUniformLocation("mulColor");
	}

	
}
