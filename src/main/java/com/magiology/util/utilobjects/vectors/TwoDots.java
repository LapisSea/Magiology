package com.magiology.util.utilobjects.vectors;

public class TwoDots{
	public double x1;
	public double x2;
	public double y1;
	public double y2;
	public double z1;
	public double z2;
	
	public TwoDots(double x1,double y1,double z1,double x2,double y2,double z2){
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
		this.z1=z1;
		this.z2=z2;
	}
	
	public double[] getDot(boolean firstOrSecond){
		if(firstOrSecond)return new double[]{x1,y1,z1};
		else return new double[]{x2,y2,z2};
	}
	
}
