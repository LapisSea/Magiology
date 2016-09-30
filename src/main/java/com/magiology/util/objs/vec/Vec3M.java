package com.magiology.util.objs.vec;

import java.io.Serializable;

import com.magiology.util.interf.Calculable;
import com.magiology.util.statics.math.*;

import org.lwjgl.util.vector.*;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.relauncher.*;

public class Vec3M extends BazeVec3M implements Serializable, WritableVector3f, Calculable<Vec3M>{

	
	public Vec3M(){
		super();
	}

	public Vec3M(double x,double y,double z){
		super(x,y,z);
	}

	public Vec3M(Vec3d vec){
		super(vec);
	}

	public Vec3M(Vec3i vec){
		super(vec);
	}
	public void setX(double x){
		this.x=x;
	}

	public void setY(double y){
		this.y=y;
	}

	public void setZ(double z){
		this.z=z;
	}

	public Vec3M abs(){
		return new Vec3M(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public Vec3M add(BlockPos pos){
		return new Vec3M(x()+pos.getX(), y()+pos.getY(), z()+pos.getZ());
	}
	
	public Vec3M add(double x, double y, double z){
		return new Vec3M(this.x+x, this.y+y, this.z+z);
	}
	
	@Override
	public Vec3M add(float var){
		return this.add(var, var, var);
	}
	
	public Vec3M add(Vec3i vec){
		return add(vec.getX(), vec.getY(), vec.getZ());
	}
	
	@Override
	public Vec3M add(Vec3M vec){
		return this.add(vec.x, vec.y, vec.z);
	}
	
	
	public Vec3M addSelf(double x, double y, double z){
		this.x+=x;
		this.y+=y;
		this.z+=z;
		return this;
	}
	
	public Vec3M addSelf(float var){
		this.x+=var;
		this.y+=var;
		this.z+=var;
		return this;
	}
	
	public Vec3M addSelf(Vec3i vec){
		this.x+=vec.getX();
		this.y+=vec.getY();
		this.z+=vec.getZ();
		return this;
	}
	
	public Vec3M addSelf(Vec3M vec){
		this.x+=vec.x;
		this.y+=vec.y;
		this.z+=vec.z;
		return this;
	}

	public Vec3M addX(double var){
		return new Vec3M(x+var, y, z);
	}
	
	public Vec3M addY(double var){
		return new Vec3M(x, y+var, z);
	}
	
	public Vec3M addZ(double var){
		return new Vec3M(x, y, z+var);
	}

	public Vec3M addSelfX(double var){
		this.x+=var;
		return this;
	}
	
	public Vec3M addSelfY(double var){
		this.y+=var;
		return this;
	}
	
	public Vec3M addSelfZ(double var){
		this.z+=var;
		return this;
	}

	public Vec3M mulX(double var){
		return new Vec3M(x*var, y, z);
	}
	
	public Vec3M mulY(double var){
		return new Vec3M(x, y*var, z);
	}
	
	public Vec3M mulZ(double var){
		return new Vec3M(x, y, z*var);
	}

	public Vec3M mulSelfX(double var){
		this.x*=var;
		return this;
	}
	
	public Vec3M mulSelfY(double var){
		this.y*=var;
		return this;
	}
	
	public Vec3M mulSelfZ(double var){
		this.z*=var;
		return this;
	}
	
	public Vec3M divX(double var){
		return new Vec3M(x/var, y, z);
	}
	
	public Vec3M divY(double var){
		return new Vec3M(x, y/var, z);
	}
	
	public Vec3M divZ(double var){
		return new Vec3M(x, y, z/var);
	}

	public Vec3M divSelfX(double var){
		this.x/=var;
		return this;
	}
	
	public Vec3M divSelfY(double var){
		this.y/=var;
		return this;
	}
	
	public Vec3M divSelfZ(double var){
		this.z/=var;
		return this;
	}

	public Vec3M subX(double var){
		return new Vec3M(x-var, y, z);
	}
	
	public Vec3M subY(double var){
		return new Vec3M(x, y-var, z);
	}
	
	public Vec3M subZ(double var){
		return new Vec3M(x, y, z-var);
	}

	public Vec3M subSelfX(double var){
		this.x-=var;
		return this;
	}
	
	public Vec3M subSelfY(double var){
		this.y-=var;
		return this;
	}
	
	public Vec3M subSelfZ(double var){
		this.z-=var;
		return this;
	}
	
	public Vec3d conv(){
		return new Vec3d(x(), y(), z());
	}
	
	@Override
	public Vec3M copy(){
		return new Vec3M(x, y, z);
	}
	
	public Vec3M crossProduct(Vec3M vec){
		return new Vec3M(this.y*vec.z-this.z*vec.y, this.z*vec.x-this.x*vec.z, this.x*vec.y-this.y*vec.x);
	}
	
	public double distanceTo(double x, double y, double z){
		double d0=x-this.x;
		double d1=y-this.y;
		double d2=z-this.z;
		return MathHelper.sqrt_double(d0*d0+d1*d1+d2*d2);
	}
	
	public float distanceTo(Vec3d vec){
		double d0=vec.xCoord-this.x;
		double d1=vec.yCoord-this.y;
		double d2=vec.zCoord-this.z;
		return MathHelper.sqrt_double(d0*d0+d1*d1+d2*d2);
	}
	
	public double distanceTo(Vec3i pos){
		double d0=pos.getX()-this.x;
		double d1=pos.getY()-this.y;
		double d2=pos.getZ()-this.z;
		return MathHelper.sqrt_double(d0*d0+d1*d1+d2*d2);
	}
	
	public double distanceTo(Vec3M vec){
		double d0=vec.x-this.x;
		double d1=vec.y-this.y;
		double d2=vec.z-this.z;
		return MathHelper.sqrt_double(d0*d0+d1*d1+d2*d2);
	}
	public double distanceTo(ReadableVector3f vec){
		double d0=vec.getX()-this.x;
		double d1=vec.getY()-this.y;
		double d2=vec.getZ()-this.z;
		return MathHelper.sqrt_double(d0*d0+d1*d1+d2*d2);
	}
	
	@Override
	public Vec3M div(float var){
		return this.div(new Vec3M(var, var, var));
	}
	
	@Override
	public Vec3M div(Vec3M var){
		return new Vec3M(x/var.x, y/var.y, z/var.z);
	}
	
	public double dotProduct(Vec3M vec){
		return this.x*vec.x+this.y*vec.x+this.z*vec.x;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Vec3M)) return false;
		Vec3M vec=(Vec3M)obj;
		if(vec==this) return true;
		return vec.x==x&&vec.y==y&&vec.z==z;
	}
	
