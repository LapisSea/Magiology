package com.magiology.util.renderers;

import static java.lang.Math.*;

import java.lang.reflect.Field;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.client.render.Textures;
import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.core.MReference;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.AdvancedModelLoader;
import com.magiology.handlers.obj.handler.revived.yayformc1_8.IModelCustom;
import com.magiology.mcobjects.effect.EntityFXM;
import com.magiology.util.renderers.tessellatorscripts.CubeModel;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilclasses.math.MatrixUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;
import com.magiology.util.utilobjects.vectors.QuadUV;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;


/**
 * Tessellator Helper
 * @author LapisSea
 */
public class TessUtil{
	
	private static IModelCustom arrowModel;
	private static IModelCustom ballModel;
	private static CubeModel brainC1,brainC2;
	private static VertexRenderer buf=new VertexRenderer();
	static Field equippedProgress,prevEquippedProgress;
	private static FontRendererMBase fontRendererMBase=new FontRendererMBase(new ResourceLocation("textures/font/ascii.png"));
	
	
	static final ResourceLocation inventoryPict = new ResourceLocation("textures/gui/container/inventory.png");
	private static IModelCustom SV98;
	static{
		QuadUV uv=new QuadUV(0, 0, 1, 0, 1, 1, 0, 1);
		brainC1=new CubeModel(-UtilM.p, -UtilM.p, -UtilM.p, 0, 0, 0,new QuadUV[]{uv},new ResourceLocation[]{Textures.Brain});
		brainC2=new CubeModel(0, 0, 0 , UtilM.p, UtilM.p, UtilM.p,new QuadUV[]{uv},new ResourceLocation[]{Textures.Brain});
	}
	public static void bindTexture(ResourceLocation texture){U.getMC().getTextureManager().bindTexture(texture);}
	public static float[] calculateRenderPos(Entity entity){
		Vec3M pos=PartialTicksUtil.calculatePos(entity);
		return new float[]{pos.getX(),pos.getY(),pos.getZ()};
	}
	public static void drawArrow(){
		if(arrowModel==null)arrowModel=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/arrow.obj"));
		else{ 
			OpenGLM.pushMatrix();
			OpenGLM.translate(0.6F, -0.03F, 0.52F);
			GL11U.glRotate(0, 45, 0);
			arrowModel.renderAll();
			OpenGLM.popMatrix();
		}
	}
	public static void drawBall(){
		if(ballModel==null)ballModel=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/ball.obj"));
		else{
			ballModel.renderAll();
		}
	}
	public static void drawBlurredCube(int x,int y,int z,AxisAlignedBB cube,int blurQuality,double resolution,double r,double g,double b,double alpha){
		if(blurQuality<1||cube==null)return;
		alpha=alpha/blurQuality;
		OpenGLM.pushMatrix();
		
		OpenGLM.color(r, g, b, alpha);
		for(int size=0;size<blurQuality;size++){
			OpenGLM.translate((cube.maxX-cube.minX)/2+cube.minX, (cube.maxY-cube.minY)/2+cube.minY, (cube.maxZ-cube.minZ)/2+cube.minZ);
			OpenGLM.scale(-resolution+1, -resolution+1, -resolution+1);
			OpenGLM.translate(-((cube.maxX-cube.minX)/2+cube.minX), -((cube.maxY-cube.minY)/2+cube.minY), -((cube.maxZ-cube.minZ)/2+cube.minZ));
			drawCube(cube);
		}
		OpenGLM.color(1,1,1,1);
		OpenGLM.popMatrix();
	}
	public static void drawBlurredCube(int x,int y,int z,double minX,double minY,double minZ,double maxX,double maxy,double maxZ,int blurQuality,double resolution,double r,double g,double b,double alpha){
		drawBlurredCube(x,y,z, new AxisAlignedBB(minX, minY, minZ, maxX, maxy, maxZ), blurQuality, resolution,r,g,b, alpha);
	}
	public static VertexRenderer drawBrain(Vec3M pos,double scale1,double anim){
		anim=Math.toRadians(anim);
		
		TessUtil.bindTexture(Textures.Brain);
		
		VertexRenderer wrapper=new VertexRenderer();
		VertexRenderer buff=TessUtil.getVB();
		
		
		buff.setInstantNormalCalculation(false);
		buff.pushMatrix();
		buff.translate(pos.x, pos.y, pos.z);
		buff.scale(scale1);
		{
			buff.pushMatrix();
			buff.importComplexCube(brainC1,brainC2);
			
			double scale=Math.sin(anim*10);
			scale=scale*scale;
			scale*=0.5;
			scale+=0.5;
			
			buff.scale(scale);
			buff.rotate(Math.sin(anim*4)*180,Math.sin(anim*8+235)*90,Math.sin(anim*2+859)*360);
			
			buff.transformAndSaveTo(wrapper);
			buff.popMatrix();
		}{
			buff.pushMatrix();
			buff.importComplexCube(brainC1,brainC2);
			
			double scale=Math.sin(anim*4);
			scale=scale*scale;
			scale*=0.5;
			scale+=0.5;
			
			buff.scale(scale);
			buff.rotate(Math.sin(anim*8)*90,Math.sin(anim*8+140)*90,-Math.sin(anim*2+90)*360);
			
			buff.transformAndSaveTo(wrapper);
			buff.popMatrix();
		}
		buff.popMatrix();
		
		buff.setInstantNormalCalculation(true);
		
		wrapper.recalculateNormals();
		return wrapper;
	}
	public static void drawCircleRes180(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		if(startAngle%180!=0)startAngle=startAngle%180;
		if(endAngle%180!=0)endAngle=endAngle%180;
		startAngle*=2;
		endAngle*=2;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		OpenGLM.color(r, g, b, alpha);
		OpenGLM.disableTexture2D();
		OpenGLM.pushMatrix();
		Renderer.POS.begin(GL11.GL_TRIANGLES);
		{
			int i=0;
			while(i<xy[0].length-1){
				Renderer.POS.addVertex(0, 0, 0);
				Renderer.POS.addVertex(xy[0][i], xy[1][i], 0);
				if(i<xy[0].length)i++;
				Renderer.POS.addVertex(xy[0][i], xy[1][i], 0);
			}
		}
		Renderer.POS.draw();
		OpenGLM.popMatrix();
		OpenGLM.enableTexture2D();
	}
	public static void drawCircleRes45(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		endAngle++;
		if(startAngle%45!=0)startAngle=startAngle%45;
		if(endAngle%45!=0)endAngle=endAngle%45;
		startAngle*=8;
		endAngle*=8;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		OpenGLM.color(r, g, b, alpha);
		OpenGLM.disableTexture2D();
		OpenGLM.pushMatrix();
		{
			VertexRenderer r1=TessUtil.getVB();
			r1.cleanUp();
			int i=0;
			while(i<xy[0].length-1){
				r1.addVertexWithUV(0, 0, -scale,0,0);
				r1.addVertexWithUV(0, 0, -scale,0,0);
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
				i++;
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
			}
			r1.draw();
		}
		OpenGLM.popMatrix();
		OpenGLM.enableTexture2D();
	}
	public static void drawCircleRes90(double r,double g,double b,double alpha,double scale,int startAngle,int endAngle){
		if(startAngle%90!=0)startAngle=startAngle%90;
		if(endAngle%90!=0)endAngle=endAngle%90;
		startAngle*=4;
		endAngle*=4;
		double[][] xy=new double[2][Math.abs(endAngle-startAngle)];
		for(int xory=0;xory<xy.length;xory++){
			for(int index=0;index<xy[xory].length;index++){
				xy[xory][index]=xory==0?CricleUtil.sin(index+startAngle):CricleUtil.cos(index+startAngle);
				xy[xory][index]*=scale;
			}
		}
		OpenGLM.color(r, g, b, alpha);
		OpenGLM.disableTexture2D();
		OpenGLM.pushMatrix();
		{
			VertexRenderer r1=TessUtil.getVB();
			r1.cleanUp();
			int i=0;
			while(i<xy[0].length-1){
				r1.addVertexWithUV(0, 0, 0,0,0);
				r1.addVertexWithUV(0, 0, 0,0,0);
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
				i++;
				r1.addVertexWithUV(xy[0][i], xy[1][i], 0,0,0);
			}
			r1.draw();
		}
		OpenGLM.popMatrix();
		OpenGLM.enableTexture2D();
	}
	public static void drawCube(AxisAlignedBB a){drawCube(a.minX,a.minY,a.minZ,a.maxX,a.maxY,a.maxZ);}

