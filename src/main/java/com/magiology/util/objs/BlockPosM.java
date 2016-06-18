package com.magiology.util.objs;

import com.magiology.util.statics.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPosM extends BlockPos{

	public static final BlockPosM ORIGIN = new BlockPosM();
	public static BlockPosM get(Vec3i pos){
		if(pos instanceof BlockPosM)return (BlockPosM)pos;
		return new BlockPosM(pos);
	}
	public BlockPosM(){
		super(0,0,0);
	}
	public BlockPosM(double x, double y, double z){
		super(x, y, z);
	}
	public BlockPosM(Entity source){
		this(source.posX, source.posY, source.posZ);
	}
	public BlockPosM(int x, int y, int z){
		super(x, y, z);
	} 
	public BlockPosM(Vec3d source){
		this(source.xCoord, source.yCoord, source.zCoord);
	}
	public BlockPosM(Vec3i source){
		this(source!=null?source.getX():0, source!=null?source.getY():0, source!=null?source.getZ():0);
	}
	
	public BlockPosM(Vec3M source){
		this(source.x, source.y, source.z);
	}
	public BlockPos conv(){
		return new BlockPos(getX(), getY(), getZ());
	}
	public IBlockState getBlockState(IBlockAccess world){
		return world.getBlockState(this);
	}
	public Block getBlock(IBlockAccess world){
		return UtilM.getBlock(world, this);
	}
	public int getRedstonePower(IBlockAccess world, EnumFacing side){
		if(world instanceof World)((World)world).getRedstonePower(this, side);
		IBlockState iblockstate = world.getBlockState(this);
		Block block = iblockstate.getBlock();
		return block.shouldCheckWeakPower(iblockstate, world, this, side)?getStrongPower(world):block.getWeakPower(iblockstate, world, this, side);
	}
	public IBlockState getState(IBlockAccess world){
		return world.getBlockState(this);
	}
	private int getStrongPower(IBlockAccess world){
		byte b0=0;
		int i=Math.max(b0, world.getStrongPower(this.down(), EnumFacing.DOWN));
		if(i>=15)return i;
		else{
			i=Math.max(i, world.getStrongPower(this.up(), EnumFacing.UP));
			if(i>=15)return i;
			else{
				i=Math.max(i, world.getStrongPower(this.north(), EnumFacing.NORTH));
				if(i>=15)return i;
				else{
					i=Math.max(i, world.getStrongPower(this.south(), EnumFacing.SOUTH));
					if(i>=15)return i;
					else{
						i=Math.max(i, world.getStrongPower(this.west(), EnumFacing.WEST));
						if(i>=15)return i;
						else i=Math.max(i, world.getStrongPower(this.east(), EnumFacing.EAST));
						return i>=15?i:i;
					}
				}
			}
		}
	}
	
	public TileEntity getTile(IBlockAccess world){
		return world.getTileEntity(this);
	}
	public<T extends TileEntity> T getTile(IBlockAccess world,Class<T> type){
		try{
			return (T)getTile(world);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public BlockPosM offset(EnumFacing facing){
		return offset(facing,1);
	}
	@Override
	public BlockPosM offset(EnumFacing facing, int n){
		return new BlockPosM(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
	}
	public Vec3M vecAdd(Vec3M vec){
		vec.x+=getX();
		vec.y+=getY();
		vec.z+=getZ();
		return vec;
	}
	public Vec3M vecSub(Vec3M vec){
		vec.x-=getX();
		vec.y-=getY();
		vec.z-=getZ();
		return vec;
	}
}
