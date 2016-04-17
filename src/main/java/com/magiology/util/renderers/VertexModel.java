package com.magiology.util.renderers;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.renderers.glstates.GlStateCell;

public class VertexModel extends VertexRenderer{
	protected static VertexModel create(){
		return new VertexModel();
	}
	
	public GlStateCell glStateCell;
	
	private boolean isInit=false;
	@Override
	public void cleanUp(){}
	@Override
	public void draw(){
		if(glStateCell!=null){
			if(glStateCell.willRender.get()){
				glStateCell.set();
				super.draw();
				glStateCell.reset();
			}
		}else super.draw();
	}
	
	protected void init(List<ShadedQuad> shadedTriangles){
		if(isInit)return;isInit=true;
		this.shadedTriangles=new ArrayList<ShadedQuad>();
		this.shadedTriangles.addAll(shadedTriangles);
	}
	
}