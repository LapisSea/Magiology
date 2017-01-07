package com.magiology.mc_objects.particles;

import com.magiology.client.renderers.Renderer;
import com.magiology.core.MReference;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.color.IColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleMessageFactory extends ParticleFactory{
	
	
	public static final ResourceLocation MIST=new ResourceLocation(MReference.MODID,"/textures/particle/smooth_buble1.png");

	
	public void spawn(Vec3M pos, Vec3M speed, float size, float lifeTime, IColorM textColor, String text){
		if(UtilM.isRemote()&&shouldSpawn(pos))addParticle(new ParticleMessage(pos, speed, size, lifeTime, textColor, text));
	}
	
	@Override
	public boolean hasDistanceLimit(){
		return true;
	}
	
	@Override
	public boolean hasStaticModel(){
		return false;
	}
	
	@Override
	public void setUpOpenGl(){
		OpenGLM.setUpOpaqueRendering(BlendFunc.NORMAL);
	}
	
	@Override
	public void resetOpenGl(){
		OpenGLM.endOpaqueRendering();
	}
	
	@Override
	public void compileDisplayList(){}
	
	@SideOnly(Side.CLIENT)
	public class ParticleMessage extends ParticleM{
		
		protected float lifeTime,originalSize,originalAlpha;
		protected String text;
		
		protected ParticleMessage(Vec3M pos, Vec3M speed, float size, float lifeTime, IColorM textColor, String text){
			super(pos, speed);
			setSizeTo(0);
			this.lifeTime=lifeTime;
			setGravity(new Vec3M());
			setColor(ColorM.toColorM(textColor));
			originalSize=size;
			originalAlpha=textColor.a();
			this.text=text;
		}
		
		@Override
		public void update(){
			super.update();
			moveParticle(getSpeed());
			float mul=1-Math.abs((getParticleAge()*2-lifeTime)/lifeTime);
			mul=Math.min(1, mul*6);
			mul*=mul;
			
			setSize(originalSize*mul);
			getColor().a(mul*originalAlpha);
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
			return -1;
		}
		
		
		
		@Override public int[] getModelIds(){return null;}
		@Override public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){
			FontRenderer fr=UtilC.getMC().fontRendererObj;
			int w=fr.getStringWidth(text);
			
			ColorM bgCol=new ColorM(0,0,0,0.2*getColor().a());
			
			OpenGLM.scale(-1F/16);
			OpenGLM.rotateY(-90);
			OpenGLM.translate(-0.5*w,-0.5*fr.FONT_HEIGHT,0);
			OpenGLM.disableTexture2D();
			Renderer.POS_COLOR.beginQuads();
			Renderer.POS_COLOR.addVertex(-1,fr.FONT_HEIGHT-1,0,bgCol);
			Renderer.POS_COLOR.addVertex(w,fr.FONT_HEIGHT-1,0,bgCol);
			Renderer.POS_COLOR.addVertex(w,-1,0,bgCol);
			Renderer.POS_COLOR.addVertex(-1,-1,0,bgCol);
			Renderer.POS_COLOR.draw();
			OpenGLM.enableTexture2D();
			fr.drawString(text, 0, 0, getColor().hashCode());
		}
		
		@Override
		public ParticleFactory getFactorfy(){
			return ParticleMessageFactory.this;
		}
	}
}
