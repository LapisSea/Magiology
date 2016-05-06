package com.magiology.util.utilobjects.vectors.physics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.math.PartialTicksUtil;

public class PhysicsFloat{
	
	public float point,prevPoint,wantedPoint,speed,friction=1,acceleration,bounciness;
	public boolean simpleVersion=false;
	private Map<String, Boolean> wallsB=new HashMap<String, Boolean>();
	private Map<String, Float>   wallsF=new HashMap<String, Float>();
	
	public PhysicsFloat(float startingPoint,float acceleration){
		prevPoint=point=wantedPoint=startingPoint;
		this.acceleration=acceleration;
	}
	public PhysicsFloat(float startingPoint,float acceleration, boolean simple){
		this(startingPoint, acceleration);
		simpleVersion=simple;
	}
	public void addWall(String key, float wallPos,boolean pointOnSideOfTheWall){
		wallsF.put(key, wallPos);
		wallsB.put(key, pointOnSideOfTheWall);
	}
	public void bounce(float multiplyer){
		speed*=-multiplyer;
	}
	@Override
	public PhysicsFloat clone(){
		PhysicsFloat result=new PhysicsFloat(point,acceleration);
		result.simpleVersion=simpleVersion;
		result.prevPoint=prevPoint;
		result.wantedPoint=wantedPoint;
		result.speed=speed;
		result.friction=friction;
		result.bounciness=bounciness;
		result.wallsF=new HashMap<>();
		result.wallsB=new HashMap<>();
		wallsF.forEach((k,v)->result.wallsF.put(k,v));
		wallsB.forEach((k,v)->result.wallsB.put(k,v));
		
		return result;
	}
	public float difference(){
		return Math.abs(point-wantedPoint);
	}
	public float getPoint(){
		return PartialTicksUtil.calculate(prevPoint, point);
	}
	public Object[] removeWall(String key){
		Object[] Return=new Object[2];
		Return[0]=wallsF.remove(key);
		Return[1]=wallsB.remove(key);
		return Return;
	}
	public void removeWalls(){
		wallsF.clear();
		wallsB.clear();
	}
	public void update(){
		prevPoint=point;
		if(simpleVersion){
			point=UtilM.graduallyEqualize(point, wantedPoint, acceleration);
		}else{
			speed=UtilM.handleSpeedFolower(speed, point, wantedPoint, acceleration);
			speed*=friction;
			point+=speed;
			if(!wallsF.isEmpty()){
				Iterator<Float> fi=wallsF.values().iterator();
				Iterator<Boolean> bi=wallsB.values().iterator();
				try{
					while(fi.hasNext()){
						float   f=fi.next();
						boolean b=bi.next();
						if(b){
							if(point>f){
								bounce(bounciness);
								point=f;
							}
						}else{
							if(point<f){
								bounce(bounciness);
								point=f;
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
}
