package com.magiology.mcobjects.tileentityes.network;

import java.util.List;

import com.magiology.api.connection.IConnection;
import com.magiology.api.network.ISidedNetworkComponent;
import com.magiology.api.network.Redstone;
import com.magiology.api.network.skeleton.TileEntityNetworkInteract;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.util.utilclasses.NetworkUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityNetworkInterface extends TileEntityNetworkInteract implements ITickable{
	
	public List<CollisionBox> collisionBoxes;
	
	SlowdownUtil optimizer=new SlowdownUtil(40);
	
	public TileEntityNetworkInterface(){
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
	public <T extends TileEntity>boolean getExtraClassCheck(Class<T> clazz, T tile,Object[] array,int side){
		return NetworkUtil.canConnect(this, (ISidedNetworkComponent)tile);
	}
	@Override
	public CollisionBox getMainBox(){
		return collisionBoxes.get(6);
	}
	
	@Override
	public void getValidTileEntitys(List<Class> included,List<Class> excluded){
		included.add(ISidedNetworkComponent.class);
	}
	@Override
	public void initNetworkComponent(){
		
	}

	@Override
	public boolean isStrate(EnumFacing facing){
		return false;
	}

	@Override
	public void messageReceved(String action){
		if(U.isRemote(this))return;
		if(getInterfaceProvider()!=null)return;
		int side=SideUtil.getOppositeSide(getOrientation());
		BlockPos pos1=SideUtil.offsetNew(side, pos);
		try{
			String[] actionWords=action.split(" ");
			int acitonSize=actionWords.length;
			if(acitonSize>1){
				if(actionWords[0].equals("block")){
					if(actionWords[1].equals("place")){
						if(acitonSize>2&&U.isInteger(actionWords[2])){
							Block block=Block.getBlockById(Integer.parseInt(actionWords[2]));
							int meta=0;
							if(acitonSize>4&&U.isInteger(actionWords[3]))meta=Integer.parseInt(actionWords[3]);
							UtilM.setBlock(worldObj, pos1, block, meta);
						} 
					}
					else if(actionWords[1].equals("destroy")){
						U.getBlockMetadata(worldObj, pos1);
						Block block=U.getBlock(worldObj, pos1);
						if(U.isRemote(this)){
//							Get.Render.ER().addBlockDestroyEffects(pos, block, 0);
						}else{
							block.dropBlockAsItem(worldObj, pos1, worldObj.getBlockState(pos1), 1);
							worldObj.setBlockToAir(pos1);
						}
					}
					else if(actionWords[1].equals("get")){
						
					}
					else if(actionWords[1].equals("is")){
						
					}
				}
				else if(actionWords[0].equals("redstone")){
					if(actionWords[1].equals("set")){
						if(acitonSize>2&&U.isBoolean(actionWords[2])){
							
							int strenght=15;
							boolean isStrong=Boolean.parseBoolean(actionWords[2]);
							Redstone redstoneData=new Redstone();
							
							if(acitonSize>3&&U.isInteger(actionWords[3])){
								strenght=Integer.parseInt(actionWords[3]);
							}
							redstoneData.on=true;
							redstoneData.strenght=strenght;
							redstoneData.isStrong=isStrong;
							
							setInteractData("redstone", redstoneData);
							IBlockState state=worldObj.getBlockState(pos1);
							if(!U.getBlock(worldObj, pos1).isFullCube())worldObj.notifyBlockOfStateChange(pos1, state.getBlock());
							else if(!U.isNull(pos1,worldObj))for(int i=0;i<6;i++)worldObj.notifyBlockOfStateChange(pos1.offset(EnumFacing.getFront(i)), U.getBlock(worldObj, pos1.offset(EnumFacing.getFront(i))));
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
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
		if(!hasWorldObj())return;
		int side=getOrientation();
		
		
		float p2=p*2,p14=p*14,p6=p*6.5F,p10=p*9.5F;
		collisionBoxes=CollisionBox.genColisionBoxList(new DoubleObject[]{
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(side==5?p2:0, p6, p6, p6,  p10, p10),EnumFacing.getFront(5)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p6, side==1?p2:0, p6, p10, p6,  p10),EnumFacing.getFront(1)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p6, p6, side==3?p2:0, p10, p10, p6 ),EnumFacing.getFront(2)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p10,p6, p6, side==4?p14:1, p10, p10),EnumFacing.getFront(3)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p6, p10,p6, p10, side==0?p14:1, p10),EnumFacing.getFront(0)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p6, p6, p10,p10, p10, side==2?p14:1),EnumFacing.getFront(4)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p6, p6, p6, p10, p10, p10),null),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p14,p2, p2, 1,   p14, p14),EnumFacing.getFront(5)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p2, p14,p2, p14, 1,   p14),EnumFacing.getFront(1)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p2, p2, 0,  p14, p14, p2 ),EnumFacing.getFront(2)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p2, p2, p14,p14, p14, 1  ),EnumFacing.getFront(3)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p2, 0,  p2, p14, p2,  p14),EnumFacing.getFront(0)),
				new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0,  p2, p2, p2 , p14, p14),EnumFacing.getFront(4))
		});
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
		collisionBoxes.get(7 ).isGhost=side==4;
		collisionBoxes.get(8 ).isGhost=side==0;
		collisionBoxes.get(9 ).isGhost=side==3;
		collisionBoxes.get(10).isGhost=side==2;
		collisionBoxes.get(11).isGhost=side==1;
		collisionBoxes.get(12).isGhost=side==5;
	}

	@Override
	public void updateConnections(){
		EnumFacing[] sides=new EnumFacing[6];
		UpdateablePipeHandler.setConnections(sides, this);
		for(int i=0;i<sides.length;i++)connections[i].setMain(sides[i]!=null);
		for(int i=0;i<connections.length;i++){
			int side=SideUtil.getOppositeSide(getOrientation());
			if(!connections[i].getMain()&&i==side)connections[i].setMain(true);
			setAccessibleOnSide(i, i!=side);
		}
		updateColisionBoxes();
	}
}
