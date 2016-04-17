package com.magiology.api.lang;

public interface ICommandInteract{
	public String getArgs();
	public String getName();
	public String getProgramName();
	public boolean isFullBlown();
	public Object onCommandReceive(String command);
	
	public void sendCommand();
	public void setArgs(String args);
	public void setName(String name);
	public void setProgramName(String name);
}
