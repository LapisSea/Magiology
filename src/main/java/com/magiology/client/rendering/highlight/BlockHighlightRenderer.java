package com.magiology.client.rendering.highlight;

import com.magiology.util.objs.LockabaleArrayList;
import com.magiology.util.objs.block_bounds.IBlockBounds;
import com.magiology.util.objs.vec.IVec3M;
import com.magiology.util.objs.vec.Vec3M;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class BlockHighlightRenderer<OwnerType extends IBlockBounds>{
	
	public interface HighlightListener<T extends IBlockBounds>{
		
		void drawBoundsOutline(T parent, IBlockState state, World world, BlockPos pos, RayTraceResult hit);
	}
	
	private final OwnerType owner;
	private final LockabaleArrayList<HighlightListener<OwnerType>> listeners=new LockabaleArrayList<>();
	
	public BlockHighlightRenderer(OwnerType owner){
		this.owner=owner;
	}
	
	public void addListener(HighlightListener<OwnerType> listener){
		if(listeners.isLocked()) throw new IllegalStateException("To late to add listener!");
		listeners.add(listener);
	}
	
	public void removeListener(HighlightListener<OwnerType> listener){
		if(listeners.isLocked()) throw new IllegalStateException("To late to remove listener!");
		listeners.remove(listener);
	}
	
	public OwnerType getOwner(){
		return owner;
	}
	
	public final void drawBounds(IBlockState state, World world, BlockPos pos, RayTraceResult hit){
		listeners.lock();
		drawBoundsOutline(state, world, pos, hit);
		listeners.forEach(l->l.drawBoundsOutline(getOwner(), state, world, pos, hit));
	}
	
	public abstract void drawBoundsOutline(IBlockState state, World world, BlockPos pos, RayTraceResult hit);
	
	public abstract void markDirty();
	
	@SideOnly(Side.CLIENT)
	protected static List<IVec3M> generateBoxLines(AxisAlignedBB box){
		List<IVec3M> positions=new ArrayList<>();
		
		positions.add(new Vec3M(box.minX, box.minY, box.minZ));
		positions.add(new Vec3M(box.maxX, box.minY, box.minZ));
		
		positions.add(new Vec3M(box.minX, box.minY, box.maxZ));
		positions.add(new Vec3M(box.maxX, box.minY, box.maxZ));
		
		positions.add(new Vec3M(box.minX, box.maxY, box.minZ));
		positions.add(new Vec3M(box.maxX, box.maxY, box.minZ));
		
		positions.add(new Vec3M(box.minX, box.maxY, box.maxZ));
		positions.add(new Vec3M(box.maxX, box.maxY, box.maxZ));
		
		positions.add(new Vec3M(box.minX, box.minY, box.minZ));
		positions.add(new Vec3M(box.minX, box.maxY, box.minZ));
		
		positions.add(new Vec3M(box.maxX, box.minY, box.minZ));
		positions.add(new Vec3M(box.maxX, box.maxY, box.minZ));
		
		positions.add(new Vec3M(box.minX, box.minY, box.maxZ));
		positions.add(new Vec3M(box.minX, box.maxY, box.maxZ));
		
		positions.add(new Vec3M(box.maxX, box.minY, box.maxZ));
		positions.add(new Vec3M(box.maxX, box.maxY, box.maxZ));
		
		positions.add(new Vec3M(box.minX, box.minY, box.minZ));
		positions.add(new Vec3M(box.minX, box.minY, box.maxZ));
		
		positions.add(new Vec3M(box.maxX, box.minY, box.minZ));
		positions.add(new Vec3M(box.maxX, box.minY, box.maxZ));
		
		positions.add(new Vec3M(box.minX, box.maxY, box.minZ));
		positions.add(new Vec3M(box.minX, box.maxY, box.maxZ));
		
		positions.add(new Vec3M(box.maxX, box.maxY, box.minZ));
		positions.add(new Vec3M(box.maxX, box.maxY, box.maxZ));
		
		return positions;
	}
}
