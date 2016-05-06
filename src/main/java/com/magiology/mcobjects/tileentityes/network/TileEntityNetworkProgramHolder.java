package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.NetworkBaseComponent;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.NBTUtil;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityNetworkProgramHolder extends TileEntityNetwork implements ISidedInventory,ITickable{
	
	public List<CollisionBox> collisionBoxes;
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	
	public ItemStack[] slots=new ItemStack[16];
	
	public TileEntityNetworkProgramHolder(){}
	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return true;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing side){
		return true;
	}
	@Override
	public void clear(){
		
	}
	@Override
	public void closeInventory(EntityPlayer player){
		
	}
	@Override
	public ItemStack decrStackSize(int v1, int v2){
		if(this.slots[v1]!=null){
			ItemStack ItemS=null;
			
			if(this.slots[v1].stackSize<=v2){
				ItemS=this.slots[v1];
				this.slots[v1]=null;
				return ItemS;
			}else{
				ItemS=this.slots[v1].splitStack(v2);
				if(this.slots[v1].stackSize==0){
					this.slots[v1]=null;
				}
			}
			return ItemS;
		}
		return null;
	}
	public NetworkInterface getBoundedBaseInterface(){
		int side=getOrientation();
		TileEntity test=worldObj.getTileEntity(SideUtil.offset(side, pos));
		if(test instanceof NetworkInterface)return (NetworkInterface)test;
		return null;
	}
	@Override
	public List<CollisionBox> getBoxes(){
		return collisionBoxes;
	}
	@Override
	public IConnection[] getConnections(){
		return null;
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return null;
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkUtil.canConnect(this, (NetworkBaseComponent)tile);
	}
	@Override
	public int getField(int id){
		return 0;
	}
	@Override
	public int getFieldCount(){
		return 0;
	}
	@Override
	public int getInventoryStackLimit(){
		return 1;
	}
	@Override
	public CollisionBox getMainBox(){
		return collisionBoxes.get(6);
	}
	@Override
	public String getName(){
		return "NetworkPointerContainer";
	}
	@Override
	public int getSizeInventory(){
		return slots.length;
	}
	@Override
	public int[] getSlotsForFace(EnumFacing side){
		int[] result=new int[getSizeInventory()];
		for(int i=0;i<getSizeInventory();i++)result[i]=i;
		return result;
	}
	@Override
	public ItemStack getStackInSlot(int id){
		return slots[id];
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(NetworkBaseComponent.class);
	}
	@Override
	public boolean hasCustomName(){
		return true;
	}
	@Override
	public void initNetworkComponent(){}

	@Override
	public boolean isItemValidForSlot(int id, ItemStack stack){
		return UtilM.isItemInStack(MItems.networkPointer, stack);
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player){
		return UtilM.isItemInStack(MItems.networkPointer, player.getHeldItemMainhand());
	}

	@Override
	public void openInventory(EntityPlayer player){
		
	}

	@Override
	public void readFromNBT(NBTTagCompound NBT){
		super.readFromNBT(NBT);
		slots=NBTUtil.loadItemsFromNBT(NBT, "inventory", slots);
	}

	@Override
	public ItemStack removeStackFromSlot(int v1){
		if(this.slots[v1]!=null){
			ItemStack ItemS=this.slots[v1];
			this.slots[v1]=null;
			return ItemS;
		}
		return null;
	}

	@Override
	public void setField(int id, int value){
		
	}

	@Override
	public void setInventorySlotContents(int v1, ItemStack stack){
		this.slots[v1]=stack;
		if(stack!=null&&stack.stackSize>this.getInventoryStackLimit()){
			stack.stackSize=this.getInventoryStackLimit();
		}
		
	}

	@Override
	public void update(){
		if(getBrain()==null){
			findBrain();
			UpdateablePipeHandler.updatePipe(worldObj, pos);
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}

	@Override
	public void updateColisionBoxes(){
		collisionBoxes=CollisionBox.genColisionBoxList(new DoubleObject[]{
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*13,p*6, p*6, 1,	p*10, p*10 ),EnumFacing.getFront(5)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*13,p*6, p*10, 1,	  p*10 ),EnumFacing.getFront(1)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, 0,   p*10, p*10, p*3 ),EnumFacing.getFront(2)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, p*13,p*10, p*10, 1   ),EnumFacing.getFront(3)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, 0,   p*6, p*10, p*3,  p*10),EnumFacing.getFront(0)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,   p*6, p*6, p*3,  p*10, p*10),EnumFacing.getFront(4)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*3, p*3, p*3, p*13, p*13, p*13),null)
		});
	}
	
	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		updateColisionBoxes();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		NBTUtil.saveItemsToNBT(NBT, "inventory", slots);
	}
}
