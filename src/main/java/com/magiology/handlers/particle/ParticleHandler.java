package com.magiology.handlers.particle;

import java.util.ArrayList;
import java.util.List;

import com.magiology.core.ConfigM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MathUtil;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleHandler{
	
	private static final ParticleHandler instance=new ParticleHandler();
	public static ParticleHandler get(){
		return instance;
	}
	private ParticleHandler(){}
	
	private final List<ParticleFactory> particleFactories=new ArrayList<>();
	private Vec3M interpPos=new Vec3M();
	
	public int registerParticle(ParticleFactory factory){
		particleFactories.add(factory);
		factory.setId(particleFactories.indexOf(factory));
		factory.compileDisplayList();
		return factory.getID();
	}
	
	public boolean shouldSpawn(ParticleFactory factory, Vec3M spawnPos){
		Minecraft mc=UtilC.getMC();
		boolean shouldSpawn=mc.gameSettings.particleSetting!=2;
//		if(shouldSpawn&&mc.gameSettings.particleSetting==1){
//
//		}

		if(shouldSpawn&&factory.hasDistanceLimit()){
			Entity ent=mc.getRenderViewEntity();
			if(ent==null)return false;
			Vec3M distanceFromCamera=UtilM.getEntityPos(ent).addY(ent.getEyeHeight()).sub(spawnPos);
			if(distanceFromCamera.lengthSquared()>MathUtil.sq(factory.getSpawnDistanceInBlocks()))shouldSpawn=false;
		}
		return shouldSpawn;
	}
	

	public void updateParticles(){
		if(UtilC.getMC().gameSettings.particleSetting==2)clearEffects();
		else if(UtilC.getWorldTime()%20==0&&getCount()>ConfigM.getMaxParticleCount()){
//			while(getCount()>Config.getMaxParticleCount()){
//				particleFactories.forEach(ParticleFactory::removeLast);
//			}
		}
		particleFactories.forEach(ParticleFactory::update);
	}

	/**
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles(){
//		if(UtilM.TRUE())return;
		try{
			interpPos=PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1);
	        
			GlStateManager.pushMatrix();
			OpenGLM.translate(interpPos);
			particleFactories.forEach(type->type.render(ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY()));
			GlStateManager.popMatrix();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void clearEffects(){
		particleFactories.forEach(ParticleFactory::clear);
	}
	
	public int getCount(){
		int i=0;
		for(ParticleFactory layer:particleFactories){
			i+=layer.size();
		}
		return i;
	}
}
