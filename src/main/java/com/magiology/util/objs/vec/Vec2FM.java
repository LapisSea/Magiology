package com.magiology.util.objs.vec;

import com.magiology.util.interf.Calculable;

public class Vec2FM implements Calculable<Vec2FM>{
	
	public float x,y;
	
	public Vec2FM(){
		
	}
	public Vec2FM(float x, float y){
		set(x, y);
	}

	public float length(){
		return (float)Math.sqrt(x*x+y*y);
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	@Override
	public Vec2FM add(float var){
		return new Vec2FM(x+var, y+var);
	}
	@Override
	public Vec2FM add(Vec2FM var){
		return new Vec2FM(x+var.x, y+var.y);
	}
	@Override
	public Vec2FM div(float var){
		return new Vec2FM(x/var, y/var);
	}
	@Override
	public Vec2FM div(Vec2FM var){
		return new Vec2FM(x/var.x, y/var.y);
	}
	@Override
	public Vec2FM mul(float var){
		return new Vec2FM(x*var, y*var);
	}
	@Override
	public Vec2FM mul(Vec2FM var){
		return new Vec2FM(x*var.x, y*var.y);
	}
	@Override
	public Vec2FM sub(float var){
		return new Vec2FM(x-var, y-var);
	}
	@Override
	public Vec2FM sub(Vec2FM var){
		return new Vec2FM(x-var.x, y-var.y);
	}
	@Override
	public Vec2FM copy(){
		return new Vec2FM(x,y);
	}
	public void set(Vec2FM vec){
		set(vec.x,vec.y);
	}
	@Override
	public String toString(){
		return "["+x+", "+y+"]";
	}
	
	public Vec2i toVec2i(){
		return new Vec2i((int)x, (int)y);
	}
}
