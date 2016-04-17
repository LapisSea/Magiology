package com.magiology.client.render.tilerender.network;

import org.lwjgl.opengl.GL11;

import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.VertexRenderer.ShadedQuad;
import com.magiology.util.renderers.glstates.GlState;
import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.renderers.tessellatorscripts.SidedModel;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.QuadUV;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderNetworkController extends TileEntitySpecialRendererM{
	
	private static VertexModel body1,body2;
	private static SidedModel connections;
	private static ColorF glassColor=new ColorF(0.6,0,0,0.1);
	
	private static void initModels(){
		VertexRenderer buff=TessUtil.getVB();
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		QuadUV all=QuadUV.all().rotate1();
		CubeModel[] sideThingys={
				new CubeModel(p*6.5F, p*6.5F, p*11F, p*7F, p*7F, p*13F, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new CubeModel(p*6.5F, p*6.5F, p*13F, p*7F, p*7F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new CubeModel(p*6.5F, p*9F, p*9.5F, p*7F, p*9.5F, p*13F, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new CubeModel(p*6.5F, p*9F, p*13F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
		};
		sideThingys[0].points[0].y-=p*1.5;
		sideThingys[0].points[1].y-=p*1.5;
		sideThingys[0].points[4].y-=p*1.5;
		sideThingys[0].points[5].y-=p*1.5;
		
		sideThingys[2].points[0].y-=p*3;sideThingys[2].points[0].z+=p*0.5;
		sideThingys[2].points[1].y-=p*3.5;
		sideThingys[2].points[4].y-=p*3;sideThingys[2].points[4].z+=p*0.5;
		sideThingys[2].points[5].y-=p*3.5;
		
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].willSideRender[4]=sideThingys[i].willSideRender[5]=false;

		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertexModel sideThingysModel=buff.exportToNormalisedVertexBufferModel();
		sideThingysModel.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
			, null);
		float height=1/12F;
		QuadUV 
			sideCon1=new QuadUV(
				1,height*4,
				1,0,
				0,height*2,
				0,height*4
			).rotate1(),
			sideCon2=new QuadUV(
				1,height*4,
				1,0,
				0,height*2,
				0,height*4
			).mirror1().rotate1(),
			front=new QuadUV(
				0.5F,height*8,
				0.5F,1,
				0,1,
				0,height*8
			),
			sideCon3=new QuadUV(
				1,height*8,
				1,height*4,
				0,height*4,
				0,height*8
			).rotate1().mirror1();
		
		CubeModel[] cores={
			new CubeModel(p*7.75F, p*7.75F, p*12F, p*8.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate1(),sideCon3.mirror1().rotate1(),sideCon3,sideCon3,front,all},null),
					
			new CubeModel(p*7.75F, p*8.75F, p*12F, p*8.25F, p*9.25F, p*13, new QuadUV[]{
					sideCon1.mirror1().rotate1(),sideCon1.rotate1(),sideCon3,sideCon3,front,all},null),
			new CubeModel(p*7.75F, p*6.75F, p*12F, p*8.25F, p*7.25F, p*13, new QuadUV[]{
					sideCon2.mirror1().rotate1(),sideCon2.rotate1(),sideCon3,sideCon3,front,all},null),
					
			new CubeModel(p*8.75F, p*7.75F, p*12F, p*9.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate1(),sideCon3.mirror1().rotate1(),sideCon2.mirror1(),sideCon1.mirror1(),front,all},null),
			new CubeModel(p*6.75F, p*7.75F, p*12F, p*7.25F, p*8.25F, p*13, new QuadUV[]{
					sideCon3.rotate1(),sideCon3.mirror1().rotate1(),sideCon1.mirror1(),sideCon2.mirror1(),front,all},null)
		};
		for(int i=0;i<cores.length;i++)cores[i].willSideRender[5]=false;
		
		
		cores[1].points[2].y-=p*0.25;
		cores[1].points[6].y-=p*0.25;
		
		cores[2].points[3].y+=p*0.25;
		cores[2].points[7].y+=p*0.25;
		
		cores[3].points[3].x-=p*0.25;
		cores[3].points[2].x-=p*0.25;
		
		cores[4].points[6].x+=p*0.25;
		cores[4].points[7].x+=p*0.25;
		
		buff.importComplexCube(cores);
		
		VertexModel horisontalConnectionToBrain=buff.exportToNormalisedVertexBufferModel();
		horisontalConnectionToBrain.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_brain_con.png"));
			})
			, null);
		float w1=1F/216F;
		QuadUV coreUV=new QuadUV(
				w1*56F,1,
				w1*56F,0,
				w1*24,0,
				w1*24,1
			).rotate1().mirror1();
		CubeModel core=new CubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		
		buff.importComplexCube(core);
		
		VertexModel horisontalCore1=buff.exportToNormalisedVertexBufferModel();
		horisontalCore1.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		for(int i=0;i<core.willSideRender.length;i++)core.willSideRender[i]=i==4;
		buff.importComplexCube(core);
		VertexModel horisontalCore2=buff.exportToNormalisedVertexBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_end.png"));
			})
		, null);
		CubeModel[] upConsCubes={
				
		};
		buff.importComplexCube(upConsCubes);
		VertexModel upCons=buff.exportToNormalisedVertexBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		
		sideThingys[0].points[0].y+=p*1.5;sideThingys[0].points[1].y+=p*1.5;sideThingys[0].points[4].y+=p*1.5;sideThingys[0].points[5].y+=p*1.5;
		sideThingys[2].points[0].y+=p*3;sideThingys[2].points[0].z-=p*0.5;sideThingys[2].points[1].y+=p*3.5;sideThingys[2].points[4].y+=p*3;sideThingys[2].points[4].z-=p*0.5;sideThingys[2].points[5].y+=p*3.5;
		
		sideThingys[0].points[0].z-=p/2;sideThingys[0].points[1].z-=p/2;sideThingys[0].points[4].z-=p/2;sideThingys[0].points[5].z-=p/2;
		sideThingys[2].points[0].z+=p;sideThingys[2].points[1].z+=p;sideThingys[2].points[4].z+=p;sideThingys[2].points[5].z+=p;
		
		sideThingys[0].points[0].y-=p*0.5;
		sideThingys[0].points[1].y-=p*0.5;
		sideThingys[0].points[4].y-=p*0.5;
		sideThingys[0].points[5].y-=p*0.5;
		sideThingys[0].points[0].x+=p*0.5;
		sideThingys[0].points[1].x+=p*0.5;
		sideThingys[0].points[4].x+=p*0.5;
		sideThingys[0].points[5].x+=p*0.5;
		
		
		sideThingys[2].points[0].y+=p*0.5;
		sideThingys[2].points[1].y+=p*0.5;
		sideThingys[2].points[4].y+=p*0.5;
		sideThingys[2].points[5].y+=p*0.5;
		sideThingys[2].points[0].x+=p*0.5;
		sideThingys[2].points[1].x+=p*0.5;
		sideThingys[2].points[4].x+=p*0.5;
		sideThingys[2].points[5].x+=p*0.5;
		
		
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(-p*2.5F, 0, 0);
		sideThingys[0].points[0].x-=p;
		sideThingys[0].points[1].x-=p;
		sideThingys[0].points[4].x-=p;
		sideThingys[0].points[5].x-=p;
		sideThingys[2].points[0].x-=p;
		sideThingys[2].points[1].x-=p;
		sideThingys[2].points[4].x-=p;
		sideThingys[2].points[5].x-=p;
		buff.importComplexCube(sideThingys);
		VertexModel sideThingysModel2=buff.exportToNormalisedVertexBufferModel();
		sideThingysModel2.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
			, null);
		
		
		
		
		CubeModel core1=new CubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		
		core1.willSideRender[5]=core1.willSideRender[4]=false;
		buff.importComplexCube(core1);
		VertexModel upModelCore1=buff.exportToNormalisedVertexBufferModel();
		upModelCore1.glStateCell=new GlStateCell(new GlState(()->{TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));}), null);
		
		for(int i=0;i<core1.willSideRender.length;i++)core1.willSideRender[i]=i==4;
		buff.importComplexCube(core1);
		VertexModel upModelCore2=buff.exportToNormalisedVertexBufferModel();
		upModelCore2.glStateCell=new GlStateCell(new GlState(()->{TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe_end.png"));}), null);
		
		
		
		buff.importComplexCube(new CubeModel(p*7F, p*7F, p*13F, p*9F, p*9F, 1));
		VertexModel downModel=buff.exportToNormalisedVertexBufferModel();
		
		
		connections=new SidedModel(
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
					horisontalConnectionToBrain,
					sideThingysModel,
					horisontalCore1,
					horisontalCore2
				},
				new int[]{
					2,
					3,
					4,
					5
				}
			),
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
						upModelCore1,
						upModelCore2,
						sideThingysModel2,
						horisontalConnectionToBrain
				},
				new int[]{
					0,1
				}
			)
