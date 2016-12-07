package com.magiology.util.objs.color;

import java.awt.Color;

import com.magiology.util.statics.math.MathUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ColorMFinal implements IColorM{
	
	private final float r, g, b, a;
	
	public ColorMFinal(){
		this(1, 1, 1);
	}

	public ColorMFinal(IColorM color){
		this(color.r(),color.g(),color.b(),color.a());
	}
	
	public ColorMFinal(float r, float g, float b){
		this(r, g, b, 1);
	}
	
	public ColorMFinal(float r, float g, float b, float a){
		this.r=MathUtil.snap(r, 0, 1);
		this.g=MathUtil.snap(g, 0, 1);
		this.b=MathUtil.snap(b, 0, 1);
		this.a=MathUtil.snap(a, 0, 1);
	}
	
	@Override
	public float r(){
		return r;
	}
	
	@Override
	public float g(){
		return g;
	}
	
	@Override
	public float b(){
		return b;
	}
	
	@Override
	public float a(){
		return a;
	}
	
	public static ColorMFinal convert(Color color){
		return new ColorMFinal(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F, color.getAlpha()/256F);
	}
	
	@SideOnly(Side.CLIENT)
	public void bind(){
		GlStateManager.color(r(), g(), b(), a());
	}
	
	@Override
	public String toString(){
		return "(r="+r()+", g="+g()+", b="+b()+", a="+a()+")";
	}
}
