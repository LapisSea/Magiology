package com.magiology.util.renderers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.utilobjects.IndexedModel;
import com.magiology.util.utilobjects.vectors.Vec2FM;
import com.magiology.util.utilobjects.vectors.Vec2i;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class MultiTransfromModel{
	
	private static void draw(MultiTransfromModel model){
		if(model.cell==null)TessUtil.getVB().draw();
		else{
			model.cell.set();
			TessUtil.getVB().draw();
			model.cell.reset();
		}
	}
	public GlStateCell cell;
	private List<MultiTransfromModel> childern=new ArrayList<>();
	private List<int[]> matrices=new ArrayList<>();
	private IndexedModel model;
	public boolean noDrawMode;
	public Vec2i parentVertices;
	
	public List<Vec3M> rednerLeftover;
	
	public MultiTransfromModel(IndexedModel model){
		this.model=model;
	}
	public void addChild(MultiTransfromModel model){
		childern.add(model);
	}
	public void addMatrix(int[] effectedVertices){
		matrices.add(effectedVertices);
	}
	public void draw(final List<Matrix4f> transformations){
		if(cell!=null&&!cell.willRender.get())return;
		List<Vec3M> data=getTransfromed(transformations);
		rednerLeftover=data;
		tesselate(data);
		draw(this);
		
		childern.forEach(child->{
			if(child.cell==null||child.cell.willRender.get()){
				List<Vec3M> data1;
				if(child.parentVertices!=null)data1=data.subList(child.parentVertices.x,child.parentVertices.y);
				else data1=data;
				child.tesselate(data1).draw(child);
			}
		});
	}
	
	public IndexedModel getChild(){
		return model;
	}
	public List<Integer> getIndices(){
		return model.getIndices();
	}
	public List<Vec3M> getTransfromed(final List<Matrix4f> transformations){
		if(matrices.size()!=transformations.size())throw new IllegalStateException("incorrect matrix list sizes! "+matrices.size()+" vs "+transformations.size());
		
		List<Vec3M> vertices=new ArrayList<>();
		model.getVertices().forEach(vertex->vertices.add(vertex.copy()));
		
		for(int i=matrices.size()-1;i>-1;i--){
			int[] matrixIndices=matrices.get(i);
			if(matrixIndices.length==0){
				Matrix4f vert=transformations.get(i);
				vertices.forEach(ver->ver.transformSelf(vert));
			}
			else for(int j=0;j<matrixIndices.length;j++)vertices.get(matrixIndices[j]).transformSelf(transformations.get(i));
		}
		
		return vertices;
	}

	public void removeMatrix(int id){
		matrices.remove(id);
	}

	private MultiTransfromModel tesselate(final List<Vec3M> vertices){
		List<Integer> indices=model.getIndices();
		Iterator<Vec2FM> uvs=model.getUVs().iterator();
		
		VertexRenderer buff=TessUtil.getVB();
		buff.cleanUp();
		indices.forEach(index->{
			Vec2FM uv=uvs.hasNext()?uvs.next():new Vec2FM();
			buff.addVertexWithUV(vertices.get(index), uv.x, uv.y);
		});
		return this;
	}
}
