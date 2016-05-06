package com.magiology.client.render.tilerender.network;

import org.lwjgl.opengl.GL11;

import com.magiology.api.connection.IConnectionProvider;
import com.magiology.api.network.NetworkBaseComponent;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkInterface;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.glstates.GlState;
import com.magiology.util.renderers.glstates.GlStateCell;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.renderers.tessellatorscripts.SidedModel;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.tileentity.TileEntity;


public class RenderNetworkInterface extends RenderNetworkConductor{
	
	private TileEntityNetworkInterface curentTile;
	protected SidedModel interfacePlate;
	
	public RenderNetworkInterface(){
		super();
	}
	@Override
	protected void initModels(){
		super.initModels();
		VertexRenderer buff=TessUtil.getVB();
		
		buff.importComplexCube(new CubeModel(p*6,p*6,0,p*10,p*10,p*2));
		VertexModel centerCube=buff.exportToNormalisedVertexBufferModel();
		centerCube.glStateCell=new GlStateCell(new GlState(new int[]{}, new int[]{GL11.GL_TEXTURE_2D},()->{GL11.glColor3f(0.1F, 0.1F, 0.1F);}), null);
		
		
		for(int i=0;i<6;i++){
			int angle=i*60;
			double 
				sin1=Math.sin(Math.toRadians(angle)),
				sin2=Math.sin(Math.toRadians(angle+60)),
				cos1=Math.cos(Math.toRadians(angle)),
				cos2=Math.cos(Math.toRadians(angle+60)),
				
				xIn1=sin1*p*5+0.5,
				xOut1=sin1*p*6+0.5,
				xIn2=sin2*p*5+0.5,
				xOut2=sin2*p*6+0.5,
				
				yIn1=cos1*p*5+0.5,
				yOut1=cos1*p*6+0.5,
				yIn2=cos2*p*5+0.5,
				yOut2=cos2*p*6+0.5;
			
			buff.addVertex(xIn1,  yIn1,  0);
			buff.addVertex(xOut1, yOut1, 0);
			buff.addVertex(xOut2, yOut2, 0);
			buff.addVertex(xIn2,  yIn2,  0);
			
			buff.addVertex(xOut1, yOut1, p/2);
			buff.addVertex(xIn1,  yIn1,  p/2);
			buff.addVertex(xIn2,  yIn2,  p/2);
			buff.addVertex(xOut2, yOut2, p/2);
			
			buff.addVertex(xOut2, yOut2, 0);
			buff.addVertex(xOut1, yOut1, 0);
			buff.addVertex(xOut1, yOut1, p/2);
			buff.addVertex(xOut2, yOut2, p/2);

			buff.addVertex(xIn1,  yIn1,  0);
			buff.addVertex(xIn2,  yIn2,  0);
			buff.addVertex(xIn2,  yIn2,  p/2);
			buff.addVertex(xIn1,  yIn1,  p/2);
		};
		VertexModel plate=buff.exportToNormalisedVertexBufferModel();
		plate.glStateCell=new GlStateCell(new GlState(()->{

			long offset=curentTile.x()*7-curentTile.y()*15+curentTile.z()*9;
			GL11.glColor3f(UtilC.fluctuateSmooth(80, offset), UtilC.fluctuateSmooth(134, 40+offset), UtilC.fluctuateSmooth(156, 56+offset));
			
			if(curentTile.getBrain()!=null){
				OpenGLM.pushMatrix();
				Vec3M rotation=null,translation=null;
				
				switch (interfacePlate.getCurentSide()){
				case 0:rotation=new Vec3M( 90,   0, 0);translation=new Vec3M( 0.5,  0.5,  -1);break;
				case 1:rotation=new Vec3M(-90,   0, 0);translation=new Vec3M( 0.5, -0.5,   0);break;
				case 2:rotation=new Vec3M(  0, 180, 0);translation=new Vec3M(-0.5,  0.5, -1);break;
				case 3:rotation=new Vec3M(  0,   0, 0);translation=new Vec3M( 0.5,  0.5,   0);break;
				case 4:rotation=new Vec3M(  0, -90, 0);translation=new Vec3M( 0.5,  0.5,  -1);break;
				case 5:rotation=new Vec3M(  0,  90, 0);translation=new Vec3M(-0.5,  0.5,   0);break;
				}
				double tim=UtilM.getWorldTime(getWorld())+PartialTicksUtil.partialTicks;
				
				float bob1=(float)Math.sin(tim/10+offset);
				float bob2=(float)Math.cos(tim/13+offset);
				
				GL11U.glRotate(rotation);
				OpenGLM.translate(0, 0, bob1*p/2+p/2);
				GL11U.glTranslate(translation);
				GL11U.glRotate(0, 0, 90);
				GL11U.glScale(0.9-bob2*0.2);
				GL11U.glTranslate(translation.mul(-1));
				GL11U.glRotate(rotation.mul(-1));
			}
			
		}), new GlState(new int[]{GL11.GL_TEXTURE_2D}, new int[]{},()->{
			if(curentTile.getBrain()!=null)OpenGLM.popMatrix();
		}));
		
		interfacePlate=new SidedModel(new DoubleObject<VertexModel[], int[]>(
			new VertexModel[]{
					centerCube,
					plate
			},
			new int[]{
				0,
				1,
				2,
				3,
				4,
				5
			}
		));
		plate.setRenderer(Renderer.POS_UV);
	}
	
	@Override
	protected <NetworkComponent extends IConnectionProvider & NetworkBaseComponent> void renderNetworkPipe(NetworkComponent networkComponent, double x, double y, double z) {
		super.renderNetworkPipe(networkComponent, x, y, z);
		OpenGLM.pushMatrix();
		OpenGLM.translate(x, y, z);
		int side=((TileEntityNetworkInterface)networkComponent).getOrientation();
		boolean[] sides=new boolean[6];
		for(int i=0;i<6;i++)sides[i]=side==i;
		interfacePlate.draw(sides);
		OpenGLM.popMatrix();
		GL11U.glColor(ColorF.WHITE);
	}
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float pt){
		curentTile=(TileEntityNetworkInterface)tile;
//		if(UtilM.getThePlayer().isSneaking())
			initModels();
		renderNetworkPipe(curentTile, x, y, z);
	}
}
