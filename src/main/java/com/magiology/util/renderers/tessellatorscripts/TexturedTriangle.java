package com.magiology.util.renderers.tessellatorscripts;

import org.lwjgl.opengl.GL11;

import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.client.model.PositionTextureVertex;

public class TexturedTriangle{
	private boolean invertNormal;
	public int nVertices;
	public PositionTextureVertex[] vertexPositions;

	public TexturedTriangle(PositionTextureVertex[] PTV){
		if(PTV.length!=3)return;
		this.vertexPositions = PTV;
		this.nVertices = PTV.length;
	}
	public void draw(){
		Vec3M Vec3=Vec3M.conv(vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D));
		Vec3M vec31=Vec3M.conv(vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D));
		Vec3M vec32=vec31.crossProduct(Vec3).normalize();
		if(invertNormal)vec32=vec32.mul(-1);
		
		Renderer.POS_UV_NORMAL.begin(GL11.GL_TRIANGLES);
		
		for(int i=0;i<3;++i){
			PositionTextureVertex vp=vertexPositions[i];
			Renderer.POS_UV_NORMAL.addVertex(((float)vp.vector3D.xCoord), ((float)vp.vector3D.yCoord), ((float)vp.vector3D.zCoord), vp.texturePositionX, vp.texturePositionY,(float)vec32.x,(float)vec32.y,(float)vec32.z);
		}
		Renderer.POS_UV_NORMAL.draw();
	}

	public void flipFace(){
		PositionTextureVertex[] apositiontexturevertex = new PositionTextureVertex[this.vertexPositions.length];
		for(int i=0;i<this.vertexPositions.length;++i)apositiontexturevertex[i] = this.vertexPositions[this.vertexPositions.length - i - 1];
		this.vertexPositions = apositiontexturevertex;
	}
}
