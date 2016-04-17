package com.magiology.api.connection;

import java.util.HashMap;
import java.util.Map;

import com.magiology.util.utilclasses.SideUtil;

import net.minecraft.util.EnumFacing;

//IConnection code holder
public class Connection implements IConnection{
	
	private Map<String, String> extra=new HashMap<String, String>();
	private final EnumFacing face;
	private final IConnectionProvider host;
	
	private boolean main,in,out,inEnabled,outEnabled,banned,willRender=true,thePowerOfTheDarkSide=true;
	
	private final ConnectionType type;
	
	public Connection(IConnectionProvider host,ConnectionType type,EnumFacing side){
		this.type=type;
		this.face=side;
		this.host=host;
	}
	public Connection(IConnectionProvider host,ConnectionType type,int side){
		this(host, type, EnumFacing.getFront(side));
	}
	
	@Override
	public void clear(){
		setMain(false);
		setIn(false);
		setOut(false);
	}
	//top of the morning dear ladies
	private int getActiveMates(){
		int result=0;
		for(IConnection i:getMates())if(i.isActive())result++;
		return result;
	}
	@Override public Map<String, String> getAllExtra(){return extra;}
	@Override public Object getExtra(String tag){return extra.get(tag);}
	@Override public EnumFacing getFaceEF(){return face;}

	@Override public int getFaceI(){return face.getIndex();}
	@Override public IConnectionProvider getHost(){return host;}
	@Override public boolean getIn(){return in;}
	@Override public boolean getInEnabled(){return inEnabled;}
	@Override public boolean getMain(){return main;}
	@Override public IConnection[] getMates(){return host.getConnections();}
	@Override public boolean getOut(){return out;}
	@Override public boolean getOutEnabled(){return outEnabled;}
	@Override public ConnectionType getType(){return type;}
	@Override public boolean hasForce(){return thePowerOfTheDarkSide;}
	@Override
	public boolean hasOpposite(){
		IConnection[] mates=getMates();
		return mates[SideUtil.getOppositeSide(getFaceI())].isActive();
	}
	@Override
	public boolean isActive(){
		return getMain()||(getIn()||getOut());
	}
	@Override public boolean isBanned(){return banned;}
	@Override public boolean isEnding(){return getActiveMates()==1;}
	@Override public boolean isIntersection(){return getActiveMates()>2;}
	@Override public void setBanned(boolean var){banned=var;}
	@Override public void setExtra(String tag, String obj){extra.put(tag, obj);}
	@Override public void setForce(boolean var){thePowerOfTheDarkSide=var;}
	@Override public void setIn(boolean var){in=var;}
	@Override public void setInEnabled(boolean var){inEnabled=var;}
	@Override public void setMain(boolean var){main=var;}
	@Override public void setOut(boolean var){out=var;}
	@Override public void setOutEnabled(boolean var){outEnabled=var;}
	@Override public void setWillRender(boolean var){willRender=var;}
	@Override
	public String toString(){
		return "Connection[face="+face+",type="+type+",host="+host+",main="+main+",in="+in+",out="+out+",inEnabled="+inEnabled+",outEnabled="+outEnabled+",banned="+banned+",willRender="+willRender+",hasForce="+hasForce()+"]";
	}
	@Override public boolean willRender(){return willRender;}
}