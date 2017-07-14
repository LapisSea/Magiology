package com.magiology.util.m_extensions;

import com.magiology.util.interf.Locateable;
import com.magiology.util.interf.Locateable.LocateableVec3D;
import com.magiology.util.interf.Locateable.LocateableVec3M;
import com.magiology.util.interf.Worldabale;
import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.statics.UtilM;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;

public class MutableBlockPosM extends MutableBlockPos{
	
	public MutableBlockPosM(){
		this(0, 0, 0);
	}
	
	public MutableBlockPosM(BlockPos pos){
		this(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public MutableBlockPosM(int x, int y, int z){
		super(0, 0, 0);
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public MutableBlockPosM add(double x, double y, double z){
		return add(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}
	
	public MutableBlockPosM add(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
		return this;
	}
	
	/**
	 * Offsets this BlockPos n blocks in the given direction
	 */
	public MutableBlockPosM offset(EnumFacing facing, int n){
		x+=facing.getFrontOffsetX()*n;
		y+=facing.getFrontOffsetY()*n;
		z+=facing.getFrontOffsetZ()*n;
		
		return this;
	}
	
	public MutableBlockPosM rotate(Rotation rotationIn){
		switch(rotationIn){
		case NONE:
		default:
			return this;
		
		case CLOCKWISE_90:
			return setPos(-this.getZ(), this.getY(), this.getX());
		case CLOCKWISE_180:
			return setPos(-this.getX(), this.getY(), -this.getZ());
		case COUNTERCLOCKWISE_90:
			return setPos(this.getZ(), this.getY(), -this.getX());
		}
	}
	
	public MutableBlockPosM setPos(int x, int y, int z){
		this.x=x;
		this.y=y;
		this.z=z;
		return this;
	}
	
	public MutableBlockPosM setPos(double x, double y, double z){
		return this.setPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}
	
	@SideOnly(Side.CLIENT)
	public MutableBlockPosM setPos(Entity entityIn){
		return this.setPos(entityIn.posX, entityIn.posY, entityIn.posZ);
	}
	
	public MutableBlockPosM setPos(Vec3i vec){
		return this.setPos(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public MutableBlockPosM setPos(Locateable<? extends Vec3i> vec){
		return this.setPos(vec.getPos());
	}
	
	public MutableBlockPosM setPos(LocateableVec3D vec){
		Vec3d v=vec.getPos();
		return this.setPos(v.x, v.y, v.z);
	}
	
	public MutableBlockPosM setPos(LocateableVec3M vec){
		IVec3M v=vec.getPos();
		return this.setPos(v.x(), v.y(), v.z());
	}
	
	public MutableBlockPosM move(EnumFacing facing){
		return this.move(facing, 1);
	}
	
	public MutableBlockPosM move(EnumFacing facing, int length){
		return this.setPos(this.x+facing.getFrontOffsetX()*length, this.y+facing.getFrontOffsetY()*length, this.z+facing.getFrontOffsetZ()*length);
	}
	
	public void setY(int y){
		this.y=y;
	}
	
	public BlockPosM toImmutable(){
		return new BlockPosM(this);
	}
	
	public MutableBlockPosM setPos(long serialized){
		return setPos((int)(serialized<<64-BlockPosM.X_SHIFT-BlockPosM.NUM_X_BITS>>64-BlockPosM.NUM_X_BITS), (int)(serialized<<64-BlockPosM.Y_SHIFT-BlockPosM.NUM_Y_BITS>>64-BlockPosM.NUM_Y_BITS), (int)(serialized<<64-BlockPosM.NUM_Z_BITS>>64-BlockPosM.NUM_Z_BITS));
	}
	
	public TileEntity getTile(Worldabale worldContainer){
		return getTile(worldContainer.getWorld());
	}
	
	public TileEntity getTile(IBlockAccess world){
		return world.getTileEntity(this);
	}
	
	public <T> T getTile(Worldabale worldContainer, Class<T> type, Consumer<T> onFound){
		return getTile(worldContainer.getWorld(), type, onFound);
	}
	
	public <T> T getTile(Worldabale worldContainer, Class<T> type){
		return getTile(worldContainer.getWorld(), type);
	}
	
	public <T> T getTile(IBlockAccess world, Class<T> type, Consumer<T> onFound){
		T tile=getTile(world, type);
		if(tile!=null) onFound.accept(tile);
		return tile;
	}
	
	public <T> T getTile(IBlockAccess world, Class<T> type){
		TileEntity tile=getTile(world);
		if(UtilM.instanceOf(tile, type)) return (T)tile;
		return null;
	}
}
