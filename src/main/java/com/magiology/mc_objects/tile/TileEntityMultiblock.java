package com.magiology.mc_objects.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.MutableBlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.NBTUtil;

import jdk.nashorn.internal.ir.BreakableNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class TileEntityMultiblock<T extends TileEntityMultiblock<T,PartType>,PartType extends LocateableBlockM> extends TileEntityM{
	
	private MultiblockHandler<T,PartType> mbHandler;
	private T brain;
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		NBTTagCompound multiblockData=compound.getCompoundTag("multiblock");
		if(multiblockData.hasKey("mb")){
			NBTTagCompound nbt=compound.getCompoundTag("mb");
			
			mbHandler=createMultiblockHandler();
			NBTUtil.iterateL(nbt,l->mbHandler.addBlockPos(new BlockPosM(l)));
			
			if(mbHandler.multiblockValid())mbHandler.onCreate();
			
		}else if(multiblockData.hasKey("br")){
			new BlockPosM(multiblockData.getLong("br")).getTile(this, TileEntityMultiblock.class, b->brain=(T)b);
		}
	}
	
	
	public void buildMultiblock(){
		if(isBrain()||hasBrain())return;
		mbHandler=createMultiblockHandler();
		List<BlockPosM> positions=new ArrayList<>();
		explorePossibleMultiblockParts(positions);
		
		positions.forEach(mbHandler::addBlockPos);
		if(mbHandler.multiblockValid())mbHandler.onCreate();
	}
	
	public MultiblockHandler<T,PartType> getHandler(){
		return mbHandler;
	}
	
	public T getBrain(){
		if(isBrain())return (T)this;
		return brain;
	}
	
	public boolean isBrain(){
		return mbHandler!=null;
	}
	
	public boolean hasBrain(){
		return getBrain()!=null;
	}
	
	protected abstract MultiblockHandler createMultiblockHandler();
	
	protected void explorePossibleMultiblockParts(List<BlockPosM> positions){
		List<BlockPosM> updating=new ArrayList<>();
		updating.addAll(updating);
		
		while(updating.size()>0){
			List<BlockPosM> working=new ArrayList<>();
			working.addAll(updating);
			positions.addAll(updating);
			updating.clear();
			
			working.forEach(pos->{
				boolean added=false;
				for(EnumFacing side:EnumFacing.values()){
					BlockPosM sidePos=pos.offset(side);
					
					if(mbHandler.canBePart(sidePos)&&!positions.contains(sidePos)){
						added=true;
						updating.add(sidePos);
					}
				}
				
				if(added&&!positions.contains(pos))updating.add(pos);
			});
			
			
		}
	}
	public void updateMultiblock(){
		
	}
	
}
