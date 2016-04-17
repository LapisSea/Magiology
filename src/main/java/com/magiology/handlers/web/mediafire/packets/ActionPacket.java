package com.magiology.handlers.web.mediafire.packets;

import java.io.Serializable;

import com.magiology.handlers.web.mediafire.core.MFAction;

public class ActionPacket implements Serializable{
	private static final long serialVersionUID=1546453L;
	private String name;
	public ActionPacket(MFAction action){
		name=action.toString();
	}
	public MFAction getAction(){
		return MFAction.valueOf(name);
	}
}