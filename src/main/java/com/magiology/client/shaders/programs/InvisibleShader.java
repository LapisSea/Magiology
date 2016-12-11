package com.magiology.client.shaders.programs;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.magiology.client.shaders.ShaderProgram;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.effects.SoftEffectsShader;
import com.magiology.client.shaders.upload.UniformUploaderF1;
import com.magiology.client.shaders.upload.UniformUploaderF2;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class InvisibleShader extends ShaderProgram{
	
	public static InvisibleShader	instance;
	public static float				screenSizeF;
	
	private UniformUploaderF2	screenDim;
	private UniformUploaderF1	screenSize;
	
	//	private UniformUploaderCustom<T>
	
	private List<PositionAwareEffect>		effects				=new ArrayList<>();
	public static List<EntityLivingBase>	INVISIBLE_ENTITYS	=new ArrayList<>();
	
	public InvisibleShader(){
		instance=this;
		effects.add(SoftEffectsShader.instance);
	}
	
	@Override
	protected CharSequence getVertexShaderSrc(){
		return null;
	}
	
	@Override
	protected CharSequence getFragmentShaderSrc(){
		return getShaderFile("invisible.fs");
	}
	
	@Override
	protected void bindAtributes(){
		
	}
	
	@Override
	public void initUniforms(){
		screenDim.upload(Display.getWidth(), Display.getHeight());
		screenSizeF=new Vec2FM(Display.getWidth(), Display.getHeight()).length()/2;
		screenSize.upload(screenSizeF);
		effects.forEach(fx->fx.upload());
		
	}
	
	public void addEffect(PositionAwareEffect effect){
		effects.add(effect);
	}
	
	public void activate(){
		bind();
		initUniforms();
	}
	
	@Override
	protected void initUniformLocations(){
		screenDim=new UniformUploaderF2(this, "screenDim");
		screenSize=new UniformUploaderF1(this, "screenSize");
		effects.forEach(fx->fx.initUniformLocations());
		
	}
	
	public boolean shouldRender(){
		return effects.stream().anyMatch(effect->effect.shouldRender());
	}
	
	public void render(){
		if(INVISIBLE_ENTITYS.isEmpty())return;
		
		if(!shouldRender())return;
		
		RenderManager renderManager=UtilC.getMC().getRenderManager();
		GlStateManager.pushMatrix();
		activate();
		OpenGLM.translate(PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1));
		
		for(EntityLivingBase entity:INVISIBLE_ENTITYS){
			Vec3M pos=PartialTicksUtil.calculate(entity);
			GlStateManager.enableBlend();
			GL11.glAlphaFunc(GL11.GL_GREATER, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			entity.setInvisible(false);
			renderManager.doRenderEntity(entity, pos.x(), pos.y(), pos.z(), 0, PartialTicksUtil.partialTicks, false);
			entity.setInvisible(true);
		}
		
		GlStateManager.disableBlend();
		INVISIBLE_ENTITYS.clear();
		deactivate();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.popMatrix();
		
	}
}
