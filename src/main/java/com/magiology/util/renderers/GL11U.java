package com.magiology.util.renderers;


import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.magiology.util.utilclasses.DataStalker;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilobjects.ColorF;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.util.math.BlockPos;


/**
 * GL11 helper! :D
 * @author LapisSea
 */
public class GL11U{
	@Deprecated
	public static int textureId;
		
/**Is all opaque texture accepted to be rendered.
	 * true  = everything
	 * false = only 100% opaque texture rendered
	 * @param enabled is opacity enabled
	 * */
	public static void allOpacityIs(boolean enabled){
		if(enabled)glAlphaFunc(GL_GREATER, 0);
		else glAlphaFunc(GL_GREATER, 0.99F);
	}
	/**@param ID 1-2*/
	public static void blendFunc(int ID){switch(ID){
		case 1:glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);return;
		case 2:glBlendFunc(GL_SRC_ALPHA, GL_ONE);return;
		case 3:glBlendFunc(GL_ONE, GL_ONE);return;
		case 4:glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);return;
		default:PrintUtil.println(">>>WARNING!!<<<\nGL11Helper failed to get glBlendFunc from chosen ID!\n--------------------------\n");
	}}
//	public static void glRotate(double xAngle,double yAngle,double zAngle,double xOffset,double yOffset,double zOffset,boolean... willTranslateBack){
	@Deprecated
	public static void captureTexture(){
		try{
			Integer id=(Integer)DataStalker.getVariable(OpenGLM.class, "activeTextureUnit").get(null);
			Object texture=((Object[])DataStalker.getVariable(OpenGLM.class, "textureState").get(null))[id];
			if(texture!=null)textureId=DataStalker.getVariable(texture.getClass(), "textureName", texture);
		}catch(Exception ignored){}
	}
	/**Sets the rendering mode to render 100% opaque texture only.*/
	public static void endOpaqueRendering(){
		glBlend(false);
		glEnable(GL_ALPHA_TEST);
		OpenGLM.depthMask(true);
		blendFunc(1);
		resetOpacity();
	}
	public static void endProtection(){
		OpenGLM.color(1, 1, 1, 1);
		OpenGLM.popMatrix();
		GL11.glPopAttrib();
	}
	public static void glBlend(boolean enabled){
		if(enabled)glEnable(GL_BLEND);
		else glDisable(GL_BLEND);
	}
	public static void glColor(ColorF color){
		OpenGLM.color(color.r, color.g, color.b, color.a);
	}
	public static void glColor(int color){
		OpenGLM.color((color>>16&255)/255.0F, (color>>8&255)/255.0F, (color & 255)/255.0F, (color>>24&255)/255.0F);
	}
	public static void glCulFace(boolean enabled){
		if(enabled)glEnable(GL_CULL_FACE);
		else glDisable(GL_CULL_FACE);
	}
	public static void glDepth(boolean enabled){
		if(enabled)glEnable(GL_DEPTH_TEST);
		else glDisable(GL_DEPTH_TEST);
	}
	public static void glLighting(boolean enabled){
		if(enabled)glEnable(GL_LIGHTING);
		else glDisable(GL_LIGHTING);
	}
	public static void glRotate(double x,double y,double z){
		OpenGLM.rotate((float)x,1,0,0);
		OpenGLM.rotate((float)y,0,1,0);
		OpenGLM.rotate((float)z,0,0,1);
	}
	public static void glRotate(double xAngle,double yAngle,double zAngle,double xOffset,double yOffset,double zOffset){
		OpenGLM.translate(xOffset, yOffset, zOffset);
		glRotate(xAngle, yAngle, zAngle);
		OpenGLM.translate(-xOffset, -yOffset, -zOffset);
	}
	public static void glRotate(double xAngle,double yAngle,double zAngle,Vec3M offset){
		glRotate(xAngle, yAngle, zAngle, offset.x, offset.y, offset.z);
	}
	public static void glRotate(double[] double3){
		glRotate(double3[0], double3[1], double3[2]);
	}
	public static void glRotate(float[] float3){
		glRotate(float3[0], float3[1], float3[2]);
	}
	public static void glRotate(Vec3M vec){
		glRotate(vec.x, vec.y, vec.z);
	}
	public static void glRotate(Vec3M angle,double xOffset,double yOffset,double zOffset){
		glRotate(angle.x, angle.y, angle.z, xOffset, yOffset, zOffset);
	}
	//	}
	public static void glRotate(Vec3M angle,Vec3M offset){
		glRotate(angle.x, angle.y, angle.z, offset);
	}
	public static void glRotate(Vector3f vec){
		glRotate(vec.x, vec.y, vec.z);
	}
	public static void glScale(double scale){
		OpenGLM.scale(scale, scale, scale);
	}
	public static void glScale(float scale){
		OpenGLM.scale(scale, scale, scale);
	}
	public static void glTranslate(double[] arrayOf3D){
		if(arrayOf3D.length!=3)return;
		OpenGLM.translate(arrayOf3D[0],arrayOf3D[1],arrayOf3D[2]);
	}
	public static void glTranslate(float[] arrayOf3F){
		if(arrayOf3F.length!=3)return;
		OpenGLM.translate(arrayOf3F[0],arrayOf3F[1],arrayOf3F[2]);
	}
	public static void glTranslate(Vec3M vec){
		OpenGLM.translate(vec.x,vec.y,vec.z);
	}
	public static void glTranslatep(BlockPos pos){
		glTranslate(new float[]{pos.getX(),pos.getY(),pos.getZ()});
	}
	public static void protect(){
		OpenGLM.color(1, 1, 1, 1);
		glLighting(true);
		resetOpacity();
		OpenGLM.depthMask(true);
		OpenGLM.pushMatrix();
		GL11.glPushAttrib(GL11.GL_CULL_FACE);
		GL11U.glCulFace(true);
	}
	/**Sets the texture opaque minimal limit to default MC setting.*/
	public static void resetOpacity(){glAlphaFunc(GL_GREATER, 0.1F);}
	@Deprecated
	public static void setTexture(){
		OpenGLM.bindTexture(textureId);
	}
	/**Sets the rendering mode to render opaque texture.*/
	public static void setUpOpaqueRendering(int ID){
		OpenGLM.depthMask(false);
		glBlend(true);
		blendFunc(ID);
		allOpacityIs(true);
		OpenGLM.disableAlphaTest();
	}
	public static void texture(boolean enabled){
		if(enabled)glEnable(GL_TEXTURE_2D);
		else glDisable(GL_TEXTURE_2D);
	}
}
