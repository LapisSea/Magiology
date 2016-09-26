package com.magiology.client.shaders.effects;

import java.lang.reflect.Method;

import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.Vec2FM;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MatrixUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public abstract class PositionAwareEffect{

	public abstract void upload();
	public abstract void initUniformLocations();
	public abstract boolean shouldRender();
	
	private static Matrix4f viewTransformation=new Matrix4f();
	private static Method setupCamViewFunc=((ObjectReturn<Method>)()->{
		try{
			Method meth=EntityRenderer.class.getDeclaredMethod("setupCameraTransform", float.class,int.class);
			meth.setAccessible(true);
			return meth;
		}catch(Exception e){
			e.printStackTrace();
			UtilM.exit(404);
			return null;
		}
	}).process();
	public static void updateViewTransformation(){
		//Isolate transformation from any outside effects and create a clean camera transformation that is correct for the current frame
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		
		try{
			setupCamViewFunc.invoke(UtilC.getER(), PartialTicksUtil.partialTicks,0);
		}catch(Exception e){e.printStackTrace();}
		
		viewTransformation=MatrixUtil.fromArray(ClippingHelperImpl.getInstance().clippingMatrix);
		viewTransformation.translate(new Vector3f(
			-(float)TileEntityRendererDispatcher.staticPlayerX,
			-(float)TileEntityRendererDispatcher.staticPlayerY,
			-(float)TileEntityRendererDispatcher.staticPlayerZ
		));
		
		GlStateManager.popMatrix();
	}
	public static PairM<Vec2FM, Float> convertWorldToScreenPos(Vec3M worldPos){
		Vec3M pos=MatrixUtil.transformVector(worldPos, viewTransformation);
		if(pos.z<0)pos.z=0;
		return new PairM<Vec2FM, Float>(new Vec2FM((float)(pos.x/pos.z),(float)(pos.y/pos.z)), (float)pos.z);
	}
	
}