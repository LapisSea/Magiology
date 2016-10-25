package com.magiology.util.objs.vec;

public interface IVec3M<T extends IVec3M>{

	double x();
	double y();
	double z();
	float getX();
	float getY();
	float getZ();

	Vec2FM swizzleXY();
	Vec2FM swizzleXZ();
	Vec2FM swizzleYX();
	Vec2FM swizzleYZ();
	Vec2FM swizzleZX();
	Vec2FM swizzleZY();
	T swizzleXYZ();
	T swizzleXZY();
	T swizzleYXZ();
	T swizzleYZX();
	T swizzleZXY();
	T swizzleZYX();
	
	<V extends Vec2FM>V swizzleXY(V dest);
	<V extends Vec2FM>V swizzleXZ(V dest);
	<V extends Vec2FM>V swizzleYX(V dest);
	<V extends Vec2FM>V swizzleYZ(V dest);
	<V extends Vec2FM>V swizzleZX(V dest);
	<V extends Vec2FM>V swizzleZY(V dest);
	<V extends Vec3M>V swizzleXYZ(V dest);
	<V extends Vec3M>V swizzleXZY(V dest);
	<V extends Vec3M>V swizzleYXZ(V dest);
	<V extends Vec3M>V swizzleYZX(V dest);
	<V extends Vec3M>V swizzleZXY(V dest);
	<V extends Vec3M>V swizzleZYX(V dest);
}
