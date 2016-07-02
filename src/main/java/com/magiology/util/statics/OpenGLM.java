package com.magiology.util.statics;

import static org.lwjgl.opengl.GL11.*;

import com.magiology.util.objs.Vec3M;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Short for OpenGLM
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
			OpenGLM.blendFunc(sfactor, dfactor);
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
		rotate((float)angle, (float)x, (float)y, (float)z);
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
}
