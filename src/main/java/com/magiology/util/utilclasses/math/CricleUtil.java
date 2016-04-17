package com.magiology.util.utilclasses.math;


public class CricleUtil{
	private static float[] 
		cos=new float[361*resolution],
		sin=cos.clone();
	//init
	private static int resolution=3;
	//load
	static{
		for(int i=0;i<cos.length;i++){
			float rad=(float)(Math.toRadians(i)/resolution);
			cos[i]=(float) Math.cos(rad);
			sin[i]=(float) Math.sin(rad);
		}
	}
	public static float cos(double angleD){
		int angle=fix(angleD);
		try{
			return cos[angle];
		}catch(Exception e){
			e.printStackTrace();
			return cos[0];
		}
	}
	//------
	private static int fix(double angle){
		while(angle<0)angle+=360;
		while(angle>360)angle-=360;
		return (int)(angle*resolution);
	}
	//use----
	public static float sin(double angleD){
		int angle=fix(angleD);
		try{
			return sin[angle];
		}catch(Exception e){
			e.printStackTrace();
			return sin[0];
		}
	}
}
