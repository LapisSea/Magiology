package com.magiology.client.render.models;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.client.render.Textures;
import com.magiology.core.Config;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData;
import com.magiology.mcobjects.entitys.ComplexPlayerRenderingData.CyborgWingsFromTheBlackFireData;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.vectors.QuadUV;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ModelWingsFromTheBlackFire extends ModelBiped{
	private static CubeModel[] models;
	private static CubeModel[][] modelsBack;
	private static float p=1F/16F;
	
	private static QuadUV[] genUV(int x1,int y1){
		float x=(1F*x1)/7F,y=y1/6F,x2=2/14F,y2=1/6F;
		return new QuadUV[]{new QuadUV(
				x,	y+y2,
				x,	y,
				x+x2, y, 
				x+x2, y+y2
				).rotate1().mirror1()};
	}
	private static void init(float[][] renderRotations){
		for(int a=0;a<renderRotations.length;a++){
			float thickness=0;
			if(a>0)thickness=(((float)a)/((float)renderRotations.length))*p;
			models=ArrayUtils.add(models, new CubeModel(thickness, thickness, 0, p*3-thickness, p*3-thickness, p*7,genUV(a, 0),new ResourceLocation[]{Textures.WingColors}));
			CubeModel[] WingPart={};
			for(int b=0;b<5;b++){
				float njnj=(float)(renderRotations.length-a)/(float)renderRotations.length;
				WingPart=ArrayUtils.add(WingPart, new CubeModel(thickness, thickness+njnj*(b*0.02F), thickness/2, p*3-thickness, p*3-thickness-njnj*(b*0.02F), p*8-thickness,genUV(a, b),new ResourceLocation[]{Textures.WingColors}));
			}
			modelsBack=ArrayUtils.add(modelsBack, WingPart);
			wings3D(Config.isWingsThick());
		}
		
		for(int a=0;a<modelsBack.length;a++){
			for(int b=0;b<modelsBack[a].length;b++)for(int c=0;c<models[a].UVs.length;c++){
				if(c==0)modelsBack[a][b].UVs[c]=genUV(a, b+1)[0].rotate1();
				else if(c==1)modelsBack[a][b].UVs[c]=genUV(a, b+1)[0].mirror1().rotate1();
				else if(c==4){
					modelsBack[a][b].UVs[c]=genUV(a, b+1)[0];
					modelsBack[a][b].UVs[c].x2=modelsBack[a][b].UVs[c].x4;
					modelsBack[a][b].UVs[c].x3=modelsBack[a][b].UVs[c].x1;
				}
				else modelsBack[a][b].UVs[c]=genUV(a, b+1)[0];
				
				if(c==0)modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].rotate1();
				else if(c==1)modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].mirror1().rotate1();
				else if(c==4){
					modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0];
					modelsBack[a][b].UVs2[c].x2=modelsBack[a][b].UVs[c].x4;
					modelsBack[a][b].UVs2[c].x3=modelsBack[a][b].UVs[c].x1;
					modelsBack[a][b].UVs2[c]=modelsBack[a][b].UVs2[c].rotate1().mirror1().rotate1().rotate1().rotate1();
				}
				else modelsBack[a][b].UVs2[c]=genUV(a, b+1)[0].rotate1().mirror1().rotate1().rotate1().rotate1();
			}
			for(int c=0;c<models[a].UVs.length;c++){
				//uv 1
				if(c==0){
					models[a].UVs[c]=genUV(a, 0)[0].mirror1().rotate1();
					models[a].UVs[c].y2=models[a].UVs[c].y4;
					models[a].UVs[c].y3=models[a].UVs[c].y1;
					models[a].UVs[c]=models[a].UVs[c].rotate1().mirror1().rotate1().rotate1().rotate1();
				}
				else if(c==1)models[a].UVs[c]=genUV(a, 0)[0].mirror1().rotate1();
				else if(c==4){
					models[a].UVs[c]=genUV(a, 0)[0];
					models[a].UVs[c].x2=models[a].UVs[c].x4;
					models[a].UVs[c].x3=models[a].UVs[c].x1;
				}
				else models[a].UVs[c]=genUV(a, 0)[0];
				//uv 2
				if(c==0)models[a].UVs2[c]=genUV(a, 0)[0].rotate1();
				else if(c==1){
					models[a].UVs2[c]=genUV(a, 0)[0].mirror1().rotate1();
					models[a].UVs2[c].y2=models[a].UVs[c].y4;
					models[a].UVs2[c].y3=models[a].UVs[c].y1;
				}
				else if(c==4){
					models[a].UVs2[c]=genUV(a, 0)[0];
					models[a].UVs2[c].x2=models[a].UVs[c].x4;
					models[a].UVs2[c].x3=models[a].UVs[c].x1;
					models[a].UVs2[c]=models[a].UVs2[c].rotate1().mirror1().rotate1().rotate1().rotate1();
				}
				else models[a].UVs2[c]=genUV(a, 0)[0] .rotate1().mirror1().rotate1().rotate1().rotate1();
			}
		}
	}
	
	public static void wings3D(boolean bol){
		if(models==null)return;
		for(int a=0;a<models.length;a++)models[a].willSideRender=new boolean[]{bol,bol,bol,true,bol,bol};
		for(int a=0;a<modelsBack.length;a++)for(int b=0;b<modelsBack[0].length;b++)modelsBack[a][b].willSideRender=new boolean[]{bol,bol,bol,true,bol,bol};
	}
	
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){
		//calculating and safety
		if(!(entity instanceof EntityPlayer))return;
		EntityPlayer player=(EntityPlayer)entity;
		CyborgWingsFromTheBlackFireData data=validateForRender(player);
		if(data==null)return;
		float rotation=0;
		rotation=PartialTicksUtil.calculatePos(data.prevPlayerAngle, data.playerAngle);
		float[][] renderRotations=new float[7][3];
		for(int a=0;a<data.calcRotationAnglesBase.length;a++)for(int a1=0;a1<data.calcRotationAnglesBase[a].length;a1++)renderRotations[a][a1]=PartialTicksUtil.calculatePos(data.calcPrevRotationAnglesBase[a][a1],data.calcRotationAnglesBase[a][a1]);
		if(models==null)init(renderRotations);
		//rendering
		GL11U.protect();
		GL11U.glCulFace(Config.isWingsThick());
		OpenGLM.enableDepth();
		OpenGLM.translate(-p*1.5,-p*0.5,p*3.5);
		GL11U.glRotate(-rotation, 0, 0);
		OpenGLM.pushMatrix();
		OpenGLM.translate(-p*4, 0, 0);
		GL11U.glRotate(0,-90+10, 0,  p*1.5, p*1.5,0);
		renderWingBase(renderRotations,false,data);
		OpenGLM.popMatrix();
		OpenGLM.pushMatrix();
		OpenGLM.translate(p*4, 0, 0);
		GL11U.glRotate(0, 90-10, 0, p*1.5, p*1.5,0);
		renderWingBase(renderRotations,true,data);
		OpenGLM.popMatrix();
		GL11U.endProtection();
//		Config.setWingsThick(true);
	}
	public void renderWingBase(float[][] renderRotations,boolean side, CyborgWingsFromTheBlackFireData data){
		for(int a=0;a<renderRotations.length;a++){
			renderRotations[a][1]*=-1;
			renderRotations[a][2]*=-1;
		}
		if(!side){
			for(int a=0;a<modelsBack.length;a++){
				for(int b=0;b<modelsBack[a].length;b++)modelsBack[a][b].flipUVs();
				models[a].flipUVs();
			}
		}
		OpenGLM.pushMatrix();
		for(int a1=0;a1<models.length;a1++){
			CubeModel a=models[a1];
			GL11U.glRotate(renderRotations[a1][0],renderRotations[a1][1],renderRotations[a1][2], a.points[0].x/2, a.points[0].y/2, 0);
			a.draw();
			OpenGLM.pushMatrix();
			OpenGLM.translate((a.points[3].x-a.points[4].x)*(side?-1:1), 0, 0);
			for(int l=0;l<modelsBack[a1].length;l++){
				CubeModel part=modelsBack[a1][l];
				part.draw(); 
				OpenGLM.translate((float)(part.points[3].x-part.points[4].x)*(side?-1:1), 0, -p/2*(l*0.6F));
				GL11U.glRotate(3, 2, 0);
			}
			OpenGLM.color(1,1,1,1);
			OpenGLM.popMatrix();
			OpenGLM.translate(0, 0, a.points[3].getZ());
		}
		OpenGLM.popMatrix();
		if(!side){
			for(int a=0;a<modelsBack.length;a++){
				for(int b=0;b<modelsBack[a].length;b++)modelsBack[a][b].flipUVs();
				models[a].flipUVs();
			}
		}
	}
	
	
	
	//static...
	private CyborgWingsFromTheBlackFireData validateForRender(EntityPlayer player){
		ComplexPlayerRenderingData playerRenderingData=ComplexPlayerRenderingData.get(player);
		if(playerRenderingData==null){
			ComplexPlayerRenderingData.registerEntityPlayerRenderer(player);
			ComplexPlayerRenderingData.get(player).getCyborgWingsFromTheBlackFireData();
		}
		return ComplexPlayerRenderingData.get(player).getCyborgWingsFromTheBlackFireData();
	}
}