	public Vec3M getIntermediateWithXValue(Vec3M vec, double x){
		double d1=vec.x-this.x;
		double d2=vec.x-this.y;
		double d3=vec.x-this.z;
		if(d1*d1<1.0000000116860974E-7D){
			return null;
		}else{
			double d4=(x-this.x)/d1;
			return d4>=0.0D&&d4<=1.0D?new Vec3M(this.x+d1*d4, this.y+d2*d4, this.z+d3*d4):null;
		}
	}
	
	public Vec3M getIntermediateWithYValue(Vec3M vec, double y){
		double d1=vec.x-this.x;
		double d2=vec.x-this.y;
		double d3=vec.x-this.z;
		if(d2*d2<1.0000000116860974E-7D){
			return null;
		}else{
			double d4=(y-this.y)/d2;
			return d4>=0.0D&&d4<=1.0D?new Vec3M(this.x+d1*d4, this.y+d2*d4, this.z+d3*d4):null;
		}
	}
	
	public Vec3M getIntermediateWithZValue(Vec3M vec, double z){
		double d1=vec.x-this.x;
		double d2=vec.x-this.y;
		double d3=vec.x-this.z;
		if(d3*d3<1.0000000116860974E-7D){
			return null;
		}else{
			double d4=(z-this.z)/d3;
			return d4>=0.0D&&d4<=1.0D?new Vec3M(this.x+d1*d4, this.y+d2*d4, this.z+d3*d4):null;
		}
	}
	
	
	public double lengthVector(){
		return MathHelper.sqrt_double(this.x*this.x+this.y*this.y+this.z*this.z);
	}
	
	public double lightProduct(Vec3M vec){
		return 1-MathUtil.snap(normalize().distanceTo(vec.normalize()), 0, 1);
	}
	
	
	public Vec3M max(Vec3M other){
		return new Vec3M(Math.max(x, other.x), Math.max(y, other.y), Math.max(z, other.z));
	}
	
	public Vec3M min(Vec3M other){
		return new Vec3M(Math.min(x, other.x), Math.min(y, other.y), Math.min(z, other.z));
	}
	
	public Vec3M mul(double value){
		return new Vec3M(x()*value, y()*value, z()*value);
	}
	
	public Vec3M mul(double x, double y, double z){
		return new Vec3M(x()*x, y()*y, z()*z);
	}
	
	@Override
	public Vec3M mul(float var){
		return this.mul(var, var, var);
	}
	
	@Override
	public Vec3M mul(Vec3M vec){
		return new Vec3M(x()*vec.x(), y()*vec.y(), z()*vec.z());
	}
	
	public Vec3M offset(EnumFacing facing){
		return offset(facing, 1);
	}
	
	public Vec3M offset(EnumFacing facing, float mul){
		return new Vec3M(x+facing.getFrontOffsetX()*mul, y+facing.getFrontOffsetY()*mul, z+facing.getFrontOffsetZ()*mul);
	}
	
	public Vec3M reflect(Vec3M normal){
		Vec3M norm=normal.normalize();
		Vec3M base=normalize();
		Vec3M difference=base.sub(norm);
		Matrix4f rot=new Matrix4f();
		rot.rotate((float)Math.PI, norm.toLWJGLVec());
		difference=MatrixUtil.transformVector(difference, rot);
		return norm.add(difference);
	}
	
