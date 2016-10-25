package com.magiology.util.objs.vec;

import net.minecraft.util.math.*;

public class Vec3MFinal implements IVec3M<Vec3MFinal>{
	
	private final double x,y,z;
	
	public Vec3MFinal(Vec3i vec){
		this(vec.getX(),vec.getY(),vec.getZ());
	}
	
	public Vec3MFinal(Vec3d vec){
		this(vec.xCoord,vec.yCoord,vec.zCoord);
	}
	
	public Vec3MFinal(IVec3M vec){
		this(vec.x(),vec.y(),vec.z());
	}
	
	public Vec3MFinal(double x,double y,double z){
		this.x=x;
		this.y=y;
		this.z=z;
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
	@Override
	public String toString(){
		return "F("+x()+", "+y()+", "+z()+")";
	}
	
	
	public Vec2FM popX(Vec2FM destination){
		destination.set(getY(),getZ());
		return destination;
	}
	
	public Vec2FM popY(Vec2FM destination){
		destination.set(getX(),getZ());
		return destination;
	}
	
	public Vec2FM popZ(Vec2FM destination){
		destination.set(getX(),getY());
		return destination;
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
	public Vec3MFinal swizzleXYZ(){
		return new Vec3MFinal(this);
	}
	
	@Override
	public Vec3MFinal swizzleXZY(){
		return new Vec3MFinal(x(), z(), y());
	}
	
	@Override
	public Vec3MFinal swizzleYXZ(){
		return new Vec3MFinal(y(), x(), z());
	}
	
	@Override
	public Vec3MFinal swizzleYZX(){
		return new Vec3MFinal(y(), z(), x());
	}
	
	@Override
	public Vec3MFinal swizzleZXY(){
		return new Vec3MFinal(z(), x(), y());
	}
	
	@Override
	public Vec3MFinal swizzleZYX(){
		return new Vec3MFinal(z(), y(), x());
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
}
