package com.magiology.util.objs.vec;

import net.minecraft.util.EnumFacing;

import java.util.Objects;

public class Vec2i{
	
	private static final Vec2i zero=new Vec2i(0, 0);
	
	public static Vec2i zero(){
		return zero.copy();
	}
	
	public int x, y;
	
	public Vec2i(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public Vec2i add(int var){
		return new Vec2i(x+var, y+var);
	}
	
	public Vec2i add(int x1, int y1){
		return new Vec2i(x+x1, y+y1);
	}
	
	public Vec2i add(Vec2i var){
		return new Vec2i(x+var.x, y+var.y);
	}
	
	public Vec2i copy(){
		return new Vec2i(x, y);
	}
	
	public double distanceTo(Vec2i other){
		return Math.sqrt(Math.pow(other.x-x, 2)+Math.pow(other.y-y, 2));
	}
	
	@Override
	public boolean equals(Object obj){
		if(this==obj) return true;
		if(obj==null) return false;
		if(obj instanceof Vec2i) return false;
		Vec2i other=(Vec2i)obj;
		return x==other.x&&y==other.y;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(x, y);
	}
	
	public Vec2i mul(float var){
		return mul(var, var);
	}
	
	public Vec2i mul(float x, float y){
		return new Vec2i((int)(this.x*x), (int)(this.y*y));
	}
	
	public Vec2i offset(EnumFacing dir){
		return new Vec2i(x+dir.getFrontOffsetX(), y+dir.getFrontOffsetZ());
	}
	
	public Vec2i sub(int var){
		return new Vec2i(x-var, y-var);
	}
	
	public Vec2i sub(int x1, int y1){
		return new Vec2i(x-x1, y-y1);
	}
	
	public Vec2i sub(Vec2i var){
		return new Vec2i(x-var.x, y-var.y);
	}
	
	@Override
	public String toString(){
		return "Vec2i["+x+", "+y+"]";
	}
}
