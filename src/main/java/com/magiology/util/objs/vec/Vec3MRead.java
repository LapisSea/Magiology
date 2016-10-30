package com.magiology.util.objs.vec;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.util.math.*;

public class Vec3MRead implements IVec3M<Vec3MRead>{
	
	protected double x,y,z;
	
	public Vec3MRead(double x, double y, double z){
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public Vec3MRead(Vec3i vec){
		this.x=vec.getX();
		this.y=vec.getY();
		this.z=vec.getZ();
	}
	
	public Vec3MRead(Vec3d vec){
		this.x=vec.xCoord;
		this.y=vec.yCoord;
		this.z=vec.zCoord;
	}
	
	@Override
	public double x(){
		return x;
	}
	
	@Override
	public double y(){
		return y;
	}
	
	@Override
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
	
	public Vec3MRead(){
		this(0, 0, 0);
	}
	
	public Vec3MRead(IVec3M vec){
		this(vec.x(), vec.y(), vec.z());
	}
	
	public Vec3MRead load(FloatBuffer buf){
		x=buf.get();
		y=buf.get();
		z=buf.get();
		return this;
	}
	
	public Vec3MRead store(FloatBuffer buf){
		buf.put((float)x);
		buf.put((float)y);
		buf.put((float)z);
		return this;
	}
	
	public float lengthSquared(){
		return (float)(this.x*this.x+this.y*this.y+this.z*this.z);
	}
	
	public Vec3MRead negate(){
		return new Vec3MRead(-x, -y, -z);
	}
	
	public Vec3MRead scale(float scale){
		return new Vec3MRead(x*scale, y*scale, z*scale);
	}
	
	public final float length(){
		return (float)Math.sqrt(lengthSquared());
	}
	
	public Vec3M normalize(){// normalise
		double d0=MathHelper.sqrt_double(this.x*this.x+this.y*this.y+this.z*this.z);
		return d0<1.0E-4D?new Vec3M():new Vec3M(this.x/d0, this.y/d0, this.z/d0);
	}
	
	@Override
	public String toString(){
		return "R("+this.x+", "+this.y+", "+this.z+")";
	}
	
	@Override
	public Vec2FM swizzleXY(){
		return new Vec2FM(getX(), getY());
	}
	
	@Override
	public Vec2FM swizzleXZ(){
		return new Vec2FM(getX(), getZ());
	}
	
	@Override
	public Vec2FM swizzleYX(){
		return new Vec2FM(getY(), getX());
	}
	
	@Override
	public Vec2FM swizzleYZ(){
		return new Vec2FM(getY(), getZ());
	}
	
	@Override
	public Vec2FM swizzleZX(){
		return new Vec2FM(getZ(), getX());
	}
	
	@Override
	public Vec2FM swizzleZY(){
		return new Vec2FM(getZ(), getY());
	}
	
	@Override
	public Vec3MRead swizzleXYZ(){
		return new Vec3MRead(this);
	}
	
	@Override
	public Vec3MRead swizzleXZY(){
		return new Vec3MRead(x(), z(), y());
	}
	
	@Override
	public Vec3MRead swizzleYXZ(){
		return new Vec3MRead(y(), x(), z());
	}
	
	@Override
	public Vec3MRead swizzleYZX(){
		return new Vec3MRead(y(), z(), x());
	}
	
	@Override
	public Vec3MRead swizzleZXY(){
		return new Vec3MRead(z(), x(), y());
	}
	
	@Override
	public Vec3MRead swizzleZYX(){
		return new Vec3MRead(z(), y(), x());
	}
	
	@Override
	public <V extends Vec2FM> V swizzleXY(V dest){
		dest.set(getX(), getY());
		return dest;
	}
	
	@Override
	public <V extends Vec2FM> V swizzleXZ(V dest){
		dest.set(getX(), getZ());
		return dest;
	}
	
	@Override
	public <V extends Vec2FM> V swizzleYX(V dest){
		dest.set(getY(), getX());
		return dest;
	}
	
	@Override
	public <V extends Vec2FM> V swizzleYZ(V dest){
		dest.set(getY(), getZ());
		return dest;
	}
	
	@Override
	public <V extends Vec2FM> V swizzleZX(V dest){
		dest.set(getZ(), getX());
		return dest;
	}
	
	@Override
	public <V extends Vec2FM> V swizzleZY(V dest){
		dest.set(getZ(), getY());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleXYZ(V dest){
		dest.set(x(), y(), z());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleXZY(V dest){
		dest.set(x(), z(), y());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleYXZ(V dest){
		dest.set(y(), x(), z());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleYZX(V dest){
		dest.set(y(), z(), x());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleZXY(V dest){
		dest.set(z(), x(), y());
		return dest;
	}
	
	@Override
	public <V extends Vec3M> V swizzleZYX(V dest){
		dest.set(z(), y(), x());
		return dest;
	}
	
	@Override
	public Vector3f toLWJGLVec(){
		return toLWJGLVec(new Vector3f());
	}
	
	@Override
	public Vector3f toLWJGLVec(Vector3f dest){
		dest.x=getX();
		dest.y=getY();
		dest.z=getZ();
		return dest;
	}
}
