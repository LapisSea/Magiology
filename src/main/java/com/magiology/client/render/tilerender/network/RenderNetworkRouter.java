package com.magiology.client.render.tilerender.network;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.magiology.core.MReference;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.glstates.GlState;
import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.renderers.tessellatorscripts.SidedModel;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.LinearAnimation;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;
import com.magiology.util.utilobjects.vectors.QuadUV;
import com.magiology.util.utilobjects.vectors.QuadUVGenerator;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderNetworkRouter extends TileEntitySpecialRendererM{
	
	protected static SidedModel connections,core;
	protected static TileEntityNetworkRouter curentTile;
	protected static LinearAnimation<Vec3M> insertionAnimation=new LinearAnimation(
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-8,1,0)},0F),
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-8,0,0)},0.4F),
		new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M( 0,0,1)},1F)
	);
	protected static VertexModel lever,usb;
	
	
	public RenderNetworkRouter(){
		initModels();
	}
	
	protected void initModels(){
		float w=1F/155,h=1F/88;
		VertexRenderer buff=TessUtil.getVB();
		QuadUVGenerator uvGen=new QuadUVGenerator(155, 88);
		
		
		
		QuadUV all=QuadUV.all().rotate1();
		{
			CubeModel[] sideThingys={
					new CubeModel(p*6.5F, p*6.5F, p*11F, p*7F, p*7F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null),
					new CubeModel(p*6.5F, p*9F, p*11F, p*7F, p*9.5F, 1, new QuadUV[]{all.rotate1(),all.rotate1(),all,all,all,all},null)
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
					w1*84,0,
					w1*84,1
				).rotate1();
			CubeModel core=new CubeModel(p*7F, p*7F, p*11F, p*9F, p*9F, 1,new QuadUV[]{
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
				w1*156,1,
				w1*156,0,
				1,0,
				1,1
			).rotate1().mirror1();
			core.UVs=new QuadUV[]{
				coreUV.rotate1(),coreUV.mirror1().rotate1(),coreUV,coreUV,QuadUV.all(),coreUV
			};
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
		}
		CubeModel 
			cubes[]={
					new CubeModel(p*5.5F, p*5.5F, p*10F, p*10.5F, p*10.5F, p*10.5F),
					new CubeModel(p*5.5F, p*5.5F, p*10.5F, p*10.5F, p*10.5F, p*11F)
			},
			leverBoxes[]={
					new CubeModel(p*9.75F, p*9.25F, p*6F, p*10.25F, p*9.5F, p*6.75F),
					new CubeModel(p*9.875F, p*9F, p*6.25F, p*10.125F, p*9.25F, p*8.5F),
					new CubeModel(p*9.875F, p*9F, p*8.5F, p*10.125F, p*9.25F, p*10F)
			},
			plug=new CubeModel(p*9.75F, p*9.75F, p*9F, p*10.25F, p*10.25F, p*10F);
		
		
		
		cubes[0].willSideRender[5]=false;
		cubes[1].willSideRender[4]=false;
		
		cubes[1].points[2]=cubes[1].points[2].add(-p, -p, 0);
		cubes[1].points[3]=cubes[1].points[3].add(-p, p, 0);
		cubes[1].points[6]=cubes[1].points[6].add(p, -p, 0);
		cubes[1].points[7]=cubes[1].points[7].add(p, p, 0);
		
		cubes[0].UVs[4]=new QuadUV(
			w*80, 0,
			w*80, h*80,
			w*160,h*80,
			w*160,0
		).mirror2().translate(-w*5, 0);
		
		cubes[1].UVs[5]=new QuadUV(
			w*20,h*76,
			w*20,h*116,
			w*60,h*116,
			w*60,h*76
		).mirror2();
		

		cubes[0].UVs[0]=
//				uvGen.create(67,0,8,80)
		new QuadUV(
			w*67, h*0,
			w*67, h*80,
			w*75,h*80,
			w*75,h*0
		).mirror2();
		
		cubes[0].UVs[1]=uvGen.create(80,80,80,8).rotate2().mirror1().translate(-w*5, 0);
		
		cubes[0].UVs[2]=cubes[0].UVs[0].rotate2().mirror2();
		cubes[0].UVs[3]=cubes[0].UVs[1].rotate1().mirror2();
		
		
		cubes[1].UVs[0]=cubes[1].UVs[1]=cubes[1].UVs[2]=cubes[1].UVs[3]=cubes[1].UVs[5]=uvGen.create(52, 40, 0, 0);
		
		
		buff.importComplexCube(cubes);
		
		leverBoxes[2].UVs[1]=new QuadUV(
			w*0,h*56,
			w*0,h*60,
			w*24,h*68,
			w*24,h*64
		).mirror1();
		leverBoxes[2].UVs[0]=leverBoxes[2].UVs[1].mirror2();
		
		leverBoxes[1].UVs[0]=uvGen.create(24, 64, 40, 4).mirror1();
		leverBoxes[1].UVs[1]=leverBoxes[1].UVs[0].mirror2();
		
		leverBoxes[1].UVs[4]=uvGen.create(60, 52, 4, 4).mirror1();
		
		leverBoxes[0].UVs[4]=uvGen.create(58, 48, 8, 4).mirror1();
		leverBoxes[0].UVs[1]=leverBoxes[0].UVs[0]=uvGen.create(38, 52, 12, 4).mirror1();
		
		leverBoxes[0].UVs[5]=uvGen.create(50, 52, 8, 4).mirror1();
		
		leverBoxes[0].UVs[2]=uvGen.create(38, 56, 12, 8).rotate2();
		leverBoxes[0].UVs[3]=uvGen.create(50, 56, 12, 8).rotate1();
		
		leverBoxes[1].UVs[2]=uvGen.create(24, 72, 40, 4).rotate2().mirror2();
		leverBoxes[1].UVs[3]=uvGen.create(24, 68, 40, 4).rotate2();
		
		leverBoxes[2].UVs[3]=uvGen.create(0, 72, 24, 4).rotate2().mirror2();
		leverBoxes[2].UVs[2]=uvGen.create(0, 68, 24, 4).rotate2();
		
		
		
		
		
		leverBoxes[1].willSideRender[5]=
		leverBoxes[2].willSideRender[5]=
		leverBoxes[2].willSideRender[4]=
		plug.willSideRender[5]=false;
		leverBoxes[2].points[2]=leverBoxes[2].points[2].add(0,-p/2,0);
		leverBoxes[2].points[3]=leverBoxes[2].points[3].add(0,-p/2,0);
		leverBoxes[2].points[6]=leverBoxes[2].points[6].add(0,-p/2,0);
		leverBoxes[2].points[7]=leverBoxes[2].points[7].add(0,-p/2,0);
		
		Vec3M trans=new Vec3M(p*10F, p*9.125F, p*8.5F);
		
		Matrix4f matrix=MatrixUtil.createMatrix(trans, 0, 0, 180, 1).finish();
		Matrix4f.mul(matrix, MatrixUtil.createMatrix(trans.mul(-1), 0, 0, 0, 1).finish(), matrix);
		
		leverBoxes[2].transform(matrix);
		
		
		plug.UVs[0]=new QuadUV(
			w*36,h*40,
			w*36,h*48,
			w*52,h*48,
			w*52,h*40
		);
		plug.UVs[2]=plug.UVs[3]=plug.UVs[0].rotate1().mirror1();
		plug.UVs[1]=plug.UVs[0].mirror2();
		plug.UVs[4]=new QuadUV(
			w*28,h*40,
			w*28,h*48,
			w*36,h*48,
			w*36,h*40
		);
		
		for(int i=0;i<3;i++)for(int j=0;j<3;j++){
			plug.translate(-i*p*2, -j*p*2, 0);
			buff.importComplexCube(plug);
			plug.translate(i*p*2, j*p*2, 0);
		}
		
		VertexModel core=buff.exportToNormalisedVertexBufferModel();
		core.glStateCell=new GlStateCell(new GlState(()->{
			
			GL11U.texture(true);
			GL11.glColor3f(1,1,1);
			TessUtil.bindTexture(new ResourceLocation(MReference.MODID,"textures/models/network_router.png"));
			for(int i=0;i<3;i++)for(int j=0;j<3;j++){
				Vec3M rots=insertionAnimation.get(curentTile.animationos[j+i*3].get().value)[0];
				
				rots.y*=rots.y;
				
				
				OpenGLM.pushMatrix();
				Vec3M rot=this.core.rotations[this.core.getCurentSide()];
				GL11U.glRotate(rot,0.5F,0.5F,0.5F);
				OpenGLM.translate(-i*p*2, -j*p*2, 0);
				
				insertionAnimation=new LinearAnimation(
						new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-8, 1, 0)},0F),
						new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-8, 0, 1)},0.4F),
						new DoubleObject<Vec3M[], Float>(new Vec3M[]{new Vec3M(-0, 0, 1)},1F)
					);
				
				if(rots.z>0){
					
					OpenGLM.pushMatrix();
					
					OpenGLM.translate(p*10, p*10, p*6.9F);
					OpenGLM.translate(0,rots.y*p*3,-rots.y*p*3);
					GL11U.glRotate(rots.y*40, 0, 0,0,0,-p*4);
					GL11U.glScale(rots.z);
					OpenGLM.translate(-p*10, -p*10, -p*6.9F);
					
					usb.draw();
					OpenGLM.popMatrix();
				}
					
				GL11U.glRotate(rots.x,0,0, p*10.125F, p*9.75F, p*10F);
				lever.draw();
				OpenGLM.popMatrix();
			}
		}),new GlState(new int[]{GL11.GL_TEXTURE_2D},new int[]{}));
		
		buff.importComplexCube(leverBoxes);
		lever=buff.exportToNormalisedVertexBufferModel();
		
		
		CubeModel[] usbModel={
				new CubeModel(p*9.5F, p*9.5F, p*4.8F, p*10.5F, p*10.5F, p*8),
				new CubeModel(p*9.5F, p*9.5F, p*8F, p*10.5F, p*10.5F, p*9),
				
				new CubeModel(p*9.25F, p*9.25F, p*4.8F, p*9.5F, p*9.5F, p*8),
				new CubeModel(p*10.75F, p*10.75F, p*4.8F, p*10.5F, p*10.5F, p*8),
				
				new CubeModel(p*9.25F, p*9.25F, p*8F, p*9.5F, p*9.5F, p*9),
				new CubeModel(p*10.75F, p*10.75F, p*8F, p*10.5F, p*10.5F, p*9)
		};
		usbModel[0].UVs[2]=new QuadUV(
			w*51.2F,h*16,
			w*51.2F,0,
			0,	  0,
			0,	  h*16
		).mirror1().rotate1();
		usbModel[0].UVs[0]=usbModel[0].UVs[2].rotate1().mirror1();		
		usbModel[0].UVs[1]=new QuadUV(
			w*51.2F,h*32,
			w*51.2F,h*16,
			0,	  h*16,
			0,	  h*32
		).mirror1();
		usbModel[0].UVs[3]=usbModel[0].UVs[1].mirror1().rotate1();
		
		usbModel[0].UVs[4]=new QuadUV(
			w*16,h*56,
			w*16,h*40,
			0,   h*40,
			0,   h*56
		).mirror1();
		
		
		usbModel[1].UVs[2]=new QuadUV(
			w*67,   h*12,
			w*67,   h*4,
			w*51.2F,0,
			w*51.2F,h*16
		).mirror1().rotate1();
		usbModel[1].UVs[0]=usbModel[1].UVs[2].rotate1().mirror1();		
		usbModel[1].UVs[1]=new QuadUV(
			w*67,   h*28,
			w*67,   h*20,
			w*51.2F,h*16,
			w*51.2F,h*32
		).mirror1();
		usbModel[1].UVs[3]=usbModel[1].UVs[1].mirror1().rotate1();
		usbModel[1].UVs[5]=new QuadUV(
			w*24,h*40,
			w*24,h*48,
			w*16,h*48,
			w*16,h*40
		);
		
		
		usbModel[2].UVs[2]=new QuadUV(
			w*51.2F,h*36,
			w*51.2F,h*32,
			0,	  h*32,
			0,	  h*36
		).mirror1().rotate1();
		usbModel[2].UVs[0]=usbModel[2].UVs[2].rotate1().mirror1();		
		usbModel[2].UVs[1]=new QuadUV(
			w*51.2F,h*40,
			w*51.2F,h*36,
			0,	  h*36,
			0,	  h*40
		).mirror1();
		usbModel[2].UVs[3]=usbModel[2].UVs[1].mirror1().rotate1();
		
		usbModel[2].UVs[4]=new QuadUV(
			w*28,h*40,
			w*28,h*44,
			w*24,h*44,
			w*24,h*40
		).mirror1();
		
		usbModel[3].UVs=usbModel[2].UVs;
		
		
		usbModel[4].UVs[2]=new QuadUV(
			w*67,	h*32.8F,
			w*67,	h*32,
			w*51.2F, h*32,
			w*51.2F, h*36
		).mirror1().rotate1();
		usbModel[4].UVs[0]=usbModel[4].UVs[2].rotate1().mirror1();		
		usbModel[4].UVs[1]=new QuadUV(
			w*67,	h*36.8F,
			w*67,	h*36,
			w*51.2F, h*36,
			w*51.2F, h*40
		).mirror1();
		usbModel[4].UVs[3]=usbModel[4].UVs[1].mirror1().rotate1();
		
		usbModel[5].UVs=usbModel[4].UVs;
		
		
		usbModel[0].willSideRender[5]=usbModel[1].willSideRender[4]=false;
		
		usbModel[1].points[2]=usbModel[1].points[2].add(-p/4, -p/4, 0);
		usbModel[1].points[3]=usbModel[1].points[3].add(-p/4, p/4, 0);
		usbModel[1].points[6]=usbModel[1].points[6].add(p/4, -p/4, 0);
		usbModel[1].points[7]=usbModel[1].points[7].add(p/4, p/4, 0);
		
		
		usbModel[4].points[2]=usbModel[1].points[7];
		usbModel[4].points[3]=usbModel[4].points[2].add(
				usbModel[1].points[7].sub(usbModel[4].points[3]).mul(-0.05, -0.05, 0));
		usbModel[4].points[6]=usbModel[4].points[2].add(
				usbModel[1].points[7].sub(usbModel[4].points[6]).mul(-0.05, -0.05, 0));
		usbModel[4].points[7]=usbModel[4].points[2].add(
				usbModel[1].points[7].sub(usbModel[4].points[7]).mul(-0.05, -0.05, 0));
		
		float mul=-0.1F;
		usbModel[5].points[7]=usbModel[1].points[2];
		usbModel[5].points[3]=usbModel[5].points[7].add(
				usbModel[1].points[2].sub(usbModel[5].points[3]).mul(mul, mul, 0));
		usbModel[5].points[6]=usbModel[5].points[7].add(
				usbModel[1].points[2].sub(usbModel[5].points[6]).mul(mul, mul, 0));
		usbModel[5].points[2]=usbModel[5].points[7].add(
				usbModel[1].points[2].sub(usbModel[5].points[2]).mul(mul, mul, 0));
		
		
		
		usbModel[4].willSideRender[4]=usbModel[5].willSideRender[4]=usbModel[4].willSideRender[5]=usbModel[5].willSideRender[5]=false;
		usbModel[2].willSideRender[5]=usbModel[3].willSideRender[5]=false;
		
		buff.importComplexCube(usbModel);
		usb=buff.exportToNormalisedVertexBufferModel();
		
		
		this.core=new SidedModel(
			new DoubleObject<VertexModel[],int[]>(
				new VertexModel[]{
					core
				},
				new int[]{
					1,
					3,
					5,
					0,
					2,
					4
				}
			)
		);
	}
	
	
	
	@Override
	public void renderTileEntityAt(TileEntity tile0, double x, double y, double z, float pt){
		initModels();
		
		
		TileEntityNetworkRouter tile=curentTile=(TileEntityNetworkRouter)tile0;
		boolean[] connectionsSides=new boolean[6],coreSides=new boolean[6];
		int or=SideUtil.getOppositeSide(tile.getOrientation());
		for(int i=0;i<6;i++)connectionsSides[i]=(coreSides[i]=i==or)&&tile.getConnections()[i].getMain();
		
		
		OpenGLM.pushMatrix();
		OpenGLM.translate(x,y,z);
		
		core.draw(coreSides);
		connections.draw(connectionsSides);
		
		
		OpenGLM.popMatrix();
	}

}
