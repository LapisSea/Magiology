package com.magiology.util.objs.color;

import com.magiology.util.interf.Calculable;
import com.magiology.util.statics.math.MathUtil;

import java.awt.*;

public class ColorM extends ColorMRead implements Calculable<ColorM>{
	
	private static final float minDiff=1/256F;
	
	public ColorM(){
		super();
	}
	
	public ColorM(IColorM color){
		this(color.r(), color.g(), color.b(), color.a());
	}
	
	public ColorM(double r, double g, double b){
		super(r, g, b);
	}
	
	public ColorM(double r, double g, double b, double a){
		super(r, g, b, a);
	}
	
	public ColorM(float r, float g, float b, float a){
		super(r, g, b, a);
	}
	
	public ColorM(float r, float g, float b){
		super(r, g, b);
	}
	
	public void r(float r){
		this.r=MathUtil.snap(r, 0, 1);
	}
	
	public void g(float g){
		this.g=MathUtil.snap(g, 0, 1);
	}
	
	public void b(float b){
		this.b=MathUtil.snap(b, 0, 1);
	}
	
	public void a(float a){
		this.a=MathUtil.snap(a, 0, 1);
	}
	
	public ColorM mix(Color color){
		return mix(IColorM.convert(color));
	}
	
	public ColorM mix(Color color, float scale1, float scale2){
		return mix(IColorM.convert(color), scale1, scale2);
	}
	
	public ColorM negative(){
		return this;
	}
	
	public ColorM set(float modifier, char c){
		modifier=MathUtil.snap(modifier, 0, 1);
		return new ColorM(c=='r'?modifier:r, c=='g'?modifier:g, c=='b'?modifier:b, c=='a'?modifier:a);
	}
	
	@Override
	public ColorM add(float var){
		r(r()+var);
		g(g()+var);
		b(b()+var);
		a(a()+var);
		return this;
	}
	
	public ColorM addR(float r){
		r(r()+r);
		return this;
	}
	
	public ColorM addG(float r){
		g(g()+g);
		return this;
	}
	
	public ColorM addB(float b){
		b(b()+b);
		return this;
	}
	
	public ColorM addA(float b){
		a(a()+a);
		return this;
	}
	
	@Override
	public ColorM add(ColorM var){
		r(r()+var.r());
		g(g()+var.g());
		b(b()+var.b());
		a(a()+var.a());
		return this;
	}
	
	@Override
	public ColorM div(float var){
		r(r()/var);
		g(g()/var);
		b(b()/var);
		a(a()/var);
		return this;
	}
	
	public ColorM divR(float r){
		r(r()/r);
		return this;
	}
	
	public ColorM divG(float r){
		g(g()/g);
		return this;
	}
	
	public ColorM divB(float b){
		b(b()/b);
		return this;
	}
	
	public ColorM divA(float b){
		a(a()/a);
		return this;
	}
	
	@Override
	public ColorM div(ColorM var){
		r(r()/var.r());
		g(g()/var.g());
		b(b()/var.b());
		a(a()/var.a());
		return this;
	}
	
	@Override
	public ColorM mul(float var){
		r(r()*var);
		g(g()*var);
		b(b()*var);
		a(a()*var);
		return this;
	}
	
	public ColorM mulR(float r){
		r(r()*r);
		return this;
	}
	
	public ColorM mulG(float r){
		g(g()*g);
		return this;
	}
	
	public ColorM mulB(float b){
		b(b()*b);
		return this;
	}
	
	public ColorM mulA(float b){
		a(a()*a);
		return this;
	}
	
	@Override
	public ColorM mul(ColorM var){
		r(r()*var.r());
		g(g()*var.g());
		b(b()*var.b());
		a(a()*var.a());
		return this;
	}
	
	@Override
	public ColorM sub(float var){
		r(r()-var);
		g(g()-var);
		b(b()-var);
		a(a()-var);
		return this;
	}
	
	public ColorM subR(float r){
		r(r()-r);
		return this;
	}
	
	public ColorM subG(float r){
		g(g()-g);
		return this;
	}
	
	public ColorM subB(float b){
		b(b()-b);
		return this;
	}
	
	public ColorM subA(float b){
		a(a()-a);
		return this;
	}
	
	@Override
	public ColorM sub(ColorM var){
		r(r()-var.r());
		g(g()-var.g());
		b(b()-var.b());
		a(a()-var.a());
		return this;
	}
	
	@Override
	public ColorM copy(){
		return new ColorM(r(), g(), b(), a);
	}
	
	public static ColorM toColorM(IColorM color){
		return color instanceof ColorM?(ColorM)color:new ColorM(color);
	}
}