//			,
//			new DoubleObject<NormalizedVertixBufferModel[],int[]>(
//				new NormalizedVertixBufferModel[]{
//					upModelCore1,
//					upModelCore2,
//					sideThingysModel2,
//					horisontalConnectionToBrain
//				},
//				new int[]{
//					1
//				})
			);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		TessUtil.drawLine(p*5.05, p*6,p*5.05, p*6.05, p*10, p*6.05, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*5.05, p*9.95, p*10, p*6.05, p/4, false, buff, 0, 10);
		
		TessUtil.drawLine(p*5.05, p*6,p*10.95, p*6.05, p*10, p*9.95, p/4, false, buff, 0, 10);
		TessUtil.drawLine(p*10.95,p*6,p*10.95, p*9.95, p*10, p*9.95, p/4, false, buff, 0, 10);
		
		buff.importComplexCube(new CubeModel(p*5, p*5, p*5, p*11, p*6, p*11),new CubeModel(p*6, p*10, p*6, p*10, p*10.5F, p*10));
		
		body1=buff.exportToNormalisedVertexBufferModel();
		body1.glStateCell=new GlStateCell(new GlState(()->{
			GL11.glColor3b((byte)0, (byte)12, (byte)26);
			GL11U.texture(false);
		}), new GlState(()->{
			GL11U.texture(true);
			ColorF.WHITE.bind();
		}));
		
		CubeModel glass=new CubeModel(p*6.5F, p*6, p*6.5F, p*9.5F, p*10, p*9.5F);
		glass.willSideRender[2]=false;
		glass.willSideRender[3]=false;
		for(int i=0;i<5;i++){
			glass.expand(-0.004F,0,0.004F);
			buff.importComplexCube(glass);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		body2=buff.exportToNormalisedVertexBufferModel();
		body2.glStateCell=new GlStateCell(new GlState(()->{
			GL11U.setUpOpaqueRendering(1);
			glassColor.bind();
		}), new GlState(()->{
			ColorF.WHITE.bind();
			GL11U.endOpaqueRendering();
			GL11U.texture(true);
		}));
	}
	
	public RenderNetworkController(){
		initModels();
	}
	
	private void animate(TileEntity tile0, double x, double y, double z){
		TileEntityNetworkController tile=(TileEntityNetworkController)tile0;
		double tim=tile.getWorld().getTotalWorldTime()+(x+y+z)*30;
		float anim=(PartialTicksUtil.calculatePos(tim-1, tim)/3)%360;
		
		double upDown=Math.sin(Math.toRadians(anim)*4)*UtilM.p/2;
		
		VertexRenderer brainModel=TessUtil.drawBrain(new Vec3M(0.5, 0.5+upDown, 0.5), 1, anim);
		
		Vec3d middle=brainModel.getTriangle(0).pos4[2].vector3D, minY=middle,maxY=middle;
		
		
		for(ShadedQuad tri:brainModel.getTriangles())for(int i=0;i<3;i++){
			Vec3d newPos=tri.pos4[i].vector3D;
			if(newPos.yCoord>maxY.yCoord)maxY=newPos;
			if(newPos.yCoord<minY.yCoord)minY=newPos;
		}
		brainModel.draw();
		
		VertexRenderer buff=TessUtil.getVB();
		GL11U.texture(false);
		TessUtil.drawLine(minY.xCoord+RandUtil.CRF(minY.yCoord-p*6)/2, p*6,   minY.zCoord+RandUtil.CRF(minY.yCoord-p*6)/2, minY.xCoord, minY.yCoord+0.01, minY.zCoord, p/8, true, buff, 0, 10);
		TessUtil.drawLine(maxY.xCoord+RandUtil.CRF(p*10-maxY.yCoord)/2, p*10,  maxY.zCoord+RandUtil.CRF(p*10-maxY.yCoord)/2, maxY.xCoord, maxY.yCoord-0.01, maxY.zCoord, p/8, true, buff, 0, 10);
		GL11.glColor3b((byte)0, (byte)12, (byte)26);
		buff.draw();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
//		initModels();
		GL11U.protect();
		OpenGLM.translate(x,y,z);
		
		
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=((TileEntityNetworkController)tile0).connections[i].getMain();
		connections.draw(sides);
		body1.draw();
		animate(tile0, x, y, z);
		body2.draw();
		GL11U.endProtection();
	}
}
