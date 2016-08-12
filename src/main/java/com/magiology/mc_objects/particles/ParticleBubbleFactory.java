package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.VertexRenderer;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.AlphaFunc;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleBubbleFactory extends ParticleFactory{
	
	public static int defultModel=-1;
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorF color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
		if(!UtilM.isRemote()||!shouldSpawn(pos))return;
		addParticle(new ParticleBubble(pos, speed, size, lifeTime, gravity, color));
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
		if(defultModel!=-1)GL11.glDeleteLists(defultModel, 1);
		startList();
		VertexRenderer buff=new VertexRenderer();
		
		buff.addVertex( 0.5, -0.5, -0.5);
		buff.addVertex( 0.5,  0.5, -0.5);
		buff.addVertex( 0.5,  0.5,  0.5);
		buff.addVertex( 0.5, -0.5,  0.5);
		
		buff.addVertex(-0.5, -0.5,  0.5);
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex(-0.5,  0.5, -0.5);
		buff.addVertex(-0.5, -0.5, -0.5);

		buff.addVertex(-0.5,  0.5, -0.5);
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex( 0.5,  0.5,  0.5);
		buff.addVertex( 0.5,  0.5, -0.5);
		
		buff.addVertex( 0.5, -0.5, -0.5);
		buff.addVertex( 0.5, -0.5,  0.5);
		buff.addVertex(-0.5, -0.5,  0.5);
		buff.addVertex(-0.5, -0.5, -0.5);

		buff.addVertex( 0.5,  0.5, -0.5);
		buff.addVertex( 0.5, -0.5, -0.5);
		buff.addVertex(-0.5, -0.5, -0.5);
		buff.addVertex(-0.5,  0.5, -0.5);
		
		buff.addVertex(-0.5,  0.5,  0.5);
		buff.addVertex(-0.5, -0.5,  0.5);
		buff.addVertex( 0.5, -0.5,  0.5);
		buff.addVertex( 0.5,  0.5,  0.5);
		
		buff.draw();
		
		defultModel=endList();
	}
	
	@SideOnly(Side.CLIENT)
	public class ParticleBubble extends ParticleM{
		
		protected float lifeTime,originalSize,originalAlpha;
		protected Vec3M rotation=new Vec3M(),prevRotation=new Vec3M(),rotationSpeed=new Vec3M(RandUtil.CRF(4),RandUtil.CRF(4),RandUtil.CRF(4));
		
		
		protected ParticleBubble(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
			super(pos, speed);
			setSizeTo(0);
			this.lifeTime=lifeTime;
			setGravity(gravity);
			setColor(color);
			originalSize=size;
		}
		
		@Override
		public void update(){
			super.update();
			float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
			setSize(originalSize*(float)Math.sqrt(mul));
			prevRotation.set(rotation);
			
			rotationSpeed=rotationSpeed.add(RandUtil.CRF(1)*getSpeed().x*120,RandUtil.CRF(1)*getSpeed().y*120,RandUtil.CRF(1)*getSpeed().z*120);
			rotation.x+=rotationSpeed.x;
			rotation.y+=rotationSpeed.y;
			rotation.y+=rotationSpeed.z;
			
			if(getParticleAge()>lifeTime){
				kill();
				return;
			}
			moveParticle(getSpeed());
		}
		
		@Override
		public void onCollided(Vec3i direction){}
		
		@Override
		public void moveParticle(Vec3M speed){
			setBoundingBox(getBoundingBox().offset(speed.x,speed.y,speed.z));
			setPosFromBoundingBox();
		}
		@Override
		public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}

		@Override
		public void setUpOpenGl(){
			
			OpenGLM.translate(PartialTicksUtil.calculate(this));
			OpenGLM.rotate(PartialTicksUtil.calculate(prevRotation, rotation));
			OpenGLM.scale(PartialTicksUtil.calculate(getPrevSize(), getSize()));
			ColorF color=getColor();
			OpenGLM.color(color);
			if(color.a<254/255F)OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
			else OpenGLM.endOpaqueRendering();
		}

		@Override
		public int[] getModelIds(){
			return null;
		}
		
		@Override
		public int getModelId(){
			return defultModel;
		}
	}
}
