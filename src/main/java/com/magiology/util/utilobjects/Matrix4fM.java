package com.magiology.util.utilobjects;

import org.lwjgl.util.vector.Matrix4f;

import com.magiology.api.Calculable;

public class Matrix4fM extends Matrix4f implements Calculable<Matrix4fM>{

	public static Matrix4f div(Matrix4f left, Matrix4f right, Matrix4f dest) {
		if (dest == null)
			dest = new Matrix4f();

		float m00 = left.m00 / right.m00 + left.m10 / right.m01 + left.m20 / right.m02 + left.m30 / right.m03;
		float m01 = left.m01 / right.m00 + left.m11 / right.m01 + left.m21 / right.m02 + left.m31 / right.m03;
		float m02 = left.m02 / right.m00 + left.m12 / right.m01 + left.m22 / right.m02 + left.m32 / right.m03;
		float m03 = left.m03 / right.m00 + left.m13 / right.m01 + left.m23 / right.m02 + left.m33 / right.m03;
		float m10 = left.m00 / right.m10 + left.m10 / right.m11 + left.m20 / right.m12 + left.m30 / right.m13;
		float m11 = left.m01 / right.m10 + left.m11 / right.m11 + left.m21 / right.m12 + left.m31 / right.m13;
		float m12 = left.m02 / right.m10 + left.m12 / right.m11 + left.m22 / right.m12 + left.m32 / right.m13;
		float m13 = left.m03 / right.m10 + left.m13 / right.m11 + left.m23 / right.m12 + left.m33 / right.m13;
		float m20 = left.m00 / right.m20 + left.m10 / right.m21 + left.m20 / right.m22 + left.m30 / right.m23;
		float m21 = left.m01 / right.m20 + left.m11 / right.m21 + left.m21 / right.m22 + left.m31 / right.m23;
		float m22 = left.m02 / right.m20 + left.m12 / right.m21 + left.m22 / right.m22 + left.m32 / right.m23;
		float m23 = left.m03 / right.m20 + left.m13 / right.m21 + left.m23 / right.m22 + left.m33 / right.m23;
		float m30 = left.m00 / right.m30 + left.m10 / right.m31 + left.m20 / right.m32 + left.m30 / right.m33;
		float m31 = left.m01 / right.m30 + left.m11 / right.m31 + left.m21 / right.m32 + left.m31 / right.m33;
		float m32 = left.m02 / right.m30 + left.m12 / right.m31 + left.m22 / right.m32 + left.m32 / right.m33;
		float m33 = left.m03 / right.m30 + left.m13 / right.m31 + left.m23 / right.m32 + left.m33 / right.m33;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	@Override
	public Matrix4fM add(float var){
		return null;
	}

	@Override
	public Matrix4fM add(Matrix4fM var){
		return (Matrix4fM)add(this,var,new Matrix4fM());
	}

	@Override
	public Matrix4fM div(float var){
		return null;
	}

	@Override
	public Matrix4fM div(Matrix4fM var){
		return (Matrix4fM)div(this,var,new Matrix4fM());
	}

	@Override
	public Matrix4fM mul(float var){
		return null;
	}

	@Override
	public Matrix4fM mul(Matrix4fM var){
		return (Matrix4fM)mul(this,var,new Matrix4fM());
	}

	@Override
	public Matrix4fM sub(float var){
		return null;
	}
	
	@Override
	public Matrix4fM sub(Matrix4fM var){
		return (Matrix4fM)sub(this,var,new Matrix4fM());
	}

	@Override
	public Matrix4fM copy(){
		return (Matrix4fM)load(this, null);
	}
	
}
