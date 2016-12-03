package com.magiology.handlers;

import java.util.ArrayList;
import java.util.List;

import com.magiology.forge.events.TickEvents;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.vec.Vec3MRead;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class TileEntityOneBlockStructure<T extends TileEntityOneBlockStructure<T>> extends TileEntityM implements IBlockBreakListener{
	
	private T					brain;
	private Vec3MRead			size,brainOffset;
	private BlockPosM			start,end;
	private AxisAlignedBB		boundingBox;
	private boolean				isBrain,hasBrain,breakingMb;
	private List<T>				multiblock;
	private BlockPosM			loadPositions[],brainPos;
	
	protected static final String BASE_NBT_NAME="multiblock",BRAIN_MARKER="isBrain",MULTIBLOCK_DATA="blocks",BRAIN_POINTER="brainPos";
	

	public List<T> getMultiblock(){
		return multiblock;
	}
	protected void setMultiblock(List<T> multiblock){
		this.multiblock=multiblock;
		if(multiblock==null)clearBrain();
		else initBrainObjects(multiblock);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		
		NBTTagCompound multiblockNbt=(NBTTagCompound)compound.getTag(BASE_NBT_NAME);
		
		if(multiblockNbt.hasKey(MULTIBLOCK_DATA)){
			NBTTagCompound blocks=multiblockNbt.getCompoundTag(MULTIBLOCK_DATA);
			BlockPosM[] positions=new BlockPosM[blocks.getSize()];
			for(int i=0;i<positions.length;i++){
				positions[i]=BlockPosM.fromLong(blocks.getLong(String.valueOf(i)));
			}
			loadPositions=positions;
			Runnable run=()->{
				
				List<T> parts=new ArrayList<>();
				for(BlockPos pos:loadPositions){
					TileEntity tile=worldObj.getTileEntity(pos);
					if(checkType(tile)&&validateLoaded(tile)){
						parts.add((T)tile);
					}
				}
				setBrain((T)this,parts);
				finalizeStructure(parts);
				loadPositions=null;
			};
			
			if(worldObj==null)TickEvents.nextTick(UtilM.isRemote(),run);
			else run.run();
		}
		else if(multiblockNbt.hasKey(BRAIN_POINTER)){
			brainPos=BlockPosM.fromLong(multiblockNbt.getLong(BRAIN_POINTER));
			Runnable run=()->{
				TileEntity brainPos=this.brainPos.getTile(worldObj);
				if(checkType(brainPos))setBrain((T)brainPos,null);
				this.brainPos=null;
			};
			if(worldObj==null)TickEvents.nextTick(UtilM.isRemote(),run);
			else run.run();
		}else setBrain(null,null);
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		
		NBTTagCompound multiblockNbt=new NBTTagCompound();
		BlockPos[] blockPositions;
		if(isBrain()&&(blockPositions=getMultiblock()!=null?CollectionConverter.convAr(getMultiblock(),BlockPos.class,t->t.getPos()):loadPositions)!=null){
			if(blockPositions!=null){
				NBTTagCompound blocks=new NBTTagCompound();
				
				Long[] positions=CollectionConverter.convAr(blockPositions,Long.class,pos->pos.toLong());
				for(int i=0;i<positions.length;i++){
					blocks.setLong(String.valueOf(i),positions[i]);
				}
				multiblockNbt.setTag(MULTIBLOCK_DATA,blocks);
			}
		}
		else if(hasBrain()){
			multiblockNbt.setLong(BRAIN_POINTER,getBrain().getPos().toLong());
		}else if(brainPos!=null){
			multiblockNbt.setLong(BRAIN_POINTER,brainPos.toLong());
		}
		
		compound.setTag(BASE_NBT_NAME,multiblockNbt);
		return super.writeToNBT(compound);
	}
	@Override
	public void onBroken(World world,BlockPos pos,IBlockState state){
		if(!isRemote()&&hasBrain()){
			TickEvents.nextTick(false, ()->getBrain().setBrain(null,null));
		}
	}
	
	@Override
	public void onLoad(){
	}
	
	protected boolean checkType(TileEntity tile){
		return tile!=null&&getClass().equals(tile.getClass());
	}
	
	public void formMultiblock(Vec3i clickedPos){
		
		List<T> parts=explore(new ArrayList<>());
		if(parts.isEmpty()){
			setBrain(null,null);
			return;
		}
		filterBlocks(parts,clickedPos);
		finalizeStructure(parts);
	}
	public void breakMultiblock(){
		if(breakingMb||!hasBrain())return;
		breakingMb=true;
		if(isBrain()){
			if(getMultiblock()!=null)getMultiblock().forEach(part->{
				part.onMultiblockLeave(getMultiblock());
				part.setBrain(null,null);
			});
		}else{
			getBrain().breakMultiblock();
			return;
		}
		setBrain(null,null);
		breakingMb=false;
	}
	
	protected void finalizeStructure(List<T> parts){
		if(!parts.contains(this)){
			setBrain(null,null);
			if(!parts.isEmpty())parts.get(0).finalizeStructure(parts);
			return;
		}
		if(!isRemote()&&!isBrain())breakMultiblock();
		
		
		parts.stream().filter(t->t!=this).forEach(part->{
			part.breakMultiblock();
			part.setBrain((T)this,null);
			part.onMultiblockJoin(parts,(T)this);
		});
		setBrain((T)this,parts);
		onMultiblockJoin(parts,(T)this);
	}
	public abstract void filterBlocks(List<T> parts, Vec3i clickedPos);
	public abstract boolean validateLoaded(TileEntity tile);
	
	protected List<T> explore(List<T> parts){
		parts.add((T)this);
		UtilM.getTileSides(getWorld(), getPos()).stream().forEach(tile->{
			if(checkType(tile)){
				T t=(T)tile;
				if(t.getBrain()==null&&!parts.contains(t))t.explore(parts);
			}
		});
		return parts;
	}
	
	protected void onMultiblockJoin(List<T> multiblock,T brain){}
	protected void onMultiblockLeave(List<T> multiblock){}
	
	public boolean isBrain(){
		return isBrain;
	}
	public T getBrain(){
		return brain;
	}
	public boolean hasBrain(){
		return hasBrain;
	}
	public void setBrain(T brain, List<T> multiblock){
		if(this.brain==brain)return;
		
		if(brain==null){
			breakMultiblock();
			setMultiblock(null);
		}
		this.brain=brain;
		boolean isNowBrain=this==brain;
		
		if(!isNowBrain)clearBrain();
		
		isBrain=isNowBrain;
		hasBrain=brain!=null;
		setMultiblock(multiblock);
		if(worldObj!=null&&!isRemote())UpdateTileNBTPacket.markForSync(this);
	}
	
	protected abstract void clearBrain();
	protected abstract void initBrainObjects(List<T> multiblock);
	
	public Vec3MRead getBrainOffset(){
		return brainOffset;
	}
}
