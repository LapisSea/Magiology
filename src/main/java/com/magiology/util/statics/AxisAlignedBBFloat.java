package com.magiology.util.statics;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

public class AxisAlignedBBFloat{
	
	private static float[] buffer;
	private static int     pointer;
	
	public static void setFloatBuffer(float[] buffer0, int pointer0){
		buffer=buffer0;
		pointer=pointer0;
	}
	
	public static void set(float x1, float y1, float z1, float x2, float y2, float z2){
		buffer[pointer]=Math.min(x1, x2);
		buffer[pointer+1]=Math.min(y1, y2);
		buffer[pointer+2]=Math.min(z1, z2);
		buffer[pointer+3]=Math.max(x1, x2);
		buffer[pointer+4]=Math.max(y1, y2);
		buffer[pointer+5]=Math.max(z1, z2);
	}
	
	public static void setTrusted(float x1, float y1, float z1, float x2, float y2, float z2){
		buffer[pointer]=x1;
		buffer[pointer+1]=y1;
		buffer[pointer+2]=z1;
		buffer[pointer+3]=x2;
		buffer[pointer+4]=y2;
		buffer[pointer+5]=z2;
	}
	
	public static void set(Vec3i pos){
		buffer[pointer]=pos.getX();
		buffer[pointer+1]=pos.getY();
		buffer[pointer+2]=pos.getZ();
		buffer[pointer+3]=pos.getX()+1;
		buffer[pointer+4]=pos.getY()+1;
		buffer[pointer+5]=pos.getZ()+1;
	}
	
	public static void set(Vec3i pos1, Vec3i pos2){
		buffer[pointer]=pos1.getX();
		buffer[pointer+1]=pos1.getY();
		buffer[pointer+2]=pos1.getZ();
		buffer[pointer+3]=pos2.getX();
		buffer[pointer+4]=pos2.getY();
		buffer[pointer+5]=pos2.getZ();
	}
	
	public static float[] extract(){
		return new float[]{buffer[pointer], buffer[pointer+1], buffer[pointer+2], buffer[pointer+3], buffer[pointer+4], buffer[pointer+5]};
	}
	
	public static AxisAlignedBB loadToClass(){
		return new AxisAlignedBB(buffer[pointer], buffer[pointer+1], buffer[pointer+2], buffer[pointer+3], buffer[pointer+4], buffer[pointer+5]);
	}
	
	public static void setMaxY(float y){
		buffer[pointer+4]=y;
	}
	
