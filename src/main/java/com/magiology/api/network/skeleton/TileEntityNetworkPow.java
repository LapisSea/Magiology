package com.magiology.api.network.skeleton;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProviderHandler;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityNetworkPow extends TileEntityPow implements MultiColisionProvider,ISidedNetworkComponent,UpdateableTile{
	public boolean[] accessibleSides={true,true,true,true,true,true};
	protected BlockPos brainPos;
	
	public boolean canPathFindTheBrain,didCheckSides=false;

	private boolean changesDetected;
	private List<Integer> ghostHits=new ArrayList<>(),prevGhostHits=new ArrayList<>();
	protected boolean isInitialized=false;
	
	public int pointId,prevPointId;
	
	public TileEntityNetworkPow(boolean[] allowedSidedPower,boolean[] sidedPower,int minTSpeed, int middleTSpeed, int maxTSpeed, int maxEnergyBuffer){
		super(allowedSidedPower, sidedPower, minTSpeed, middleTSpeed, maxTSpeed, maxEnergyBuffer);
	}
	@Override
	public boolean changesDetected(){
		return changesDetected;
	}
	public void checkBrainConnection(){
		canPathFindTheBrain=getBrain()!=null?getBrain().isInNetwork(this):false;
	}
	@Override
	public void detectChanges(){
		DefaultMultiColisionProvider.detectChanges(this);
	}

	public void findBrain(){
		int side=-1;TileEntity test=null;
		for(int i=0;i<this.connections.length;i++)if(this.connections[i].getMain()&&
				(test=worldObj.getTileEntity(SideUtil.offsetNew(i, pos)))instanceof ISidedNetworkComponent&&((ISidedNetworkComponent)test).getBrain()!=null){
			side=i;i=this.connections.length;
		}
		if(side!=-1){
			ISidedNetworkComponent component=(ISidedNetworkComponent) worldObj.getTileEntity(SideUtil.offsetNew(side, pos));
			if(component!=null)NetworkBaseComponentHandler.setBrain(component.getBrain(), this);
		}
	}
	@Override
	public boolean getAccessibleOnSide(int side){
		return connections[side].getMain();
	}
	@Override
	public TileEntityNetworkController getBrain(){
		if(!canPathFindTheBrain)return null;
		TileEntity tile=worldObj.getTileEntity(brainPos);
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
	public long getNetworkId(){
		return getBrain().getNetworkId();
	}
	@Override
	public int getOrientation(){
		return U.getBlockMetadata(worldObj, pos);
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
		return MultiColisionProviderHandler.combineNormal(this).addCoord(getPos()).conv();
	}
	public abstract void initNetworkComponent();
	@Override
	public void initTheComponent(){
		isInitialized=true;
		initNetworkComponent();
		updateColisionBoxes();
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
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		brainPos=readPos(NBT, "brainP");
		canPathFindTheBrain=NBT.getBoolean("brainPath");
	}
	@Override
	public void sendChanges(){
		DefaultMultiColisionProvider.sendChanges(this);
	}
	@Override
	public void setAccessibleOnSide(int side, boolean accessible){
		accessibleSides[side]=accessible;}
	@Override
	public void setBrain(TileEntityNetworkController brain){
		if(brain!=null)brainPos=brain.getPos().add(0,0,0);
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
		UtilM.setMetadata(worldObj,pos, orientation);
	}
	@Override 
	public void setPointedBoxID(int box){
		prevPointId=pointId;
		pointId=box;
		detectChanges();
	}
	@Override
	public void setPrevGhostHits(List<Integer> prevGhostHits){
		this.prevGhostHits=prevGhostHits;
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
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		if(brainPos!=null)writePos(NBT, brainPos, "brainP");
		NBT.setBoolean("brainPath", canPathFindTheBrain);
	}
}
