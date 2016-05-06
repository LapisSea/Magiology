package com.magiology.util.utilobjects.m_extension;

import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AxisAlignedBBM{
	
	public static AxisAlignedBBM conv(AxisAlignedBB cube){
		return new AxisAlignedBBM(cube.minX,cube.minY,cube.minZ,cube.maxX,cube.maxY,cube.maxZ);
	}
	
	public static AxisAlignedBBM conv(AxisAlignedBBM cube){
		return new AxisAlignedBBM(cube.minX,cube.minY,cube.minZ,cube.maxX,cube.maxY,cube.maxZ);
	}
	
	public static AxisAlignedBBM fromBounds(double x1,double y1,double z1,double x2,double y2,double z2){
		double d0=Math.min(x1,x2);
		double d1=Math.min(y1,y2);
		double d2=Math.min(z1,z2);
		double d3=Math.max(x1,x2);
		double d4=Math.max(y1,y2);
		double d5=Math.max(z1,z2);
		return new AxisAlignedBBM(d0,d1,d2,d3,d4,d5);
	}

	public static AxisAlignedBBM fromBounds(Vec3M vec1, Vec3M vec2){
		return fromBounds(vec1.x,vec1.y,vec1.z,vec2.x,vec2.y,vec2.z);
	}
	public static AxisAlignedBBM fullBlock(){
		return new AxisAlignedBBM(0,0,0,1,1,1);
	}
	
	public double minX,minY,minZ,maxX,maxY,maxZ;
	public AxisAlignedBBM(BlockPos pos1,BlockPos pos2){
		this.minX=pos1.getX();
		this.minY=pos1.getY();
		this.minZ=pos1.getZ();
		this.maxX=pos2.getX();
		this.maxY=pos2.getY();
		this.maxZ=pos2.getZ();
	}
	public AxisAlignedBBM(double x1,double y1,double z1,double x2,double y2,double z2){
		this.minX=Math.min(x1,x2);
		this.minY=Math.min(y1,y2);
		this.minZ=Math.min(z1,z2);
		this.maxX=Math.max(x1,x2);
		this.maxY=Math.max(y1,y2);
		this.maxZ=Math.max(z1,z2);
	}
	public AxisAlignedBBM(Vec3M pos1,Vec3M pos2){
		this(pos1.x,pos1.y,pos1.z,pos2.x,pos2.y,pos2.z);
	}
	public AxisAlignedBBM addCoord(double x,double y,double z){
		double d0=this.minX;
		double d1=this.minY;
		double d2=this.minZ;
		double d3=this.maxX;
		double d4=this.maxY;
		double d5=this.maxZ;
		
		if(x<0.0D)d0+=x;
		else if(x>0.0D)d3+=x;
		if(y<0.0D)d1+=y;
		else if(y>0.0D)d4+=y;
		if(z<0.0D)d2+=z;
		else if(z>0.0D)d5+=z;
		
		return new AxisAlignedBBM(d0,d1,d2,d3,d4,d5);
	}
	public AxisAlignedBBM addCoord(Vec3i vec){
		return addCoord(vec.getX(),vec.getY(),vec.getZ());
	}
	
	public AxisAlignedBBM addCoord(Vec3M vec){
		return addCoord(vec.x,vec.y,vec.z);
	}
	public RayTraceResult calculateIntercept(Vec3M vecA,Vec3M vecB){
		Vec3M 
			vec3=vecA.getIntermediateWithXValue(vecB,this.minX),
			vec31=vecA.getIntermediateWithXValue(vecB,this.maxX),
			vec32=vecA.getIntermediateWithYValue(vecB,this.minY),
			vec33=vecA.getIntermediateWithYValue(vecB,this.maxY),
			vec34=vecA.getIntermediateWithZValue(vecB,this.minZ),
			vec35=vecA.getIntermediateWithZValue(vecB,this.maxZ);
		
		if(!this.isVecInYZ(vec3))vec3=null;
		if(!this.isVecInYZ(vec31))vec31=null;
		if(!this.isVecInXZ(vec32))vec32=null;
		if(!this.isVecInXZ(vec33))vec33=null;
		if(!this.isVecInXY(vec34))vec34=null;
		if(!this.isVecInXY(vec35))vec35=null;

		Vec3M vec36=null;
		
		if(vec3!=null)vec36=vec3;
		if(vec31!=null&&(vec36==null||vecA.squareDistanceTo(vec31)<vecA.squareDistanceTo(vec36)))vec36=vec31;
		if(vec32!=null&&(vec36==null||vecA.squareDistanceTo(vec32)<vecA.squareDistanceTo(vec36)))vec36=vec32;
		if(vec33!=null&&(vec36==null||vecA.squareDistanceTo(vec33)<vecA.squareDistanceTo(vec36)))vec36=vec33;
		if(vec34!=null&&(vec36==null||vecA.squareDistanceTo(vec34)<vecA.squareDistanceTo(vec36)))vec36=vec34;
		if(vec35!=null&&(vec36==null||vecA.squareDistanceTo(vec35)<vecA.squareDistanceTo(vec36)))vec36=vec35;
		if(vec36==null)return null;
		else{
			EnumFacing enumfacing=null;

			if(vec36==vec3)enumfacing=EnumFacing.WEST;
			else if(vec36==vec31)enumfacing=EnumFacing.EAST;
			else if(vec36==vec32)enumfacing=EnumFacing.DOWN;
			else if(vec36==vec33)enumfacing=EnumFacing.UP;
			else if(vec36==vec34)enumfacing=EnumFacing.NORTH;
			else enumfacing=EnumFacing.SOUTH;

			return new RayTraceResult(vec36.conv(),enumfacing);
		}
	}
	public double calculateXOffset(AxisAlignedBB other,double offsetX){
		if(other.maxY>this.minY&&other.minY<this.maxY&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
			if(offsetX>0.0D&&other.maxX<=this.minX){
				double d1=this.minX-other.maxX;

				if(d1<offsetX){
					offsetX=d1;
				}
			}else if(offsetX<0.0D&&other.minX>=this.maxX){
				double d0=this.maxX-other.minX;

				if(d0>offsetX){
					offsetX=d0;
				}
			}

			return offsetX;
		}else{
			return offsetX;
		}
	}

	public double calculateXOffset(AxisAlignedBBM other,double offsetX){
		if(other.maxY>this.minY&&other.minY<this.maxY&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
			if(offsetX>0.0D&&other.maxX<=this.minX){
				double d1=this.minX-other.maxX;
				
				if(d1<offsetX){
					offsetX=d1;
				}
			}else if(offsetX<0.0D&&other.minX>=this.maxX){
				double d0=this.maxX-other.minX;
				
				if(d0>offsetX){
					offsetX=d0;
				}
			}
			
			return offsetX;
		}else{
			return offsetX;
		}
	}
	public double calculateYOffset(AxisAlignedBB other,double offsetY){
		if(other.maxX>this.minX&&other.minX<this.maxX&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
			if(offsetY>0.0D&&other.maxY<=this.minY){
				double d1=this.minY-other.maxY;
				
				if(d1<offsetY){
					offsetY=d1;
				}
			}else if(offsetY<0.0D&&other.minY>=this.maxY){
				double d0=this.maxY-other.minY;
				
				if(d0>offsetY){
					offsetY=d0;
				}
			}
			return offsetY;
		}else return offsetY;
	}
	
	public double calculateYOffset(AxisAlignedBBM other,double offsetY){
		if(other.maxX>this.minX&&other.minX<this.maxX&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
			if(offsetY>0.0D&&other.maxY<=this.minY){
				double d1=this.minY-other.maxY;
				
				if(d1<offsetY){
					offsetY=d1;
				}
			}else if(offsetY<0.0D&&other.minY>=this.maxY){
				double d0=this.maxY-other.minY;
				
				if(d0>offsetY){
					offsetY=d0;
				}
			}
			return offsetY;
		}else return offsetY;
	}
	public double calculateZOffset(AxisAlignedBB other,double offsetZ){
		if(other.maxX>this.minX&&other.minX<this.maxX&&other.maxY>this.minY&&other.minY<this.maxY){
			if(offsetZ>0.0D&&other.maxZ<=this.minZ){
				double d1=this.minZ-other.maxZ;
				
				if(d1<offsetZ){
					offsetZ=d1;
				}
			}else if(offsetZ<0.0D&&other.minZ>=this.maxZ){
				double d0=this.maxZ-other.minZ;
				
				if(d0>offsetZ){
					offsetZ=d0;
				}
			}
			
			return offsetZ;
		}else return offsetZ;
	}
	
	public double calculateZOffset(AxisAlignedBBM other,double offsetZ){
		if(other.maxX>this.minX&&other.minX<this.maxX&&other.maxY>this.minY&&other.minY<this.maxY){
			if(offsetZ>0.0D&&other.maxZ<=this.minZ){
				double d1=this.minZ-other.maxZ;
				
				if(d1<offsetZ){
					offsetZ=d1;
				}
			}else if(offsetZ<0.0D&&other.minZ>=this.maxZ){
				double d0=this.maxZ-other.minZ;
				
				if(d0>offsetZ){
					offsetZ=d0;
				}
			}
			
			return offsetZ;
		}else return offsetZ;
	}
	public AxisAlignedBBM combine(AxisAlignedBB box){
		double 
			minX=MathUtil.min(this.minX,this.maxX,box.minX,box.maxX),
			minY=MathUtil.min(this.minY,this.maxY,box.minY,box.maxY),
			minZ=MathUtil.min(this.minZ,this.maxZ,box.minZ,box.maxZ),
			maxX=MathUtil.max(this.minX,this.maxX,box.minX,box.maxX),
			maxY=MathUtil.max(this.minY,this.maxY,box.minY,box.maxY),
			maxZ=MathUtil.max(this.minZ,this.maxZ,box.minZ,box.maxZ);
		
		return new AxisAlignedBBM(minX,minY,minZ,maxX,maxY,maxZ);
	}
	
	public AxisAlignedBBM combine(AxisAlignedBBM box){
		double 
			minX=MathUtil.min(this.minX,this.maxX,box.minX,box.maxX),
			minY=MathUtil.min(this.minY,this.maxY,box.minY,box.maxY),
			minZ=MathUtil.min(this.minZ,this.maxZ,box.minZ,box.maxZ),
			maxX=MathUtil.max(this.minX,this.maxX,box.minX,box.maxX),
			maxY=MathUtil.max(this.minY,this.maxY,box.minY,box.maxY),
			maxZ=MathUtil.max(this.minZ,this.maxZ,box.minZ,box.maxZ);
		
		return new AxisAlignedBBM(minX,minY,minZ,maxX,maxY,maxZ);
	}
	public AxisAlignedBB contract(double x,double y,double z){
		double d0=this.minX+x;
		double d1=this.minY+y;
		double d2=this.minZ+z;
		double d3=this.maxX-x;
		double d4=this.maxY-y;
		double d5=this.maxZ-z;
		return new AxisAlignedBB(d0,d1,d2,d3,d4,d5);
	}
	
	public AxisAlignedBB contract(Vec3M vec){
		return contract(vec.x,vec.y,vec.z);
	}
	public AxisAlignedBB conv(){
		return new AxisAlignedBB(minX,minY,minZ,maxX,maxY,maxZ);
	}
	
	@Override
	public boolean equals(Object obj){
		if(this==obj)return true;
		if(obj instanceof AxisAlignedBB){
			AxisAlignedBB box=(AxisAlignedBB)obj;
			return this.minX==box.minX&&this.minY==box.minY&&this.minZ==box.minZ&&this.maxX==box.maxX&&this.maxY==box.maxY&&this.maxZ==box.maxZ;
		}
		if(obj instanceof AxisAlignedBBM){
			AxisAlignedBBM box=(AxisAlignedBBM)obj;
			return this.minX==box.minX&&this.minY==box.minY&&this.minZ==box.minZ&&this.maxX==box.maxX&&this.maxY==box.maxY&&this.maxZ==box.maxZ;
		}
		return false; 
	}
	public AxisAlignedBBM expand(double xyz){
		return expand(xyz,xyz,xyz);
	}
	public AxisAlignedBBM expand(double x,double y,double z){
		double d0=this.minX-x;
		double d1=this.minY-y;
		double d2=this.minZ-z;
		double d3=this.maxX+x;
		double d4=this.maxY+y;
		double d5=this.maxZ+z;
		return new AxisAlignedBBM(d0,d1,d2,d3,d4,d5);
	}
	public AxisAlignedBBM expand(Vec3M vec){
		return expand(vec.x,vec.y,vec.z);
	}
	
	public AxisAlignedBBM fix(){
		this.minX=Math.min(minX,maxX);
		this.minY=Math.min(minY,maxY);
		this.minZ=Math.min(minZ,maxZ);
		this.maxX=Math.max(minX,maxX);
		this.maxY=Math.max(minY,maxY);
		this.maxZ=Math.max(minZ,maxZ);
		return this;
	}
	
	public double getAverageEdgeLength(){
		double d0=this.maxX-this.minX;
		double d1=this.maxY-this.minY;
		double d2=this.maxZ-this.minZ;
		return (d0+d1+d2)/3.0D;
	}
	
	public double getDepth(){
		return maxZ-minZ;
	}
	
	public double getHeight(){
		return maxY-minY;
	}
	public double getVolume(){
		return getWidth()*getHeight()*getDepth();
	}

	public double getWidth(){
		return maxX-minX;
	}
	
	public boolean intersectsWith(AxisAlignedBB other){
		return other.maxX>this.minX&&other.minX<this.maxX?(other.maxY>this.minY&&other.minY<this.maxY?other.maxZ>this.minZ&&other.minZ<this.maxZ:false):false;
	}
	
	public boolean intersectsWith(AxisAlignedBBM other){
		return other.maxX>this.minX&&other.minX<this.maxX?(other.maxY>this.minY&&other.minY<this.maxY?other.maxZ>this.minZ&&other.minZ<this.maxZ:false):false;
	}
	
	public boolean isValid(){
		return 
			MathUtil.isNumValid(this.minX)||
			MathUtil.isNumValid(this.minY)||
			MathUtil.isNumValid(this.minZ)||
			MathUtil.isNumValid(this.maxX)||
			MathUtil.isNumValid(this.maxY)||
			MathUtil.isNumValid(this.maxZ);
	}

	public boolean isVecInside(Vec3d vec){
		return vec.xCoord>this.minX&&vec.xCoord<this.maxX?(vec.yCoord>this.minY&&vec.yCoord<this.maxY?vec.zCoord>this.minZ&&vec.zCoord<this.maxZ:false):false;
	}

	public boolean isVecInside(Vec3M vec){
		return vec.x>this.minX&&vec.x<this.maxX?(vec.y>this.minY&&vec.y<this.maxY?vec.z>this.minZ&&vec.z<this.maxZ:false):false;
	}
	
	private boolean isVecInXY(Vec3M vec){
		return vec==null?false:vec.x>=this.minX&&vec.x<=this.maxX&&vec.y>=this.minY&&vec.y<=this.maxY;
	}
	
	private boolean isVecInXZ(Vec3M vec){
		return vec==null?false:vec.x>=this.minX&&vec.x<=this.maxX&&vec.z>=this.minZ&&vec.z<=this.maxZ;
	}
	private boolean isVecInYZ(Vec3M vec){
		return vec==null?false:vec.y>=this.minY&&vec.y<=this.maxY&&vec.z>=this.minZ&&vec.z<=this.maxZ;
	}
	public AxisAlignedBBM offset(double x,double y,double z){
		return new AxisAlignedBBM(this.minX+x,this.minY+y,this.minZ+z,this.maxX+x,this.maxY+y,this.maxZ+z);
	}
	public AxisAlignedBBM offset(Vec3M vec){
		return offset(vec.x, vec.y, vec.z);
	}
	@Override
	public String toString(){
		return "box["+this.minX+", "+this.minY+", "+this.minZ+" -> "+this.maxX+", "+this.maxY+", "+this.maxZ+"]";
	}
	public AxisAlignedBBM translate(double x,double y,double z){
		return new AxisAlignedBBM(minX+x,minY+y,minZ+z,maxX+x,maxY+y,maxZ+z);
	}
	public AxisAlignedBBM translate(Vec3i vec){
		return translate(vec.getX(),vec.getY(),vec.getZ());
	}
	public AxisAlignedBBM translate(Vec3M vec){
		return translate(vec.x,vec.y,vec.z);
	}
	public AxisAlignedBBM union(AxisAlignedBB other){
		double d0=Math.min(this.minX,other.minX);
		double d1=Math.min(this.minY,other.minY);
		double d2=Math.min(this.minZ,other.minZ);
		double d3=Math.max(this.maxX,other.maxX);
		double d4=Math.max(this.maxY,other.maxY);
		double d5=Math.max(this.maxZ,other.maxZ);
		return new AxisAlignedBBM(d0,d1,d2,d3,d4,d5);
	}
	
	public AxisAlignedBBM union(AxisAlignedBBM other){
		double d0=Math.min(this.minX,other.minX);
		double d1=Math.min(this.minY,other.minY);
		double d2=Math.min(this.minZ,other.minZ);
		double d3=Math.max(this.maxX,other.maxX);
		double d4=Math.max(this.maxY,other.maxY);
		double d5=Math.max(this.maxZ,other.maxZ);
		return new AxisAlignedBBM(d0,d1,d2,d3,d4,d5);
	}
}
