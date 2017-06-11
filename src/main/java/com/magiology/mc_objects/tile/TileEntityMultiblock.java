package com.magiology.mc_objects.tile;

import com.google.common.collect.Lists;
import com.magiology.core.registry.init.ParticlesM;
import com.magiology.forge.events.TickEvents;
import com.magiology.forge.networking.PacketBufferM;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.MutableBlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.NBTUtil;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.util.vector.Matrix4f;

public abstract class TileEntityMultiblock<T extends TileEntityMultiblock<T,PartType>, PartType extends LocateableBlockM> extends TileEntityM implements IBlockBreakListener{
	
	protected static MutableBlockPosM STATIC_BPOS=new MutableBlockPosM();
	
	private MultiblockHandler<T,PartType> mbHandler;
	private T                             brain;
	private boolean waitingForBrain;
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		super.readFromNbtWithWorld(compound);
		
		NBTTagCompound multiblockData=getTileData().getCompoundTag("multiblock");
		if(multiblockData.hasKey("mb")){
			NBTTagCompound nbt=multiblockData.getCompoundTag("mb");
			
			mbHandler=createMultiblockHandler();
			NBTUtil.iterateL(nbt, l->mbHandler.addBlockPos(new BlockPosM(l)));
			mbHandler.tryToForm();
		}else if(multiblockData.hasKey("br")){
			STATIC_BPOS.setPos(multiblockData.getLong("br")).getTile(this, TileEntityMultiblock.class, b->setBrain((T)b));
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		super.writeToNBT(compound);
		
		updateMultiblock();
		if(hasBrain()){
			NBTTagCompound multiblockData=new NBTTagCompound();
			
			if(isBrain()){
				NBTTagCompound mb=new NBTTagCompound();
				
				Consumer<PartType> addToNbt=part->mb.setLong(String.valueOf(mb.getSize()), part.getPos().toLong());
				
				mbHandler.loaded.forEach(addToNbt);
				mbHandler.pending.forEach(addToNbt);
				
				if(mb.getSize()>0) multiblockData.setTag("mb", mb);
			}else{
				multiblockData.setLong("br", getBrain().getPos().toLong());
			}
			getTileData().setTag("multiblock", multiblockData);
			
		}
		
		ParticlesM.MESSAGE.spawn(new Vec3M(getPos()).add(-0.5, 0.4, 0.5), new Vec3M(-0.01, 0, 0), 2/16F, 32, ColorM.YELLOW, "Nbt write: "+compound);
		return compound;
	}
	
	@Override
	protected boolean nbtUsingWorld(NBTTagCompound compound){
		return true;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
		return oldState.getBlock()!=newSate.getBlock();
	}
	
	public void buildMultiblock(){
		if(isBrain()||hasBrain()) return;
		mbHandler=createMultiblockHandler();
		List<BlockPosM> positions=Lists.newArrayList(getPos());
		
		explorePossibleMultiblockParts(positions);
		
		for(BlockPosM p : positions){
			ParticlesM.MESSAGE.spawn(new Vec3M(p).add(-0.5, 0.5, 0.5), new Vec3M(-0.01, 0, 0), 2/16F, 30, ColorM.YELLOW, "Added possible part");
		}
		
		positions.forEach(mbHandler::addBlockPos);
		
		mbHandler.tryToForm();
	}
	
	@Override
	public void onBroken(World world, BlockPos pos, IBlockState state){
		if(hasBrain()){
			if(isBrain())getHandler().onBreak();
			else getBrain().getHandler().onBreak();
		}
	}
	
	
	public MultiblockHandler<T,PartType> getHandler(){
		return mbHandler;
	}
	
	public void setBrain(T brain){
		if(this.brain==brain)return;
		if(server())UpdateTileNBTPacket.markForSync(this);
		this.brain=brain;
		if(server()&&!waitingForBrain){
			waitingForBrain=true;
			TickEvents.waitRunUntil(this, ()->!brain.isNbtLoaded(), ()->brain.getHandler().addBlockPos(getPos()));
		}
	}
	
	public T getBrain(){
		if(isBrain()) return (T)this;
		return brain;
	}
	
	public boolean isBrain(){
		return mbHandler!=null;
	}
	
	public boolean hasBrain(){
		return getBrain()!=null;
	}
	
	public void runInMB(Consumer<PartType> action){
		getHandler().loaded.forEach(action);
	}
	
	protected abstract MultiblockHandler createMultiblockHandler();
	
	protected void explorePossibleMultiblockParts(List<BlockPosM> positions){
		List<BlockPosM> updating=new ArrayList<>();
		updating.addAll(positions);
		positions.clear();
		
		while(updating.size()>0){
			List<BlockPosM> working=new ArrayList<>();
			working.addAll(updating);
			positions.addAll(updating);
			updating.clear();
			
			working.forEach(pos->{
				boolean added=false;
				for(EnumFacing side : EnumFacing.values()){
					BlockPosM sidePos=pos.offset(side);
					
					if(mbHandler.canBePart(sidePos)&&!positions.contains(sidePos)&&!updating.contains(pos)){
						added=true;
						updating.add(sidePos);
					}
				}
				
				if(added&&!positions.contains(pos)&&!updating.contains(pos)) updating.add(pos);
			});
			
		}
	}
	
	public void updateMultiblock(){
		
	}
	
}
