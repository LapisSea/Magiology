package com.magiology.mc_objects.particles;

import org.lwjgl.opengl.GL11;

import com.magiology.client.Renderer;
import com.magiology.core.MReference;
import com.magiology.handlers.particle.ParticleFactory;
import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.OpenGLM.BlendFunc;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ParticleMistyEnergyFactory extends ParticleFactory{

	public static final ResourceLocation texture=new ResourceLocation(MReference.MODID,"/textures/particle/explosion_stages.png");
	public static int[] defultModel=new int[32];
	
	public void spawn(Vec3M pos, Vec3M speed, float size, float gravity, ColorF color){
		spawn(pos, speed, size, new Vec3M(0, gravity, 0), color);
	}
	
	public void spawn(Vec3M pos, Vec3M speed, float size, Vec3M gravity, ColorF color){
		if(!UtilM.isRemote()||!shouldSpawn(pos))return;
		addParticle(new ParticleMistyEnergy(pos, speed, size, gravity, color));
//		if(UtilC.getThePlayer().isSneaking()&&UtilC.getWorldTime()%20==0){
//			compileDisplayList();
//		}
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
		UtilC.getMC().getTextureManager().bindTexture(texture);
	}
	
	@Override
	public void resetOpenGl(){
		OpenGLM.endOpaqueRendering();
	}
	
	@Override
	public void compileDisplayList(){
		if(defultModel[0]!=-1){
			for(int i=0;i<defultModel.length;i++)GL11.glDeleteLists(defultModel[i], 1);
		}
		for(int part=0;part<2;part++){
			for(int x=0;x<4;x++){
				float maxX=(x+1)*0.125F,minX=x*0.125F;
				if(part==1){
					maxX+=0.5;
					minX+=0.5;
				}
				for(int y=0;y<4;y++){
					float maxY=(y+1)*0.25F,minY=y*0.25F;
					
					startList();
					Renderer.POS_UV.beginQuads();
					Renderer.POS_UV.addVertex(0, -0.5, -0.5, maxX,maxY);
					Renderer.POS_UV.addVertex(0,  0.5, -0.5, maxX,minY);
					Renderer.POS_UV.addVertex(0,  0.5,  0.5, minX,minY);
					Renderer.POS_UV.addVertex(0, -0.5,  0.5, minX,maxY);
					Renderer.POS_UV.draw();
					defultModel[part*16+x*4+y]=endList();
				}
				
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public class ParticleMistyEnergy extends ParticleM{
		
		private final int txtType=RandUtil.RB()?0:16;
		
		protected ParticleMistyEnergy(Vec3M pos, Vec3M speed, float size, Vec3M gravity, ColorF color){
			super(pos, speed);
			setSizeTo(size);
			setGravity(gravity);
			setColor(color);
		}
		
		@Override
		public void update(){
			if(getParticleAge()>19){
				kill();
				return;
			}
			super.update();
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
			float agePrecent=(getParticleAge()+PartialTicksUtil.partialTicks)/20;
			return defultModel[(int)Math.floor(agePrecent*15)+txtType];
		}
		
		@Override public int[] getModelIds(){return null;}
		@Override public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}
	}
}
