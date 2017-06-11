package com.magiology.client.post;

import com.magiology.util.interf.ObjectSimpleCallback;

import java.util.ArrayList;
import java.util.List;

public abstract class PostProcessFX{
	
	private static List<PostProcessFX> TO_RENDER=new ArrayList<>(), BATCHED_PASS=new ArrayList<>(), INSTANT_PASS=new ArrayList<>();
	
	private static final ObjectSimpleCallback<PostProcessFX> NOW=fx->{
		fx.render(INSTANT_PASS);
		processList(INSTANT_PASS);
		INSTANT_PASS.clear();
	}, OR_NEVER                                                 =fx->fx.render(BATCHED_PASS);
	
	private ObjectSimpleCallback<PostProcessFX> process;
	
	public static void processAll(){
		while(TO_RENDER.size()>0){
			processList(TO_RENDER);
			if(BATCHED_PASS.size()>0){
				TO_RENDER=BATCHED_PASS;
				BATCHED_PASS=new ArrayList<>();
			}
		}
	}

	private static void processList(List<PostProcessFX> l){
		l.forEach(PostProcessFX::process);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PostProcessFX(){
		this(false);
	}
	
	public PostProcessFX(boolean immediatePass){
		setImmediatePass(immediatePass);
	}
	
	public void process(){
		process.process(this);
	}
	
	protected abstract void render(List<PostProcessFX> anotherPass);
	
	protected void setImmediatePass(boolean immediatePass){
		process=immediatePass?NOW:OR_NEVER;
	}
}
