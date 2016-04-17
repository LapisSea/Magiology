package com.magiology.util.renderers.tessellatorscripts;

import com.magiology.util.utilobjects.vectors.QuadUV;

import net.minecraft.util.ResourceLocation;

public class NormalCubeModel{
	public float minX,minY,minZ,maxX,maxY,maxZ;
	PlateModel[] sides;
	ResourceLocation[] st=new ResourceLocation[6];
	QuadUV[] UVs=new QuadUV[6];
	public boolean[] willSideRender={true,true,true,true,true,true};
	public NormalCubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ){
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		sides=genSides();
	}
	public NormalCubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ,QuadUV[] quadUVs,ResourceLocation[] sidedtextures){
		if(quadUVs.length!=6||sidedtextures.length!=6)return;
		UVs=quadUVs;
		st=sidedtextures;
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		sides=genSides();
	}
	public void draw(){
		if(willSideRender!=null)for(int a1=0;a1<sides.length;a1++){
			PlateModel a=sides[a1];
			if(willSideRender[a1]&&a!=null)a.render();
		}
	}
	
	private PlateModel[] genSides(){
		PlateModel[] result=new PlateModel[6];
		result[0]=new PlateModel(-(maxX-minX), 0, maxZ-minZ,UVs[0],st[0], false).setX(maxX).setZ(minZ).setY(minY);
		result[1]=new PlateModel(  maxX-minX,  0, maxZ-minZ,UVs[1],st[1], false).setX(minX).setZ(minZ).setY(maxY);
		result[2]=new PlateModel(  maxX-minX,  maxY-minY, 0,UVs[2],st[2], false).setX(minX).setZ(minZ).setY(minY);
		result[3]=new PlateModel(-(maxX-minX), maxY-minY, 0,UVs[3],st[3], false).setX(maxX).setZ(maxZ).setY(minY);
		result[4]=new PlateModel(0, maxY-minY, -(maxZ-minZ),UVs[4],st[4], false).setX(minX).setZ(maxZ).setY(minY);result[4].renderOnZ=true;
		result[5]=new PlateModel(   0, maxY-minY, maxZ-minZ,UVs[5],st[5], false).setX(maxX).setZ(minZ).setY(minY);result[5].renderOnZ=true;
		return result;
	}
}
