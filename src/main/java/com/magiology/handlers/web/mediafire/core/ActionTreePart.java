package com.magiology.handlers.web.mediafire.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActionTreePart implements Serializable{
	private static final long serialVersionUID = 324245623L;
	
	private Stack<ActionTreePart> layers=new Stack<>();
	private String message;
	private List<ActionTreePart> subActions=new ArrayList<>();
	
	public ActionTreePart(String message){
		this.message=message;
	}

	public void add(ActionTreePart part){
		subActions.add(part);
	}
	public String getMessage(){
		return message;
	}
	public ActionTreePart getTop(){
		try{
			return layers.peek();
		}catch(Exception e){
			return this;
		}
	}
	
	public void popLayer(){
		layers.pop();
	}
	public void pushLayer(ActionTreePart actionTreePart){
		layers.push(actionTreePart);
	}
	public void remove(ActionTreePart part){
		subActions.remove(part);
	}
	public void setMessage(String message){
		this.message=message;
	}

	@Override
	public String toString(){
		StringBuilder result=new StringBuilder();
		result.append("action[msg=");
		result.append(getMessage());
		result.append(", child=");
		result.append(subActions);
		result.append(", layers=");
		result.append(layers);
		return result.toString();
	}

	public String toStringMultiLine(){
		StringBuilder result=new StringBuilder();
		
		if(!getMessage().isEmpty())result.append("msg=").append(getMessage()).append('\n');
		
		if(!subActions.isEmpty()){
			result.append("child{").append('\n');
			for(ActionTreePart part:subActions){
				String[] lines=part.toStringMultiLine().split("\n");
				for(String line:lines){
					result.append("\t").append(line).append('\n');
				}
			}
			result.append("}").append('\n');
		}
		if(!layers.isEmpty()){
			result.append("child{").append('\n');
			for(ActionTreePart part:layers){
				String[] lines=part.toStringMultiLine().split("\n");
				for(String line:lines){
					result.append("\t").append(line).append('\n');
				}
			}
			result.append("}").append('\n');
		}
		
		return result.toString();
	}
}
