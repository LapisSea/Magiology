package com.magiology.mc_objects.particles;

import com.magiology.handlers.particle.ParticleM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public void renderModel(float xRotation, float zRotation, float yzRotation, float xyRotation, float xzRotation){}

	@Override
	public void setUpOpenGl(){
		EntityPlayer player=UtilC.getThePlayer();
		float s=PartialTicksUtil.calculate(getPrevSize(), getSize());
		ColorF color=getColor();
		
		OpenGLM.translate(PartialTicksUtil.calculate(this));
		OpenGLM.rotate(-player.rotationYaw+90, 0, 1, 0);
		OpenGLM.rotate(player.rotationPitch, 0, 0, 1);
		OpenGLM.scale(s,s,s);
		
		OpenGLM.color(color.r, color.g, color.b, color.a);
	}

	@Override
	public int[] getModelIds(){
		return ParticleMistBubbleFactory.defultModel;
	}
}