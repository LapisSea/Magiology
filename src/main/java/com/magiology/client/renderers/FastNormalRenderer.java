package com.magiology.client.renderers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.magiology.client.renderers.Renderer.RendererBase;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.GeometryUtil;

/**
 * muuuuuuch faster remake of {@link AdvancedRenderer}
 * 
 * @author LapisSea
 */
public class FastNormalRenderer{
	
	public static final int				POS		=0,POS_UV=1,POS_UV_COLOR=2,POS_COLOR=3,BLOCK=4;
	private static final String[]		names	={"POS","POS_UV","POS_UV_COLOR","POS_COLOR","BLOCK"};
	private static final RendererBase[]	formats	={Renderer.POS_NORMAL,Renderer.POS_UV_NORMAL,Renderer.POS_UV_COLOR_NORMAL,Renderer.POS_COLOR_NORMAL,Renderer.BLOCK};
	
	private static final Vec3M POSITION1=new Vec3M(),POSITION2=new Vec3M(),POSITION3=new Vec3M();
	
	private List<Vertex>	data=new ArrayList<>();
	private boolean			isQuads,isUsingQuadConversion,locked=true,normalCalc=true;
	private int				format;
	
	public FastNormalRenderer begin(boolean isQuads,int format){
		this.format=format;
		this.isQuads=isQuads;
		locked=false;
		return this;
	}
	
	public void usingQuadConversion(){
		isUsingQuadConversion=true;
	}
	
	public void add(double x,double y,double z){
		checkFormat(0);
		data.add(new Vertex(x,y,z));
	}
	
	public void add(double x,double y,double z,double u,double v){
		checkFormat(1);
		data.add(new VertexUv(x,y,z,u,v));
	}
	
	public void add(double x,double y,double z,double u,double v,float r,float g,float b,float a){
		checkFormat(2);
		data.add(new VertexUvColor(x,y,z,u,v,r,g,b,a));
	}

	public void add(double x,double y,double z,float r,float g,float b,float a){
		checkFormat(3);
		data.add(new VertexColor(x,y,z,r,g,b,a));
	}
	public void add(double x,double y,double z,double u,double v,float r,float g,float b,float a, int lightX, int lightY){
		checkFormat(4);
		data.add(new VertexBock(x, y, z, u, v, r, g, b, a, lightX, lightY));
	}
	
	public void draw(){
		draw(true);
	}
	
	public void draw(boolean shouldClear){
		if(data.size()%(isQuads?4:3)!=0)throw new IllegalStateException("Invalid vertex count");
		locked=true;
		if(isQuads){
			
			if(isUsingQuadConversion){
				
				List<Vertex> reformated=new ArrayList<>();
				
				for(int i=0;i<data.size();i+=4){
					Vertex p1=data.get(i),p2=data.get(i+1),p3=data.get(i+2),p4=data.get(i+3);
					reformated.add(p1);
					reformated.add(p2);
					reformated.add(p3);
					reformated.add(p1.clone());
					reformated.add(p3.clone());
					reformated.add(p4);
				}
				data=reformated;
				
				isUsingQuadConversion=false;
				isQuads=false;
				
				uploadTriangles();
			}else uploadQuads();
		}else{
			uploadTriangles();
		}
		
		if(!shouldClear)return;
		this.format=-1;
		data.clear();
		normalCalc=true;
	}
	
	private void uploadQuads(){
		calcNormalsQua();
		RendererBase form=formats[format];
		form.beginQuads();
		data.forEach(Vertex::upload);
		form.draw();
	}
	
	private void uploadTriangles(){
		calcNormalsTri();
		RendererBase form=formats[format];
		
		form.begin(GL11.GL_TRIANGLES);
		data.forEach(Vertex::upload);
		form.draw();
	}
	
	private void calcNormalsTri(){
		if(!normalCalc)return;
		normalCalc=false;
		for(int i=0;i<data.size()/3;i++){
			Vertex pos1=data.get(i*3),pos2=data.get(i*3+1),pos3=data.get(i*3+2);
			
			POSITION1.set(pos1.x,pos1.y,pos1.z);
			POSITION2.set(pos2.x,pos2.y,pos2.z);
			POSITION3.set(pos3.x,pos3.y,pos3.z);
			
			Vec3M normal=GeometryUtil.getNormalM(POSITION1,POSITION2,POSITION3);
			pos1.setNormal(normal.getX(),normal.getY(),normal.getZ());
			pos2.setNormal(normal.getX(),normal.getY(),normal.getZ());
			pos3.setNormal(normal.getX(),normal.getY(),normal.getZ());
		}
	}
	
