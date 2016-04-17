package com.magiology.mcobjects.tileentityes.corecomponents;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.connection.Connection;
import com.magiology.api.connection.ConnectionType;
import com.magiology.api.connection.IConnection;
import com.magiology.api.connection.IConnectionFactory;
import com.magiology.api.connection.IConnectionProvider;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class TileEntityConnectionProvider extends TileEntityM implements UpdateableTile{
	
	public static IConnection[] readIConnections(IConnectionProvider host,NBTTagCompound NBT, String tag){
		IConnection[] result=new IConnection[6]; 
		NBTTagList list= NBT.getTagList(tag+"Cons", 10);
		for(int i=0;i<list.tagCount();i++){
			NBTTagCompound connection=list.getCompoundTagAt(i);
			result[i]=new Connection(host, 
			ConnectionType.values()[connection.getByte("type")], 
			EnumFacing.getFront(connection.getByte("face")));
			IConnection con=result[i];
			con.setMain(connection.getBoolean("main"));
			con.setIn(connection.getBoolean("in"));
			con.setOut(connection.getBoolean("out"));
			con.setBanned(connection.getBoolean("banned"));
			con.setWillRender(connection.getBoolean("render"));
			int extraSize=connection.getInteger("extraSize");
			for(int j=0;j<extraSize;j++)con.setExtra(connection.getString("k"+j), connection.getString("v"+j));
		}
		return result;
	}
	
	public static void writeIConnections(IConnection[] connections,NBTTagCompound NBT,String tag){
		NBTTagList list= new NBTTagList();
		for(int i=0;i<connections.length;i++){
			NBTTagCompound connection=new NBTTagCompound();
			IConnection con=connections[i];
			
			connection.setByte("face", (byte)con.getFaceI());
			connection.setByte("type", (byte)con.getType().id);
			connection.setBoolean("main", con.getMain());
			connection.setBoolean("in", con.getIn());
			connection.setBoolean("out", con.getOut());
			connection.setBoolean("banned", con.isBanned());
			connection.setBoolean("render", con.willRender());
			Set<Entry<String, String>> k=con.getAllExtra().entrySet();
			connection.setInteger("extraSize", k.size());
			int count=0;
			for(Entry<String, String> j:k){
				connection.setString("k"+count, j.getKey());
				connection.setString("v"+count, j.getValue());
				count++;
			}
			list.appendTag(connection);
		}
		NBT.setTag(tag+"Cons", list);
	}
	
	public IConnection[] connections=IConnectionFactory.NewArray(this, ConnectionType.Energy);
	
	 protected long lastUpdate;
		
		private boolean updateConnectionsWaiting=false;
	
	@Override
	public void connectionUpdateWaiting(){
		updateConnectionsWaiting=true;
	}
	@Override
	public IConnection[] getConnections(){
		return connections;
	}
	@Override
	public TileEntity getHost(){
		return this;
	}
	
	@Override
	public long getLastUpdateTime(){
		return lastUpdate;
	}
	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if((connections[0].getMain()&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false))return true;
		}
		if(facing==EnumFacing.WEST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&connections[0].getMain()==false&&connections[2].getMain()==false&&connections[3].getMain()==false))return true;
		}
		if(facing==EnumFacing.SOUTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&connections[0].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false))return true;
		}
		return false;
	}
	@Override
	public boolean isUpdateWaiting(){
		return updateConnectionsWaiting;
	}
	@Override
		public void readFromNBT(NBTTagCompound NBTTC){
			super.readFromNBT(NBTTC);
			IConnection[] a=readIConnections(this,NBTTC,"conData");
			if(ArrayUtils.indexOf(a, null)==-1)connections=a;
		}
	@Override
	public void updateWaitingUpdate(){
		if(!hasWorldObj()){
			connectionUpdateWaiting();
			return;
		}
		if(isUpdateWaiting())updateConnections();
		lastUpdate=getTime();
	}
	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
		writeIConnections(getConnections(), NBTTC, "conData");
	}
}
