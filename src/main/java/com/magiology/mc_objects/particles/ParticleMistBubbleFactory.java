package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.Renderer;
import com.magiology.core.MReference;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;

import net.minecraft.util.ResourceLocation;

public class ParticleMistBubbleFactory extends ParticleFactory<ParticleMistBubble>{
	
	
	public static int[] defultModel={
			-1
	};
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorF color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		if(!shouldSpawn(pos)||!UtilM.isRemote())return;
		addParticle(new ParticleMistBubble(pos, speed, size, lifeTime, gravity, color));
		if(UtilC.getThePlayer().isSneaking()&&UtilC.getWorldTime()%20==0){
			compileDisplayList();
		}
	}
	
	@Override
	public float getSpawnDistanceInBlocks(){
		return defultSpawnDistance;
	}
	
	@Override
	public boolean hasDistanceLimit(){
		return true;
	}
	
	@Override
	public boolean hasStaticModel(){
		return true;
	}
	
	@Override
	public void setUpOpenGl(){
		OpenGLM.setUpOpaqueRendering(BlendFunc.ADD);
		UtilC.getMC().getTextureManager().bindTexture(new ResourceLocation(MReference.MODID,"/textures/particle/smooth_buble1.png"));
	}
	
	@Override
	public void resetOpenGl(){
		OpenGLM.endOpaqueRendering();
	}
	
	@Override
	public void compileDisplayList(){
		if(defultModel[0]!=-1)GL11.glDeleteLists(defultModel[0], 1);
		startList();
		
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(0, -0.5, -0.5,0,0);
		Renderer.POS_UV.addVertex(0,  0.5, -0.5,0,1);
		Renderer.POS_UV.addVertex(0,  0.5,  0.5,1,1);
		Renderer.POS_UV.addVertex(0, -0.5,  0.5,1,0);
		Renderer.POS_UV.draw();
		
		defultModel[0]=endList();
		PrintUtil.println(defultModel);
	}
}
