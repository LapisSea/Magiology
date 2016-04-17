package com.magiology.client.render.tilerender.network;

import org.lwjgl.opengl.GL11;

import com.magiology.api.connection.IConnectionProvider;
import com.magiology.api.network.NetworkBaseComponent;
import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.glstates.GlState;
import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.renderers.tessellatorscripts.SidedModel;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.QuadUV;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderNetworkConductor extends TileEntitySpecialRendererM{
	
	protected static VertexModel body1,body2[]=new VertexModel[8];
	protected static SidedModel connections,strateConnections;
	
	public RenderNetworkConductor(){
		initModels();
	}
	
	protected void initModels(){
		VertexRenderer buff=TessUtil.getVB();
		
		QuadUV all=QuadUV.all().rotate1();
		CubeModel[] sideThingys={
				new CubeModel(p*6.5F, p*6.5F, p*9F, p*7F, p*7F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new CubeModel(p*6.5F, p*9F, p*9F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
		};
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].willSideRender[4]=sideThingys[i].willSideRender[5]=false;
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertexModel sideThingysModel1=buff.exportToNormalisedVertexBufferModel();
		sideThingysModel1.glStateCell=new GlStateCell(
			new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(-p*2.5F, 0, 0);
		
		for(int i=0;i<sideThingys.length;i++){
			sideThingys[i].UVs[2]=sideThingys[i].UVs[2].rotate1().rotate1().mirror1();
			sideThingys[i].UVs[3]=sideThingys[i].UVs[3].rotate1().rotate1().mirror1();
		}
		
		buff.importComplexCube(sideThingys);
		for(int i=0;i<sideThingys.length;i++)sideThingys[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys);
		
		VertexModel sideThingysModel2=buff.exportToNormalisedVertexBufferModel();
		sideThingysModel2.glStateCell=new GlStateCell(
				new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{}, ()->{
					TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
				})
				, null);
		
		
		float w1=1F/216F;
		QuadUV coreUV=new QuadUV(
				w1*24,1,
				w1*24,0,
				w1*108,0,
				w1*108,1
			).rotate1();
		CubeModel core=new CubeModel(p*7F, p*7F, p*9F, p*9F, p*9F, 1,new QuadUV[]{
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

		coreUV=new QuadUV(
			w1*132,1,
			w1*132,0,
			1,0,
			1,1
		).rotate1().mirror1();
		core=new CubeModel(p*7F, p*7F, p*9F, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		buff.importComplexCube(core);
		
		VertexModel horisontalCore2=buff.exportToNormalisedVertexBufferModel();
		horisontalCore2.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		connections=new SidedModel(
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
						sideThingysModel1,
						horisontalCore1
				},
				new int[]{
					1,
					3,
					5
				}
			),
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
						sideThingysModel2,
						horisontalCore2
				},
				new int[]{
					0,
					2,
					4
				}
			)
		);
		
		

		coreUV=new QuadUV(
			w1*24,1,
			w1*24,0,
			1,0,
			1,1
		).rotate1();
		core=new CubeModel(p*7F, p*7F, 0, p*9F, p*9F, 1,new QuadUV[]{
			coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
		},null);
		core.willSideRender[5]=core.willSideRender[4]=false;
		buff.importComplexCube(core);
		
		VertexModel coreLong=buff.exportToNormalisedVertexBufferModel();
		coreLong.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		
		
		CubeModel[] sideThingys1={
				new CubeModel(p*6.5F, p*6.5F, 0, p*7F, p*7F,   1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
				new CubeModel(p*6.5F, p*9F,   0, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
		};
		for(int i=0;i<sideThingys1.length;i++)sideThingys1[i].willSideRender[4]=sideThingys1[i].willSideRender[5]=false;
		buff.importComplexCube(sideThingys1);
		for(int i=0;i<sideThingys1.length;i++)sideThingys1[i].translate(p*2.5F, 0, 0);
		buff.importComplexCube(sideThingys1);
		
		VertexModel sideThingysModelLong=buff.exportToNormalisedVertexBufferModel();
		sideThingysModelLong.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip.png"));
			})
		, null);
		
		
		strateConnections=new SidedModel(
				new DoubleObject<>(
					new VertexModel[]{
							sideThingysModelLong,
							coreLong
					},
					new int[]{
						1,
						3,
						5
					}
				)
			);
		
		
		
		
		CubeModel cube=new CubeModel(p*7F, p*7F, p*7F, p*9F, p*9F, p*9F,new QuadUV[]{new QuadUV(
				w1*24,1,
				w1*24,0,
				0,0,
				0,1
			)},null);
		buff.importComplexCube(cube);

		body1=buff.exportToNormalisedVertexBufferModel();
		body1.glStateCell=new GlStateCell(
			new GlState(()->{
				TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/network_pipe.png"));
			})
		, null);
		
		int i=0;
		CubeModel cube1=new CubeModel(p*8.99F, p*8.99F, p*8.99F, p*9.51F, p*9.51F, p*9.51F,new QuadUV[]{QuadUV.all()},null);
		
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(-p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(0, -p*2.5F, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(0, 0, -p*2.5F);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(-p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(0, p*2.5F, 0);
		buff.importComplexCube(cube1);
		body2[i++]=buff.exportToNormalisedVertexBufferModel();
		cube1.translate(p*2.5F, 0, 0);
		buff.importComplexCube(cube1);
		body2[i]=buff.exportToNormalisedVertexBufferModel();
		
	}
	
	protected <NetworkComponent extends IConnectionProvider&NetworkBaseComponent> void renderNetworkPipe(NetworkComponent networkComponent, double x, double y, double z){
		GL11U.protect();
		GL11U.texture(true);
		OpenGLM.translate(x,y,z);
		
//		GL11U.texture(false);
//		ColorF.WHITE.bind();
//		OpenGLM.shadeModel(GL11.GL_SMOOTH);
//		ShinySurfaceRenderer renderer=new ShinySurfaceRenderer();
//		renderer.setModelOriginPos(new Vec3M().add(networkComponent.getHost().getPos()));
//		renderer.setBaseColor(new ColorF(0.3,0.6,0,1));
//
//		renderer.addLight(new SpecularLight(new Vec3M(0,-1,0), new ColorF(0.9,0.9,1,1),6));
//		renderer.addLight(new SpecularLight(new Vec3M(UtilM.fluctuateSmooth(50, 0),-1,0.5), new ColorF(1,0.2,0.8,1),6));
//		
//		
//		CubeModel cube=new CubeModel(0,1,0,1F,2.001F,1F);
//		Matrix4f rot=new Matrix4f();
//		
//		rot.translate(new Vector3f(0,1,0));
//		rot.rotate((float)Math.toRadians(40),new Vector3f(0,0,1));
//		rot.translate(new Vector3f(0,-1,0));
//		
////		cube.transform(rot);
//		renderer.modelTransf=rot;
//		renderer.importComplexCube(cube);
////		OpenGLM.translate(0, 1, 0);
////		GL11U.glRotate(0, 0, 40);
////		OpenGLM.translate(0, -1, 0);
//		renderer.draw();
//		OpenGLM.shadeModel(GL11.GL_FLAT);
//
//		
//		GL11U.texture(true);
		
		
		boolean[] strateSides=new boolean[6];
		strateSides[1]=networkComponent.isStrate(EnumFacing.UP);
		strateSides[3]=networkComponent.isStrate(EnumFacing.SOUTH);
		strateSides[5]=networkComponent.isStrate(EnumFacing.WEST);
		
		if(networkComponent.getBrain()==null)GL11U.glColor(ColorF.GRAY);
		
		if(strateSides[1]||strateSides[3]||strateSides[5])strateConnections.draw(strateSides);
		else{
			boolean[] sides=new boolean[6];
			for(int i=0;i<6;i++)sides[i]=networkComponent.getConnections()[i].getMain();
			
			connections.draw(sides);
			body1.draw();
			
			TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"/textures/models/iron_strip_end.png"));
			for(int i=0;i<body2.length;i++)switch(i){
			case 0:if(		  sides[1]		  ||sides[3]		  ||sides[5])body2[i].draw();break;
			case 1:if(		  sides[1]		  ||sides[3]||sides[4]		  )body2[i].draw();break;
			case 2:if(sides[0]					||sides[3]||sides[4]		  )body2[i].draw();break;
			case 3:if(sides[0]					||sides[3]		  ||sides[5])body2[i].draw();break;
			case 4:if(sides[0]		  ||sides[2]					||sides[5])body2[i].draw();break;
			case 5:if(sides[0]		  ||sides[2]		  ||sides[4]		  )body2[i].draw();break;
			case 6:if(		  sides[1]||sides[2]		  ||sides[4]		  )body2[i].draw();break;
			case 7:if(		  sides[1]||sides[2]					||sides[5])body2[i].draw();break;
			}
		}
		
		GL11U.endProtection();
	}
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks){
		renderNetworkPipe((TileEntityNetworkConductor)tile, x, y, z);
	}
}
