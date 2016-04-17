package com.magiology.util.utilobjects;

import java.util.Stack;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.util.utilobjects.vectors.Vec3M;

public class MatrixStack{
	private Stack<Matrix4f> stack=new Stack<>();
	public Matrix4f get(){
		if(stack.isEmpty())stack.push(new Matrix4f());
		return stack.peek();
	}
	protected Matrix4f getNewObject(){
		if(stack.isEmpty())return new Matrix4f();
		return get().transpose(null);
	}
	public void popMatrix(){
		stack.pop();
	}
	public void pushMatrix(){
		stack.push(getNewObject());
	}
	public void rotate(double x,double y,double z){
		Matrix4f.rotate((float)Math.toRadians(x), new Vector3f(1, 0, 0), get(), get());
		Matrix4f.rotate((float)Math.toRadians(y), new Vector3f(0, 1, 0), get(), get());
		Matrix4f.rotate((float)Math.toRadians(z), new Vector3f(0, 0, 1), get(), get());
	}
	public void rotate(float angle,float x,float y,float z){
		Matrix4f.rotate((float)Math.toRadians(angle), new Vector3f(x,y,z), get(), get());
	}
	public void rotate(Vec3M vec){rotate(vec.x, vec.y, vec.z);}
	
	public void rotate(Vector3f vec){rotate(vec.x, vec.y, vec.z);}
	public void scale(double scale){
		scale((float)scale);
	}
	public void scale(double x,double y,double z){
		scale(new Vector3f((float)x,(float)y,(float)z));
	}
	public void scale(float scale){
		scale(new Vector3f(scale, scale, scale));
	}
	public void scale(float x,float y,float z){
		scale(new Vector3f(x,y,z));
	}
	public void scale(Vec3M vec){
		get().scale(new Vector3f(vec.getX(), vec.getY(), vec.getZ()));
	}
	public void scale(Vector3f vec){
		get().scale(vec);
	}
	public void set(Matrix4f matrix){
		stack.set(stack.size()-1, matrix);
	}
	public void translate(double x,double y,double z){translate(new Vector3f((float)x, (float)y, (float)z));}
	public void translate(Vec3M vec){translate(vec.x,vec.y,vec.z);}
	public void translate(Vector3f vec){
		Matrix4f.translate(vec, get(), get());
	}
}
