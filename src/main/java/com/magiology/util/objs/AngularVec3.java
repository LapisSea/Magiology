package com.magiology.util.objs;

import com.magiology.util.statics.math.MathUtil;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by LapisSea on 29.1.2016..
 */
public class AngularVec3{

	public float length=0;
	private float xRotation, yRotation;

	public AngularVec3(float xRotation, float yRotation, float length){
		this.setXRotation(xRotation);
		this.setYRotation(yRotation);
		this.length=length;


	}
	public AngularVec3(Vec3M vec){
		float
				distanceX=(float)-vec.x,
				distanceY=(float)-vec.y,
				distanceZ=(float)-vec.z,
				rotationX=(float)-Math.toDegrees(Math.atan2(distanceY,new Vec3M(-distanceX, 0, -distanceZ).lengthVector())),
				rotationY=(float)-Math.toDegrees(Math.atan2(distanceX, -distanceZ));

		this.setXRotation(rotationX);
		this.setYRotation(rotationY);
		this.length=vec.length();
	}

	public AngularVec3 abs(){
		return new AngularVec3(Math.abs(getXRotation()),Math.abs(getYRotation()),length);
	}
	public AngularVec3 add(AngularVec3 vec){
		return new AngularVec3(getXRotation()+vec.getXRotation(), getYRotation()+vec.getYRotation(),length+vec.length);
	}
	public float angleToDot(){
		float
			angleArea=180,
			x=Math.abs(getXRotation()),
			y=Math.abs(getYRotation());

//		x=Math.min(x, Math.abs(x-180));
//		y=Math.min(y, Math.abs(y-180));
		
		return 1-MathUtil.snap(new Vector2f(x,y).length(),0,angleArea)/angleArea;
	}
	public AngularVec3 expand(float length){
		return new AngularVec3(this.getXRotation(), this.getYRotation(),this.length+length);
	}
	public float getXRotation(){
		return xRotation;
	}
	public float getYRotation(){
		return yRotation;
	}
	public AngularVec3 mul(float mul){
		return new AngularVec3(getXRotation()+mul, getYRotation()+mul,length+mul);
	}
	public AngularVec3 mul(float mulx,float muly,float mull){
		return new AngularVec3(getXRotation()+mulx, getYRotation()+muly,length+mull);
	}
	public AngularVec3 rotate(AngularVec3 vec){
		return new AngularVec3(getXRotation()+vec.getXRotation(), getYRotation()+vec.getYRotation(),length);
	}
	public AngularVec3 rotate(float xRotation, float yRotation){
		return new AngularVec3(this.getXRotation()-xRotation, this.getYRotation()-yRotation,length);
	}

	public void setXRotation(float xRotation){
		this.xRotation=MathUtil.normaliseDegrees(xRotation);
	}

	public void setYRotation(float yRotation){
		this.yRotation=MathUtil.normaliseDegrees(yRotation);
	}

	public AngularVec3 sub(AngularVec3 vec){
		return new AngularVec3(getXRotation()-vec.getXRotation(), getYRotation()-vec.getYRotation(),length-vec.length);
	}

	@Override
	public String toString(){
		return new StringBuilder().append("AngularVec3[xRot=").append(xRotation).append(", yRot=").append(yRotation).append(", length=").append(length).append("]").toString();
	}
	
	public Vec3M toVec3M(){
		Vec3M result=new Vec3M();
		result.x=-MathUtil.sin((int)getXRotation())*MathUtil.cos((int)getYRotation())*length;
		result.z= MathUtil.cos((int)getXRotation())*MathUtil.cos((int)getYRotation())*length;
		result.y=-MathUtil.sin((int)getYRotation())*length;
		return result;
	}
}
