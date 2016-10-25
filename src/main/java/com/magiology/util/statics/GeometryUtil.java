package com.magiology.util.statics;

import java.util.*;

import com.magiology.util.objs.PairM;
import com.magiology.util.objs.vec.*;
import com.magiology.util.statics.math.MatrixUtil;

import org.lwjgl.util.vector.*;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

public class GeometryUtil{

	public static class Quad extends Triangle{
		public Vec3M pos4;
		public Quad(PositionTextureVertex[] quad){
			this(new Vec3M(quad[0].vector3D),new Vec3M(quad[1].vector3D),new Vec3M(quad[2].vector3D),new Vec3M(quad[3].vector3D));
		}
		public Quad(Vec3M pos1, Vec3M pos2, Vec3M pos3, Vec3M pos4){
			super(pos1, pos2, pos3);
			this.pos4=pos4;
		}
		public Quad add(Quad tri){
			return new Quad(pos1.add(tri.pos1), pos2.add(tri.pos2), pos3.add(tri.pos3), pos4.add(tri.pos4));
		}
		@Override
		public Quad add(Vec3M pos){
			return new Quad(pos1.add(pos), pos2.add(pos), pos3.add(pos), pos4.add(pos));
		}
		@Override
		public Quad copy(){
			return new Quad(pos1.copy(), pos2.copy(), pos3.copy(), pos4.copy());
		}
		public Triangle getTriangle1(){
			return new Triangle(pos1, pos2, pos3);
		}
		public Triangle getTriangle2(){
			return new Triangle(pos1, pos3, pos4);
		}
		@Override
		public Quad mul(double mul){
			return new Quad(pos1.mul(mul), pos2.mul(mul), pos3.mul(mul),pos4.mul(mul));
		}
		public Quad mul(Quad tri){
			return new Quad(pos1.mul(tri.pos1), pos2.mul(tri.pos2), pos3.mul(tri.pos3), pos4.mul(tri.pos4));
		}
		public Quad sub(Quad tri){
			return new Quad(pos1.sub(tri.pos1), pos2.sub(tri.pos2), pos3.sub(tri.pos3), pos4.sub(tri.pos4));
		}
		@Override
		public void transform(Matrix4f matrix){
			super.transform(matrix);
			pos4=MatrixUtil.transformVector(pos4, matrix);
		}
	}
	public static class Ray{
		public static Ray byDir(Vec3M origin, Vec3M normal){
			return new Ray(origin, normal);
		}
		
		public static Ray byPos(Vec3M pos1, Vec3M pos2){
			return new Ray(pos1, pos2.sub(pos1).normalize());
		}
		public Vec3M origin,dir;
		
		private Ray(Vec3M origin, Vec3M normal){
			this.origin=origin;
			this.dir=normal;
		}
	}
	public static class Triangle{
		private Vec3M normal;
		public Vec3M pos1,pos2,pos3;
		public Triangle(Vec3M pos1, Vec3M pos2, Vec3M pos3){
			this.pos1=pos1;
			this.pos2=pos2;
			this.pos3=pos3;
		}
		public Triangle add(Triangle tri){
			return new Triangle(pos1.add(tri.pos1), pos2.add(tri.pos2), pos3.add(tri.pos3));
		}
		public Triangle add(Vec3M pos){
			return new Triangle(pos1.add(pos), pos2.add(pos), pos3.add(pos));
		}
		public Triangle copy(){
			Triangle result=new Triangle(pos1.copy(), pos2.copy(), pos3.copy());
			result.normal=normal;
			return result;
		}
		public Vec3M getNormal(){
			if(normal==null){
				normal=GeometryUtil.getNormalM(pos1, pos2, pos3);
			}
			return normal;
		}
		public Triangle mul(double mul){
			return new Triangle(pos1.mul(mul), pos2.mul(mul), pos3.mul(mul));
		}
		public Triangle mul(Triangle tri){
			return new Triangle(pos1.mul(tri.pos1), pos2.mul(tri.pos2), pos3.mul(tri.pos3));
		}
		public Triangle sub(Triangle tri){
			return new Triangle(pos1.sub(tri.pos1), pos2.sub(tri.pos2), pos3.sub(tri.pos3));
		}
		public void transform(Matrix4f matrix){
			pos1=MatrixUtil.transformVector(pos1, matrix);
			pos2=MatrixUtil.transformVector(pos2, matrix);
			pos3=MatrixUtil.transformVector(pos3, matrix);
			normal=null;
		}
	}
	
