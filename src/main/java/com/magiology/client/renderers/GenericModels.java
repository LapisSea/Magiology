package com.magiology.client.renderers;

import org.lwjgl.opengl.GL11;

public class GenericModels{
	
	private static int texturelessCricle=-1;
	private static boolean compiled=false;
	
	public static void compile(){
		if(compiled){
			GL11.glDeleteLists(texturelessCricle, 1);
		}
	}
	
}
