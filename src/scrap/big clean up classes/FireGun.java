package com.magiology.mcobjects.blocks.fire;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FireGun extends BlockContainer {
	private float p= 1F/16F;

	@Override
	public int getRenderType(){
		return -1;
		
	}
	@Override
	public boolean isOpaqueCube() {return false;}
	public FireGun() {
		super(Material.gourd);
		this.setHardness(0.2F).setHarvestLevel("pickaxe", 1);
		this.setBlockBounds(p*6.6F, p*0F, p*6.5F, p*9.5F, p*7F, p*9.5F);
		this.useNeighborBrightness=true;
	}
	
	
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TileEntityFireGun rotation= (TileEntityFireGun) world.getTileEntity(pos);
		float p= 1F/16F;
		if(rotation !=null){
			float minX=p*6.6F  -(rotation.rotation[3]!=null?(p*6.6F):0)  -(rotation.rotation[1]!=null?(float)(p*1.6F+rotation.animation):0);
			float minY=p*0;//------------------------------------------------------------------------------------;
			float minZ=p*6.5F  -(rotation.rotation[0]!=null?(p*6.5F):0)  -(rotation.rotation[2]!=null?(float)(p*1.5F+rotation.animation):0);
			float maxX=p*9.5F  +(rotation.rotation[3]!=null?(float)(p*1.5F+rotation.animation):0)  +(rotation.rotation[1]!=null?(p*6.5F):0);
			float maxY=p*7;//------------------------------------------------------------------------------------;
			float maxZ=p*9.5F  +(rotation.rotation[0]!=null?(float)(p*1.5F+rotation.animation):0)  +(rotation.rotation[2]!=null?(p*6.5F):0);
		this.setBlockBounds(minX,minY,minZ,maxX,maxY,maxZ);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state){
	TileEntityFireGun rotation= (TileEntityFireGun) world.getTileEntity(pos);
	float p= 1F/16F;
	if(rotation !=null){
		float minX=p*6.6F  -(rotation.rotation[3]!=null?(p*6.6F):0)  -(rotation.rotation[1]!=null?(p*1.6F):0);
		float minY=p*0;//------------------------------------------------------------------------------------;
		float minZ=p*6.5F  -(rotation.rotation[0]!=null?(p*6.5F):0)  -(rotation.rotation[2]!=null?(p*1.5F):0);
		float maxX=p*9.5F  +(rotation.rotation[3]!=null?(p*1.5F):0)  +(rotation.rotation[1]!=null?(p*6.5F):0);
		float maxY=p*7;//------------------------------------------------------------------------------------;
		float maxZ=p*9.5F  +(rotation.rotation[0]!=null?(p*1.5F):0)  +(rotation.rotation[2]!=null?(p*6.5F):0);
	this.setBlockBounds(minX,minY,minZ,maxX,maxY,maxZ);
	}
	return new AxisAlignedBB(this.minX, this.minY,this.minZ, this.maxX, this.maxY, this.maxZ).addCoord(pos.getX(), pos.getY(), pos.getZ());
}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFireGun();
	}
}
