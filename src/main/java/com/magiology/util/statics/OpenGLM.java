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
}
