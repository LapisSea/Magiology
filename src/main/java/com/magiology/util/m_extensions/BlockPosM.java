package com.magiology.util.m_extensions;

import java.util.function.Consumer;

import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPosM extends BlockPos{
	
	public static final BlockPosM ORIGIN=new BlockPosM();
	
	private static final int	NUM_X_BITS	=1+MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
	private static final int	NUM_Z_BITS	=NUM_X_BITS;
	private static final int	NUM_Y_BITS	=64-NUM_X_BITS-NUM_Z_BITS;
	private static final int	Y_SHIFT		=0+NUM_Z_BITS;
	private static final int	X_SHIFT		=Y_SHIFT+NUM_Y_BITS;
	private static final long	X_MASK		=(1L<<NUM_X_BITS)-1L;
	private static final long	Y_MASK		=(1L<<NUM_Y_BITS)-1L;
	private static final long	Z_MASK		=(1L<<NUM_Z_BITS)-1L;
	
	public static BlockPosM get(Vec3i pos){
		if(pos instanceof BlockPosM)
			return (BlockPosM)pos;
		return new BlockPosM(pos);
	}
	
	public BlockPosM(){
		super(0, 0, 0);
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
	
	public BlockPosM(int[] src){
		this(src[0], src[1], src[2]);
	}
	
	public BlockPosM(Vec3M source){
		this(source.x(), source.y(), source.z());
	}
	
	public BlockPosM(PathPoint point){
		this(point.xCoord, point.yCoord, point.zCoord);
	}
	
	public BlockPosM(long serialized){
		this((int)(serialized<<64-X_SHIFT-NUM_X_BITS>>64-NUM_X_BITS), (int)(serialized<<64-Y_SHIFT-NUM_Y_BITS>>64-NUM_Y_BITS), (int)(serialized<<64-NUM_Z_BITS>>64-NUM_Z_BITS));
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
		if(world instanceof World)
			((World)world).getRedstonePower(this, side);
		IBlockState iblockstate=world.getBlockState(this);
		Block block=iblockstate.getBlock();
		return block.shouldCheckWeakPower(iblockstate, world, this, side)?getStrongPower(world):block.getWeakPower(iblockstate, world, this, side);
	}
	
	public IBlockState getState(IBlockAccess world){
		return world.getBlockState(this);
	}
	
	private int getStrongPower(IBlockAccess world){
		byte b0=0;
		int i=Math.max(b0, world.getStrongPower(this.down(), EnumFacing.DOWN));
		if(i>=15)
			return i;
		else{
			i=Math.max(i, world.getStrongPower(this.up(), EnumFacing.UP));
			if(i>=15)
				return i;
			else{
				i=Math.max(i, world.getStrongPower(this.north(), EnumFacing.NORTH));
				if(i>=15)
					return i;
				else{
					i=Math.max(i, world.getStrongPower(this.south(), EnumFacing.SOUTH));
					if(i>=15)
						return i;
					else{
						i=Math.max(i, world.getStrongPower(this.west(), EnumFacing.WEST));
						if(i>=15)
							return i;
						else
							i=Math.max(i, world.getStrongPower(this.east(), EnumFacing.EAST));
						return i>=15?i:i;
					}
				}
			}
		}
	}
	
	public TileEntity getTile(IBlockAccess world){
		return world.getTileEntity(this);
	}
	
	public <T> T getTile(IBlockAccess world, Class<T> type){
		TileEntity tile=getTile(world);
		if(UtilM.instanceOf(tile, type)) return (T)tile;
		return null;
	}
	
	@Override
	public BlockPosM offset(EnumFacing facing){
		return offset(facing, 1);
	}
	
	@Override
	public BlockPosM offset(EnumFacing facing, int n){
		return new BlockPosM(this.getX()+facing.getFrontOffsetX()*n, this.getY()+facing.getFrontOffsetY()*n, this.getZ()+facing.getFrontOffsetZ()*n);
	}
	
	public Vec3M vecAdd(Vec3M vec){
		return vec.addSelf(this);
	}
	
	public Vec3M vecSub(Vec3M vec){
		return vec.subSelf(this);
	}
	
	@Override
	public BlockPosM add(double x, double y, double z){
		return x==0.0D&&y==0.0D&&z==0.0D?this:new BlockPosM(getX()+x, getY()+y, getZ()+z);
	}
	
	@Override
	public BlockPosM add(int x, int y, int z){
		return x==0&&y==0&&z==0?this:new BlockPosM(getX()+x, getY()+y, getZ()+z);
	}
	
	@Override
	public BlockPosM add(Vec3i vec){
		return vec.getX()==0&&vec.getY()==0&&vec.getZ()==0?this:new BlockPosM(getX()+vec.getX(), getY()+vec.getY(), getZ()+vec.getZ());
	}
	
	public BlockPosM add(Vec3M vec){
		return vec.x()==0&&vec.y()==0&&vec.z()==0?this:new BlockPosM(getX()+vec.x(), getY()+vec.y(), getZ()+vec.z());
	}
	
	@Override
	public BlockPosM up(){
		return this.up(1);
	}
	
	@Override
	public BlockPosM up(int n){
		return this.offset(EnumFacing.UP, n);
	}
	
	@Override
	public BlockPosM down(){
		return this.down(1);
	}
	
	@Override
	public BlockPosM down(int n){
		return this.offset(EnumFacing.DOWN, n);
	}
	
	@Override
	public BlockPosM north(){
		return this.north(1);
	}
	
	@Override
	public BlockPosM north(int n){
		return this.offset(EnumFacing.NORTH, n);
	}
	
	@Override
	public BlockPosM south(){
		return this.south(1);
	}
	
	@Override
	public BlockPosM south(int n){
		return this.offset(EnumFacing.SOUTH, n);
	}
	
	@Override
	public BlockPosM west(){
		return this.west(1);
	}
	
	@Override
	public BlockPosM west(int n){
		return this.offset(EnumFacing.WEST, n);
	}
	
	@Override
	public BlockPosM east(){
		return this.east(1);
	}
	
	@Override
	public BlockPosM east(int n){
		return this.offset(EnumFacing.EAST, n);
	}
	
	@Override
	public BlockPosM subtract(Vec3i vec){
		return vec.getX()==0&&vec.getY()==0&&vec.getZ()==0?this:new BlockPosM(this.getX()-vec.getX(), this.getY()-vec.getY(), this.getZ()-vec.getZ());
	}
	
	public static void iterateBlocks(BlockPos start, BlockPos end, Consumer<BlockPos> callback){
		MutableBlockPos.getAllInBox(start, end).forEach(callback::accept);
	}
	
	public static BlockPosM fromLong(long serialized){
		return new BlockPosM(serialized);
	}

	public static boolean isPosNextTo(BlockPos p1, BlockPos p2){
		int x=p1.getX()-p2.getX();
		int y=p1.getY()-p2.getY();
		int z=p1.getZ()-p2.getZ();

		if(x*x==1)return y==0&&z==0;
		if(y*y==1)return x==0&&z==0;
		if(z*z==1)return x==0&&y==0;
		
		return false;
	}
}
