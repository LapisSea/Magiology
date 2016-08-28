package com.magiology.util.objs;

import java.awt.Color;

import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.math.MathUtil;

import net.minecraft.client.renderer.GlStateManager;

public class ColorF{
	public static final ColorF 
		BLACK	 =ColorF.convert(Color.BLACK),
		BLUE	  =ColorF.convert(Color.BLUE),
		CYAN	  =ColorF.convert(Color.CYAN),
		DARK_GRAY =ColorF.convert(Color.DARK_GRAY),
		GRAY	  =ColorF.convert(Color.GRAY),
		GREEN	  =ColorF.convert(Color.GREEN),
		LIGHT_GRAY=ColorF.convert(Color.LIGHT_GRAY),
		MAGENTA	  =ColorF.convert(Color.MAGENTA),
		ORANGE	  =ColorF.convert(Color.ORANGE),
		PINK	  =ColorF.convert(Color.PINK),
		RED	  	  =ColorF.convert(Color.RED),
		WHITE	  =ColorF.convert(Color.WHITE),
		YELLOW	  =ColorF.convert(Color.YELLOW);
	
	public static ColorF randomRGB(){
		return new ColorF(RandUtil.RF(),RandUtil.RF(),RandUtil.RF(),1);
	}
	public static ColorF randomRGBA(){
		return new ColorF(RandUtil.RF(),RandUtil.RF(),RandUtil.RF(),RandUtil.RF());
	}
	public static ColorF convert(Color color){
		return new ColorF(color.getRed()/256F, color.getGreen()/256F, color.getBlue()/256F, color.getAlpha()/256F);
	}
	
	public float r,g,b,a;
	public ColorF(){
		this(1,1,1,1);
	}
	public ColorF(float r, float g, float b, float a){
		this.r=MathUtil.snap(r, 0, 1);
		this.g=MathUtil.snap(g, 0, 1);
		this.b=MathUtil.snap(b, 0, 1);
		this.a=MathUtil.snap(a, 0, 1);
	}
	public ColorF(double r, double g, double b, double a){
		this((float)r,(float)g,(float)b,(float)a);
	}
	public ColorF add(ColorF color){
		return new ColorF(color.r+r,color.g+g,color.b+b,color.a+a);
	}
	public void bind(){
		GlStateManager.color(r,g,b,a);
	}
	public ColorF blackNWhite(){
		float sum=(r+g+b)/3F;
		return new ColorF(sum,sum,sum,a);
	}
	public ColorF copy(){
		return new ColorF(r, g, b, a);
	}
	public ColorF disablBlend(){
		return new ColorF(r,g,b,1);
	}
	public ColorF mix(Color color){
		return mix(convert(color));
	}
	public ColorF mix(Color color, float scale1,float scale2){
		return mix(convert(color), scale1, scale2);
	}
	public ColorF mix(ColorF color){
		return new ColorF((r+color.r)/2F,(g+color.g)/2F,(b+color.b)/2F,(a+color.a)/2F);
	}
	public ColorF mix(ColorF color, float scale1,float scale2){
		return new ColorF(
				(r*scale1+color.r*scale2)/(scale1+scale2),
				(g*scale1+color.g*scale2)/(scale1+scale2),
				(b*scale1+color.b*scale2)/(scale1+scale2),
				(a*scale1+color.a*scale2)/(scale1+scale2)
				);
	}
	public ColorF mul(double var){
		return new ColorF(r*var, g*var, b*var, a*var);
	}
	public ColorF mul(double r, double g, double b, double a){
		return new ColorF(this.r*r, this.g*g, this.b*b, this.a*a);
	}
	public ColorF negative(){
		return new ColorF(1-r, 1-g, 1-b, 1-a);
	}
	public ColorF set(float modifier, char c){
		modifier=MathUtil.snap(modifier, 0, 1);
		return new ColorF(c=='r'?modifier:r, c=='g'?modifier:g, c=='b'?modifier:b, c=='a'?modifier:a);
	}
	public int toCode(){
		return new Color(r,g,b,a).hashCode();
	}

	@Override
	public String toString(){
		return "("+(r+"").substring(0, Math.min((r+"").length(),4))+", "+(g+"").substring(0, Math.min((g+"").length(),4))+", "+(b+"").substring(0, Math.min((b+"").length(),4))+", "+(a+"").substring(0, Math.min((a+"").length(),4))+")";
	}
}
