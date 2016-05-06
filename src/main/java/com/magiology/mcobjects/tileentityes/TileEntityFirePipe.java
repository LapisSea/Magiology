package com.magiology.mcobjects.tileentityes;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.magiology.api.power.ISidedPower;
import com.magiology.core.init.MItems;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.DefaultMultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.util.utilclasses.PowerUtil;
import com.magiology.util.utilclasses.RandUtil;
import com.magiology.util.utilclasses.SideUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.AxisAlignedBBM;
import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFirePipe extends TileEntityPow implements MultiColisionProvider{
	public List<CollisionBox> collisionBoxes=CollisionBox.genColisionBoxList(new DoubleObject[]{
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6,0	  ,p*6,p*10,p*6,p*10),EnumFacing.DOWN),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6,p*10,p*6,p*10,1	   ,p*10),EnumFacing.UP),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6,p*6,0	  ,p*10,p*10,p*6),EnumFacing.NORTH),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6,p*6,p*10,p*10,p*10,1    ),EnumFacing.SOUTH),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(0	  ,p*6,p*6,p*6,p*10,p*10),EnumFacing.WEST),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*10,p*6,p*6,1	  ,p*10,p*10),EnumFacing.EAST),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6, p*6, p*6, p*10, p*10, p*10),null),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*4.5F,-p*4.7F,p*4.5F,p*11.5F,p*0.1F,p*11.5F),null),
			new DoubleObject<AxisAlignedBBM, EnumFacing>(new AxisAlignedBBM(p*6,0 ,p*6,p*10,p*6F,p*10),null)
	});
	public boolean
		DCFFL,
		isSolidDown,
		isSolidUp,
		texAnimP2=true,changesDetected;
	private List<Integer> ghostHits=new ArrayList<>(),prevGhostHits=new ArrayList<>();
	
	SlowdownUtil optimizer=new SlowdownUtil(40);
	PowerUtil PH=new PowerUtil();
	
	public int texAnim=0,pointId,prevPointId;
	public TileEntityFirePipe(){
		super(null, null, 1, 5, 50, 3000);
	}
	
	@Override
	public boolean changesDetected(){
		return changesDetected;
	}
	
	@Override
	public void detectChanges(){
		DefaultMultiColisionProvider.detectChanges(this);
		
	}
	
	@Override
	public List<CollisionBox> getBoxes(){
		return collisionBoxes;
	}
	
	@Override
	public List<Integer> getGhostHits(){
		return ghostHits;
	}
	
	@Override
	public boolean getIn(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(in)return true;
		if(direction==0&&DCFFL)return true;
		return false;
	}

	@Override
	public CollisionBox getMainBox(){
		return collisionBoxes.get(6);
	}
	@Override
	public boolean getOut(int direction){
		boolean in=connections[direction].getIn(),out=connections[direction].getOut(),inOut=connections[direction].getMain();
		if(inOut&&!in&&!out)return true;
		if(out)return true;
		return false;
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
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		AxisAlignedBB bb = new AxisAlignedBB(pos.getX(), pos.getY()-(DCFFL?0.5:0), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);
		return bb;
	}
	@Override
	public boolean isPowerKeptOnWrench(){
		return false;
	}
	@Override
	public boolean isStrate(EnumFacing facing){
		if(facing==EnumFacing.UP||facing==EnumFacing.DOWN||facing==null){
			if(((connections[0].getMain()&&!DCFFL)&&connections[1].getMain())&&(connections[2].getMain()==false&&connections[3].getMain()==false&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("1");
				return true;
			}
		}
		if(facing==EnumFacing.WEST||facing==EnumFacing.EAST||facing==null){
			if((connections[4].getMain()&&connections[5].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false&&!DCFFL)&&connections[2].getMain()==false&&connections[3].getMain()==false)){
//				Helper.printInln("2");
				return true;
			}
		}
		if(facing==EnumFacing.SOUTH||facing==EnumFacing.NORTH||facing==null){
			if((connections[2].getMain()&&connections[3].getMain())&&(connections[1].getMain()==false&&(connections[0].getMain()==false&&!DCFFL)&&connections[4].getMain()==false&&connections[5].getMain()==false)){
//				Helper.printInln("3");
				return true;
			}
		}
		return false;
	}
	
	public boolean isTLamp(BlockPos pos){
		return worldObj.getTileEntity(pos)instanceof TileEntityFireLamp;
	}
	
	public boolean isTPipe(int side){
		BlockPos pos1=SideUtil.offsetNew(side,pos);
		int dir=SideUtil.getOppositeSide(side);
		TileEntity tile=worldObj.getTileEntity(pos1);
		if(tile instanceof TileEntityFirePipe){
			if(!((TileEntityFirePipe)tile).connections[dir].isBanned()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onGhostHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){
		if(id>5)return false;
		return sideHit(entity,stack,id,hitPos,false);
	}
	@Override
	public void onLoad(){
		super.onLoad();
		updateColisionBoxes();
	}
	@Override
	public boolean onNormalHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos){
		if(id>5)return false;
		return sideHit(entity,stack,id,hitPos,true);
	}
	public void power(boolean isRepeatable){
		handleStandardPowerTransmission(isRepeatable);
		if(RandUtil.RI(5)==0){
			int side=RandUtil.RI(6);
			TileEntity tile=worldObj.getTileEntity(SideUtil.offsetNew(side, pos));
			
			if(connections[side].getMain()&&connections[side].getIn()&&connections[side].getOut()&&tile instanceof TileEntityFirePipe&&getEnergy()>0&&((TileEntityFirePipe)tile).getEnergy()<((TileEntityFirePipe)tile).getMaxEnergy()-1){
				PowerUtil.tryToDrainFromTo(this, tile, 1,side);
			}
			
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound NBTTC){
		super.readFromNBT(NBTTC);
		UpdateablePipeHandler.updatein3by3(worldObj, pos);
	}

	@Override
	public void sendChanges(){
		if(changesDetected())DefaultMultiColisionProvider.sendChanges(this);
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
	public void setPointedBoxID(int box){
		setPrevPointedBox(getPointedBoxID());
		pointId=box;
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
	
	private boolean sideHit(EntityPlayer entity, ItemStack stack, int id, Vec3M hitPos, boolean boxEnabled){
		if(!UtilM.isItemInStack(MItems.fireHammer,stack))return false;
		EnumFacing side=connections[id].getFaceEF();
		connections[id].setBanned(connections[id].getMain());
		TileEntity tile2=worldObj.getTileEntity(pos.add(side.getDirectionVec()));
		if(tile2 instanceof ISidedPower)((ISidedPower)tile2).setBannedSide(connections[id].getMain(), side.getOpposite().getIndex());
		UpdateablePipeHandler.updatein3by3(worldObj,pos);
		UpdateablePipeHandler.updatePipe(worldObj,pos);
		UpdateablePipeHandler.updatein3by3(worldObj,pos);
		UpdateablePipeHandler.updatePipe(worldObj,pos);
		return true;
	}
	
	public void texAnimation(){
		if(texAnimP2)texAnim++;
		else texAnim--;
		
		if(texAnim>=10)texAnimP2=false;
		if(texAnim<=1)texAnimP2=true;
	}
	@Override
	public void update(){
		power(true);
		
//		//Get if/what side is first
//		hasPriorityUpg=false;
//		for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null&&containerItems[i].hasTagCompound()){
//			UpgradeType type=RegisterItemUpgrades.getItemUpgradeType(RegisterItemUpgrades.getItemUpgradeID(containerItems[i].getItem()));
//			if(type==UpgradeType.Priority){
//				hasPriorityUpg=true;
//				FirstSide=containerItems[i].getTagCompound().getInteger("side");
//				continue;
//			}
//		}
		
		texAnimation();
		
		if(optimizer.isTimeWithAddProgress()){
			updateConnections();
		}
//		if(worldObj.isRemote)for(int i=0;i<containerItems.length;i++)if(containerItems[i]!=null){
//			Helper.spawnEnitiyFX(new EntitySmokeFXM(worldObj, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0, 0.2, 0));
//		}
		PowerUtil.sortSides(this);
	}
	
	@Override
	public void updateColisionBoxes(){
		DefaultMultiColisionProvider.setStandardConnectionToBox(this);
		getBoxes().get(7).isGhost=getBoxes().get(8).isGhost=!DCFFL;
		CollisionBox.fixIds(this);
	}


	@Override
	public void updateConnections(){
		UpdateablePipeHandler.onConnectionUpdate(this);
		if(!hasWorldObj())return;
		if(U.isRemote(worldObj))updatestand();
//		for(int i=0;i<6;i++)connections[i].setIsMainAutomatic(false);
		boolean[] in1={},out1={};
		TileEntity[] tiles=SideUtil.getTilesOnSides(this);
		for(int a=0;a<6;a++){
			if(tiles[a]==null){
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}else if(tiles[a] instanceof ISidedPower){
				in1 =ArrayUtils.add( in1, ((ISidedPower)tiles[a]).getIn(SideUtil.getOppositeSide(a)));
				out1=ArrayUtils.add(out1, ((ISidedPower)tiles[a]).getOut(SideUtil.getOppositeSide(a)));
			}else{
				in1 =ArrayUtils.add( in1, false);
				out1=ArrayUtils.add(out1, false);
			}
		}
		
		connections[0].setMain(isTPipe(0));
		connections[1].setMain(isTPipe(1));
		connections[2].setMain(isTPipe(2));
		connections[3].setMain(isTPipe(3));
		connections[4].setMain(isTPipe(4));
		connections[5].setMain(isTPipe(5));
		
		connections[0].setOut(out1[0]);
		connections[1].setOut(out1[1]);
		connections[2].setOut(out1[2]);
		connections[3].setOut(out1[3]);
		connections[4].setOut(out1[4]);
		connections[5].setOut(out1[5]);
		
		connections[0].setIn(in1[0]);
		connections[1].setIn(in1[1]);
		connections[2].setIn(in1[2]);
		connections[3].setIn(in1[3]);
		connections[4].setIn(in1[4]);
		connections[5].setIn(in1[5]);
		
		
		for(int a=0;a<6;a++){
			connections[a].setWillRender(true);
			connections[a].setForce(true);
			if(connections[a].getIn()||connections[a].getOut())connections[a].setMain(true);
			if(connections[a].isBanned())connections[a].clear();
		}
		DCFFL = isTLamp(pos.add(0,-1,0))&&connections[0]!=null&&!connections[EnumFacing.DOWN.getIndex()].isBanned();
		
		
		if(DCFFL){
			connections[0].setMain(false);
			connections[0].setForce(false);
			connections[0].setWillRender(false);
		}
		updateColisionBoxes();
		for(int a=0;a<6;a++){
			boolean in=connections[a].getIn(),out=connections[a].getOut(),inOut=connections[a].getMain();
			setReceaveOnSide(a, ((inOut||in)&&out==false));
			setSendOnSide   (a, (inOut||out)&&in==false);
		}
	}


	public void updatestand(){
		if(worldObj.isSideSolid(pos.add(0, -1, 0), EnumFacing.DOWN)==true&&connections[1]==null)isSolidDown=true;
		else isSolidDown=false;
		
		if(worldObj.isSideSolid(pos.add(0, 1, 0), EnumFacing.UP)==true&&connections[0]==null)isSolidUp=true;
		else isSolidUp=false;
	}


	@Override
	public void writeToNBT(NBTTagCompound NBTTC){
		super.writeToNBT(NBTTC);
	}
}
