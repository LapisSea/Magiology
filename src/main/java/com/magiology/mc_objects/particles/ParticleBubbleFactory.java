package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.AlphaFunc;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleBubbleFactory extends ParticleFactory{

	public static int defultModel=-1;
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, float gravity, ColorM color){
		spawn(pos, speed, size, lifeTime, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorM color){
		if(!shouldSpawn(pos))return;
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
		GlStateManager.disableTexture2D();
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		if(UtilC.getThePlayer().isSneaking())compileDisplayList();
	}
	
	@Override
	public void resetOpenGl(){
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.enableTexture2D();
		OpenGLM.endOpaqueRendering();
	}
	
	@Override
	public void compileDisplayList(){
		if(defultModel!=-1)GL11.glDeleteLists(defultModel, 1);
		startList();
		
		
		FastNormalRenderer buff=new FastNormalRenderer();
		buff.begin(true,FastNormalRenderer.POS);
		
		buff.add( 0.5, -0.5, -0.5);
		buff.add( 0.5,  0.5, -0.5);
		buff.add( 0.5,  0.5,  0.5);
		buff.add( 0.5, -0.5,  0.5);
		
		buff.add(-0.5, -0.5,  0.5);
		buff.add(-0.5,  0.5,  0.5);
		buff.add(-0.5,  0.5, -0.5);
		buff.add(-0.5, -0.5, -0.5);

		buff.add(-0.5,  0.5, -0.5);
		buff.add(-0.5,  0.5,  0.5);
		buff.add( 0.5,  0.5,  0.5);
		buff.add( 0.5,  0.5, -0.5);
		
		buff.add( 0.5, -0.5, -0.5);
		buff.add( 0.5, -0.5,  0.5);
		buff.add(-0.5, -0.5,  0.5);
		buff.add(-0.5, -0.5, -0.5);

		buff.add( 0.5,  0.5, -0.5);
		buff.add( 0.5, -0.5, -0.5);
		buff.add(-0.5, -0.5, -0.5);
		buff.add(-0.5,  0.5, -0.5);
		
		buff.add(-0.5,  0.5,  0.5);
		buff.add(-0.5, -0.5,  0.5);
		buff.add( 0.5, -0.5,  0.5);
		buff.add( 0.5,  0.5,  0.5);
		
		buff.draw();
		
		defultModel=endList();
	}
	
	@SideOnly(Side.CLIENT)
	public class ParticleBubble extends ParticleM{
		
		protected float lifeTime,originalSize,originalAlpha;
		protected Vec3M rotation=new Vec3M(),prevRotation=new Vec3M(),rotationSpeed=new Vec3M(RandUtil.CRF(4),RandUtil.CRF(4),RandUtil.CRF(4));
		
		
		protected ParticleBubble(Vec3M pos, Vec3M speed, float size, float lifeTime, Vec3M gravity, ColorM color){
			super(pos, speed);
			setSizeTo(0);
			this.lifeTime=lifeTime;
			setGravity(gravity);
			setColor(color);
			originalSize=size;
			originalAlpha=color.a();
		}
		
		@Override
		public void update(){
			super.update();
			float mul=1,age=getParticleAge(),age3=age*3;
			
			if(age3<lifeTime)mul=age3/lifeTime;
			else if(age3-lifeTime*2>0){
				float add=Math.min(1, (age-lifeTime)/lifeTime*-10F/3);
				getColor().a(originalAlpha*add);
				mul=2-add;
				mul*=mul;
			}
			setSize(originalSize*(float)Math.sqrt(mul));
			prevRotation.set(rotation);
			
			rotationSpeed.addSelf(RandUtil.CRF(1)*getSpeed().x()*120,RandUtil.CRF(1)*getSpeed().y()*120,RandUtil.CRF(1)*getSpeed().z()*120);
			rotation.addSelf(rotationSpeed);
			
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
			setBoundingBox(getBoundingBox().offset(speed.x(),speed.y(),speed.z()));
			setPosFromBoundingBox();
		}
		@Override
		public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}

		@Override
		public void setUpOpenGl(){
			
			OpenGLM.translate(PartialTicksUtil.calculate(this));
			OpenGLM.rotate(PartialTicksUtil.calculate(prevRotation, rotation));
			OpenGLM.scale(PartialTicksUtil.calculate(getPrevSize(), getSize()));
			double angle=(getParticleAge()+PartialTicksUtil.partialTicks)/3,mul=0.15;
			OpenGLM.scale(1+Math.sin(angle)*mul-mul,1+Math.sin(angle+1.5)*mul-mul,1+Math.sin(angle+3)*mul-mul);
			ColorM color=getColor();
			if(color.a()<254/255F)OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
			else OpenGLM.endOpaqueRendering();
			OpenGLM.color(color);
		}

		@Override
		public int[] getModelIds(){
			return null;
		}
		
		@Override
		public int getModelId(){
			return defultModel;
		}
		
		@Override
		public ParticleFactory getFactorfy(){
			return null;
		}
	}
}
