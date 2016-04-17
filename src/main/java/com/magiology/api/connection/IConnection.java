package com.magiology.api.connection;

import java.util.Map;

import net.minecraft.util.EnumFacing;

public interface IConnection{
	public void clear();
	public Map<String, String> getAllExtra();
	
	public Object getExtra(String tag);
	public EnumFacing getFaceEF();
	public int getFaceI();
	public IConnectionProvider getHost();
	public boolean getIn();
	public boolean getInEnabled();
	public boolean getMain();
	public IConnection[] getMates();
	
	public boolean getOut();
	public boolean getOutEnabled();
	
	public ConnectionType getType();
	//I have the force! Taaaaaa! ta taaaaa.
	public boolean hasForce();
	
	public boolean hasOpposite();
	public boolean isActive();
	
	public boolean isBanned();
	
	public boolean isEnding();
	public boolean isIntersection();
	public void setBanned(boolean var);
	
	public void setExtra(String tag, String obj);
	
	public void setForce(boolean var);
	public void setIn(boolean var);
	public void setInEnabled(boolean var);
	
	public void setMain(boolean var);
	public void setOut(boolean var);
	
	public void setOutEnabled(boolean var);
	
	public void setWillRender(boolean var);
	public boolean willRender();
}
