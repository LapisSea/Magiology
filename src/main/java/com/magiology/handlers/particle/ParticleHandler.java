package com.magiology.handlers.particle;

import java.util.ArrayList;
import java.util.List;

import com.magiology.core.Config;
import com.magiology.mcobjects.particles.ParticleFactory;
import com.magiology.util.renderers.GL11U;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.MathUtil;
import com.magiology.util.utilclasses.math.PartialTicksUtil;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleHandler{
	
	public static final ParticleHandler instance=new ParticleHandler();
	
	private ParticleHandler(){}
	
	private final List<ParticleFactory> particleTypes=new ArrayList<>();
	private Vec3M interpPos=new Vec3M();
	
	public int registerParticle(ParticleFactory factory){
		particleTypes.add(factory);
		factory.setId(particleTypes.indexOf(factory));
		return factory.getID();
	}
	
	public boolean shouldSpawn(ParticleFactory factory, Vec3M spawnPos){
		Minecraft mc=UtilC.getMC();
		
		boolean shouldSpawn=mc.gameSettings.particleSetting!=2;
		
		if(shouldSpawn&&factory.hasDistanceLimit()){
			Vec3M distanceFromCamera=UtilM.getEntityPos(mc.getRenderViewEntity()).sub(spawnPos);
			if(distanceFromCamera.lengthSquared()>MathUtil.sq(factory.getSpawnDistanceInBlocks()))shouldSpawn=false;
		}
		return shouldSpawn;
	}
	

	public void updateParticles(){
//		if(UtilM.TRUE())return;
		if(UtilC.getMC().gameSettings.particleSetting==2)clearEffects();
		else if(UtilC.getWorldTime()%20==0&&getCount()>Config.getMaxParticleCount()){
			while(getCount()>Config.getMaxParticleCount()){
				particleTypes.forEach(ParticleFactory::removeLast);
			}
		}
		particleTypes.forEach(ParticleFactory::update);
	}

	/**
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles(){
//		if(UtilM.TRUE())return;
		try{
			interpPos=PartialTicksUtil.calculate(UtilC.getViewEntity()).mul(-1);
	        float f = ActiveRenderInfo.getRotationX();
	        float f1 = ActiveRenderInfo.getRotationZ();
	        float f2 = ActiveRenderInfo.getRotationYZ();
	        float f3 = ActiveRenderInfo.getRotationXY();
	        float f4 = ActiveRenderInfo.getRotationXZ();

			OpenGLM.pushMatrix();
			GL11U.glTranslate(interpPos);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.alphaFunc(516, 0.003921569F);
			GlStateManager.depthMask(false);
			
			particleTypes.forEach(type->type.render(f, f4, f1, f2, f3));
			TessUtil.getWB().setTranslation(0,0,0);
			
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.alphaFunc(516, 0.1F);
			OpenGLM.popMatrix();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void clearEffects(){
		particleTypes.forEach(ParticleFactory::clear);
	}
	
	public int getCount(){
		int i=0;
		for(ParticleFactory layer:particleTypes){
			i+=layer.size();
		}
		return i;
	}
}