	public static Vec3M checkPointToTrianleMovmentColision(final Vec3M point, final List<Triangle> start, final List<Triangle> end){
		if(start.size()!=end.size())return null;
		Iterator<Triangle> Start=start.iterator(),End=end.iterator();
		Vec3M result=null;
		while(Start.hasNext()){
			Triangle startTri=Start.next(),endTri=End.next();
			
			Vec3M res=checkPointToTrianleMovmentColision(point, startTri, endTri);
			if(res!=null){
				if(result==null)result=res;
				else result=result.add(res);
			}
		}
//		if(result!=null)result=result.mul();
		return result;
	}
	public static Vec3M checkPointToTrianleMovmentColision(final Vec3M point, final Triangle start, final Triangle end){
		int quality=6;
		float qualityMul=1F/(quality-1);
		
		
		Vec3M
			startMiddle=start.pos1.add(start.pos2).add(start.pos3).mul(1F/3F),
			endMiddle  =  end.pos1.add(  end.pos2).add(  end.pos3).mul(1F/3F),
			pointDistVec=point.sub(startMiddle),
			endDistVec=endMiddle.sub(startMiddle);
		
		Triangle difference=start.sub(end);
		if(intersectRayTriangle(
				Ray.byPos(
					point, 
					point.add(endDistVec.normalize().mul(
						Math.min(difference.pos1.length(),
						Math.min(difference.pos2.length(),
								 difference.pos3.length()))
					))),
				end
			)==null
		)return null;
		
		
		float 
			pointDistance=pointDistVec.length(),
			endDistance=endDistVec.length(),
			precentage=pointDistance/endDistance;
		if(precentage>1)return null;
		precentage/=quality;
		
		boolean nullFromStart=true,notNullAtSomePoint=false;
		Triangle[] stages=new Triangle[quality];
		stages[0]=start.copy();
		for(int i=0;i<quality-1;i++){
			int j=i+1;
			if(stages[j]==null)stages[j]=start.sub(difference.mul(qualityMul*j));
			
			float distance1=(float)stages[i].pos1.distanceTo(point),distance2=(float)stages[i].pos2.distanceTo(point),distance3=(float)stages[i].pos3.distanceTo(point),maxDistance=Math.min(distance1, Math.min(distance2, distance3));
			distance1/=maxDistance;distance2/=maxDistance;distance3/=maxDistance;
			distance1*=distance1*distance1;distance2*=distance2*distance2;distance3*=distance3*distance3;
			
			Triangle diff=stages[i+1].sub(stages[i]);
			
			float maxDiff=Math.max(diff.pos1.length(), Math.max(diff.pos2.length(), diff.pos3.length()))*1.001F;
			diff.pos1=diff.pos1.mul(1/distance1);
			diff.pos2=diff.pos2.mul(1/distance2);
			diff.pos3=diff.pos3.mul(1/distance3);
			Vec3M 
				triNormal=diff.pos1.add(diff.pos2).add(diff.pos3).normalize().mul(maxDiff),
				result=intersectRayTriangle(Ray.byPos(point, point.sub(triNormal)), stages[i]);
			
			if(nullFromStart&&result!=null){
				nullFromStart=false;
				notNullAtSomePoint=true;
			}
			if(notNullAtSomePoint&&result==null)return triNormal.mul(quality);
		}

		return null;
	}

	public static Vec3d getNormalD(Vec3d pos0,Vec3d pos1, Vec3d pos2){
		Vec3d vec3  = pos1.subtractReverse(pos0);
		Vec3d vec31 = pos1.subtractReverse(pos2);
		return vec31.crossProduct(vec3).normalize();
	}
	public static Vec3d getNormalD(Vec3M pos0,Vec3M pos1, Vec3M pos2){
		Vec3M vec3  = pos1.subtractReverse(pos0);
		Vec3M vec31 = pos1.subtractReverse(pos2);
		return vec31.crossProduct(vec3).normalize().conv();
	}
	public static Vec3M getNormalM(Vec3d pos0,Vec3d pos1, Vec3d pos2){
		Vec3d vec3  = pos1.subtractReverse(pos0);
		Vec3d vec31 = pos1.subtractReverse(pos2);
		return new Vec3M(vec31.crossProduct(vec3).normalize());
	}
	public static Vec3M getNormalM(Vec3M pos0,Vec3M pos1, Vec3M pos2){
		Vec3M vec3  = pos1.subtractReverse(pos0);
		Vec3M vec31 = pos1.subtractReverse(pos2);
		return vec31.crossProduct(vec3).normalize();
	}
	
