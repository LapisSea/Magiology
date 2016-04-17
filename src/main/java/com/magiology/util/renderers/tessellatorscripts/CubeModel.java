package com.magiology.util.renderers.tessellatorscripts;

import org.lwjgl.util.vector.Matrix4f;

import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilobjects.vectors.QuadUV;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.ResourceLocation;

public class CubeModel{
	VertexRenderer buf=Render.NVB();
	private final float minX,minY,minZ,maxX,maxY,maxZ;
	public Vec3M[] points=new Vec3M[8];
	ResourceLocation[] st=new ResourceLocation[6];
	public QuadUV[] UVs=new QuadUV[6];
	public QuadUV[] UVs2=new QuadUV[6];
	public boolean[] willSideRender={true,true,true,true,true,true};
	public CubeModel(final CubeModel cube){
		st=cube.st;
		this.minX=cube.minX;this.minY=cube.minY;this.minZ=cube.minZ;
		this.maxX=cube.maxX;this.maxY=cube.maxY;this.maxZ=cube.maxZ;
		points=cube.points.clone();
		UVs=cube.UVs.clone();
		UVs2=cube.UVs2.clone();
	}
	public CubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ){
		this.minX=Math.min(minX, maxX);
		this.minY=Math.min(minY, maxY);
		this.minZ=Math.min(minZ, maxZ);
		this.maxX=Math.max(minX, maxX);
		this.maxY=Math.max(minY, maxY);
		this.maxZ=Math.max(minZ, maxZ);
		for(int i=0;i<UVs.length;i++)UVs[i]=QuadUV.all();
		points=genPoints();
	}
	public CubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ,QuadUV[] quadUVs,ResourceLocation[] sidedtextures){
		if(quadUVs!=null){
			if(quadUVs.length==6)UVs=quadUVs;
			else if(quadUVs.length==1)UVs=new QuadUV[]{quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0],quadUVs[0]};
		}
		if(sidedtextures!=null){
			if(sidedtextures.length==6)st=sidedtextures;
			else if(sidedtextures.length==1)st=new ResourceLocation[]{sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0]};
		}
		if(quadUVs==null||sidedtextures==null){
			quadUVs=null;
			sidedtextures=null;
		}
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		points=genPoints();
	}
	public CubeModel(float minX,float minY,float minZ,float maxX,float maxY,float maxZ,ResourceLocation[] sidedtextures){
		if(sidedtextures!=null){
			if(sidedtextures.length==6)st=sidedtextures;
			else if(sidedtextures.length==1)st=new ResourceLocation[]{sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0],sidedtextures[0]};
		}
		this.minX=minX;this.minY=minY;this.minZ=minZ;
		this.maxX=maxX;this.maxY=maxY;this.maxZ=maxZ;
		points=genPoints();
		{
			float a=1-maxY;
			minY+=a;
			maxY+=a;
		}
		for(int i=0;i<UVs.length;i++)UVs[i]=QuadUV.all();
		UVs2=UVs.clone();
	}
	
	public void draw(){
		try{
			if(willSideRender[0])try{
				if(st[0]!=null)TessUtil.bindTexture(st[0]);
				buf.addVertexWithUV(points[0],UVs[0].x2,UVs[0].y2);
				buf.addVertexWithUV(points[1],UVs[0].x1,UVs[0].y1);
				buf.addVertexWithUV(points[2],UVs[0].x4,UVs[0].y4);
				buf.addVertexWithUV(points[3],UVs[0].x3,UVs[0].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[0],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[3],0,0);
				buf.draw();
				OpenGLM.enableTexture2D();
			}
			if(willSideRender[1])try{
				if(st[1]!=null)TessUtil.bindTexture(st[1]);
				buf.addVertexWithUV(points[7],UVs[1].x2,UVs[1].y2);
				buf.addVertexWithUV(points[6],UVs[1].x1,UVs[1].y1);
				buf.addVertexWithUV(points[5],UVs[1].x4,UVs[1].y4);
				buf.addVertexWithUV(points[4],UVs[1].x3,UVs[1].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[7],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[4],0,0);
				buf.draw();
				OpenGLM.enableTexture2D();
			}
			if(willSideRender[2])try{
				if(st[2]!=null)TessUtil.bindTexture(st[2]);
				buf.addVertexWithUV(points[2],UVs[2].x2,UVs[2].y2);
				buf.addVertexWithUV(points[1],UVs[2].x1,UVs[2].y1);
				buf.addVertexWithUV(points[5],UVs[2].x4,UVs[2].y4);
				buf.addVertexWithUV(points[6],UVs[2].x3,UVs[2].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.draw();
				OpenGLM.enableTexture2D();
			}
			if(willSideRender[3])try{
				if(st[3]!=null)TessUtil.bindTexture(st[3]);
				buf.addVertexWithUV(points[7],UVs[3].x2,UVs[3].y2);
				buf.addVertexWithUV(points[4],UVs[3].x1,UVs[3].y1);
				buf.addVertexWithUV(points[0],UVs[3].x4,UVs[3].y4);
				buf.addVertexWithUV(points[3],UVs[3].x3,UVs[3].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[7],0,0);
				buf.addVertexWithUV(points[4],0,0);
				buf.addVertexWithUV(points[0],0,0);
				buf.addVertexWithUV(points[3],0,0);
				buf.draw();
				OpenGLM.enableTexture2D();
			}
			if(willSideRender[4])try{
				if(st[4]!=null)TessUtil.bindTexture(st[4]);
				buf.addVertexWithUV(points[4],UVs[4].x2,UVs[4].y2);
				buf.addVertexWithUV(points[5],UVs[4].x1,UVs[4].y1);
				buf.addVertexWithUV(points[1],UVs[4].x4,UVs[4].y4);
				buf.addVertexWithUV(points[0],UVs[4].x3,UVs[4].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[4],0,0);
				buf.addVertexWithUV(points[5],0,0);
				buf.addVertexWithUV(points[1],0,0);
				buf.addVertexWithUV(points[0],0,0);
				buf.draw();
			OpenGLM.enableTexture2D();
			}
			if(willSideRender[5])try{
				if(st[5]!=null)TessUtil.bindTexture(st[5]);
				buf.addVertexWithUV(points[3],UVs[5].x2,UVs[5].y2);
				buf.addVertexWithUV(points[2],UVs[5].x1,UVs[5].y1);
				buf.addVertexWithUV(points[6],UVs[5].x4,UVs[5].y4);
				buf.addVertexWithUV(points[7],UVs[5].x3,UVs[5].y3);
				buf.draw();
			}catch(Exception exception){
				OpenGLM.disableTexture2D();
				buf.addVertexWithUV(points[3],0,0);
				buf.addVertexWithUV(points[2],0,0);
				buf.addVertexWithUV(points[6],0,0);
				buf.addVertexWithUV(points[7],0,0);
				buf.draw();
				OpenGLM.enableTexture2D();
			}
			
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public CubeModel expand(float var){
		return expand(var, var, var);
	}
	
	public CubeModel expand(float x,float y,float z){
		points[0]=points[0].add(-x, y,-z);
		points[1]=points[1].add(-x,-y,-z);
		points[2]=points[2].add(-x,-y, z);
		points[3]=points[3].add(-x, y, z);
		points[4]=points[4].add( x, y,-z);
		points[5]=points[5].add( x,-y,-z);
		points[6]=points[6].add( x,-y, z);
		points[7]=points[7].add( x, y, z);
		return this;
	}
	public void flipUVs(){
		QuadUV[] h=UVs2;
		UVs2=UVs;
		UVs=h;
	}
	
	private Vec3M[] genPoints(){
		//2=all max, 4=all min
		Vec3M[] result=new Vec3M[8];
		result[0]=new Vec3M(maxX, minY, minZ);
		result[1]=new Vec3M(maxX, maxY, minZ);
		result[2]=new Vec3M(maxX, maxY, maxZ);
		result[3]=new Vec3M(maxX, minY, maxZ);
		result[4]=new Vec3M(minX, minY, minZ);
		result[5]=new Vec3M(minX, maxY, minZ);
		result[6]=new Vec3M(minX, maxY, maxZ);
		result[7]=new Vec3M(minX, minY, maxZ);
		return result;
	}
	/**true = max, false = min*/
	public Vec3M getPoint(boolean x,boolean y,boolean z){
			 if( x&&!y&&!z)return points[0];
		else if( x&& y&&!z)return points[1];
		else if( x&& y&& z)return points[2];
		else if( x&&!y&& z)return points[3];
		else if(!x&&!y&&!z)return points[4];
		else if(!x&& y&&!z)return points[5];
		else if(!x&& y&& z)return points[6];
		else if(!x&&!y&& z)return points[7];
		return null;
	}
	public CubeModel transform(Matrix4f matrix){
		for(int i=0;i<points.length;i++)points[i]=MatrixUtil.transformVector(points[i], matrix);
		return this;
	}
	public CubeModel translate(float x, float y, float z){
		for(int i=0;i<points.length;i++)points[i]=points[i].add(x,y,z);
		return this;
	}
}
