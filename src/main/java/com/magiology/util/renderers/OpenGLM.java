package com.magiology.util.renderers;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.GlStateManager;

/**
 * Short for OpenGLM
 */
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

}
