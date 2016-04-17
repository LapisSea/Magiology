package com.magiology.util.utilobjects.vectors;

public class X1y1z1x2y2z2{
	public float minx,miny,minz,maxx,maxy,maxz;
	public X1y1z1x2y2z2(){}
	public X1y1z1x2y2z2(double minX,double minY,double minZ,double maxX,double maxY,double maxZ){
		minx=(float)minX;
		miny=(float)minY;
		minz=(float)minZ;
		maxx=(float)maxX;
		maxy=(float)maxY;
		maxz=(float)maxZ;
	}
	public void refresh(double minX,double minY,double minZ,double maxX,double maxY,double maxZ) {
		minx=(float)minX;
		miny=(float)minY;
		minz=(float)minZ;
		maxx=(float)maxX;
		maxy=(float)maxY;
		maxz=(float)maxZ;
	}
}
