package com.magiology.util.renderers.tessellatorscripts;

import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilobjects.vectors.QuadUV;

import net.minecraft.util.ResourceLocation;

public class PlateModel{
	float Point1X=0,Point1Y=0,Point1Z=0;
	float Point2X=Width,Point2Y=0,Point2Z=0;
	float Point3X=Width,Point3Y=Height,Point3Z=Lenght;
	float Point4X=0,Point4Y=Height,Point4Z=Lenght,xPos,yPos,zPos;
	public QuadUV quadUV=null;
	boolean renderOnZ=false;
	ResourceLocation texture;
	public float Width,Lenght,Height;
	
	public PlateModel(float Width,float Height,float Lenght,boolean instantRender){
		this.Width=Width;
		this.Lenght=Lenght;
		this.Height=Height;
		Point1X=0;Point1Y=0;Point1Z=0;
		Point2X=Width;Point2Y=0;Point2Z=0;
		Point3X=Width;Point3Y=Height;Point3Z=Lenght;
		Point4X=0;Point4Y=Height;Point4Z=Lenght;
		if(instantRender)render();
	}
	public PlateModel(float Width,float Height,float Lenght,QuadUV quadUV,ResourceLocation resourceLocation,boolean instantRender){
		this.Width=Width;
		this.Lenght=Lenght;
		this.Height=Height;
		this.quadUV=quadUV;
		texture=resourceLocation;
		Point1X=0;Point1Y=0;Point1Z=0;
		Point2X=Width;Point2Y=0;Point2Z=0;
		Point3X=Width;Point3Y=Height;Point3Z=Lenght;
		Point4X=0;Point4Y=Height;Point4Z=Lenght;
		if(instantRender)render();
	}
	public PlateModel flipUVX(){
		if(quadUV==null)return this;
		float x1=quadUV.x1;
		float x2=quadUV.x2;
		float x3=quadUV.x3;
		float x4=quadUV.x4;
		quadUV.x1=x4;
		quadUV.x2=x3;
		quadUV.x3=x2;
		quadUV.x4=x1;
		return this;
	}
	public PlateModel flipUVY(){
		if(quadUV==null)return this;
		float y1=quadUV.y1;
		float y2=quadUV.y2;
		float y3=quadUV.y3;
		float y4=quadUV.y4;
		quadUV.y1=y4;
		quadUV.y2=y3;
		quadUV.y3=y2;
		quadUV.y4=y1;
		return this;
	}
	public void render(){
		if(renderOnZ){
			Point1X=0;Point1Y=0;Point1Z=0;
			Point2X=0;Point2Y=0;Point2Z=Lenght;
			Point3X=Width;Point3Y=Height;Point3Z=Lenght;
			Point4X=Width;Point4Y=Height;Point4Z=0;
		}
		OpenGLM.pushMatrix();
		OpenGLM.translate(xPos, yPos, zPos);
		VertexRenderer buf=TessUtil.getVB();
		if(quadUV==null||texture==null){
			OpenGLM.disableTexture2D();
			buf.cleanUp();
			buf.addVertexWithUV(Point4X, Point4Y, Point4Z,0,0);
			buf.addVertexWithUV(Point3X, Point3Y, Point3Z,0,0);
			buf.addVertexWithUV(Point2X, Point2Y, Point2Z,0,0);
			buf.addVertexWithUV(Point1X, Point1Y, Point1Z,0,0);
			buf.draw();
			OpenGLM.enableTexture2D();
		}else{
//			Helper.println(texture);
			TessUtil.bindTexture(texture);
			buf.cleanUp();
			buf.addVertexWithUV(Point4X, Point4Y, Point4Z,quadUV.x4,quadUV.y4);
			buf.addVertexWithUV(Point3X, Point3Y, Point3Z,quadUV.x3,quadUV.y3);
			buf.addVertexWithUV(Point2X, Point2Y, Point2Z,quadUV.x2,quadUV.y2);
			buf.addVertexWithUV(Point1X, Point1Y, Point1Z,quadUV.x1,quadUV.y1);
			buf.draw();
		}
		OpenGLM.popMatrix();
	}
	public PlateModel setX(float x){
		xPos=x;
		return this;
	}
	public PlateModel setY(float y){
		yPos=y;
		return this;
	}
	public PlateModel setZ(float z){
		zPos=z;
		return this;
	}
	public PlateModel switchSides(){
		float Point1Xa=Point1X;
		float Point2Xa=Point2X;
		float Point3Xa=Point3X;
		float Point4Xa=Point4X;
		Point1X=Point1Xa;
		Point2X=Point2Xa;
		Point3X=Point3Xa;
		Point4X=Point4Xa;
		return flipUVX();
	}
}
