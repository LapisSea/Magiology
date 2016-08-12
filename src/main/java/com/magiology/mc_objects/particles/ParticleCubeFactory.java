package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.VertexRenderer;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.AlphaFunc;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleCubeFactory extends ParticleFactory{
	
	public static int defultModel=-1;
	
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
	public class ParticleCube extends ParticleM{
		
		protected float lifeTime,originalSize,originalAlpha;
		protected Vec3M rotation=new Vec3M(RandUtil.RF(90),RandUtil.RF(90),RandUtil.RF(90)),prevRotation=new Vec3M(),rotationSpeed=new Vec3M(RandUtil.CRF(4),RandUtil.CRF(4),RandUtil.CRF(4));
		
		
		protected ParticleCube(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorF color){
			super(pos, speed);
			setSizeTo(0);
			this.lifeTime=lifeTime;
			setGravity(gravity);
			setColor(color);
			getColor().a=1;
			originalSize=size;
			originalAlpha=1;
		}
		
		@Override
		public void update(){
			super.update();
			float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
			setSize(originalSize*(float)Math.sqrt(mul));
			prevRotation.set(rotation);
			if(!isCollided()){
				rotationSpeed=rotationSpeed.add(RandUtil.CRF(1)*getSpeed().x*120,RandUtil.CRF(1)*getSpeed().y*120,RandUtil.CRF(1)*getSpeed().z*120);
				rotation.x+=rotationSpeed.x;
				rotation.y+=rotationSpeed.y;
				rotation.y+=rotationSpeed.z;
			}
			if(getParticleAge()>lifeTime){
				kill();
				return;
			}
			moveParticle(getSpeed());
			pushOutOfBlocks();
		}
		
		@Override
		public void onCollided(Vec3i direction){
//			PrintUtil.println(getSpeed());
			if(direction.getX()!=0)getSpeed().x*=-1;
			if(direction.getY()!=0)getSpeed().y*=-1;
			if(direction.getZ()!=0)getSpeed().z*=-1;
			
			rotationSpeed=rotationSpeed.mul(-0.3);
			rotation.x=(rotation.x*2+Math.round(rotation.x/90)*90)/3;
			rotation.y=(rotation.y*2+Math.round(rotation.y/90)*90)/3;
			rotation.z=(rotation.z*2+Math.round(rotation.z/90)*90)/3;
//			PrintUtil.println(getSpeed());
//			PrintUtil.println(direction);
//			super.onCollided(direction);
		}
		
		@Override
		public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}

		@Override
		public void setUpOpenGl(){
			
			OpenGLM.translate(PartialTicksUtil.calculate(this));
			OpenGLM.rotate(PartialTicksUtil.calculate(prevRotation, rotation));
			OpenGLM.scale(PartialTicksUtil.calculate(getPrevSize(), getSize()));
			
			OpenGLM.color(getColor());
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