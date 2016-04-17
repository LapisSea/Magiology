package com.magiology.util.renderers;

import com.magiology.util.renderers.glstates.GlStateCell;

import net.minecraft.client.renderer.vertex.VertexBuffer;

@Deprecated
public class VertexBufferM{
	
	public int drawMode=7;
	private VertexBuffer model;
	public GlStateCell stateCell;
	
	public VertexBufferM(VertexBuffer model){
		this.model=model;
	}
	public VertexBufferM(VertexBuffer model, GlStateCell stateCell){
		this(model);
		this.stateCell=stateCell;
	}
	public VertexBufferM(VertexBuffer model, GlStateCell stateCell, int drawMode){
		this(model, stateCell);
		this.drawMode=drawMode;
	}
	
	public void draw(){
		model.bindBuffer();
		if(stateCell==null)model.drawArrays(drawMode);
		else{
			stateCell.set();
			model.drawArrays(drawMode);
			stateCell.reset();
		}
		model.unbindBuffer();
	}
	@Override
	protected void finalize()throws Throwable{
		model.deleteGlBuffers();
		super.finalize();
	}
}
