package com.magiology.core.registry.init;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.client.shaders.programs.InvisibleShader;
import com.magiology.client.shaders.programs.MatterJumperShader;
import com.magiology.client.shaders.programs.MultiTransformShader;
import com.magiology.client.shaders.programs.SepiaShader;
import com.magiology.core.ConfigM;
import com.magiology.core.registry.imp.AutoReferencedRegistry;
import com.magiology.util.statics.UtilM;

//<GEN:	IMPORTS START>
//<GEN:	IMPORTS END>

public class ShadersM extends AutoReferencedRegistry<ShaderProgram>{
	
	private static final ShadersM instance=new ShadersM();
	
	public static ShadersM get(){return instance;}
	
	//<GEN:	REFERENCE START>
	public static InvisibleShader      INVISIBLE;
	public static MatterJumperShader   MATTER_JUMPER;
	public static MultiTransformShader MULTI_TRANSFORM;
	public static SepiaShader          SEPIA;
	//<GEN:	REFERENCE END>
	
	private ShadersM(){
		super(ShaderProgram.class);
	}
	
	@Override
	public void registerObj(ShaderProgram shader){
		shader.compile();
	}
	
	@Override
	protected void init(){
		//<GEN:	INIT START>
		add(INVISIBLE      =new InvisibleShader());
		add(MATTER_JUMPER  =new MatterJumperShader());
		add(MULTI_TRANSFORM=new MultiTransformShader());
		add(SEPIA          =new SepiaShader());
		//<GEN:	INIT END>
	}
	
	@Override
	protected String classNameToCutName(String className){
		if(className.endsWith("Shader")) className=className.substring(0, className.length()-"Shader".length());
		return UtilM.standardizeName(className);
	}
	
	public static void reload(){
		if(!ConfigM.shadersEnabled()) return;
		for(ShaderProgram shader : get().getDatabase()) shader.compile();
	}
}
