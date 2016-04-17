package com.magiology.util.utilobjects;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.renderers.VertexRenderer.ShadedQuad;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilobjects.vectors.Vec2FM;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class IndexedModel{
	
	private final List<Integer> indices=new ArrayList<>();
	private final List<Vec2FM> uvs=new ArrayList<>();
	private final List<Vec3M> vertices=new ArrayList<>();
	
	public IndexedModel(){
		
	}
	public IndexedModel(List<ShadedQuad> quads){
		for(ShadedQuad shadedQuad:quads)addQuad(shadedQuad);
	}
	public IndexedModel(List<Vec3M> vertices, List<Integer> indices){
		this(vertices, indices, new ArrayList<>());
	}
	public IndexedModel(List<Vec3M> vertices, List<Integer> indices, List<Vec2FM> uvs){
		this.vertices.addAll(vertices);
		this.indices.addAll(indices);
		this.uvs.addAll(uvs);
	}
	
	
	public void addCube(CubeModel model){
		float index=vertices.size();
//		for(Integer integer:indices)index=Math.max(index, integer)+1;
		
		addVertices(model.points);
		
		if(model.willSideRender[0])addIndices(index,0,1,2,3);
		if(model.willSideRender[1])addIndices(index,7,6,5,4);
		if(model.willSideRender[2])addIndices(index,2,1,5,6);
		if(model.willSideRender[3])addIndices(index,7,4,0,3);
		if(model.willSideRender[4])addIndices(index,4,5,1,0);
		if(model.willSideRender[5])addIndices(index,3,2,6,7);
		
	}
	public void addIndex(int index){
		indices.add(index);
	}
	public void addIndices(float offset,int...indices){
		int offse1=(int)offset;
		for(int index:indices)this.indices.add(index+offse1);
	}
	public void addIndices(int...indices){
		for(int index:indices)this.indices.add(index);
	}
	public void addQuad(ShadedQuad quad){
		float index=vertices.size();
//		for(Integer integer:indices)index=Math.max(index, integer)+1;
		
		addVertices(
			Vec3M.conv(quad.pos4[0].vector3D),
			Vec3M.conv(quad.pos4[1].vector3D),
			Vec3M.conv(quad.pos4[2].vector3D),
			Vec3M.conv(quad.pos4[3].vector3D)
		);
		addIndices(index,0,1,2,3);
	}
	public void addUV(Vec2FM uv){
		uvs.add(uv);
	}
	public void addUVs(Vec2FM...uvs){
		for(Vec2FM uv:uvs)addUV(uv);
	}
	public void addVertices(Vec3M...vec){
		for(Vec3M vec3m:vec)vertices.add(vec3m);
	}
	public void addVetex(Vec3M vec){
		vertices.add(vec);
	}
	public List<Integer> getIndices(){
		return indices;
	}
	public List<Vec2FM> getUVs(){
		return uvs;
	}
	public List<Vec3M> getVertices(){
		return vertices;
	}
}
