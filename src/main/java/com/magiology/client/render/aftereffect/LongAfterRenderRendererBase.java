package com.magiology.client.render.aftereffect;

public abstract class LongAfterRenderRendererBase implements LongAfterRenderRenderer{
	private boolean isDead=false;

	@Override
	public boolean isDead(){
		return isDead;
	}

	@Override
	public void kill(){
		isDead=true;
	}
	
}