	public static boolean equals0(Object obj){
		if(buffer==obj) return true;
		else if(obj instanceof float[]){
			float[] arr=(float[])obj;
			if(buffer.length!=arr.length) return false;
			for(int i=0; i<arr.length; i++){
				if(arr[i]!=buffer[i]) return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the coordinates to the bounding box extending it if the point lies
	 * outside the current ranges. Args: x, y, z
	 */
	public static void addCoord(float x, float y, float z){
		if(x<0) buffer[pointer]+=x;
		else if(x>0) buffer[pointer+3]+=x;
		
		if(y<0) buffer[pointer+1]+=x;
		else if(y>0) buffer[pointer+4]+=x;
		
		if(z<0) buffer[pointer+4]+=x;
		else if(z>0) buffer[pointer+5]+=x;
	}
	
	/**
	 * Returns a bounding box expanded by the specified vector(if negative
	 * numbers are given it will shrink). Args: x, y, z
	 */
	public static void expand(float x, float y, float z){
		buffer[pointer]-=x;
		buffer[pointer+1]-=y;
		buffer[pointer+2]-=z;
		buffer[pointer+3]+=x;
		buffer[pointer+4]+=y;
		buffer[pointer+5]+=z;
	}
	
	public static void expandXyz(float value){
		expand(value, value, value);
	}
	
	public static void union(float[] box2, int pointer2){
		union(box2, pointer2, buffer, pointer, buffer, pointer);
	}
	
	public static void union(float[] box1, int pointer1, float[] box2, int pointer2){
		union(box1, pointer1, box2, pointer2, buffer, pointer);
	}
	
	public static void union(float[] box1, int pointer1, float[] box2, int pointer2, float[] destination, int destinationPointer){
		destination[destinationPointer]=Math.min(box1[pointer1], box2[pointer2]);
		destination[destinationPointer+1]=Math.min(box1[pointer1+1], box2[pointer2+1]);
		destination[destinationPointer+2]=Math.min(box1[pointer1+2], box2[pointer2+2]);
		destination[destinationPointer+3]=Math.max(box1[pointer1+3], box2[pointer2+3]);
		destination[destinationPointer+4]=Math.max(box1[pointer1+4], box2[pointer2+4]);
		destination[destinationPointer+5]=Math.max(box1[pointer1+5], box2[pointer2+5]);
	}
	
	/**
	 * Offsets the current bounding box by the specified coordinates. Args: x,
	 * y, z
	 */
	public static void offset(float x, float y, float z){
		buffer[pointer]+=x;
		buffer[pointer+1]+=y;
		buffer[pointer+2]+=z;
		buffer[pointer+3]+=x;
		buffer[pointer+4]+=y;
		buffer[pointer+5]+=z;
	}
	
	public static void offset(BlockPos pos){
		buffer[pointer]+=pos.getX();
		buffer[pointer+1]+=pos.getY();
		buffer[pointer+2]+=pos.getZ();
		buffer[pointer+3]+=pos.getX();
		buffer[pointer+4]+=pos.getY();
		buffer[pointer+5]+=pos.getZ();
	}
	
	public static void minX(float[] buffer, int pointer, float var /*What is this? JavaScript? xD*/){buffer[pointer]=var;}
	
	public static void minY(float[] buffer, int pointer, float var){buffer[pointer+1]=var;}
	
	public static void minZ(float[] buffer, int pointer, float var){buffer[pointer+2]=var;}
	
	public static void maxX(float[] buffer, int pointer, float var){buffer[pointer+3]=var;}
	
	public static void maxY(float[] buffer, int pointer, float var){buffer[pointer+4]=var;}
	
	public static void maxZ(float[] buffer, int pointer, float var){buffer[pointer+5]=var;}
	
	public static float minX(float[] buffer, int pointer){return buffer[pointer];}
	
	public static float minY(float[] buffer, int pointer){return buffer[pointer+1];}
	
	public static float minZ(float[] buffer, int pointer){return buffer[pointer+2];}
	
	public static float maxX(float[] buffer, int pointer){return buffer[pointer+3];}
	
	public static float maxY(float[] buffer, int pointer){return buffer[pointer+4];}
	
	public static float maxZ(float[] buffer, int pointer){return buffer[pointer+5];}
	
	/**
	 * if instance and the argument bounding boxes overlap in the Y and Z
	 * dimensions, calculate the offset between them in the X dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public static float calculateXOffset(float[] other, int pointer0, float offsetX){
		
		if(maxY(other, pointer0)>minY(buffer, pointer)&&minY(other, pointer0)<maxY(buffer, pointer)&&maxZ(other, pointer0)>minZ(buffer, pointer)&&minZ(other, pointer0)<maxZ(buffer, pointer)){
			if(offsetX>0&&maxX(other, pointer0)<=minX(buffer, pointer)){
				float d1=minX(buffer, pointer)-maxX(other, pointer0);
				
				if(d1<offsetX){
					offsetX=d1;
				}
			}else if(offsetX<0&&minX(other, pointer0)>=maxX(buffer, pointer)){
				float d0=maxX(buffer, pointer)-minX(other, pointer0);
				
				if(d0>offsetX){
					offsetX=d0;
				}
			}
			
			return offsetX;
		}else{
			return offsetX;
		}
	}
	
	/**
	 * if instance and the argument bounding boxes overlap in the X and Z
	 * dimensions, calculate the offset between them in the Y dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public static float calculateYOffset(float[] other, int pointer0, float offsetY){
		if(maxX(other, pointer0)>minX(buffer, pointer)&&minX(other, pointer0)<maxX(buffer, pointer)&&maxZ(other, pointer0)>minZ(buffer, pointer)&&minZ(other, pointer0)<maxZ(buffer, pointer)){
			if(offsetY>0&&maxY(other, pointer0)<=minY(buffer, pointer)){
				float d1=minY(buffer, pointer)-maxY(other, pointer0);
				
				if(d1<offsetY){
					offsetY=d1;
				}
			}else if(offsetY<0&&minY(other, pointer0)>=maxY(buffer, pointer)){
				float d0=maxY(buffer, pointer)-minY(other, pointer0);
				
				if(d0>offsetY){
					offsetY=d0;
				}
			}
			
			return offsetY;
		}else{
			return offsetY;
		}
	}
	
	/**
	 * if instance and the argument bounding boxes overlap in the Y and X
	 * dimensions, calculate the offset between them in the Z dimension. return
	 * var2 if the bounding boxes do not overlap or if var2 is closer to 0 then
	 * the calculated offset. Otherwise return the calculated offset.
	 */
	public static float calculateZOffset(float[] other, int pointer0, float offsetZ){
		if(maxX(other, pointer0)>minX(buffer, pointer)&&minX(other, pointer0)<maxX(buffer, pointer)&&maxY(other, pointer0)>minY(buffer, pointer)&&minY(other, pointer0)<maxY(buffer, pointer)){
			if(offsetZ>0&&maxZ(other, pointer0)<=minZ(buffer, pointer)){
				float d1=minZ(buffer, pointer)-maxZ(other, pointer0);
				
				if(d1<offsetZ){
					offsetZ=d1;
				}
			}else if(offsetZ<0&&minZ(other, pointer0)>=maxZ(buffer, pointer)){
				float d0=maxZ(buffer, pointer)-minZ(other, pointer0);
				
				if(d0>offsetZ){
					offsetZ=d0;
				}
			}
			
			return offsetZ;
		}else{
			return offsetZ;
		}
	}
	
	/**
	 * Returns whether the given bounding box intersects with this one. Args:
	 * axisAlignedBB
	 */
	public static boolean intersectsWith(float[] other, int pointer0){
		return intersects(minX(other, pointer0), minY(other, pointer0), minZ(other, pointer0), maxX(other, pointer0), maxY(other, pointer0), maxZ(other, pointer0));
	}
	
	public static boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2){
		return minX(buffer, pointer)<x2&&maxX(buffer, pointer)>x1&&minY(buffer, pointer)<y2&&maxY(buffer, pointer)>y1&&minZ(buffer, pointer)<z2&&maxZ(buffer, pointer)>z1;
	}
	
	/**
	 * Returns if the supplied Vec3D is completely inside the bounding box
	 */
	public static boolean isVecInside(Vec3d vec){
		return vec.x>minX(buffer, pointer)&&vec.x<maxX(buffer, pointer)?vec.y>minY(buffer, pointer)&&vec.y<maxY(buffer, pointer)?vec.z>minZ(buffer, pointer)&&vec.z<maxZ(buffer, pointer):false:false;
	}
	
	/**
	 * Returns the average length of the edges of the bounding box.
	 */
	public static float getAverageEdgeLength(){
		return (maxX(buffer, pointer)-minX(buffer, pointer)+maxY(buffer, pointer)-minY(buffer, pointer)+maxZ(buffer, pointer)-minZ(buffer, pointer))/3;
	}
	
	public static void shrink(float value){
		expandXyz(-value);
	}
	
	public static RayTraceResult calculateIntercept(Vec3d vecA, Vec3d vecB){
		Vec3d vec3d=func_186671_a(minX(buffer, pointer), vecA, vecB);
		EnumFacing enumfacing=EnumFacing.WEST;
		Vec3d vec3d1=func_186671_a(maxX(buffer, pointer), vecA, vecB);
		
		if(vec3d1!=null&&func_186661_a(vecA, vec3d, vec3d1)){
			vec3d=vec3d1;
			enumfacing=EnumFacing.EAST;
		}
		
		vec3d1=func_186663_b(minY(buffer, pointer), vecA, vecB);
		
		if(vec3d1!=null&&func_186661_a(vecA, vec3d, vec3d1)){
			vec3d=vec3d1;
			enumfacing=EnumFacing.DOWN;
		}
		
		vec3d1=func_186663_b(maxY(buffer, pointer), vecA, vecB);
		
		if(vec3d1!=null&&func_186661_a(vecA, vec3d, vec3d1)){
			vec3d=vec3d1;
			enumfacing=EnumFacing.UP;
		}
		
		vec3d1=func_186665_c(minZ(buffer, pointer), vecA, vecB);
		
		if(vec3d1!=null&&func_186661_a(vecA, vec3d, vec3d1)){
			vec3d=vec3d1;
			enumfacing=EnumFacing.NORTH;
		}
		
		vec3d1=func_186665_c(maxZ(buffer, pointer), vecA, vecB);
		
		if(vec3d1!=null&&func_186661_a(vecA, vec3d, vec3d1)){
			vec3d=vec3d1;
			enumfacing=EnumFacing.SOUTH;
		}
		
		return vec3d==null?null:new RayTraceResult(vec3d, enumfacing);
	}
	
	static boolean func_186661_a(Vec3d p_186661_1_, Vec3d p_186661_2_, Vec3d p_186661_3_){
		return p_186661_2_==null||p_186661_1_.squareDistanceTo(p_186661_3_)<p_186661_1_.squareDistanceTo(p_186661_2_);
	}
	
	static Vec3d func_186671_a(float p_186671_1_, Vec3d p_186671_3_, Vec3d p_186671_4_){
		Vec3d vec3d=p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
		return vec3d!=null&&intersectsWithYZ(vec3d)?vec3d:null;
	}
	
	static Vec3d func_186663_b(float p_186663_1_, Vec3d p_186663_3_, Vec3d p_186663_4_){
		Vec3d vec3d=p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
		return vec3d!=null&&intersectsWithXZ(vec3d)?vec3d:null;
	}
	
	static Vec3d func_186665_c(float p_186665_1_, Vec3d p_186665_3_, Vec3d p_186665_4_){
		Vec3d vec3d=p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
		return vec3d!=null&&intersectsWithXY(vec3d)?vec3d:null;
	}
	
	public static boolean intersectsWithYZ(Vec3d vec){
		return vec.y>=minY(buffer, pointer)&&vec.y<=maxY(buffer, pointer)&&vec.z>=minZ(buffer, pointer)&&vec.z<=maxZ(buffer, pointer);
	}
	
	public static boolean intersectsWithXZ(Vec3d vec){
		return vec.x>=minX(buffer, pointer)&&vec.x<=maxX(buffer, pointer)&&vec.z>=minZ(buffer, pointer)&&vec.z<=maxZ(buffer, pointer);
	}
	
	public static boolean intersectsWithXY(Vec3d vec){
		return vec.x>=minX(buffer, pointer)&&vec.x<=maxX(buffer, pointer)&&vec.y>=minY(buffer, pointer)&&vec.y<=maxY(buffer, pointer);
	}
	
	public static String buffToString(){
		return "box["+minX(buffer, pointer)+", "+minY(buffer, pointer)+", "+minZ(buffer, pointer)+" -> "+maxX(buffer, pointer)+", "+maxY(buffer, pointer)+", "+maxZ(buffer, pointer)+"]";
	}
	
	public static boolean hasNaN(){
		return Float.isNaN(minX(buffer, pointer))||Float.isNaN(minY(buffer, pointer))||Float.isNaN(minZ(buffer, pointer))||Float.isNaN(maxX(buffer, pointer))||Float.isNaN(maxY(buffer, pointer))||Float.isNaN(maxZ(buffer, pointer));
	}
}