	private void calcNormalsQua(){
		if(!normalCalc)return;
		normalCalc=false;
		for(int i=0;i<data.size();i+=4){
			Vertex pos1=data.get(i),pos2=data.get(i+1),pos3=data.get(i+2),pos4=data.get(i+3);
			
			POSITION1.set(pos1.x,pos1.y,pos1.z);
			POSITION2.set(pos2.x,pos2.y,pos2.z);
			POSITION3.set(pos3.x,pos3.y,pos3.z);
			
			Vec3M normal=GeometryUtil.getNormalM(POSITION1,POSITION2,POSITION3);
			pos1.setNormal(normal.getX(),normal.getY(),normal.getZ());
			pos2.setNormal(normal.getX(),normal.getY(),normal.getZ());
			pos3.setNormal(normal.getX(),normal.getY(),normal.getZ());
			pos4.setNormal(normal.getX(),normal.getY(),normal.getZ());
		}
	}
	
	private void checkFormat(int f){
		if(locked)new IllegalStateException("Renderer locked");
		if(format==f)return;
		throw new IllegalStateException("Ivalid format input: "+names[f]+", required: "+names[format]);
		
	}
	
	private static class Vertex{
		double	x,y,z;
		float	nx,ny,nz;
		
		public Vertex(double x,double y,double z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
		
		void setNormal(float x,float y,float z){
			nx=x;
			ny=y;
			nz=z;
		}
		
		void upload(){
			Renderer.POS_NORMAL.addVertex(x,y,z,nx,ny,nz);
		}
		@Override
		public Vertex clone(){
			return new Vertex(x, y, z);
		}
	}
	
	private static class VertexUv extends Vertex{
		double u,v;
		
		public VertexUv(double x,double y,double z,double u,double v){
			super(x,y,z);
			this.u=u;
			this.v=v;
		}
		@Override
		void upload(){
			Renderer.POS_UV_NORMAL.addVertex(x,y,z,u,v,nx,ny,nz);
		}
		@Override
		public Vertex clone(){
			return new VertexUv(x, y, z, u, v);
		}
	}
	
	private static class VertexUvColor extends VertexUv{
		float r,g,b,a;
		
		public VertexUvColor(double x,double y,double z,double u,double v,float r,float g,float b,float a){
			super(x,y,z,u,v);
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
		}
		
		@Override
		void upload(){
			Renderer.POS_UV_COLOR_NORMAL.addVertex(x,y,z,u,v,r,g,b,a,nx,ny,nz);
		}
		@Override
		public Vertex clone(){
			return new VertexUvColor(x, y, z, u, v, r, g, b, a);
		}
	}

	private static class VertexColor extends Vertex{
		float r,g,b,a;
		
		public VertexColor(double x,double y,double z,float r,float g,float b,float a){
			super(x,y,z);
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
		}
		
		@Override
		void upload(){
			Renderer.POS_COLOR_NORMAL.addVertex(x,y,z,r,g,b,a,nx,ny,nz);
		}
		@Override
		public Vertex clone(){
			return new VertexColor(x, y, z, r, g, b, a);
		}
	}
//	private static class VertexBock extends Vertex{
//		float r,g,b,a;
//		
//		public VertexBock(double x,double y,double z,float r,float g,float b,float a){
//			super(x,y,z);
//			this.r=r;
//			this.g=g;
//			this.b=b;
//			this.a=a;
//		}
//		
//		@Override
//		void upload(){
//			Renderer.BLOCK.addVertex(x, y, z, r, g, b, a, u, v, lightX, lightY);
//		}
//		@Override
//		public Vertex clone(){
//			return new VertexColor(x, y, z, r, g, b, a);
//		}
//	}

	private static class VertexBock extends VertexUv{
		float r,g,b,a;
		int lx,ly;
		public VertexBock(double x,double y,double z,double u,double v,float r,float g,float b,float a, int lightX, int lightY){
			super(x,y,z,u,v);
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
			this.lx=lightX;
			this.ly=lightY;
		}
		
		@Override
		void upload(){
			Renderer.BLOCK.addVertex(x, y, z, r, g, b, a, u, v, lx, ly);
		}
		@Override
		public Vertex clone(){
			return new VertexBock(x, y, z, u, v, r, g, b, a,lx,ly);
		}
	}
}
