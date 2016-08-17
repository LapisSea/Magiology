package com.magiology.client.shaders.effects;

import java.util.ArrayDeque;
import java.util.Queue;

import com.magiology.client.shaders.programs.Template;
import com.magiology.client.shaders.upload.UniformUploaderColor;
import com.magiology.client.shaders.upload.UniformUploaderF1;
import com.magiology.client.shaders.upload.UniformUploaderF2;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.DoubleObject;
import com.magiology.util.objs.Vec2FM;
import com.magiology.util.objs.Vec3M;

import org.lwjgl.opengl.Display;

public class SoftEffectsShader extends PositionAwareEffect{
	
	public static final SoftEffectsShader instance=new SoftEffectsShader();


	protected UniformUploaderColor[] 
		cricle_mulColor=new UniformUploaderColor[20];
	protected UniformUploaderF2 
		cricle_screenPos[]=new UniformUploaderF2[20];
	protected UniformUploaderF1 
		cricle_sizeP[]=new UniformUploaderF1[20],
		cricle_sizeN[]=new UniformUploaderF1[20],
		cricle_radius[]=new UniformUploaderF1[20],
		cricleArraySize;
	
	protected UniformUploaderColor[] 
		line_mulColor=new UniformUploaderColor[20];
	protected UniformUploaderF2 
		line_middle[]=new UniformUploaderF2[20],
		line_end[]=new UniformUploaderF2[20];
	protected UniformUploaderF1 
		line_widthPos1[]=new UniformUploaderF1[20],
		line_widthPos2[]=new UniformUploaderF1[20],
		line_totalLength[]=new UniformUploaderF1[20],
		lineArraySize;
	
	
	public Queue<BaseFX> data=new ArrayDeque<>();
	public int cricleArrayPos=0,lineArrayPos=0;
	

	@Override
	public void upload(){
		cricleArraySize.upload((float)cricleArrayPos);
		lineArraySize.upload((float)lineArrayPos);
		for(BaseFX fx:data){
			try{
				fx.upload();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		data.clear();
		cricleArrayPos=0;
		lineArrayPos=0;
	}
	
	public void addCricle(Vec3M worldPos, float sizeP, float sizeN, float radius, ColorF mulColor){
		if(cricleArrayPos>=20)return;
		CricleFX fx=new CricleFX(worldPos, sizeP, sizeN, radius, mulColor);
		data.add(fx);
		fx.setArrayId(cricleArrayPos++);
	}
	public void addLine(Vec3M worldPos1, Vec3M worldPos2, float radius, ColorF mulColor){
		if(lineArrayPos>=20)return;
		LineFX fx=new LineFX(worldPos1, worldPos2, radius, mulColor);
		data.add(fx);
		fx.setArrayId(lineArrayPos++);
	}
	
	@Override
	public void initUniformLocations(){
		for(int i=0;i<20;i++){
			cricle_sizeP[i]=new UniformUploaderF1(Template.instance,"cricles["+i+"].sizeP");
			cricle_sizeN[i]=new UniformUploaderF1(Template.instance,"cricles["+i+"].sizeN");
			cricle_radius[i]=new UniformUploaderF1(Template.instance,"cricles["+i+"].radius");
			cricle_mulColor[i]=new UniformUploaderColor(Template.instance,"cricles["+i+"].mulColor");
			cricle_screenPos[i]=new UniformUploaderF2(Template.instance,"cricles["+i+"].screenPos");

			line_widthPos1[i]=new UniformUploaderF1(Template.instance,"lines["+i+"].widthPos1");
			line_widthPos2[i]=new UniformUploaderF1(Template.instance,"lines["+i+"].widthPos2");
			line_totalLength[i]=new UniformUploaderF1(Template.instance,"lines["+i+"].totalLength");
			line_middle[i]=new UniformUploaderF2(Template.instance,"lines["+i+"].middle");
			line_end[i]=new UniformUploaderF2(Template.instance,"lines["+i+"].end");
			line_mulColor[i]=new UniformUploaderColor(Template.instance,"lines["+i+"].mulColor");
		}
		cricleArraySize=new UniformUploaderF1(Template.instance, "cricle_arraySize");
		lineArraySize=new UniformUploaderF1(Template.instance, "line_arraySize");
	}
	
	private static interface BaseFX{
		SoftEffectsShader e=SoftEffectsShader.instance;
		void upload();
		void setArrayId(int id);
		int getArrayId();
	}
	

	private static class CricleFX implements BaseFX{
		
		private Vec2FM screenPos;
		private ColorF mulColor;
		private int id;
		float 
			distanceScale,
			sizeP,
			sizeN,
			radius;
		
		public CricleFX(Vec3M worldPos, float sizeP, float sizeN, float radius, ColorF mulColor){
			DoubleObject<Vec2FM, Float> o=convertWorldToScreenPos(worldPos);
			screenPos=o.obj1;
			screenPos.x*=Display.getWidth()/2F;
			screenPos.y*=Display.getHeight()/2F;
			distanceScale=1/o.obj2;
			
			this.sizeP=sizeP*distanceScale*Template.screenSizeF;
			this.sizeN=sizeN*distanceScale*Template.screenSizeF;
			this.radius=radius*distanceScale*Template.screenSizeF;
			this.mulColor=mulColor;
		}
		@Override
		public void upload(){
			int i=getArrayId();
			e.cricle_sizeP[i].upload(sizeP);
			e.cricle_sizeN[i].upload(sizeN);
			e.cricle_radius[i].upload(radius);
			e.cricle_mulColor[i].upload(mulColor);
			e.cricle_screenPos[i].upload(-screenPos.x,-screenPos.y);
		}
		@Override
		public void setArrayId(int id){
			this.id=id;
		}
		@Override
		public int getArrayId(){
			return id;
		}
	}
	private static class LineFX implements BaseFX{
		
		ColorF mulColor;
		Vec2FM 
			start,
			end;
		float widthPos1,widthPos2;
		private int id;
		
		public LineFX(Vec3M worldPos1, Vec3M worldPos2, float width, ColorF mulColor){
			this.mulColor=mulColor;
			
			DoubleObject<Vec2FM, Float>
			o=convertWorldToScreenPos(worldPos1);
			this.widthPos1=width/o.obj2*Template.screenSizeF;
			start=o.obj1;
			
			o=convertWorldToScreenPos(worldPos2);
			this.widthPos2=width/o.obj2*Template.screenSizeF;
			end=o.obj1;
			
		}
		@Override
		public void upload(){
			int i=getArrayId();
			Vec2FM 
				siz=new Vec2FM(Display.getWidth()/2F,Display.getHeight()/2F),
				mid=start.add(end).div(2).mul(siz),
				en=end.mul(siz);
			e.line_widthPos1[i].upload(widthPos1);
			e.line_widthPos2[i].upload(widthPos2);
			e.line_totalLength[i].upload(mid.sub(en).length());
			e.line_middle[i].upload(mid.x,mid.y);
			e.line_end[i].upload(en.x,en.y);
			e.line_mulColor[i].upload(mulColor);
		}
		@Override
		public void setArrayId(int id){
			this.id=id;
		}
		@Override
		public int getArrayId(){
			return id;
		}
	}

	@Override
	public boolean shouldRender(){
		return cricleArrayPos+lineArrayPos>0;
	}
}
