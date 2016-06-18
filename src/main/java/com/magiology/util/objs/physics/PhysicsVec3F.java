package com.magiology.util.objs.physics;

import com.magiology.util.objs.Vec3M;

public class PhysicsVec3F{
	
	public PhysicsFloat x,y,z;
	
	public PhysicsVec3F(Vec3M startingPoint,Vec3M acceleration){
		x=new PhysicsFloat(startingPoint.getX(), acceleration.getX());
		y=new PhysicsFloat(startingPoint.getY(), acceleration.getY());
		z=new PhysicsFloat(startingPoint.getZ(), acceleration.getZ());
	}
	public PhysicsVec3F(Vec3M startingPoint,Vec3M acceleration, boolean[] simple){
		x=new PhysicsFloat(startingPoint.getX(), acceleration.getX(),simple[0]);
		y=new PhysicsFloat(startingPoint.getY(), acceleration.getY(),simple[1]);
		z=new PhysicsFloat(startingPoint.getZ(), acceleration.getZ(),simple[2]);
	}
	public void addWall(String key, float wallPos,boolean pointOnSideOfTheWall){
		x.addWall(key, wallPos, pointOnSideOfTheWall);
		y.addWall(key, wallPos, pointOnSideOfTheWall);
		z.addWall(key, wallPos, pointOnSideOfTheWall);
	}
	public void bounce(float multiplyer){
		x.bounce(multiplyer);
		y.bounce(multiplyer);
		z.bounce(multiplyer);
	}
	@Override
	public PhysicsVec3F clone(){
		PhysicsVec3F result=new PhysicsVec3F(new Vec3M(),new Vec3M());
		result.x=x.clone();
		result.y=y.clone();
		result.z=z.clone();
		return result;
	}
	public Vec3M difference(){
		return new Vec3M(x.difference(), y.difference(), z.difference());
	}
	public Vec3M getPoint(){
		return new Vec3M(x.getPoint(), y.getPoint(), z.getPoint());
	}
	public Object[][] removeWall(String key){
		Object[][] Return=new Object[3][2];
		Return[0]=x.removeWall(key);
		Return[1]=y.removeWall(key);
		Return[2]=z.removeWall(key);
		return Return;
	}
	public void removeWalls(){
		x.removeWalls();
		y.removeWalls();
		z.removeWalls();
	}
	public void setFriction(float friction){
		x.friction=friction;
		y.friction=friction;
		z.friction=friction;
	}
	public void update(){
		x.update();
		y.update();
		z.update();
	}
}
