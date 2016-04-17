package com.magiology.util.renderers.glstates;

import com.magiology.util.utilobjects.codeinsert.BooleanReturn;

public class GlStateCell{
	
	private GlState set,reset;
	private boolean suchChangeMuchMeme;
	public BooleanReturn willRender=()->{return true;};
	
	public GlStateCell(GlState set, GlState reset){
		this.set=set;
		this.reset=reset;
		suchChangeMuchMeme=set==reset||reset==null;
	}
	
	public void reset(){
		if(!suchChangeMuchMeme)reset.configureOpenGl();
	}
	public void set(){
		set.configureOpenGl();
	}
	
}
