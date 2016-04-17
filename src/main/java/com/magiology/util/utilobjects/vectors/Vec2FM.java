package com.magiology.util.utilobjects.vectors;

import org.lwjgl.util.vector.Vector2f;

public class Vec2FM extends Vector2f{
	
	public Vec2FM(){
		
	}
	public Vec2FM(float x, float y){
		set(x, y);
	}
	@Override
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
}
