package com.magiology.util.statics;

import com.magiology.util.statics.math.MathUtil;

import java.util.Random;

public class RandUtil{
	
	static Random rand=new Random();
	
	public static double CRD(double scale){
		return (0.5-RD())*scale;
	}
	
	public static float CRF(double scale){
		return (float)((0.5-RF())*scale);
	}
	
	public static int CRI(int scale){
		return scale-RI(scale*2);
	}
	
	public static boolean RB(){
		return rand.nextBoolean();
	}
	
	/**
	 * this method returns a random boolean with a custom chance of getting
	 * true. The higher the number is the higher chance will be for getting a
	 * true return. Type in a number higher or equal to 0 and lower or equal to
	 * 1.
	 */
	public static boolean RB(double percentage){
		percentage=MathUtil.snap(percentage, 0, 1);
		return percentage!=0&&(percentage==1||RF()<percentage);
	}
	
	/**
	 * this method returns a random boolean with a custom chance of getting
	 * true. The higher the number is the lower chance will be for getting a
	 * true return. Type in a number higher or equal to 1.
	 * s
	 */
	public static boolean RB(int percentage){
		percentage=Math.max(percentage, 1);
		if(percentage==1) return true;
		return RI(percentage)==0;
	}
	
	public static double RD(){
		return rand.nextDouble();
	}
	
	public static double RD(double scale){
		return RD()*scale;
	}
	
	public static float RF(){
		return rand.nextFloat();
	}
	
	public static float RF(double scale){
		return (float)(RF()*scale);
	}
	
	public static int RI(int scale){
		return rand.nextInt(scale);
	}
	
	public static long RL(){
		return rand.nextLong();
	}
	
}
