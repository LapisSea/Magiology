package com.magiology.client.render.aftereffect;

import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;

public class RenderFirePipeGlow extends LongAfterRenderRendererBase{
	
	private static VertexModel connectorGlowModel;
	private static VertexModel coreGlowModel;
	private static EntityPlayer player=UtilC.getMC().thePlayer;
	private static VertexModel strateCoreModel;
	public  double alpha=0,prevAlpha;
	
	VertexRenderer buf=TessUtil.getVB();

	private final float p= 1F/16F;
	

	public TileEntityFirePipe pipe;
	public RenderFirePipeGlow(TileEntityFirePipe pipe){
		this.pipe=pipe;
		alpha=0.1;
	}
	private void drawConnectorGlow(EnumFacing dir){
		if(connectorGlowModel==null)generateModelConnectorGlow();
		else{
			float rX=0,rY=0,rZ=0;
			if(dir.equals(EnumFacing.WEST)){}
			else if (dir.equals(EnumFacing.UP)){rZ=-90;}
			else if (dir.equals(EnumFacing.DOWN)){rZ=90;}
			else if (dir.equals(EnumFacing.SOUTH)){rY=90;}
			else if (dir.equals(EnumFacing.EAST)){rY=-180;}
			else if (dir.equals(EnumFacing.NORTH)){rY=-90;}
			
			OpenGLM.pushMatrix();
			OpenGLM.translate(0.5F, 0.5F, 0.5F);
			GL11U.glRotate(rX, rY, rZ);
			OpenGLM.translate(-0.5F, -0.5F, -0.5F);
			connectorGlowModel.draw();
			OpenGLM.popMatrix();
		}
	}
	
	
	private void drawCoreGlow(){
		if(coreGlowModel==null)generateModelCoreGlow();
		else coreGlowModel.draw();
	}
	private void drawStrateCoreGlow(EnumFacing dir){
		OpenGLM.pushMatrix();
		OpenGLM.translate(0.5F, 0.5F, 0.5F);
		if (dir.equals(EnumFacing.UP))OpenGLM.rotate(-90, 0, 0, 1);
		else if (dir.equals(EnumFacing.SOUTH))OpenGLM.rotate(90, 0, 1, 0);
		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
		if(strateCoreModel==null)generateModelStrateCoreGlow();
		else strateCoreModel.draw();
		OpenGLM.popMatrix();
	}
	private void generateModelConnectorGlow(){
		buf.cleanUp();
		CubeModel 
		c1=new CubeModel(p*3, p*10.5F, p*10.5F,p*5.5F,p*10,   p*10),
		c2=new CubeModel(p*3, p*10.5F, p*6F,   p*5.5F,p*10,   p*5.5F),
		c3=new CubeModel(p*3, p*6F,	p*10.5F,p*5.5F,p*5.5F, p*10),
		c4=new CubeModel(p*3, p*6F,	p*6F,   p*5.5F,p*5.5F, p*5.5F),
		c5=new CubeModel(0,   p*9.5F,  p*9.5F, p*3,   p*10F,  p*10F),
		c6=new CubeModel(0,   p*9.5F,  p*6F,   p*3,   p*10,   p*6.5F),
		c7=new CubeModel(0,   p*6,  p*9.5F, p*3,   p*6.5F,  p*10F),
		c8=new CubeModel(0,   p*6,  p*6F,   p*3,   p*6.5F,   p*6.5F);
		c1.willSideRender[0]=c1.willSideRender[1]=
		c2.willSideRender[0]=c2.willSideRender[1]=
		c3.willSideRender[0]=c3.willSideRender[1]=
		c4.willSideRender[0]=c4.willSideRender[1]=
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=false;
		c1.points[4].y-=p*0.5;c1.points[5].y-=p*0.5;c1.points[6].y-=p*0.5;c1.points[7].y-=p*0.5;c1.points[4].z-=p*0.5;c1.points[5].z-=p*0.5;c1.points[6].z-=p*0.5;c1.points[7].z-=p*0.5;
		c2.points[4].y-=p*0.5;c2.points[5].y-=p*0.5;c2.points[6].y-=p*0.5;c2.points[7].y-=p*0.5;c2.points[4].z+=p*0.5;c2.points[5].z+=p*0.5;c2.points[6].z+=p*0.5;c2.points[7].z+=p*0.5;
		c3.points[4].y+=p*0.5;c3.points[5].y+=p*0.5;c3.points[6].y+=p*0.5;c3.points[7].y+=p*0.5;c3.points[4].z-=p*0.5;c3.points[5].z-=p*0.5;c3.points[6].z-=p*0.5;c3.points[7].z-=p*0.5;
		c4.points[4].y+=p*0.5;c4.points[5].y+=p*0.5;c4.points[6].y+=p*0.5;c4.points[7].y+=p*0.5;c4.points[4].z+=p*0.5;c4.points[5].z+=p*0.5;c4.points[6].z+=p*0.5;c4.points[7].z+=p*0.5;
		buf.importComplexCube(c1,c2,c3,c4,c5,c6,c7,c8);
		connectorGlowModel=buf.exportToNormalisedVertexBufferModel();
	}
	
