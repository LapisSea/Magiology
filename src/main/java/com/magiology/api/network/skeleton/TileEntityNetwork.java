package com.magiology.api.network.skeleton;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.connection.ConnectionType;
import com.magiology.api.connection.IConnection;
import com.magiology.api.connection.IConnectionFactory;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProviderHandler;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.TileEntityM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class TileEntityNetwork extends TileEntityM implements MultiColisionProvider,ISidedNetworkComponent,UpdateableTile{
	public boolean[] accessibleSides={true,true,true,true,true,true};
	protected BlockPos brainLoadup=new BlockPos(new Vec3d(0,0,0));
	
	public boolean canPathFindTheBrain,didCheckSides=false;
	private boolean changesDetected;

	public IConnection[] connections=IConnectionFactory.NewArray(this, ConnectionType.Energy);
	
	private List<Integer> ghostHits=new ArrayList<>(),prevGhostHits=new ArrayList<>();
	
	protected boolean isInitialized=false;
	
	protected long lastUpdate;
	
	public int pointId,prevPointId;
	
	private boolean updateConnectionsWaiting=false;
	@Override
	public boolean changesDetected(){
		return changesDetected;
	}
	public void checkBrainConnection(){
		if(getBrain()!=null)canPathFindTheBrain=getBrain().isInNetwork(this);
		if(!canPathFindTheBrain)for(int i=0;i<6;i++)if(getAccessibleOnSide(i)){
			TileEntity test=worldObj.getTileEntity(SideUtil.offsetNew(i, pos));
			if(test instanceof ISidedNetworkComponent){
				ISidedNetworkComponent t=(ISidedNetworkComponent)test;
				if(t.getBrain()!=null){
					setBrain(t.getBrain());
					canPathFindTheBrain=true;
					getBrain().restartNetwork();
				}
			}
		}
		if(getBrain()!=null)canPathFindTheBrain=getBrain().isInNetwork(this);
	}
	@Override
	public void connectionUpdateWaiting(){
		updateConnectionsWaiting=true;
	}
	
	@Override
	public void detectChanges(){
		DefaultMultiColisionProvider.detectChanges(this);
	}
	public void findBrain(){
		int side=-1;TileEntity test=null;
		for(int i=0;i<connections.length;i++)if(this.connections[i].getMain()&&
				(test=worldObj.getTileEntity(SideUtil.offsetNew(i, getPos())))instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)test).getBrain()!=null){
			side=i;i=this.connections.length;
		}
		if(side!=-1){
			ISidedNetworkComponent component=(ISidedNetworkComponent) worldObj.getTileEntity(SideUtil.offsetNew(side, pos));
			if(component!=null)NetworkBaseComponentHandler.setBrain(component.getBrain(), this);
		}
	}
	@Override
	public boolean getAccessibleOnSide(int side){
		return connections[side].getMain()&&accessibleSides[side];
	}
	@Override
	public TileEntityNetworkController getBrain(){
		if(!canPathFindTheBrain)return null;
		TileEntity tile=worldObj.getTileEntity(brainLoadup);
		if(!(tile instanceof TileEntityNetworkController))return null;
		return (TileEntityNetworkController)tile;
	}
	@Override
	public List<Integer> getGhostHits(){
		return ghostHits;
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
	public long getNetworkId(){
		return getBrain().getNetworkId();
	}
	@Override
	public int getOrientation(){
		return hasWorldObj()?UtilM.getBlockMetadata(worldObj,pos):-1;
	}
	@Override 
	public int getPointedBoxID(){
		return pointId;
	}
	@Override
	public List<Integer> getPrevGhostHits(){
		return prevGhostHits;
	}
	@Override 
	public int getPrevPointedBoxID(){
		return prevPointId;
	}
	@Override
	public AxisAlignedBB getRenderBoundingBox(){
		return MultiColisionProviderHandler.combineNormal(this).addCoord(pos).conv();
	}
//	public List<CollisionBox> collisionBoxes=CollisionBox.genColisionBoxList(new DoubleObject[]{
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,	   p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F),EnumFacing.getFront(5)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, 0,	   p*6.5F, p*9.5F, p*6.5F, p*9.5F),EnumFacing.getFront(1)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, 0,	   p*9.5F, p*9.5F, p*6.5F),EnumFacing.getFront(2)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*9.5F, p*6.5F, p*6.5F, 1,     p*9.5F, p*9.5F),EnumFacing.getFront(3)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*9.5F, p*6.5F, p*9.5F, 1,	   p*9.5F),EnumFacing.getFront(0)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F, 1	 ),EnumFacing.getFront(4)),
//			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F),null)
//	});
//	@Override
//	public CollisionBox getMainBox(){
//		return collisionBoxes.get(6);
//	}
//	
//	@Override
//	public List<CollisionBox> getBoxes(){
//		return collisionBoxes;
//	}
	public abstract void initNetworkComponent();
	@Override
	public void initTheComponent(){
		isInitialized=true;initNetworkComponent();updateColisionBoxes();
	}
	@Override
	public boolean isInitialized(){
		return isInitialized;
	}
	@Override
	public boolean isSideEnabled(int side){
		return accessibleSides[side];
	}
	@Override
	public boolean isUpdateWaiting(){
		return updateConnectionsWaiting;
	}
	@Override
	public boolean onGhostHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){return false;}
	@Override
	public boolean onNormalHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){return false;}
	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		brainLoadup=readPos(NBT, "brainP");
		canPathFindTheBrain=NBT.getBoolean("brainPath");
	}
	@Override
	public void sendChanges(){
		DefaultMultiColisionProvider.sendChanges(this);
	}
	@Override
	public void setAccessibleOnSide(int side, boolean accessible){
		accessibleSides[side]=accessible;
	}
	@Override
	public void setBrain(TileEntityNetworkController brain){
		if(brain!=null)brainLoadup=brain.getPos().add(0, 0, 0);
	}
	
	@Override
	public void setChangesDetected(boolean changes){
		changesDetected=changes;
	}
	
	@Override
	public void setGhostHits(List<Integer> hits){
		setPrevGhostHits(getGhostHits());
		ghostHits=hits;
		detectChanges();
	}

	@Override
	public void setOrientation(int orientation){
		UtilM.setMetadata(worldObj, pos, orientation);
	}
	
	@Override 
	public void setPointedBoxID(int box){
		setPrevPointedBox(getPointedBoxID());
		setPointedBoxID(box);
		detectChanges();
	}
	@Override
	public void setPrevGhostHits(List<Integer> hits){
		prevGhostHits=hits;
	}
	@Override
	public void setPrevPointedBox(int box){
		prevPointId=box;
	}
	@Override
	public void updateColisionBoxes(){
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
	}
	@Override
	public void updateWaitingUpdate(){
		if(!hasWorldObj()){
			updateConnectionsWaiting=true;
			return;
		}
		if(isUpdateWaiting())updateConnections();
		lastUpdate=getTime();
	}

	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		writePos(NBT, brainLoadup, "brainP");
		NBT.setBoolean("brainPath", canPathFindTheBrain);
	}
}