	public static void drawCube(AxisAlignedBBM a){drawCube(a.minX,a.minY,a.minZ,a.maxX,a.maxY,a.maxZ);}
	public static void drawCube(double minX,double minY,double minZ,double maxX,double maxy,double maxZ){
		buf.cleanUp();
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(minX, maxy, minZ,0,0);
		buf.addVertexWithUV(minX, minY, minZ,0,0);
		buf.addVertexWithUV(minX, minY, maxZ,0,0);
		buf.addVertexWithUV(minX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, maxy, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, maxZ,0,0);
		buf.addVertexWithUV(maxX, minY, minZ,0,0);
		buf.addVertexWithUV(maxX, maxy, minZ,0,0);
		buf.draw();
	}
	public static void drawFullCircleRes45(double r,double g,double b,double alpha,double scale){drawCircleRes45(r, g, b, alpha, scale, 0, 359);}
	public static void drawFullCircleRes90(double r,double g,double b,double alpha,double scale){drawCircleRes90(r, g, b, alpha, scale, 0, 360);}
	public static void drawLine(double x1,double y1,double z1,double x2,double y2,double z2,float width,boolean hasNormal, VertexRenderer nvb,double textueOffset,double textueScale){
		boolean newBuff;
		if(newBuff=(nvb==null)){
			nvb=new VertexRenderer();
		}
		double lenght=new Vec3M(x1-x2, y1-y2, z1-z2).lengthVector();
		
		QuadUV uv=new QuadUV(
			(float)(lenght*textueScale+textueOffset),1,
			(float)(lenght*textueScale+textueOffset),0,
			(float)textueOffset,0,
			(float)textueOffset,1
		);

		Vec3M[] points={
			new Vec3M(0,		-width/2, 0	 ),
			new Vec3M(0,		 width/2, 0	 ),
			new Vec3M(0,		 width/2, lenght),
			new Vec3M(0,		-width/2, lenght),
			new Vec3M(-width/2,		0, 0	 ),
			new Vec3M( width/2,		0, 0	 ),
			new Vec3M( width/2,		0, lenght),
			new Vec3M(-width/2,		0, lenght)
		};
		float
			ditanceX=(float)-(x1-x2),
			ditanceY=(float)-(y1-y2),
			ditanceZ=(float)-(z1-z2),
			rotationX=(float)-Math.toDegrees(Math.atan2(ditanceY,new Vec3M(x1-x2, 0, z1-z2).lengthVector())),
			rotationY=(float)-Math.toDegrees(Math.atan2(ditanceX, -ditanceZ));
		
		for(int i=0;i<points.length;i++){
			points[i]=MatrixUtil.transformVector(points[i], new Vector3f(), rotationX, 0, 0, 1);
			points[i]=MatrixUtil.transformVector(points[i], new Vector3f(), 0, rotationY+180, 0, 1);
			points[i]=MatrixUtil.transformVector(points[i], new Vector3f((float)x1, (float)y1, (float)z1), 0, 0, 0, 1);
		}
		
		for(int i=0;i<points.length;i++)nvb.addVertexWithUV(points[i].x, points[i].y, points[i].z,uv.getUV(i%4).x,uv.getUV(i%4).y);
		for(int i=points.length-1;i>=0;i--)nvb.addVertexWithUV(points[i].x, points[i].y, points[i].z,uv.getUV(i%4).x,uv.getUV(i%4).y);
		if(newBuff)nvb.draw();
	}
	public static void drawLine(Vec3M pos1,Vec3M pos2,float width,boolean hasNormal, VertexRenderer nvb,double textueOffset,double textueScale){
		drawLine(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, width, hasNormal, nvb, textueOffset, textueScale);
	}
	public static void drawPlayerIntoGUI(int x, int y, int scale, float mouseX, float mouseY, EntityLivingBase player,boolean... WillRotate){
		boolean willRotate=false;
		if(WillRotate.length!=0){
			if(WillRotate.length!=1)return;
			willRotate=WillRotate[0];
		}
		OpenGLM.pushMatrix();
		OpenGLM.enableColorMaterial();
		OpenGLM.translate(x, y, 50.0F);
		OpenGLM.scale((-scale), scale, scale);
		OpenGLM.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = player.renderYawOffset;
		float f3 = player.rotationYaw;
		float f4 = player.rotationPitch;
		float f5 = player.prevRotationYawHead;
		float f6 = player.rotationYawHead;
		OpenGLM.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		OpenGLM.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		OpenGLM.rotate(-((float)Math.atan(mouseY / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
		if(!willRotate){
			player.renderYawOffset = (float)Math.atan(mouseX / 40.0F) * 20.0F;
			player.rotationYaw = (float)Math.atan(mouseX / 40.0F) * 40.0F;
			player.rotationPitch = -((float)Math.atan(mouseY / 40.0F)) * 20.0F;
			player.rotationYawHead = player.rotationYaw;
			player.prevRotationYawHead = player.rotationYaw;
		}
		OpenGLM.translate(0.0F, (float)player.getYOffset(), 0.0F);
		getRM().playerViewY = 180.0F;
		getRM().renderEntityWithPosYaw(player, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		player.renderYawOffset = f2;
		player.rotationYaw = f3;
		player.rotationPitch = f4;
		player.prevRotationYawHead = f5;
		player.rotationYawHead = f6;
		RenderHelper.disableStandardItemLighting();
		OpenGLM.disableRescaleNormal();
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		OpenGLM.disableTexture2D();
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		OpenGLM.popMatrix();
	}
	public static void drawPolygon(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	public static void drawQuad(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(hi%4!=0||X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	public static void drawSlotLightMapWcustomSizes(GuiContainer gui,int xPos,int yPos,int xSize,int ySize,boolean useDynamicShadow,boolean invertDepth){
		if(xSize<2||ySize<2)return;
		bindTexture(inventoryPict);
		GL11U.setUpOpaqueRendering(1);
//		int var2=Math.min(xSize, ySize),var1=(var2-var2%2)/2;
		int var1=1;
		//x
		OpenGLM.pushMatrix();
		for(int xProgress=0;xProgress<xSize;xProgress++){
			gui.drawTexturedModalRect(xPos, yPos, 10, invertDepth?119-var1:101, 1, var1);
			OpenGLM.translate(1, 0, 0);
		}
		OpenGLM.popMatrix();
		OpenGLM.pushMatrix();
		for(int xProgress=0;xProgress<xSize;xProgress++){
			gui.drawTexturedModalRect(xPos, yPos+ySize-var1, 10, invertDepth?7:119-var1, 1, var1);
			OpenGLM.translate(1, 0, 0);
		}
		OpenGLM.popMatrix();
		//y
		OpenGLM.pushMatrix();
		for(int yProgress=0;yProgress<ySize-var1;yProgress++){
			if(yProgress==0)OpenGLM.translate(0, 1, 0);
			gui.drawTexturedModalRect(xPos, yPos, invertDepth?25-var1:7, 102, var1, 1);
			OpenGLM.translate(0, 1, 0);
		}
		OpenGLM.popMatrix();
		OpenGLM.pushMatrix();
		for(int yProgress=0;yProgress<ySize-var1;yProgress++){
			if(yProgress==0)OpenGLM.translate(0, 1, 0);
			gui.drawTexturedModalRect(xPos+xSize-var1, yPos, invertDepth?7:25-var1, 102, var1, 1);
			OpenGLM.translate(0, 1, 0);
		}
		OpenGLM.popMatrix();
		gui.drawTexturedModalRect(xPos+xSize-1, yPos, 42, 101, 1, 1);
		gui.drawTexturedModalRect(xPos, yPos+ySize-1, 42, 101, 1, 1);
		OpenGLM.pushMatrix();
		if(useDynamicShadow)OpenGLM.color(0, 0, 0, 77F/255F);
		for(int xProgress=0;xProgress<xSize-2;xProgress++){
			OpenGLM.translate(1, 0, 0);
			OpenGLM.pushMatrix();
			for(int yProgress=0;yProgress<ySize-2;yProgress++){
				OpenGLM.translate(0, 1, 0);
				gui.drawTexturedModalRect(xPos, yPos, invertDepth?4:26, invertDepth?10:102, 1, 1);
			}
			OpenGLM.popMatrix();
		}
		if(useDynamicShadow)OpenGLM.color(1,1,1,1);
		OpenGLM.popMatrix();
		GL11U.endOpaqueRendering();
	}
	public static void drawSV98(){
		if(SV98==null)SV98=AdvancedModelLoader.loadModel(new ResourceLocation(MReference.MODID,"/models/SV98.obj"));
		else{
			GL11U.texture(false);
			SV98.renderAll();
			GL11U.texture(true);
		}
	}
	public static void drawTri(double[] X, double[] Y,double[] Z, double[] U, double[] V){
		int hi=max(max(max(max(X.length,Y.length),Z.length),U.length),V.length);
		if(hi%3!=0||X.length<hi||Y.length<hi||Z.length<hi||U.length<hi||V.length<hi)return;
		buf.cleanUp();
		for (int i = 0; i < X.length; i++)buf.addVertexWithUV(X[i], Y[i], Z[i], U[i], V[i]);
		buf.draw();
	}
	private static void func_178098_a(float p_178098_1_,AbstractClientPlayer clientPlayer){
		MatrixUtil.instance.rotateZ(-18.0F).rotateY(-12.0F).rotateX(-8.0F).translate(-0.9F,0.2F,0.0F);
		float f=clientPlayer.getHeldItemMainhand().getMaxItemUseDuration()-(clientPlayer.getItemInUseCount()-p_178098_1_+1.0F);
		float f1=f/20.0F;
		f1=(f1*f1+f1*2.0F)/3.0F;
		
		if(f1>1.0F){
			f1=1.0F;
		}
		
		if(f1>0.1F){
			float f2=MathHelper.sin((f-0.1F)*1.3F);
			float f3=f1-0.1F;
			float f4=f2*f3;
			MatrixUtil.instance.translate(f4*0.0F,f4*0.01F,f4*0.0F);
		}

		MatrixUtil.instance.translate(f1*0.0F,f1*0.0F,f1*0.1F).scale(1.0F,1.0F,1.0F+f1*0.2F);
	}
	private static void func_178103_d(){
		MatrixUtil.instance.translate(-0.5F,0.2F,0.0F).rotateY(30.0F).rotateX(-80.0F).rotateY(60.0F);
	}
	private static void func_178104_a(AbstractClientPlayer clientPlayer,float p_178104_2_){
		float f=clientPlayer.getItemInUseCount()-p_178104_2_+1.0F;
		float f1=f/clientPlayer.getHeldItemMainhand().getMaxItemUseDuration();
		float f2=MathHelper.abs(MathHelper.cos(f/4.0F*(float)Math.PI)*0.1F);
		
		if(f1>=0.8F){
			f2=0.0F;
		}
		
		float f3=1.0F-(float)Math.pow(f1,27.0D);
		MatrixUtil.instance.translate(0.0F,f2,0.0F).translate(f3*0.6F,f3*-0.5F,f3*0.0F).rotateY(f3*90.0F).rotateX(f3*10.0F).rotateZ(f3*30.0F);
	}
	public static FontRendererMBase getCustomFontRednerer(){
		return fontRendererMBase;
	}
	public static FontRenderer getFontRenderer(){
		return UtilM.getMC().fontRendererObj;
	}
	public static Matrix4f getItemPos(){
		EntityPlayerSP player=(EntityPlayerSP)UtilM.getThePlayer();
		
		float partialTicks=PartialTicksUtil.partialTicks;
		
		MatrixUtil.createMatrix(PartialTicksUtil.calculatePos(player).add(0,player.getEyeHeight(),0)).rotateY(-player.rotationYaw+180).rotateX(-player.rotationPitch);
		
		if(player.getItemInUseCount()>0){
			switch(player.getHeldItemMainhand().getItemUseAction()){
			case NONE:
				transformFirstPersonItem(0,0);
				break;
			case EAT:
			case DRINK:
				func_178104_a(player,partialTicks);
				transformFirstPersonItem(0,0);
				break;
			case BLOCK:
				transformFirstPersonItem(0,0);
				func_178103_d();
				break;
			case BOW:
				transformFirstPersonItem(0,0);
				func_178098_a(partialTicks,player);
			}
		}else transformFirstPersonItem(0, 0);
		
		return MatrixUtil.instance.finish();
	}
	
	public static RenderManager getRM(){return UtilM.getMC().getRenderManager();}
	
	public static Tessellator getT(){return Tessellator.getInstance();}
	
	public static VertexRenderer getVB(){return buf;}

	public static WorldRenderer getWR(){return Tessellator.getInstance().getWorldRenderer();}
	
	public static void renderParticle(){
		boolean isFP=U.getMC().gameSettings.thirdPersonView==2;
		OpenGLM.depthMask(false);
		OpenGLM.enableBlend();
		if(isFP)OpenGLM.disableCull();
		GL11U.blendFunc(2);
		OpenGLM.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		
		EntityFXM.renderBufferedParticle();
		
		
		GL11U.allOpacityIs(true);
		OpenGLM.disableBlend();
		if(isFP)OpenGLM.enableCull();
		OpenGLM.depthMask(true);
		
	}

	public static void setItemRendererEquippProgress(float From0To1,boolean isSmooth){
		ItemRenderer IR=U.getMC().entityRenderer.itemRenderer;
		if(IR!=null)try{
			if(!isSmooth){
				if(prevEquippedProgress==null)prevEquippedProgress = ItemRenderer.class.getDeclaredField("prevEquippedProgress");
				prevEquippedProgress.setAccessible(true);
				prevEquippedProgress.setFloat(IR,From0To1);
			}
			if(equippedProgress==null)equippedProgress = ItemRenderer.class.getDeclaredField("equippedProgress");
			equippedProgress.setAccessible(true);
			equippedProgress.setFloat(IR,From0To1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void transformFirstPersonItem(float equipProgress,float swingProgress){
		MatrixUtil.instance.translate(0.56F,-0.52F,-0.71999997F).translate(0.0F,equipProgress*-0.6F,0.0F).rotateY(45.0F);
		float f=MathHelper.sin(swingProgress*swingProgress*(float)Math.PI);
		float f1=MathHelper.sin(MathHelper.sqrt_float(swingProgress)*(float)Math.PI);
		MatrixUtil.instance.rotateY(f*-20.0F).rotateZ(f1*-20.0F).rotateX(f1*-80.0F).scale(0.4F);
	}

	public static void translateByEntityPos(Entity entity){
		GL11U.glTranslate(TessUtil.calculateRenderPos(entity));
	}
	
}