	public void rotateAroundX(float par1){
		float f1=MathHelper.cos(par1);
		float f2=MathHelper.sin(par1);
		double d0=this.x;
		double d1=this.y*f1+this.z*f2;
		double d2=this.z*f1-this.y*f2;
		this.x=d0;
		this.y=d1;
		this.z=d2;
	}
	
	/**
	 * Rotates the vector around the y axis by the specified angle.
	 */
	public void rotateAroundY(float par1){
		float f1=MathHelper.cos(par1);
		float f2=MathHelper.sin(par1);
		double d0=this.x*f1+this.z*f2;
		double d1=this.y;
		double d2=this.z*f1-this.x*f2;
		this.x=d0;
		this.y=d1;
		this.z=d2;
	}
	
	@SideOnly(Side.CLIENT)
	/**
	 * Rotates the vector around the z axis by the specified angle.
	 */
	public void rotateAroundZ(float par1){
		float f1=MathHelper.cos(par1);
		float f2=MathHelper.sin(par1);
		double d0=this.x*f1+this.y*f2;
		double d1=this.y*f1-this.x*f2;
		double d2=this.z;
		this.x=d0;
		this.y=d1;
		this.z=d2;
	}
	
	public Vec3M rotatePitch(float pitch){
		float f1=MathHelper.cos(pitch);
		float f2=MathHelper.sin(pitch);
		double d0=this.x;
		double d1=this.y*f1+this.z*f2;
		double d2=this.z*f1-this.y*f2;
		return new Vec3M(d0, d1, d2);
	}
	
	public Vec3M rotateYaw(float yaw){
		float f1=MathHelper.cos(yaw);
		float f2=MathHelper.sin(yaw);
		double d0=this.x*f1+this.z*f2;
		double d1=this.y;
		double d2=this.z*f1-this.x*f2;
		return new Vec3M(d0, d1, d2);
	}
	
	@Override
	public void set(float x, float y){
		setX(x);
		setY(y);
	}
	public void set(double x, double y){
		setX(x);
		setY(y);
	}

	public void set(Vec3M vec){
		x=vec.x;
		y=vec.y;
		z=vec.z;
	}
	public void set(ReadableVector3f vec){
		x=vec.getX();
		y=vec.getY();
		z=vec.getZ();
	}
	
	@Override
	public void set(float x, float y, float z){
		set(x, y);
		setZ(z);
	}
	
	@Override
	public void setX(float x){
		this.x=x;
	}
	
	@Override
	public void setY(float y){
		this.y=y;
	}
	
	@Override
	public void setZ(float z){
		this.z=z;
	}
	
	public void set(double x, double y, double z){
		set(x, y);
		setZ(z);
	}
	
	
	public Vec3M sqrt(){
		int x1=MathUtil.getNumPrefix(x), y1=MathUtil.getNumPrefix(y), z1=MathUtil.getNumPrefix(z);
		return new Vec3M(Math.sqrt(x*x1)*x1, Math.sqrt(y*y1)*y1, Math.sqrt(z*z1)*z1);
	}
	
	public double squareDistanceTo(Vec3M vec){
		double d0=vec.x-this.x;
		double d1=vec.x-this.y;
		double d2=vec.x-this.z;
		return d0*d0+d1*d1+d2*d2;
	}
	
	
	public Vec3M sub(double x, double y, double z){
		return this.add(-x, -y, -z);
	}

	@Override
	public Vec3M sub(float var){
		return this.sub(var, var, var);
	}
	
	public Vec3M sub(Vec3i vec){
		return sub(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public Vec3M subSelf(double x, double y, double z){
		return add(-x, -y, -z);
	}
	public Vec3M subSelf(float var){
		return this.sub(var, var, var);
	}
	
	public Vec3M subSelf(Vec3i vec){
		return sub(vec.getX(), vec.getY(), vec.getZ());
	}
	
	
	@Override
	public Vec3M sub(Vec3M vec){
		return this.sub(vec.x, vec.y, vec.z);
	}
	
	public Vec3M subtractReverse(double x, double y, double z){
		return new Vec3M(x-this.x, x-this.y, x-this.z);
	}
	
	public Vec3M subtractReverse(Vec3M vec){
		return new Vec3M(vec.x-this.x, vec.y-this.y, vec.z-this.z);
	}
	
	public Vector3f toLWJGLVec(){
		return new Vector3f(getX(), getY(), getZ());
	}
	
	@Override
	public String toString(){
		return "("+this.x+", "+this.y+", "+this.z+")";
	}
	
	public Vec3M transform(Matrix4f matrix){
		return MatrixUtil.transformVector(this, matrix);
	}
	
	public Vec3M transformSelf(Matrix4f matrix){
		Vec3M src=transform(matrix);
		x=src.x;
		y=src.y;
		z=src.z;
		return this;
	}
}
