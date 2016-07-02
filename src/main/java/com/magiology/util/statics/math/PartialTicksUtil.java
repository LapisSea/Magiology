package com.magiology.util.statics.math;


import org.lwjgl.util.vector.Vector2f;

import com.magiology.handlers.particle.IParticle;
import com.magiology.util.interf.Calculable;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.Vec3M;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PartialTicksUtil{

	public static float partialTicks=0;
	
	public static <T extends Calculable<T>> T calculate(T prevPos,T pos){
		return prevPos.add(pos.sub(prevPos).mul(partialTicks));
	}
	public static double calculate(double prevPos,double pos){
		return (prevPos+(pos-prevPos)*partialTicks);
	}
	public static float calculate(float prevPos,float pos){
		return (prevPos+(pos-prevPos)*partialTicks);
	}

	public static Vec3M calculate(Entity entity){
		return new Vec3M(
			calculateX(entity),
			calculateY(entity),
			calculateZ(entity)
		);
	}

	public static float[] calculate(final float[] prevPos,final float[] pos){
		if(pos.length!=prevPos.length)return null;
		float[] result=new float[pos.length];
		for(int a=0;a<pos.length;a++)result[a]=calculate(prevPos[a], pos[a]);
		return result;
	}
	
	public static float[][] calculate(float[][] prevPos,float[][] pos){
		if(pos.length!=prevPos.length)return null;
		float[][] result=new float[pos.length][0];
		for(int a=0;a<pos.length;a++)result[a]=calculate(prevPos[a], pos[a]);
		return result;
	}
	public static Vec3M calculate(Vec3M prevPos, Vec3M pos){
		return new Vec3M(
			calculate(prevPos.x, pos.x),
			calculate(prevPos.y, pos.y),
			calculate(prevPos.z, pos.z)
		);
	}
	public static Vector2f calculate(Vector2f prevVec, Vector2f vec){
		return new Vector2f(
			calculate(prevVec.x, vec.x),
			calculate(prevVec.y, vec.y)
		);
	}
	public static double calculateX(Entity entity){
		return calculate(entity.prevPosX, entity.posX);
	}
	
	public static double calculateY(Entity entity){
		return calculate(entity.prevPosY, entity.posY);
	}

	public static double calculateZ(Entity entity){
		return calculate(entity.prevPosZ, entity.posZ);
	}
	public static ColorF calculate(ColorF prevColor, ColorF color){
		return new ColorF(calculate(prevColor.r, color.r),
						  calculate(prevColor.g, color.g),
						  calculate(prevColor.b, color.b),
						  calculate(prevColor.a, color.a));
	}
	public static Vec3M calculate(IParticle particle){
		return calculate(particle.getPrevPos(),particle.getPos());
	}
	
}
