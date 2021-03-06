package com.magiology.util.statics;

import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

/**
 * Short for OpenGL Magiology
 */
@SideOnly(Side.CLIENT)
public class OpenGLM extends GlStateManager{
	
	public static final String  GL_VERSION=GL11.glGetString(GL11.GL_VERSION);
	public static final boolean GL_IS_30  =Character.getNumericValue(GL_VERSION.charAt(0))>=3;
	
	public static enum BlendFunc{
		
		ADD(SourceFactor.SRC_ALPHA, DestFactor.ONE),
		NORMAL(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA),
		ADD2(SourceFactor.ONE, DestFactor.ONE),
		INVERT(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ZERO);
		
		private final SourceFactor sfactor;
		private final DestFactor   dfactor;
		
		private BlendFunc(SourceFactor sfactor, DestFactor dfactor){
			this.sfactor=sfactor;
			this.dfactor=dfactor;
		}
		
		public void bind(){
			GlStateManager.blendFunc(sfactor, dfactor);
		}
	}
	
	public static enum AlphaFunc{
		
		ALL(GL_GREATER, 0),
		ONLY_256(GL_GREATER, 0.99F),
		LESS_THAN_256(GL_LESS, 0.99F),
		NORMAL(GL_GREATER, 0.1F);
		
		private final int   func;
		private final float ref;
		
		private AlphaFunc(int func, float ref){
			this.func=func;
			this.ref=ref;
		}
		
		public void bind(){
			alphaFunc(func, ref);
		}
	}
	
	public static void color(double r, double g, double b, double a){
		color((float)r, (float)g, (float)b, (float)a);
	}
	
	public static void disableAlphaTest(){
		glDisable(GL_ALPHA_TEST);
	}
	
	public static void enableAlphaTest(){
		glEnable(GL_ALPHA_TEST);
	}
	
	public static void lineWidth(float w){
		glLineWidth(w);
	}
	
	public static void rotate(double angle, double x, double y, double z){
		GL11.glRotated(angle, x, y, z);
	}
	
	public static void disableLightmap(){
		UtilC.getMC().entityRenderer.disableLightmap();
	}
	
	public static void enableLightmap(){
		UtilC.getMC().entityRenderer.enableLightmap();
	}
	
	public static void translate(Vec3M vec){
		translate(vec.x(), vec.y(), vec.z());
	}
	
	public static BufferBuilder getWB(){
		return Tessellator.getInstance().getBuffer();
	}
	
	public static Tessellator getT(){
		return Tessellator.getInstance();
	}
	
	public static RenderItem getRI(){
		return UtilC.getMC().getRenderItem();
	}
	
	public static void setUpOpaqueRendering(BlendFunc mode){
		depthMask(false);
		enableBlend();
		mode.bind();
		AlphaFunc.ALL.bind();
		disableAlphaTest();
	}
	
	public static void endOpaqueRendering(){
		disableBlend();
		enableAlphaTest();
		depthMask(true);
		BlendFunc.NORMAL.bind();
		AlphaFunc.NORMAL.bind();
	}
	
	public static void rotate(IVec3M rot){
		if(rot.x()!=0) rotateX(rot.x());
		if(rot.y()!=0) rotateY(rot.y());
		if(rot.z()!=0) rotateZ(rot.z());
	}
	
	public static void rotateX(double x){
		rotate(x, 1, 0, 0);
	}
	
	public static void rotateY(double y){
		rotate(y, 0, 1, 0);
	}
	
	public static void rotateZ(double z){
		rotate(z, 0, 0, 1);
	}
	
	public static void rotateX(float x){
		rotate(x, 1, 0, 0);
	}
	
	public static void rotateY(float y){
		rotate(y, 0, 1, 0);
	}
	
	public static void rotateZ(float z){
		rotate(z, 0, 0, 1);
	}
	
	public static void rotateXY(double x, double y){
		rotateX(x);
		rotateY(y);
	}
	
	public static void rotateXZ(double x, double z){
		rotateX(x);
		rotateZ(z);
	}
	
	public static void rotateYX(double y, double x){
		rotateY(y);
		rotateX(x);
	}
	
	public static void rotateYZ(double y, double z){
		rotateY(y);
		rotateZ(z);
	}
	
	public static void rotateZX(double z, double x){
		rotateZ(z);
		rotateX(x);
	}
	
	public static void rotateZY(double z, double y){
		rotateZ(z);
		rotateY(y);
	}
	
	public static void rotateXYZ(double x, double y, double z){
		rotateX(x);
		rotateY(y);
		rotateZ(z);
	}
	
	public static void rotateZYX(double z, double y, double x){
		rotateZ(z);
		rotateY(y);
		rotateX(x);
	}
	
	public static void rotateXZY(double x, double z, double y){
		rotateX(x);
		rotateZ(z);
		rotateY(y);
	}
	
	public static void rotateXY(float x, float y){
		rotateX(x);
		rotateY(y);
	}
	
	public static void rotateXZ(float x, float z){
		rotateX(x);
		rotateZ(z);
	}
	
	public static void rotateYX(float y, float x){
		rotateY(y);
		rotateX(x);
	}
	
	public static void rotateYZ(float y, float z){
		rotateY(y);
		rotateZ(z);
	}
	
	public static void rotateZX(float z, float x){
		rotateZ(z);
		rotateX(x);
	}
	
	public static void rotateZY(float z, float y){
		rotateZ(z);
		rotateY(y);
	}
	
	public static void rotateXYZ(float x, float y, float z){
		rotateX(x);
		rotateY(y);
		rotateZ(z);
	}
	
	public static void rotateZYX(float z, float y, float x){
		rotateZ(z);
		rotateY(y);
		rotateX(x);
	}
	
	public static void rotateXZY(float x, float z, float y){
		rotateX(x);
		rotateZ(z);
		rotateY(y);
	}
	
	public static void scale(double scale){
		scale(scale, scale, scale);
	}
	
	public static void scale(float scale){
		scale(scale, scale, scale);
	}
	
	public static void color(ColorM color){
		color(color.r(), color.g(), color.b(), color.a());
	}
	
	public static void bindTexture(ResourceLocation texture){
		UtilC.getMC().renderEngine.bindTexture(texture);
	}
	
	public static void checkError(){
		int err=GL11.glGetError();
		if(err!=0){
			String errName;
			switch(err){
			case GL11.GL_INVALID_ENUM:
				errName="GL_INVALID_ENUM";
				break;
			case GL11.GL_INVALID_VALUE:
				errName="GL_INVALID_VALUE";
				break;
			case GL11.GL_INVALID_OPERATION:
				errName="GL_INVALID_OPERATION";
				break;
			case GL11.GL_STACK_OVERFLOW:
				errName="GL_STACK_OVERFLOW";
				break;
			case GL11.GL_STACK_UNDERFLOW:
				errName="GL_STACK_UNDERFLOW";
				break;
			case GL11.GL_OUT_OF_MEMORY:
				errName="GL_OUT_OF_MEMORY";
				break;
			case GL30.GL_INVALID_FRAMEBUFFER_OPERATION:
				errName="GL_INVALID_FRAMEBUFFER_OPERATION";
				break;
			
			default:
				errName="UNKNOW_ERROR: "+err;
			}
			LogUtil.printStackTrace("OpenGL error: "+errName);
		}
	}
	
}
