package com.magiology.mcobjects.tileentityes.network;

import java.util.ArrayList;
import java.util.List;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.Messageable;
import com.magiology.api.network.NetworkInterface;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.items.NetworkPointer;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.util.renderers.FloatCalc;
import com.magiology.util.renderers.ValueWithPrev;
import com.magiology.util.utilclasses.CollectionConverter;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.NBTUtil;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.codeinsert.ObjectReturn;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;

public class TileEntityNetworkRouter extends TileEntityNetwork implements ISidedInventory,ITickable{
	
	public ValueWithPrev<FloatCalc>[] animationos=new ValueWithPrev[]{
		new ValueWithPrev(),new ValueWithPrev(),new ValueWithPrev(),
		new ValueWithPrev(),new ValueWithPrev(),new ValueWithPrev(),
		new ValueWithPrev(),new ValueWithPrev(),new ValueWithPrev()
	};
	public List<CollisionBox> collisionBoxesBase=CollisionBox.genColisionBoxList(new DoubleObject[]{
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,	   p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F),EnumFacing.getFront(5)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, 0,	   p*6.5F, p*9.5F, p*6.5F, p*9.5F),EnumFacing.getFront(1)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, 0,	   p*9.5F, p*9.5F, p*6.5F),EnumFacing.getFront(2)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*9.5F, p*6.5F, p*6.5F, 1,     p*9.5F, p*9.5F),EnumFacing.getFront(3)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*9.5F, p*6.5F, p*9.5F, 1,	   p*9.5F),EnumFacing.getFront(0)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F, 1	 ),EnumFacing.getFront(4)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F),null)
	}),slotBoxes[]=new ObjectReturn<List<CollisionBox>[]>(){
		@Override
		public List<CollisionBox>[] process(){
			List<CollisionBox>[] lists=new List[6];
			List<AxisAlignedBBM> data=new ArrayList<>();
			
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*9.5-i*p*2, p*9.5-j*p*2, p*4.8, p*10.5-i*p*2, p*10.5-j*p*2, p*9));
			lists[0]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			data.clear();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*9.5-i*p*2, p*9.5-j*p*2, p*7, p*10.5-i*p*2, p*10.5-j*p*2, p*11.2));
			lists[1]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			data.clear();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*4.8, p*9.5-j*p*2, p*9.5-i*p*2, p*9, p*10.5-j*p*2, p*10.5-i*p*2));
			lists[1]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			data.clear();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*7, p*9.5-j*p*2, p*9.5-i*p*2, p*11.2, p*10.5-j*p*2, p*10.5-i*p*2));
			lists[1]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			data.clear();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*9.5-i*p*2, p*4.8, p*9.5-j*p*2, p*10.5-i*p*2, p*9, p*10.5-j*p*2));
			lists[1]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			data.clear();
			for(int i=0;i<3;i++)for(int j=0;j<3;j++)data.add(new AxisAlignedBBM(p*9.5-i*p*2, p*7, p*9.5-j*p*2, p*10.5-i*p*2, p*11.2, p*10.5-j*p*2));
			lists[1]=CollisionBox.genColisionBoxList(CollectionConverter.convAr(data,DoubleObject.class,(box)->new DoubleObject<AxisAlignedBBM, EnumFacing>(box,null)));
			
			return lists;
		}
	}.process(),collisionBoxes=collisionBoxesBase;
	
	public boolean[] extractionActivated=new boolean[9];
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	
	public ItemStack[] slots=new ItemStack[10];
	
	public TileEntityNetworkRouter(){
	}
	@Override
	public boolean canExtractItem(int index, ItemStack stack,EnumFacing direction){
		return canInsertItem(index, stack,direction);
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,EnumFacing side){
		return side.getIndex()==SideUtil.getOppositeSide(getOrientation());
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
	public NetworkInterface getBoundedInterface(){
		TileEntity test=worldObj.getTileEntity(pos.offset(EnumFacing.getFront(SideUtil.getOppositeSide(getOrientation()))));
		if(test instanceof NetworkInterface)return (NetworkInterface)test;
		return null;
	}
	@Override
	public List<CollisionBox> getBoxes(){
		return collisionBoxes;
	}
	@Override
	public IConnection[] getConnections(){
		return connections;
	}
	
	@Override
	public ITextComponent getDisplayName(){
		return null;
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return !(clazz.equals(ISidedNetworkComponent.class))&&NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
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
		return 9;
	}
	@Override
	public int[] getSlotsForFace(EnumFacing side){
		if(side.getIndex()==SideUtil.getOppositeSide(getOrientation()))return new int[]{0,1,2,3,4,5,6,7,8};
		return null;
	}
	@Override
	public ItemStack getStackInSlot(int id){
		return slots[id];
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(NetworkInterface.class);
		excluded.add(ISidedNetworkComponent.class);
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
		for(int i=0;i<animationos.length;i++)if(slots[i]!=null){
			extractionActivated[i]=true;
		}
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
		
		for(int i=0;i<animationos.length;i++){
			boolean stackNull=getStackInSlot(i)==null;
			
			if(stackNull)extractionActivated[i]=false;
			
			animationos[i].update(new FloatCalc(UtilM.graduallyEqualize(animationos[i].value.value, extractionActivated[i]?1:0, 0.03F)));
			if(!stackNull&&animationos[i].prevValue.value>animationos[i].value.value&&animationos[i].value.value==0){
				EntityItem stack=UtilM.dropBlockAsItem(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, getStackInSlot(i));
				if(stack!=null){
					stack.motionX=0;
					stack.motionY=0;
					stack.motionZ=0;
				}
				setInventorySlotContents(i, null);
			}
		}
		checkBrainConnection();
		if(optimizer.isTimeWithAddProgress())updateConnections();
	}

	@Override
	public void updateColisionBoxes(){
		int side=getOrientation();
		
		switch(side){
		case 2:side=4;break;
		case 4:side=3;break;
		case 3:side=2;break;
		}
		collisionBoxes=new ArrayList<>();
		collisionBoxes.addAll(collisionBoxesBase);
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
		switch(side){
		case 4:collisionBoxes.addAll(slotBoxes[0]);break;
		case 2:collisionBoxes.addAll(slotBoxes[1]);break;
		case 3:collisionBoxes.addAll(slotBoxes[2]);break;
		case 5:collisionBoxes.addAll(slotBoxes[3]);break;
		case 0:collisionBoxes.addAll(slotBoxes[4]);break;
		case 1:collisionBoxes.addAll(slotBoxes[5]);break;
		}
	}

	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		
		int side=SideUtil.getOppositeSide(getOrientation());
		for(int i=0;i<6;i++)setAccessibleOnSide(i, i==side);
		updateColisionBoxes();
	}
	
	public <T extends TileEntity&NetworkInterface&Messageable>boolean willSendTo(T target){
		if(target==null)return false;
		TileEntityNetworkController brain=target.getBrain();
		//is in a network
		if(brain==null)return false;
		//is in the correct network
		if(brain.getNetworkId()!=this.getNetworkId())return false;
		NetworkInterface caller=getBoundedInterface();
		if(caller!=null){
			List<ItemStack> pointers=caller.getPointers();
			for(ItemStack i:pointers){
				if(NetworkPointer.getTarget(i).equals(target.getPos()))return true;
			}
		}
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound NBT){
		super.writeToNBT(NBT);
		NBTUtil.saveItemsToNBT(NBT, "inventory", slots);
	}
}
