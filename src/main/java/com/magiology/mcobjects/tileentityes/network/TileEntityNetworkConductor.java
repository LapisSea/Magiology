package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.skeleton.TileEntityNetwork;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityNetworkConductor extends TileEntityNetwork implements ITickable{
	
	public List<CollisionBox> collisionBoxes=CollisionBox.genColisionBoxList(new DoubleObject[]{
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,	   p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F),EnumFacing.getFront(5)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, 0,	   p*6.5F, p*9.5F, p*6.5F, p*9.5F),EnumFacing.getFront(1)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, 0,	   p*9.5F, p*9.5F, p*6.5F),EnumFacing.getFront(2)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*9.5F, p*6.5F, p*6.5F, 1,     p*9.5F, p*9.5F),EnumFacing.getFront(3)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*9.5F, p*6.5F, p*9.5F, 1,	   p*9.5F),EnumFacing.getFront(0)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F, 1	 ),EnumFacing.getFront(4)),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6.5F, p*6.5F, p*6.5F, p*9.5F, p*9.5F, p*9.5F),null)
	});
	
	private SlowdownUtil optimizer=new SlowdownUtil(40);
	
	public TileEntityNetworkConductor(){}
	@Override
	public List<CollisionBox> getBoxes(){
		return collisionBoxes;
	}
	@Override
	public IConnection[] getConnections(){
		return connections;
	}
	@Override
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile, Object[] array, int side){
		return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}
	@Override
	public CollisionBox getMainBox(){
		return collisionBoxes.get(6);
	}
	@Override
	public void getValidTileEntitys(List<Class> included, List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}

	@Override
	public void initNetworkComponent(){}

	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if(((connections[0].getMain())&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("1");
				return true;
			}
		}
		if(facing==EnumFacing.WEST||facing==EnumFacing.EAST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false)&&connections[2].getMain()==false&&connections[3].getMain()==false)){
//				Helper.printInln("2");
				return true;
			}
		}
		if(facing==EnumFacing.SOUTH||facing==EnumFacing.NORTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false)&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("3");
				return true;
			}
		}
		return false;
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
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
	}
	
	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		updateColisionBoxes();
	}
}
