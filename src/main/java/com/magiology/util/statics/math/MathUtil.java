package com.magiology.util.statics.math;

import com.magiology.util.statics.RandUtil;

public class MathUtil{

	public static float[][] addToDoubleFloatArray(final float[][] array1,final float[][] array2){
		float[][] result=array1.clone();
		for(int a=0;a<result.length;a++)result[a]=addToFloatArray(result[a], array2[a]);
		return result;
	}

	public static float[] addToFloatArray(final float[] array1,final float[] array2){
		float[] result=array1.clone();
		for(int a=0;a<result.length;a++)result[a]+=array2[a];
		return result;
	}

	public static double[] circleXZ(double angle){
		double[] result={0,0};
		int intAngle=(int)angle;
		result[0]=MathUtil.sin(intAngle);//-X-
		result[1]=MathUtil.cos(intAngle);//-Z-
		return result;
	}

	public static float cos(double a){
		return (float)Math.cos((float)Math.toRadians(a));
	}

	/**
	 * Creates a x,y,z offset coordinate of a ball. (can create 2 coordinates)
	 * Args:x,y,z particle speed, size
	 * @param ballSize a
	 * @param hasSecondPos a
	 * @return a
	 */
	public static double[] createBallXYZ(double ballSize, boolean hasSecondPos){
		int xRot=RandUtil.RI(360),yRot=RandUtil.RI(360);
		double[] result=new double[3*(hasSecondPos?2:1)];
		result[0]=MathUtil.sin(xRot)*MathUtil.cos(yRot);//-X-
		result[1]=MathUtil.sin(yRot);//-Y-
		result[2]=MathUtil.cos(xRot)*MathUtil.cos(yRot);//-Z-
		if(hasSecondPos){
			xRot+=RandUtil.CRI(50);
			yRot+=RandUtil.CRI(50);
			result[3]=MathUtil.sin(xRot)*MathUtil.cos(yRot);//-X-
			result[4]=MathUtil.sin(yRot);//-Y-
			result[5]=MathUtil.cos(xRot)*MathUtil.cos(yRot);//-Z-
		}
		for(int a=0;a<result.length;a++)result[a]*=ballSize;
		return result;
	}

	public static double[] cricleXZForce(double angle,double offset){
		double[] result={0,0};
		angle+=offset;
		int intAngle=(int)angle;
		result[0]=MathUtil.sin(intAngle);//-X-
		result[1]=MathUtil.cos(intAngle);//-Z-
		return result;
	}

	public static double[] cricleXZwSpeed(double angle,double offset){
		double[] result={0,0,0,0};
		{
			int intAngle=(int)angle;
			result[0]=MathUtil.sin(intAngle);//-X-
			result[1]=MathUtil.cos(intAngle);//-Z-
			angle+=offset;}{
			int intAngle=(int)angle;
			result[0]=MathUtil.sin(intAngle);//-X-
			result[1]=MathUtil.cos(intAngle);//-Z-
		}
		return result;
	}

	public static int getNumPrefix(double num){
		return num>=0?1:-1;
	}

	public static int getNumPrefix(float num){
		return num>=0?1:-1;
	}

	public static boolean isEqualInBouds(double variable,double wantedVariable,double bounds){
		//10.02==10? 0.5
		//10.52>10 9.52<10
		return variable+bounds>wantedVariable&&variable-bounds<wantedVariable;
	}

	public static boolean isNumValid(double num){
		return Double.isFinite(num)&&!Double.isNaN(num);
	}

	public static boolean isPrefixSame(double num1, double num2){
		return getNumPrefix(num1)==getNumPrefix(num2);
	}

	public static double max(double...nums){
		if(nums.length==1)return nums[0];
		double result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.max(result,nums[i]);
		return result;
	}

	public static float max(float...nums){
		if(nums.length==1)return nums[0];
		float result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.max(result,nums[i]);
		return result;
	}

	public static int max(int...nums){
		if(nums.length==1)return nums[0];
		int result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.max(result,nums[i]);
		return result;
	}

	public static double min(double...nums){
		if(nums.length==1)return nums[0];
		double result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.min(result,nums[i]);
		return result;
	}

	public static float min(float...nums){
		if(nums.length==1)return nums[0];
		float result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.min(result,nums[i]);
		return result;
	}

	public static int min(int...nums){
		if(nums.length==1)return nums[0];
		int result=nums[0];
		for(int i=1;i<nums.length;i++)result=Math.min(result,nums[i]);
		return result;
	}
	public static float normaliseDegrees(float angle){
		boolean isNegative=angle<0;
		int negative=isNegative?-1:1;
		float positiveAngle=angle*negative;
		if(positiveAngle<360)return angle;
		if(isNegative){
			while(angle<360)angle+=360;
		}else{
			while(angle>=360)angle-=360;
		}
		return angle;
	}
	public static float sin(float a){
		return (float)Math.sin((float)Math.toRadians(a));
	}
	public static double snap(double value,double min,double max){
		if(min>max)return value;
		if(value<min)value=min;
		if(value>max)value=max;
		return value;
	}
	public static float snap(float value,float min,float max){
		return (float)snap((double)value, (double)min, (double)max);
	}
	public static int snap(int value,int min,int max){
		return (int)snap((double)value, (double)min, (double)max);
	}

	public static double sq(double var){
		return var*var;
	}
	public static int sq(int var){
		return var*var;
	}
	public static float sq(float var){
		return var*var;
	}

}
