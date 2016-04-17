package com.magiology.client.render.shaders;

import com.magiology.client.render.shaders.core.ShaderAspectRenderer;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.math.CricleUtil;
import com.magiology.util.utilobjects.vectors.physics.PhysicsFloat;

public class BlurRenderer extends ShaderAspectRenderer{
	public static BlurRenderer instance;
	
	public boolean shouldRender=false;
	
	public PhysicsFloat x=new PhysicsFloat(1, 0.08F),y=new PhysicsFloat(1, 0.08F);
	
	public BlurRenderer(){
		super("blurM",0,"BlurDir");
		instance=this;
		x.friction=0.95F;
		y.friction=0.95F;
	}
	
	@Override
	public boolean getConditionForActivation(){
		return shouldRender;
	}
	
	@Override
	public void redner(){
		setUniform(uniforms.get(0), x.getPoint(),y.getPoint());
	}
	
	@Override
	public void update(){
		x.update();
		y.update();
		if(Math.sqrt(x.speed*x.speed+y.speed*y.speed)<0.2){
			float rotation=RandUtil.RF(360);
			x.wantedPoint=CricleUtil.sin(rotation)*2;
			y.wantedPoint=CricleUtil.cos(rotation)*2;
		}
	}
}
