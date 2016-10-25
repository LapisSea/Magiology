package com.magiology.util.objs.vec;

import java.nio.FloatBuffer;

import net.minecraft.util.math.*;

public class BazeVec3M{
	
	protected double x,y,z;

	public double x(){
		return x;
	}
	
	public double y(){
		return y;
	}
	
	public double z(){
		return z;
	}
	
	public float getX(){
		return (float)x;
	}

	public float getY(){
		return (float)y;
	}

	public float getZ(){
		return (float)z;
	}
	
	public BazeVec3M(){
		this(0,0,0);
	}
	
	public BazeVec3M(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public BazeVec3M(Vec3i vec){
		this.x=vec.getX();
		this.y=vec.getY();
		this.z=vec.getZ();
	}
	
	public BazeVec3M(Vec3d vec){
		this.x=vec.xCoord;
		this.y=vec.yCoord;
		this.z=vec.zCoord;
	}
	
	public BazeVec3M load(FloatBuffer buf){
		x=buf.get();
		y=buf.get();
		z=buf.get();
		return this;
	}
	
	public BazeVec3M store(FloatBuffer buf){
		buf.put((float)x);
		buf.put((float)y);
		buf.put((float)z);
		return this;
	}
	
	public float lengthSquared(){
		return (float)(this.x*this.x+this.y*this.y+this.z*this.z);
	}
	
	public BazeVec3M negate(){
		return new BazeVec3M(-x,-y,-z);
	}
	
	public BazeVec3M scale(float scale){
		return new BazeVec3M(x*scale,y*scale,z*scale);
	}
	
	public final float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	
	public Vec3M normalize(){// normalise
		double d0=MathHelper.sqrt_double(this.x*this.x+this.y*this.y+this.z*this.z);
		return d0<1.0E-4D?new Vec3M():new Vec3M(this.x/d0,this.y/d0,this.z/d0);
	}
	
}