	private void generateModelCoreGlow(){
		buf.cleanUp();
		CubeModel 
		c1=new CubeModel(p*10,   p*10,   p*6,p*10.5F, p*10.5F, p*10),
		c2=new CubeModel(p*10,   p*5.5F, p*6,p*10.5F, p*6F,	p*10),
		c3=new CubeModel(p*5.5F, p*10,   p*6,p*6,	 p*10.5F, p*10),
		c4=new CubeModel(p*5.5F, p*5.5F, p*6,p*6,	 p*6F,	p*10),
		
		c5=new CubeModel(p*6F, p*10F,  p*5.5F, p*10F, p*10.5F, p*6F),
		c6=new CubeModel(p*6F, p*5.5F, p*5.5F, p*10F, p*6F,	p*6F),
		c7=new CubeModel(p*6F, p*10F,  p*10F,  p*10F, p*10.5F, p*10.5F),
		c8=new CubeModel(p*6F, p*5.5F, p*10F,  p*10F, p*6F,	p*10.5F),
		
		c9 =new CubeModel(p*10.5F,p*10F, p*10F, p*10F,  p*6F, p*10.5F),
		c10=new CubeModel(p*6F,   p*10F, p*10F, p*5.5F, p*6F, p*10.5F),
		c11=new CubeModel(p*10.5F,p*10F, p*5.5F, p*10F,  p*6F, p*6),
		c12=new CubeModel(p*6F,   p*10F, p*5.5F, p*5.5F, p*6F, p*6),
		
		c13=new CubeModel(p*10F,  p*10, p*10,   p*10.5F,p*10.5F, p*10.5F),
		c14=new CubeModel(p*5.5F, p*10, p*10,   p*6,	p*10.5F, p*10.5F),
		c15=new CubeModel(p*5.5F, p*10, p*5.5F, p*6,	p*10.5F, p*6),
		c16=new CubeModel(p*10,   p*10, p*5.5F, p*10.5F,p*10.5F, p*6),
		
		c17=new CubeModel(p*10F,  p*5.5F, p*10,   p*10.5F,p*6, p*10.5F),
		c18=new CubeModel(p*5.5F, p*5.5F, p*10,   p*6,	p*6, p*10.5F),
		c19=new CubeModel(p*5.5F, p*5.5F, p*5.5F, p*6,	p*6, p*6),
		c20=new CubeModel(p*10,   p*5.5F, p*5.5F, p*10.5F,p*6, p*6);
		c1.willSideRender[4]=c1.willSideRender[5]=
		c2.willSideRender[4]=c2.willSideRender[5]=
		c3.willSideRender[4]=c3.willSideRender[5]=
		c4.willSideRender[4]=c4.willSideRender[5]=
		
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=
		
		 c9.willSideRender[2]= c9.willSideRender[3]=
		c10.willSideRender[2]=c10.willSideRender[3]=
		c11.willSideRender[2]=c11.willSideRender[3]=
		c12.willSideRender[2]=c12.willSideRender[3]=
		
		c13.willSideRender[1]=c13.willSideRender[3]=c13.willSideRender[4]=
		c14.willSideRender[0]=c14.willSideRender[3]=c14.willSideRender[4]=
		c15.willSideRender[0]=c15.willSideRender[3]=c15.willSideRender[5]=
		c16.willSideRender[1]=c16.willSideRender[3]=c16.willSideRender[5]=
		c17.willSideRender[1]=c17.willSideRender[2]=c17.willSideRender[4]=
		c18.willSideRender[2]=c18.willSideRender[0]=c18.willSideRender[4]=
		c19.willSideRender[2]=c19.willSideRender[0]=c19.willSideRender[5]=
		c20.willSideRender[2]=c20.willSideRender[1]=c20.willSideRender[5]=
		false;
		buf.importComplexCube(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16,c17,c18,c19,c20);
		coreGlowModel=buf.exportToNormalisedVertexBufferModel();
	}
	private void generateModelStrateCoreGlow(){
		buf.cleanUp();
		CubeModel
		c5=new CubeModel(0,   p*9.5F,  p*9.5F, 1,   p*10F,  p*10F),
		c6=new CubeModel(0,   p*9.5F,  p*6F,   1,   p*10,   p*6.5F),
		c7=new CubeModel(0,   p*6,	 p*9.5F, 1,   p*6.5F,  p*10F),
		c8=new CubeModel(0,   p*6,	 p*6F,   1,   p*6.5F,   p*6.5F);
		c5.willSideRender[0]=c5.willSideRender[1]=
		c6.willSideRender[0]=c6.willSideRender[1]=
		c7.willSideRender[0]=c7.willSideRender[1]=
		c8.willSideRender[0]=c8.willSideRender[1]=
		false;
		buf.importComplexCube(c5,c6,c7,c8);
		strateCoreModel=buf.exportToNormalisedVertexBufferModel();
	}
	@Override
	public void render(){
		float fc=MathUtil.snap(PowerUtil.getPowerPrecentage(pipe), 0, 1);
		if(fc>0.01){
			OpenGLM.pushMatrix();
			OpenGLM.translate(pipe.x(), pipe.y(), pipe.z());
			OpenGLM.disableTexture2D();
			GL11U.setUpOpaqueRendering(1);
			
			double var1=UtilC.fluctuate(20,(pipe.x()+pipe.y()+pipe.z())*4),
				   var2=UtilC.fluctuate(47,(pipe.x()+pipe.y()+pipe.z())*4);
			
			OpenGLM.color(0.9, 0.1*var1, 0.15*var2, 0.6*fc*PartialTicksUtil.calculate(prevAlpha, alpha));
			OpenGLM.depthMask(true);
			if(!pipe.isStrate(null)){
				for(int i=0; i< pipe.connections.length; i++)if(pipe.connections[i].getMain()&&pipe.connections[i].willRender())drawConnectorGlow(pipe.connections[i].getFaceEF());
				drawCoreGlow();
			}else for(int a=0;a<3;a++){
				int b=a;
				if(a==0)b=3;
				else if(a==1)b=4;
				else if(a==2)b=1;
				if(pipe.isStrate(EnumFacing.getFront(b)))drawStrateCoreGlow(EnumFacing.getFront(b));
			}
			
			GL11U.endOpaqueRendering();
			OpenGLM.color(1,1,1,1);
			OpenGLM.enableTexture2D();
			OpenGLM.popMatrix();
		}
	}
	
	
	@Override
	public void update(){
		player=UtilC.getThePlayer();
		if(player==null)return;
		
		prevAlpha=alpha;
		
		alpha+=0.4*(UtilM.isItemInStack(MItems.fireHammer, player.getHeldItemMainhand())?1:-1);
		
		if(!(player.worldObj.getTileEntity(pipe.getPos())instanceof TileEntityFirePipe))kill();
		
		alpha=MathUtil.snap(alpha, 0, 1);
		if(alpha<0.05)kill();
	}
}
