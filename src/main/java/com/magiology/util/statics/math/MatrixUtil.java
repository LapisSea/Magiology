package com.magiology.util.statics.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.util.math.Vec3d;

public final class MatrixUtil{
	
	public static final MatrixUtil instance=new MatrixUtil();
	
	private static Matrix4f matrix;
	
	public static Matrix4f copy(Matrix4f mat){
		return mat.transpose(null);
	}
	
	public static MatrixUtil createMatrix(float rotationX,float rotationY,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		matrix=result;
		return instance;
	}
	
	public static MatrixUtil createMatrix(float rotationX,float rotationY,float rotationZ, float scale){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.scale(new Vector3f(scale, scale, scale), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrix(IVec3M translation){
		matrix=new Matrix4f().translate(translation.toLWJGLVec());
		return instance;
	}
	
	public static MatrixUtil createMatrix(IVec3M translation,float rotationX,float rotationY,float rotationZ, float scale){
		createMatrix(translation.toLWJGLVec());
		return instance;
	}
	public static MatrixUtil createMatrix(Vector3f translation){
		matrix=new Matrix4f().translate(translation);
		return instance;
	}
	public static MatrixUtil createMatrix(Vector3f translation,float rotationX,float rotationY,float rotationZ, float scale){
		Matrix4f result=new Matrix4f();
		Matrix4f.translate(translation, result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.scale(new Vector3f(scale, scale, scale), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixX(float rotationX){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixXY(float rotationX,float rotationY){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixXZ(float rotationX,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixY(float rotationY){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixYZ(float rotationY,float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		matrix=result;
		return instance;
	}
	public static MatrixUtil createMatrixZ(float rotationZ){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		matrix=result;
		return instance;
	}
	
	public static MatrixUtil createMatrixZYX(float rotationZ,float rotationY,float rotationX){
		Matrix4f result=new Matrix4f();
		Matrix4f.rotate((float)Math.toRadians(rotationZ), new Vector3f(0, 0, 1), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationY), new Vector3f(0, 1, 0), result, result);
		Matrix4f.rotate((float)Math.toRadians(rotationX), new Vector3f(1, 0, 0), result, result);
		matrix=result;
		return instance;
	}
	public static Matrix4f rotate(IVec3M rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.x()), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y()), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.z()), new Vector3f(0, 0, 1), mat, mat);
		return mat;
	}
	public static Matrix4f rotate(Vector3f rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		return mat;
	}
	public static MatrixUtil rotateAt(IVec3M rotationOffset, IVec3M rot){
		return rotateAt(rotationOffset.toLWJGLVec(), rot.toLWJGLVec());
	}
	public static Matrix4f rotateAt(IVec3M rotationOffset, IVec3M rot, Matrix4f mat){
		return rotateAt(rotationOffset.toLWJGLVec(), rot.toLWJGLVec(), mat);
	}
	public static MatrixUtil rotateAt(Vector3f rotationOffset, Vector3f rot){
		matrix.translate(rotationOffset);
		rotate(rot, matrix);
		matrix.translate(rotationOffset.negate(null));
		return instance;
	}
	public static Matrix4f rotateAt(Vector3f rotationOffset, Vector3f rot, Matrix4f mat){
		mat.translate(rotationOffset);
		rotate(rot, mat);
		mat.translate(rotationOffset.negate(null));
		return mat;
	}
	
	public static Matrix4f rotateX(float rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(1, 0, 0), mat, mat);
		return mat;
	}
	
	public static Matrix4f rotateY(float rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(0, 1, 0), mat, mat);
		return mat;
	}
	
	public static Matrix4f rotateZ(float rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(0, 0, 1), mat, mat);
		return mat;
	}
	public static Matrix4f rotateZYX(IVec3M rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.z()), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y()), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.x()), new Vector3f(1, 0, 0), mat, mat);
		return mat;
	}
	
	public static Matrix4f rotateZYX(Vector3f rot, Matrix4f mat){
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), mat, mat);
		return mat;
	}
	
	public static Vec3d transformVector(Vec3d vectorForTransformation,Matrix4f transformation){
		return transformVector(new Vec3M(vectorForTransformation), transformation).conv();
	}
	public static Vec3M transformVector(IVec3M vectorForTransformation,Matrix4f transformation){
		Vector3f result=transformVector(vectorForTransformation.toLWJGLVec(), transformation);
		return new Vec3M(result.x, result.y, result.z);
	}
	public static Vec3M transformVector(Vec3M vectorForTransformation,Vector3f translation,double rotationX,double rotationY,double rotationZ, double scale){
		if(vectorForTransformation==null)vectorForTransformation=new Vec3M();
		Vector3f vec=transformVector(vectorForTransformation.toLWJGLVec(), translation, rotationX, rotationY, rotationZ, scale);
		vectorForTransformation.set(vec);
		return vectorForTransformation;
	}
	public static Vector3f transformVector(Vector3f vec,Matrix4f transformation){
		Vector4f vec4=Matrix4f.transform(transformation, new Vector4f(vec.x,vec.y,vec.z,1), null);
		vec.set(vec4);
		return vec;
	}
	public static Vector3f transformVector(Vector3f vectorForTransformation,Vector3f translation,double rotationX,double rotationY,double rotationZ, double scale){
		if(vectorForTransformation==null)vectorForTransformation=new Vector3f();
		Matrix4f transform=createMatrix(translation,(float)rotationX,(float)rotationY,(float)rotationZ,(float)scale).finish();
		return transformVector(vectorForTransformation, transform);
	}
	private MatrixUtil(){}
	public Matrix4f finish(){
		Matrix4f result=matrix;
		matrix=null;
		return result;
	}
	
	public MatrixUtil rotate(IVec3M rot){
		Matrix4f.rotate((float)Math.toRadians(rot.x()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.z()), new Vector3f(0, 0, 1), matrix, matrix);
		return instance;
	}
	
	public MatrixUtil rotate(Vector3f rot){
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), matrix, matrix);
		return instance;
	}

	public MatrixUtil rotateX(float rot){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(1, 0, 0), matrix, matrix);
		return instance;
	}

	public MatrixUtil rotateY(float rot){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(0, 1, 0), matrix, matrix);
		return instance;
	}

	public MatrixUtil rotateZ(float rot){
		Matrix4f.rotate((float)Math.toRadians(rot), new Vector3f(0, 0, 1), matrix, matrix);
		return instance;
	}

	public MatrixUtil rotateZYX(IVec3M rot){
		Matrix4f.rotate((float)Math.toRadians(rot.z()), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.x()), new Vector3f(1, 0, 0), matrix, matrix);
		return instance;
	}

	public MatrixUtil rotateZYX(Vector3f rot){
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0), matrix, matrix);
		return instance;
	}

	public MatrixUtil scale(float scale){
		return scale(new Vector3f(scale,scale,scale));
	}

	public MatrixUtil scale(float x, float y, float z){
		return scale(new Vector3f(x,y,z));
	}
	public MatrixUtil scale(Vector3f scale){
		matrix.scale(scale);
		return instance;
	}
	public MatrixUtil translate(float x, float y, float z){
		matrix.translate(new Vector3f(x, y, z));
		return instance;
	}

	public static Matrix4f fromArray(float[] src){
		return fromArray(src,new Matrix4f());
	}
	
	public static Matrix4f fromArray(float[] src, Matrix4f mat){
		mat.m00=src[0 ];
		mat.m01=src[1 ];
		mat.m02=src[2 ];
		mat.m03=src[3 ];
		mat.m10=src[4 ];
		mat.m11=src[5 ];
		mat.m12=src[6 ];
		mat.m13=src[7 ];
		mat.m20=src[8 ];
		mat.m21=src[9 ];
		mat.m22=src[10];
		mat.m23=src[11];
		mat.m30=src[12];
		mat.m31=src[13];
		mat.m32=src[14];
		mat.m33=src[15];
		return mat;
	}
	
}
