package com.magiology.util.objs.color;

import com.magiology.util.statics.math.MathUtil;

public class ColorMRead implements IColorM{
	
	protected float r, g, b, a;
	
	public ColorMRead(IColorM color){
		this(color.r(), color.g(), color.b(), color.a());
	}
	
	public ColorMRead(){
		this(1, 1, 1);
	}
	
	public ColorMRead(double r, double g, double b){
		this((float)r, (float)g, (float)b);
	}
	
	public ColorMRead(double r, double g, double b, double a){
		this((float)r, (float)g, (float)b, (float)a);
	}
	
	public ColorMRead(float r, float g, float b){
		this(r, g, b, 1);
	}
	
	public ColorMRead(float r, float g, float b, float a){
		this.r=MathUtil.snap(r, 0, 1);
		this.g=MathUtil.snap(g, 0, 1);
		this.b=MathUtil.snap(b, 0, 1);
		this.a=MathUtil.snap(a, 0, 1);
	}
	
	@Override
	public float r(){
		return r;
	}
	
	@Override
	public float g(){
		return g;
	}
	
	@Override
	public float b(){
		return b;
	}
	
	@Override
	public float a(){
		return a;
	}
	
	@Override
	public String toString(){
		return "(r="+r()+", g="+g()+", b="+b()+", a="+a()+")";
	}
}
