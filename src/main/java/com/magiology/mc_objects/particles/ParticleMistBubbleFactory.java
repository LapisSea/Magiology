package com.magiology.mc_objects.particles;

import com.magiology.client.renderers.Renderer;
import com.magiology.core.MReference;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleMistBubbleFactory extends ParticleFactory{
	
	private final static ParticleMistBubbleFactory instance=new ParticleMistBubbleFactory();
	public static ParticleMistBubbleFactory get(){return instance;}
	private ParticleMistBubbleFactory(){}
	
	public static final ResourceLocation MIST=new ResourceLocation(MReference.MODID,"/textures/particle/smooth_buble1.png");
	public static int defultModel=-1;

	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorF color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		if(UtilM.isRemote()&&shouldSpawn(pos))addParticle(new ParticleMistBubble(pos, speed, size, lifeTime, gravity, color));
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
		UtilC.getMC().getTextureManager().bindTexture(MIST);
	}
	
	@Override
	public void resetOpenGl(){
		OpenGLM.endOpaqueRendering();
	}
	
	@Override
	public void compileDisplayList(){
		if(defultModel!=-1)GL11.glDeleteLists(defultModel, 1);
		startList();
		
		Renderer.POS_UV.beginQuads();
		Renderer.POS_UV.addVertex(0, -0.5, -0.5, 0,0);
		Renderer.POS_UV.addVertex(0,  0.5, -0.5, 0,1);
		Renderer.POS_UV.addVertex(0,  0.5,  0.5, 1,1);
		Renderer.POS_UV.addVertex(0, -0.5,  0.5, 1,0);
		Renderer.POS_UV.draw();
		
		defultModel=endList();
	}
	
	@SideOnly(Side.CLIENT)
	public class ParticleMistBubble extends ParticleM{
		
		protected float lifeTime,originalSize,originalAlpha;
		
		
		protected ParticleMistBubble(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
			super(pos, speed);
			setSizeTo(0);
			this.lifeTime=lifeTime;
			setGravity(gravity);
			setColor(color);
			originalSize=size;
			originalAlpha=color.a;
		}
		
		@Override
		public void update(){
			super.update();
			float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
			setSize(originalSize*(float)Math.sqrt(mul));
			getColor().a=mul*2*originalAlpha;
			if(getParticleAge()>lifeTime)kill();
		}
		
		@Override
		public void onCollided(Vec3i direction){
			super.onCollided(direction);
		}

		@Override
		public void setUpOpenGl(){
			transformSimpleParticleColored();
		}

		@Override
		public int getModelId(){
			return defultModel;
		}
		
		
		
		@Override public int[] getModelIds(){return null;}
		@Override public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}
	}

}