	public static PairM<Vec3d,Vec3d> getStartEndLook(Entity entity){
		float 
			f=entity.rotationPitch,
			f1=entity.rotationYaw,
			f2=MathHelper.cos(-f1*0.017453292F-(float)Math.PI),
			f3=MathHelper.sin(-f1*0.017453292F-(float)Math.PI),
			f4=-MathHelper.cos(-f*0.017453292F),
			f5=MathHelper.sin(-f*0.017453292F),
			f6=f3*f4,f7=f2*f4;
		double 
			d0=entity.posX,
			d1=entity.posY+entity.getEyeHeight(),
			d2=entity.posZ,
			d3=5;
		Vec3d 
			start=new Vec3d(d0,d1,d2),
			end=start.addVector(f6*d3,f5*d3,f7*d3);
		return new PairM<Vec3d,Vec3d>(start,end);
	}
	public static Vec3M intersectRayQuad(Ray ray, Quad quad){
		return intersectRayTriangles(ray,quad.getTriangle1(),quad.getTriangle2()).obj1;
	}
	public static PairM<Vec3M, Integer> intersectRayQuads(Ray ray, List<Quad> quads){
		Triangle[] data=new Triangle[quads.size()*2];
		
		
		int i=0;
		for(Quad quad:quads){
			data[i*2]=quad.getTriangle1();
			data[i*2+1]=quad.getTriangle2();
			i++;
		}
		
		PairM<Vec3M, Integer> result=intersectRayTriangles(ray,data);
		if(result==null)return null;
		result.obj2-=result.obj2%2;
		result.obj2/=2;
		return result;
	}
	
	public static PairM<Vec3M, Integer> intersectRayQuads(Ray ray, Quad...quads){
		Triangle[] data=new Triangle[quads.length*2];
		
		
		for(int i=0;i<quads.length;i++){
			data[i*2]=quads[i].getTriangle1();
			data[i*2+1]=quads[i].getTriangle2();
		}
		
		PairM<Vec3M, Integer> result=intersectRayTriangles(ray,data);
		if(result==null)return null;
		result.obj2-=result.obj2%2;
		result.obj2/=2;
		return result;
	}
	public static Vec3M intersectRayTriangle(Ray ray, Triangle tri){
		Vector3f res=intersectRayTriangle(ray.origin.toLWJGLVec(), ray.dir.toLWJGLVec(), tri.pos1.toLWJGLVec(), tri.pos2.toLWJGLVec(), tri.pos3.toLWJGLVec());
		return res!=null?new Vec3M(res.x, res.y, res.z):null;
	}
	
	public static Vec3M intersectRayTriangle(Vec3M tri1, Vec3M tri2, Vec3M tri3, Vec3M p1, Vec3M p2){
		Vector3f res=intersectRayTriangle(p1.toLWJGLVec(), p2.sub(p1).normalize().toLWJGLVec(), tri1.toLWJGLVec(), tri2.toLWJGLVec(), tri3.toLWJGLVec());
		return res!=null?new Vec3M(res.x, res.y, res.z):null;
	}
	
