package com.magiology.api.power;

public interface ISidedPower extends PowerCore{
	public boolean getAllowedReceaver(int id);
	public boolean getAllowedSender(int id);
	
	public boolean getBannedSide(int id);
	public boolean getIn(int id);
	
	public boolean getOut(int id);
	public void setAllowedReceaver(boolean Boolean,int id);
	
	public void setAllowedSender(boolean Boolean,int id);
	public void setBannedSide(boolean Boolean,int id);
	
	public void setReceaveOnSide(int direction,boolean bolean);
	public void setSendOnSide(int direction,boolean bolean);
}
