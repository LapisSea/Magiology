package com.magiology.util.utilclasses.math;

public class ArrayMath{
	private static final char mul='*',div='/',add='+',sub='-';
	

	public static double[] calc(double value,double[] array,char type){
		double[] array2=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=value-array[i];
		return array2;
	}
	public static double[] calc(double value,float[] array,char type){
		double[] array2=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=value-array[i];
		return array2;
	}
	public static double[] calc(double value, int[] array, char type){
		double[] result=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=value-array[i];
		return result;
	}
	public static double[] calc(double value, long[] array, char type){
		double[] result=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=value-array[i];
		return result;
	}
	public static double[] calc(double[] array,double value,char type){
		double[] array2=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=array[i]-value;
		return array2;
	}
	public static double[] calc(double[] array1,double[] array2,char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static double[] calc(double[] array1, float[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static double[] calc(double[] array1, int[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static double[] calc(double[] array1, long[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static float[] calc(float value,float[] array,char type){
		float[] array2=new float[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=value-array[i];
		return array2;
	}
	public static float[] calc(float value, int[] array, char type){
		float[] result=new  float[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=value-array[i];
		return result;
	}
	public static double[] calc(float[] array,double value,char type){
		double[] array2=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=array[i]-value;
		return array2;
	}
	public static double[] calc(float[] array1, double[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static float[] calc(float[] array,float value,char type){
		float[] array2=new float[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=array[i]-value;
		return array2;
	}
	
	public static float[] calc(float[] array1,float[] array2,char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		float[] array3=new float[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static float[] calc(float[] array1, int[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		float[] array3=new float[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	
	public static float[] calc(float[] array1, long[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		float[] array3=new float[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static int[] calc(int value,int[] array,char type){
		int[] array2=new int[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=value-array[i];
		return array2;
	}
	
	public static double[] calc(int[] array, double value, char type){
		double[] result=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=array[i]-value;
		return result;
	}
	public static double[] calc(int[] array1, double[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static float[] calc(int[] array, float value, char type){
		float[] result=new float[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=array[i]-value;
		return result;
	}
	public static float[] calc(int[] array1, float[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		float[] array3=new float[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static int[] calc(int[] array,int value,char type){
		int[] array2=new int[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=array[i]-value;
		return array2;
	}
	
	
	public static int[] calc(int[] array1,int[] array2,char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		int[] array3=new int[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static long[] calc(int[] array1, long[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		long[] array3=new long[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static long[] calc(long value,long[] array,char type){
		long[] array2=new long[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=value*array[i];
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=value/array[i];
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=value+array[i];
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=value-array[i];
		return array2;
	}
	
	
	public static double[] calc(long[] array, double value, char type){
		double[] result=new double[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)result[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)result[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)result[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)result[i]=array[i]-value;
		return result;
	}
	public static double[] calc(long[] array1, double[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		double[] array3=new double[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static float[] calc(long[] array1, float[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		float[] array3=new float[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	
	
	public static long[] calc(long[] array1, int[] array2, char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		long[] array3=new long[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
	public static long[] calc(long[] array,long value,char type){
		long[] array2=new long[array.length];
		if(type==mul)for(int i=0;i<array.length;i++)array2[i]=array[i]*value;
		else if(type==div)for(int i=0;i<array.length;i++)array2[i]=array[i]/value;
		else if(type==add)for(int i=0;i<array.length;i++)array2[i]=array[i]+value;
		else if(type==sub)for(int i=0;i<array.length;i++)array2[i]=array[i]-value;
		return array2;
	}
	public static long[] calc(long[] array1,long[] array2,char type){
		if(array1.length!=array2.length)throw new IllegalStateException("Diferent array sizes. array 1: "+array1.length+" array 2: "+array2.length);
		long[] array3=new long[array1.length];
		if(type==mul)for(int i=0;i<array1.length;i++)array3[i]=array1[i]*array2[i];
		else if(type==div)for(int i=0;i<array1.length;i++)array3[i]=array1[i]/array2[i];
		else if(type==add)for(int i=0;i<array1.length;i++)array3[i]=array1[i]+array2[i];
		else if(type==sub)for(int i=0;i<array1.length;i++)array3[i]=array1[i]-array2[i];
		return array3;
	}
}
