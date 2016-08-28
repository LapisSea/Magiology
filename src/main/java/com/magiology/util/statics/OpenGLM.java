package com.magiology.util.statics;

import static org.lwjgl.opengl.GL11.*;

import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Short for OpenGL Magiology
 */
@SideOnly(Side.CLIENT)
public class OpenGLM extends GlStateManager{
	
	
	public static enum BlendFunc{
		
		ADD(SourceFactor.SRC_ALPHA, DestFactor.ONE),
		NORMAL(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA),
		ADD2(SourceFactor.ONE, DestFactor.ONE),
		INVERT(SourceFactor.ONE_MINUS_DST_COLOR, DestFactor.ZERO);
		
		private final SourceFactor sfactor;
		private final DestFactor dfactor;
		
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
		
		private final int func;
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
		translate(vec.x, vec.y, vec.z);
	}
	
	public static VertexBuffer getWB(){
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
	
	public static void rotate(Vec3M rot){
		if(rot.x!=0)rotateX(rot.x);
		if(rot.y!=0)rotateY(rot.y);
		if(rot.z!=0)rotateZ(rot.z);
	}
	public static void rotateX(double rot){
		rotate(rot, 1, 0, 0);
	}
	public static void rotateY(double rot){
		rotate(rot, 0, 1, 0);
	}
	public static void rotateZ(double rot){
		rotate(rot, 0, 0, 1);
	}
	public static void rotateX(float rot){
		rotate(rot, 1, 0, 0);
	}
	public static void rotateY(float rot){
		rotate(rot, 0, 1, 0);
	}
	public static void rotateZ(float rot){
		rotate(rot, 0, 0, 1);
	}

	public static void scale(double scale){
		scale(scale, scale, scale);
	}
	public static void scale(float scale){
		scale(scale, scale, scale);
	}
	public static void color(ColorF color){
		color(color.r, color.g, color.b, color.a);
	}

	public static void bindTexture(ResourceLocation texture){
		UtilC.getMC().renderEngine.bindTexture(texture);
	}
	
}
