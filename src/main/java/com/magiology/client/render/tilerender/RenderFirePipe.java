package com.magiology.client.render.tilerender;

import com.magiology.client.render.Textures;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexModel;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.Get.Render;
import com.magiology.util.utilobjects.m_extension.TileEntitySpecialRendererM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class RenderFirePipe extends TileEntitySpecialRendererM {
	VertexRenderer buf=TessUtil.getVB();
	private VertexModel[] conectionToObjModel=new VertexModel[2];
	private VertexModel conectorFFLModel;
	private VertexModel modelStand;
	private final float p= 1F/16F;
	private VertexModel strateCoreModel;
	private final float tH=1F/16F;
	private final float tHC=1F/40F;
	private final float tHFSL=1F/38F;
	private final float tHS=1F/32F;
	private final float tW=1F/131F;
	
	
	private final float tWC=1F/4F;
	private final float tWFSL=1F/62F;
	private final float tWS=1F/16F;
	
	
	
	private void drawConectionToObj(EnumFacing dir, String type){
		if(conectionToObjModel[0]==null)generateModelConectionToObj();
		for(int i=0;i<2;i++){
			conectionToObjModel[i].pushMatrix();
				 if(dir.equals(EnumFacing.UP))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 0, -90);
			else if(dir.equals(EnumFacing.DOWN))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 0, 90);
			else if(dir.equals(EnumFacing.EAST))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 180, 0);
			else if(dir.equals(EnumFacing.NORTH))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, -90, 0);
			else if(dir.equals(EnumFacing.WEST))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 0, 0);
			else if(dir.equals(EnumFacing.SOUTH))conectionToObjModel[i].rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
		}
		
		if(type=="inMe")bindTexture(Textures.FirePipeConecterInMe);
		else if(type=="outOfMe")bindTexture(Textures.FirePipeConecterOutOfMe);
		conectionToObjModel[0].draw();
		bindTexture(Textures.FirePipeConecterBase);
		conectionToObjModel[1].draw();
		
		conectionToObjModel[0].popMatrix();
		conectionToObjModel[1].popMatrix();
	}
	public void drawConector(EnumFacing dir){
		buf.pushMatrix();
		bindTexture(Textures.FirePipeConection);
		float rX=0,rY=0,rZ=0;
		int t=-1;
		double tw1=-1,tw2=-1,th1=-1,th2=-1;
		boolean[] flipTH=new boolean[5];
		
		if(dir.equals(EnumFacing.WEST)){t=1;}
		else if (dir.equals(EnumFacing.UP)){rZ=-90;t=1;}
		else if (dir.equals(EnumFacing.DOWN)){rZ=90;t=2;}//texture2
		else if (dir.equals(EnumFacing.SOUTH)){rY=90;t=1;}
		else if (dir.equals(EnumFacing.EAST)){rY=180;t=3;}//texture2
		else if (dir.equals(EnumFacing.NORTH)){rY=-90;t=3;}//texture2
		
		switch(t){
		case(1):{th1=tH*16;th2=0;tw1=0;tw2=tW*28;}break;
		case(2):{th1=tH*16;th2=0;tw1=1;tw2=tW*103;flipTH[1]=true;flipTH[2]=true;}break;
		case(3):{th1=tH*16;th2=0;tw1=1;tw2=tW*103;flipTH[3]=true;flipTH[4]=true;}break;
		default:{th1=1;th2=0;tw1=1;tw2=0;}break;}
		buf.cleanUp();
		buf.addVertexWithUV(p*0, p*9.5, p*9.5,tw2,flipTH[1]?th1:th2);
		buf.addVertexWithUV(p*0, p*6.5, p*9.5,tw2,flipTH[1]?th2:th1);
		buf.addVertexWithUV(p*6, p*6.5, p*9.5,tw1,flipTH[1]?th2:th1);
		buf.addVertexWithUV(p*6, p*9.5, p*9.5,tw1,flipTH[1]?th1:th2);

		buf.addVertexWithUV(p*6, p*9.5, p*6.5,tw1,flipTH[2]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*6.5,tw1,flipTH[2]?th2:th1);
		buf.addVertexWithUV(p*0, p*6.5, p*6.5,tw2,flipTH[2]?th2:th1);
		buf.addVertexWithUV(p*0, p*9.5, p*6.5,tw2,flipTH[2]?th1:th2);
		
		buf.addVertexWithUV(p*6, p*9.5, p*9.5,tw1,flipTH[3]?th2:th1);
		buf.addVertexWithUV(p*6, p*9.5, p*6.5,tw1,flipTH[3]?th1:th2);
		buf.addVertexWithUV(p*0, p*9.5, p*6.5,tw2,flipTH[3]?th1:th2);
		buf.addVertexWithUV(p*0, p*9.5, p*9.5,tw2,flipTH[3]?th2:th1);

		buf.addVertexWithUV(p*0, p*6.5, p*9.5,tw2,flipTH[4]?th2:th1);
		buf.addVertexWithUV(p*0, p*6.5, p*6.5,tw2,flipTH[4]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*6.5,tw1,flipTH[4]?th1:th2);
		buf.addVertexWithUV(p*6, p*6.5, p*9.5,tw1,flipTH[4]?th2:th1);
		
		buf.rotateAt(0.5F,0.5F,0.5F,rX, rY, rZ);
		buf.draw();
		buf.popMatrix();
	}
	public void drawConectorFFL(){
		bindTexture(Textures.FirePipeConectionFSL);
		if(conectorFFLModel==null)generateModelConectorFFL();
		else conectorFFLModel.draw();
	}
	public void drawCore(int txAnim){
		bindTexture(Textures.firePipeCore);
		float teHC=tHC*txAnim*4;
		buf.cleanUp();
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6,  p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*6,  p*10,tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*10,tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*6,  p*10, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6,  p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6 , p*10, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*6 , p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4-teHC);
		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4-teHC);
		
		buf.addVertexWithUV(p*6, p*6, p*10, tWC*0, tHC*4-teHC);
		buf.addVertexWithUV(p*6, p*6, p*6, tWC*0, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0-teHC);
		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*4-teHC);
		buf.draw();
	}
	private void drawStand(EnumFacing dir){
		if(modelStand==null)generateModelStand();
		modelStand.pushMatrix();
		if(dir.equals(EnumFacing.UP))modelStand.rotateAt(0.5, 0.5, 0.5, 0, 0, 180);
		bindTexture(Textures.FirePipeConectionFF);
		modelStand.draw();
		modelStand.popMatrix();
	}
	private void drawStrateCore(EnumFacing dir){
		if(strateCoreModel==null)generateModelStrateCore();
		strateCoreModel.pushMatrix();
		if(dir.equals(EnumFacing.UP))strateCoreModel.rotateAt(0.5, 0.5, 0.5, 0, 0, -90);
		else if(dir.equals(EnumFacing.SOUTH))strateCoreModel.rotateAt(0.5, 0.5, 0.5, 0, 90, 0);
		bindTexture(Textures.FirePipeConection);
		strateCoreModel.draw();
		
		strateCoreModel.popMatrix();
	}
	
	private void generateModelConectionToObj(){
		buf.cleanUp();
		buf.addVertexWithUV(p*0,   p*10, p*10,1, 0);
		buf.addVertexWithUV(p*0,   p*6,  p*10,1, 1);
		buf.addVertexWithUV(p*1.5, p*6,  p*10,0, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*10,0, 0);

		buf.addVertexWithUV(p*1.5, p*10, p*6,0, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*6,0, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*6,1, 1);
		buf.addVertexWithUV(p*0,   p*10, p*6,1, 0);
			
		buf.addVertexWithUV(p*0,   p*10, p*6,1, 0);
		buf.addVertexWithUV(p*0,   p*10, p*10,1, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*10,0, 1);
		buf.addVertexWithUV(p*1.5, p*10, p*6,0, 0);
			
		buf.addVertexWithUV(p*1.5, p*6,  p*6,0, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*10,0, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*10,1, 1);
		buf.addVertexWithUV(p*0,   p*6,  p*6,1, 0);
		conectionToObjModel[0]=buf.exportToNormalisedVertexBufferModel();
		
		buf.addVertexWithUV(p*1.5, p*10, p*10, 1, 1);
		buf.addVertexWithUV(p*1.5, p*6,  p*10, 1, 0);
		buf.addVertexWithUV(p*1.5, p*6,  p*6,  0, 0);
		buf.addVertexWithUV(p*1.5, p*10, p*6,  0, 1);
		conectionToObjModel[1]=buf.exportToNormalisedVertexBufferModel();
	}
	private void generateModelConectorFFL(){
		buf.cleanUp();
		buf.addVertexWithUV(p*6.5,  p*6,	 p*9.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,	 p*9.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*9.5,  p*6,	 p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,	 p*6.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*6.5,  p*6,	 p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,	 p*9.5,tWFSL*8, 0);

		buf.addVertexWithUV(p*9.5,  p*6,	 p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,	 p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*9.5,  p*6,	 p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,	 p*9.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*6.5,  p*6,	 p*6.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,	 p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*6.5,  p*6,	 p*9.5,tWFSL*8, 0);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*6.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*6.5,  p*6,	 p*6.5,tWFSL*0, 0);

		buf.addVertexWithUV(p*9.5,  p*6,	 p*6.5,tWFSL*0, 0);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*6.5,tWFSL*0, tHFSL*24);
		buf.addVertexWithUV(p*9.5, -p*0.78,  p*9.5,tWFSL*8, tHFSL*24);
		buf.addVertexWithUV(p*9.5,  p*6,	 p*9.5,tWFSL*8, 0);
		
		buf.addVertexWithUV(p*4.5, -p*2.78,  p*11.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*4.5, -p*4.78,  p*11.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*4.78,  p*11.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*2.78,  p*11.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*11.5,-p*2.78,  p*4.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*4.78,  p*4.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*4.78,  p*4.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*2.78,  p*4.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*4.5, -p*2.78, p*4.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*4.5, -p*4.78, p*4.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*4.78, p*11.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*4.5, -p*2.78, p*11.5,tWFSL*37, tHFSL*0);

		buf.addVertexWithUV(p*11.5,-p*2.78, p*11.5,tWFSL*9, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*4.78, p*11.5,tWFSL*9, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*4.78, p*4.5,tWFSL*37, tHFSL*8);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*4.5,tWFSL*37, tHFSL*0);
		
		buf.addVertexWithUV(p*4.5, -p*2.78, p*4.5,tWFSL*38, tHFSL*24);
		buf.addVertexWithUV(p*4.5, -p*2.78, p*11.5,tWFSL*38, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*11.5,tWFSL*62, tHFSL*0);
		buf.addVertexWithUV(p*11.5,-p*2.78, p*4.5,tWFSL*62, tHFSL*24);
		
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*9, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*5.5,tWFSL*9, tHFSL*18);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*10.5,tWFSL*9, tHFSL*38);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*38);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*18);
		
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*5.5, -p*0.78, p*10.5,tWFSL*29,tHFSL*9);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*10.5,tWFSL*29,tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*2.78, p*5.5,tWFSL*9,  tHFSL*17);
		buf.addVertexWithUV(p*5.5, -p*0.78, p*5.5,tWFSL*9,  tHFSL*9);

		buf.addVertexWithUV(p*10.5,-p*0.78, p*5.5,tWFSL*29, tHFSL*9);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*5.5,tWFSL*29, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*2.78, p*10.5,tWFSL*9, tHFSL*17);
		buf.addVertexWithUV(p*10.5,-p*0.78, p*10.5,tWFSL*9, tHFSL*9);

		buf.addVertexWithUV(p*10.5, -p*0.78, p*5.5,tWFSL*29, tHFSL*18);
		buf.addVertexWithUV(p*10.5, -p*0.78, p*10.5,tWFSL*29, tHFSL*38);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*10.5,tWFSL*9, tHFSL*38);
		buf.addVertexWithUV(p*5.5, -p*0.78,  p*5.5,tWFSL*9, tHFSL*18);
		conectorFFLModel=buf.exportToNormalisedVertexBufferModel();
	}
	private void generateModelStand(){
		buf.cleanUp();
		buf.addVertexWithUV(p*7.5,  p*6, p*8.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*6, p*8.5,  tWS*8, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*6, p*7.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*6, p*7.5,  tWS*8, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*6, p*7.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*7.5,  p*6, p*8.5,  tWS*8, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*6, p*8.5,  tWS*0, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*0, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*24);
		buf.addVertexWithUV(p*8.5,  p*6, p*7.5,  tWS*8, tHS*0);
		
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*10.5, tWS*8,  tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*10.5, tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*9.5,  tWS*8,  tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*9.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8,  tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*10.5, tWS*8,  tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*9.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
	 	
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*9.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*10.5, tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*6.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*6.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*5.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*5.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*8.5,  p*0, p*6.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*0, p*5.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*7.5,  p*0, p*5.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*0, p*6.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*6.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*6.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*5.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*5.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*5.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*6.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);

		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*6.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*5.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*10.5, p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*10.5, p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*9.5,  p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*9.5,  p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*7.5,  p*3, p*8.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*9.5,  p*0, p*8.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*10.5, p*0, p*8.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*8.5,  p*3, p*8.5,  tWS*16, tHS*0);
		
		buf.addVertexWithUV(p*8.5,  p*3, p*7.5,  tWS*8, tHS*0);
		buf.addVertexWithUV(p*10.5, p*0, p*7.5,  tWS*8, tHS*32);
		buf.addVertexWithUV(p*9.5,  p*0, p*7.5,  tWS*16, tHS*32);
		buf.addVertexWithUV(p*7.5,  p*3, p*7.5,  tWS*16, tHS*0);
		modelStand=buf.exportToNormalisedVertexBufferModel();
	}
	private void generateModelStrateCore(){
		buf.cleanUp();
		buf.addVertexWithUV(0, p*9.5, p*9.5,tW*103, tH*0);
		buf.addVertexWithUV(0, p*6.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(1, p*6.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*9.5,tW*28, tH*0);

		buf.addVertexWithUV(1, p*9.5, p*6.5,tW*28, tH*0);
		buf.addVertexWithUV(1, p*6.5, p*6.5,tW*28, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*6.5,tW*103, tH*16);
		buf.addVertexWithUV(0, p*9.5, p*6.5,tW*103, tH*0);

		buf.addVertexWithUV(0, p*9.5, p*6.5,tW*103, tH*0);
		buf.addVertexWithUV(0, p*9.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(1, p*9.5, p*6.5,tW*28, tH*0);

		buf.addVertexWithUV(1, p*6.5, p*6.5,tW*28, tH*0);
		buf.addVertexWithUV(1, p*6.5, p*9.5,tW*28, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*9.5,tW*103, tH*16);
		buf.addVertexWithUV(0, p*6.5, p*6.5,tW*103, tH*0);
		strateCoreModel=buf.exportToNormalisedVertexBufferModel();
	}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f){
		GL11U.protect();
		TileEntityFirePipe pipe=(TileEntityFirePipe) tileentity;
		Render.WR().setTranslation(x,y,z);
		if(pipe.DCFFL)drawConectorFFL();
		for(int i=0;i<pipe.connections.length;i++){
			if(pipe.connections[i].willRender()){
				if(pipe.connections[i].getIn())drawConectionToObj(pipe.connections[i].getFaceEF(),"outOfMe");
				else if(pipe.connections[i].getOut())drawConectionToObj(pipe.connections[i].getFaceEF(),"inMe");
			}
		}
		
		if(!pipe.isStrate(null)){
			drawCore(pipe.texAnim);
			for(int i=0; i< pipe.connections.length; i++)if(pipe.connections[i].getMain()&&pipe.connections[i].willRender())drawConector(pipe.connections[i].getFaceEF());
			if(pipe.isSolidDown==true)drawStand(EnumFacing.DOWN);
			else if(pipe.isSolidUp==true)drawStand(EnumFacing.UP);
		}else{
			for(int a=0;a<3;a++){
				int b=a==0?3:a==1?4:1;
				if(pipe.isStrate(EnumFacing.getFront(b))){
					drawStrateCore(EnumFacing.getFront(b));
				}
			}
		}
		Render.WR().setTranslation(0,0,0);
		GL11U.endProtection();
	}
}
