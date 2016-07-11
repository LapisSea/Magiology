package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.VertexRenderer;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.AlphaFunc;
import com.magiology.util.statics.PrintUtil;
import com.magiology.util.statics.UtilM;

import net.minecraft.client.renderer.RenderHelper;

public class ParticleCubeFactory extends ParticleFactory<ParticleCube>{
	
	public static int[] defultModel={-1};
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorF color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		if(!UtilM.isRemote()||!shouldSpawn(pos))return;
		addParticle(new ParticleCube(pos, speed, size, lifeTime, gravity, color));
//		if(UtilC.getWorldTime()%20==0)compileDisplayList();
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
		AlphaFunc.ALL.bind();
		OpenGLM.disableTexture2D();
		OpenGLM.enableLighting();
		RenderHelper.enableStandardItemLighting();
		OpenGLM.enableRescaleNormal();
	}
	
	@Override
	public void resetOpenGl(){
		RenderHelper.disableStandardItemLighting();
		OpenGLM.disableLighting();
		OpenGLM.enableTexture2D();
	}
	
	@Override
	public void compileDisplayList(){
		if(defultModel[0]!=-1)GL11.glDeleteLists(defultModel[0], 1);
		startList();
		VertexRenderer buff=new VertexRenderer();
		
		buff.addVertex(0.5, -0.5, -0.5);
		buff.addVertex(0.5,  0.5, -0.5);
		buff.addVertex(0.5,  0.5,  0.5);
		buff.addVertex(0.5, -0.5,  0.5);
		
		buff.addVertex(-0.5, -0.5,  0.5);
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex(-0.5,  0.5, -0.5);
		buff.addVertex(-0.5, -0.5, -0.5);

		buff.addVertex(-0.5,  0.5, -0.5);
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex(0.5,  0.5,  0.5);
		buff.addVertex(0.5,  0.5, -0.5);
		
		buff.addVertex(0.5,  -0.5, -0.5);
		buff.addVertex(0.5,  -0.5,  0.5);
		buff.addVertex(-0.5,  -0.5,  0.5);
		buff.addVertex(-0.5,  -0.5, -0.5);

		buff.addVertex(0.5,  0.5,  -0.5);
		buff.addVertex(0.5, -0.5,  -0.5);
		buff.addVertex(-0.5, -0.5, -0.5);
		buff.addVertex(-0.5,  0.5, -0.5);
		
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex(-0.5, -0.5,  0.5);
		buff.addVertex(0.5, -0.5,  0.5);
		buff.addVertex(0.5,  0.5,  0.5);
		buff.draw();
		
		defultModel[0]=endList();
		PrintUtil.println(defultModel);
	}
}
