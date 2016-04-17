package com.magiology.mcobjects.tileentityes;

import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFireGun extends TileEntityM implements ITickable{
	int optimizer=0;
	
	public double animation,prevAnimation,speed;
	public int state=0;
	public int StartStop=0;
	private float p= 1F/16F;
	
	
	public EnumFacing[] rotation = new EnumFacing[4];
	public boolean TimeForAnimation = false;
	
	public TileEntityFireGun(){}
	
	@Override
	public void update(){
		
		detectIfTimeForAnimation();
		animationF();
		optimizer++;
		if(optimizer==10){optimizer=0;
		
			this.updateConnections();
			
			if(rotation[0]==null&&rotation[1]==null&&rotation[2]==null&&rotation[3]==null)
			{
//				if(worldObj.isRemote)Get.Render.ER().addBlockDestroyEffects(pos, H.getBlock(worldObj, pos), 0);
				worldObj.setBlockToAir(pos);
			}
		}
	}
	public void updateConnections(){
		if(this.detectCore(pos.add(0,-1,-3)))rotation[0] = EnumFacing.NORTH;
		else rotation[0] = null;
		if(this.detectCore(pos.add(3,-1,0)))rotation[1] = EnumFacing.EAST;
		else rotation[1] = null;
		if(this.detectCore(pos.add(0,-1,3)))rotation[2] = EnumFacing.SOUTH;
		else rotation[2] = null;
		if(this.detectCore(pos.add(-3,-1,0)))rotation[3] = EnumFacing.WEST;
		else rotation[3] = null;
	}
	
	public void detectIfTimeForAnimation(){
		if(rotation[0]!=null)	 {pos.add(0,-1,-3);}
		else if(rotation[1]!=null){pos.add(3,-1,0);}
		else if(rotation[2]!=null){pos.add(0,-1,3);}
		else if(rotation[3]!=null){pos.add(-3,-1,0);}
		
		if(worldObj.getTileEntity(pos)instanceof TileEntityOreStructureCore){
			TileEntityOreStructureCore tile=(TileEntityOreStructureCore) worldObj.getTileEntity(pos);
			if(tile.processing==16)TimeForAnimation=true;
			else TimeForAnimation=false;
		}
	}
	public boolean detectCore(BlockPos pos){
		boolean isit;
		if(worldObj.getTileEntity(pos)instanceof TileEntityOreStructureCore){
			TileEntityOreStructureCore tile=(TileEntityOreStructureCore) worldObj.getTileEntity(pos);
			if(tile.updateStructureHelper==true){
				isit=true;
			}else isit=false;
		}else isit=false;
		return isit;
	}
	
	public void animationF(){
		prevAnimation=animation;
		if(TimeForAnimation){
			if(animation<0.01){
				if(state==0)speed=0.03;
				else speed=-speed/1.6;
				state=1;
			}
			if(animation<=p*2+0.001)speed-=0.004;
			else speed=0;
		}else{
			animation=0;
			speed=0;
			state=0;
		}
		animation+=speed;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox(){
		return new AxisAlignedBB(pos.add(-0.5,-0.5,-0.5), pos.add(1.5,1.5,1.5));
	}
	
}
