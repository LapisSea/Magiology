package com.magiology.mcobjects.tileentityes.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.magiology.api.lang.JSProgramContainer;
import com.magiology.api.lang.program.ProgramDataBase;
import com.magiology.api.lang.program.ProgramUsable;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.Messageable;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.skeleton.TileEntityNetworkPow;
import com.magiology.api.power.ISidedPower;
import com.magiology.api.power.SixSidedBoolean;
import com.magiology.api.power.SixSidedBoolean.Modifier;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityNetworkController extends TileEntityNetworkPow{
	private static Map<TileEntity,Long> networkIdMap=new HashMap<TileEntity,Long>(); 
	public static long getNetworkID(TileEntity tile){
		//basic load
		Long long1=networkIdMap.get(tile);
		if(long1!=null)return long1.longValue();
		
		//clean up
		Object[] keys=networkIdMap.keySet().toArray();
		for(int i=0;i<keys.length;i++){
			TileEntity test=(TileEntity)keys[i];
			if(test.getWorld().getTileEntity(test.getPos())!=test)networkIdMap.remove(test);
		}
		
		//generate && save
		do{
			long1=RandUtil.RL();
		}while(networkIdMap.containsValue(long1)&&long1!=-1&&long1!=-2);
		networkIdMap.put(tile, long1);
		
		//inform client
		if(tile instanceof TileEntityM)((TileEntityM)tile).sync();
		
		//end
		return long1.longValue();
	}
	public List<CollisionBox> collisionBoxes=DefaultMultiColisionProvider.getStandardBoxes();
	private List<TileEntityNetworkProgramHolder> commandHolders=new ArrayList<TileEntityNetworkProgramHolder>();
	
	private long lastRestart=0;
	private List<ISidedNetworkComponent> network=new ArrayList<ISidedNetworkComponent>();
	SlowdownUtil optimizer=new SlowdownUtil(40);
	private List<Messageable> talkers=new ArrayList<Messageable>();
	
	
	public TileEntityNetworkController(){
		super(SixSidedBoolean.create(
				Modifier.First6False,Modifier.Exclude,
				SideUtil.enumFacingOrientation(EnumFacing.UP),SideUtil.enumFacingOrientation(EnumFacing.DOWN),
				Modifier.Last6False,Modifier.Exclude,
				-1
				).sides, SixSidedBoolean.lastGen.sides, 1, 20, 200, 100000);
	}
	
	public void broadcast(String action){
		for(Messageable i:talkers)i.onMessageReceved(action);
	}
	public void broadcastWithCheck(TileEntityNetworkRouter router, String action){
		for(Messageable i:talkers){
			if(i instanceof TileEntity&&i instanceof NetworkInterface&&i!=null){
				if(router.willSendTo((TileEntity&NetworkInterface&Messageable)i))i.onMessageReceved(action);
			}
		}
	}
	@Override public void findBrain(){}
	
	@Override
	public List<CollisionBox> getBoxes(){
		return collisionBoxes;
	}
	
	@Override public TileEntityNetworkController getBrain(){return this;}
	
	public List<TileEntityNetworkProgramHolder> getCommandHolders(){return commandHolders;}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		if(tile instanceof ISidedPower){
			ISidedPower pow=(ISidedPower)tile;
			boolean Return=PowerUtil.canISidedPowerSendFromTo(pow, this, side);
			if(Return&&tile instanceof UpdateableTile)((UpdateableTile)tile).updateConnections();
			return Return;
		}else if(tile instanceof ISidedNetworkComponent){
			return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
		}
		return false;
	}
	
	@Override
	public CollisionBox getMainBox(){
		return collisionBoxes.get(6);
	}
	
	public List<ISidedNetworkComponent> getNetworkComponents(){return network;}
	
	@Override public long getNetworkId(){return getNetworkID(this);}
	public List<ProgramUsable> getProgram(){
		List<ProgramUsable> result=new ArrayList<ProgramUsable>();
		for(TileEntityNetworkProgramHolder i:commandHolders){
			for(ItemStack j:i.slots){
				if(j!=null){
					JSProgramContainer program=(JSProgramContainer) j.getItem();
					String name=program.getProgram(j).getSaveableData().programName.toString(),code=ProgramDataBase.code_get(program.getId(j));
					if(!name.isEmpty()&&!code.isEmpty())result.add(program.getProgram(j));
				}
			}
		}
		return result;
	}
	public DoubleObject<ProgramUsable,TileEntityNetworkProgramHolder> getProgram(String name){
		try{
			TileEntity tile=worldObj.getTileEntity(pos);
			if(!(tile instanceof TileEntityNetworkProgramHolder))return null;
			if(((TileEntityNetworkProgramHolder)tile).getBrain()!=this)return null;
			for(ItemStack j:((TileEntityNetworkProgramHolder)tile).slots){
				if(j!=null){
					JSProgramContainer program=(JSProgramContainer) j.getItem();
					if(program.getProgram(j).getSaveableData().programName.equals(name)){
						return new DoubleObject<ProgramUsable,TileEntityNetworkProgramHolder>(ProgramDataBase.getProgram(program.getId(j)), (TileEntityNetworkProgramHolder)tile);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public List<Messageable> getTalkers(){return talkers;}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
		included.add(ISidedPower.class);
	}
	@Override
	public void initNetworkComponent(){
		canPathFindTheBrain=true;
	}
	public boolean isInNetwork(Object object){
		try{return network.contains(object);}
		catch(Exception e){return false;}
	}
	@Override
	public boolean onGhostHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){return false;}
	@Override
	public boolean onNormalHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){return false;}
	public void power(boolean isRepeatable){
		
	}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		networkIdMap.put(this, NBT.getLong("NI"));
	}
	
	public void restartNetwork(){
		try{
			if(worldObj!=null&&worldObj.getTotalWorldTime()!=lastRestart){
				lastRestart=worldObj.getTotalWorldTime();
			}else return;
			ISidedNetworkComponent tile=this;
			ISidedNetworkComponent currentTile=tile;
			boolean more=true;
			network.clear();
			commandHolders.clear();
			talkers.clear();
			network.add(currentTile);
			Map<TileEntity,boolean[]> tileSkipper=new HashMap<TileEntity,boolean[]>();
			do{
				more=false;
				for(int j=0;j<network.size();j++){
					ISidedNetworkComponent workTile=network.get(j);
					
					if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
					boolean isDone=true;
					for(boolean a:tileSkipper.get(workTile.getHost()))if(a)isDone=false;
					if(!isDone)for(int i=0;i<6;i++)if(workTile.getAccessibleOnSide(i)){
						TileEntity test=workTile.getHost().getWorld().getTileEntity(SideUtil.offsetNew(i, workTile.getHost().getPos()));
						if(test instanceof ISidedNetworkComponent){
							ISidedNetworkComponent t=(ISidedNetworkComponent)test;
							if(t!=null&&!network.contains(t)&&t.getBrain()!=null&&t.getNetworkId()==getNetworkId()){
								more=true;
								workTile=t;
								network.add(workTile);
								if(workTile instanceof TileEntityNetworkProgramHolder)commandHolders.add((TileEntityNetworkProgramHolder)workTile);
								if(workTile instanceof Messageable)talkers.add((Messageable)workTile);
							}else{
								if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
								tileSkipper.get(workTile.getHost())[i]=false;
							}
						}else{
							if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
							tileSkipper.get(workTile.getHost())[i]=false;
						}
					}else{
						if(tileSkipper.get(workTile.getHost())==null)tileSkipper.put(workTile.getHost(), new boolean[]{true,true,true,true,true,true});
						tileSkipper.get(workTile.getHost())[i]=false;
					}
					
				}
			}while(more);
			for(int i=0;i<network.size();i++){
				ISidedNetworkComponent a=network.get(i);
				if(a instanceof UpdateableTile)((UpdateableTile)a).updateConnections();
				if(a instanceof TileEntityM)((TileEntityM)a).sync();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@Override public void setBrain(TileEntityNetworkController brain){}
	@Override
	public void update(){
		
		
		this.power(true);
		canPathFindTheBrain=true;
		if(optimizer.isTimeWithAddProgress()){
			this.updateConnections();
		}
		PowerUtil.sortSides(this);
	}
//	expectedBoxes=new AxisAlignedBB[]{
//			new AxisAlignedBB(0,	 p*6.5,  p*6.5, p*5,   p*9.5, p*9.5),
//			new AxisAlignedBB(p*6.5, 0,	  p*6.5, p*9.5, p*5,   p*9.5),
//			new AxisAlignedBB(p*6.5, p*6.5,  0,	 p*9.5, p*9.5, p*5  ),
//			new AxisAlignedBB(p*11, p*6.5,  p*6.5, 1,	 p*9.5, p*9.5),
//			new AxisAlignedBB(p*6.5, p*10.5, p*6.5, p*9.5, 1,	 p*9.5),
//			new AxisAlignedBB(p*6.5, p*6.5,  p*11, p*9.5, p*9.5, 1	),
//			new AxisAlignedBB(p*5,   p*5,	p*5, p*11, p*10.5,  p*11)
//	};
	@Override
	public void updateColisionBoxes(){
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
	}
	
	@Override
	public void updateConnections(){
		EnumFacing[] connectionsDummy=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(connectionsDummy, this);
		for(int i=0;i<connectionsDummy.length;i++){
			connections[i].setMain(connectionsDummy[i]!=null);
		}
		
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i!=SideUtil.DOWN()||i!=SideUtil.UP());
		PowerUtil.sortSides(this);
		updateColisionBoxes();
	}
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		NBT.setLong("NI", getNetworkId());
	}
}
