package com.magiology.util.utilclasses;

import com.magiology.util.utilobjects.vectors.Vec3M;

public class PhysicsUtil{
	public static double getAcceleration(double mass, double force){
		return force/mass;
	}
	
	public static float getAcceleration(float mass, float force){
		return force/mass;
	}
	public static Vec3M getAcceleration(float mass, Vec3M force){
		return new Vec3M(getAcceleration(mass, force.x), getAcceleration(mass, force.y), getAcceleration(mass, force.z));
	}
	private PhysicsUtil(){}
}