	private static Vector3f intersectRayTriangle(Vector3f start, Vector3f dir, Vector3f tri0, Vector3f tri1, Vector3f tri2){
		Vector3f u=new Vector3f(tri1);
		Vector3f.sub(u, tri0, u);
		
		Vector3f v=new Vector3f(tri2);
		Vector3f.sub(v, tri0, v);

		Vector3f n=new Vector3f();
		Vector3f.cross(u, v, n);

		if(n.length()==0)return null;

		Vector3f w=new Vector3f(start);
		Vector3f.sub(w, tri0, w);

		float a=-Vector3f.dot(n, w);
		float b=Vector3f.dot(n, dir);

		if(Math.abs(b)<0.000000001)return null;

		float r=a/b;

		if(r<0.0)return null;

		Vector3f i=new Vector3f(start);
		i.x+=r*dir.x;
		i.y+=r*dir.y;
		i.z+=r*dir.z;

		float uu, uv, vv, wu, wv, D;

		uu=Vector3f.dot(u, u);
		uv=Vector3f.dot(u, v);
		vv=Vector3f.dot(v, v);

		Vector3f.sub(i, tri0, w);

		wu=Vector3f.dot(w, u);
		wv=Vector3f.dot(w, v);

		D=uv*uv-uu*vv;

		float s, t;

		s=(uv*wv-vv*wu)/D;
		if(s<0.0||s>1.0)return null;
		

		t=(uv*wu-uu*wv)/D;
		if(t<0.0||s+t>1.0)return null;

		return i;
	}
	public static PairM<Vec3M, Integer> intersectRayTriangles(Ray ray, Triangle...triangles){
		List<Vec3M> results=new ArrayList<>();
		
		for(Triangle triangle:triangles){
			Vec3M result=intersectRayTriangle(ray, triangle);
			if(result!=null){
				results.add(result);
			}
		}
		if(results.isEmpty())return null;
		
		float minDistance=Float.MAX_VALUE;
		int closestId=-1;
		for(int i=0;i<results.size();i++){
			double distance=results.get(i).distanceTo(ray.origin);
			if(distance<minDistance){
				minDistance=(float)distance;
				closestId=i;
			}
		}
		return new PairM<Vec3M, Integer>(results.get(closestId), closestId);
	}
	
	public static RayTraceResult rayTrace(Entity player){
		return rayTrace(player,false);
	}
	public static RayTraceResult rayTrace(Entity entity,boolean useLiquids){
		PairM<Vec3d,Vec3d> look=getStartEndLook(entity);
		return entity.worldObj.rayTraceBlocks(look.obj1,look.obj2,useLiquids,!useLiquids,false);
	}
	
	public static List<Vec2FM> cricleSectorSize(double startDeg, double size, double degreeSpacing){
		return cricleSector(startDeg,startDeg+size,1,degreeSpacing);
	}
	
	public static List<Vec2FM> cricleSector(double startDeg, double endDeg, double degreeSpacing){
		return cricleSector(startDeg,endDeg,1,degreeSpacing);
	}
	
	public static List<Vec2FM> cricleSectorSize(double startDeg, double size, float radius, double degreeSpacing){
		return cricleSector(startDeg,startDeg+size,radius,degreeSpacing);
	}
	
	public static List<Vec2FM> cricleSector(double startDeg, double endDeg, float radius, double degreeSpacing){
		if(degreeSpacing>120)throw new IllegalStateException("Minimal resolution is 120deg/point");
		List<Vec2FM> points=new ArrayList<>();
		
		degreeSpacing=Math.toRadians(degreeSpacing);
		startDeg=Math.toRadians(startDeg);
		endDeg=Math.toRadians(endDeg);
		
		if(endDeg<startDeg)degreeSpacing*=-1;
		int pointCount=(int)Math.floor((endDeg-startDeg)/degreeSpacing);
		
		points.add(anglePoint(startDeg));
		if(pointCount>0){
			double angle=startDeg;
			for(int i=0;i<pointCount;i++){
				angle+=degreeSpacing;
				points.add(anglePoint(angle));
			}
		}
		points.add(anglePoint(endDeg));
		
		if(radius!=1){
			List<Vec2FM> scaled=new ArrayList<>();
			points.forEach(p->scaled.add(p.mul(radius)));
			return scaled;
		}
		return points;
	}
	public static Vec2FM anglePoint(double rad){
		return new Vec2FM((float)Math.sin(rad),(float)Math.cos(rad));
	}
	
	private static final List<IVec3M> facingRotations;
	
	static{
		List<IVec3M> arList=new ArrayList<>();
		
		for(EnumFacing facing:EnumFacing.values()){
			Vec3i rotVec=facing.getDirectionVec();
			double x,y;
			
			if(rotVec.getY()==1)x=-90;
			else if(rotVec.getY()==-1)x=90;
			else x=0;
			
			if(rotVec.getX()!=0||rotVec.getZ()!=0)y=Math.toDegrees(Math.atan2(rotVec.getX(),rotVec.getZ()));
			else y=0;
			
			arList.add(new Vec3MFinal(x,y,0));
		}
		facingRotations=Collections.unmodifiableList(arList);
	}
	
	public static IVec3M rotFromFacing(EnumFacing facing){
		return facingRotations.get(facing.getIndex());
	}
}
