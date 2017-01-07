package com.magiology.mc_objects.tile;

import java.util.ArrayList;
import java.util.List;

import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;

import net.minecraft.world.World;

public abstract class MultiblockHandler<T extends TileEntityM,PartType extends LocateableBlockM>{
	
	public final List<PartType>	loaded	=new ArrayList<>(),pending=new ArrayList<>();
	protected final T			owner;
	public boolean				built;
	
	public MultiblockHandler(T owner){
		this.owner=owner;
	}
	
	public abstract List<PartType> getMultiblockNeededPositions();
	
	protected abstract boolean validatePos(World world, PartType pos);
	
	public abstract void addBlockPos(BlockPosM pos);
	public void addPos(PartType pos){
		if(built)return;
		pending.add(pos);
		updatePending();
	}
	
	public void updatePending(){
		if(pending.isEmpty()) return;
		List<PartType> heap=new ArrayList<>();
		pending.stream().filter(p->validatePosWithLoadCheck(getWorld(), p)).forEach(heap::add);
		
		if(heap.size()==pending.size()){
			pending.clear();
			loaded.addAll(heap);
		}else pending.removeAll(heap);
	}
	
	public boolean multiblockValid(){
		List<PartType> needed=getMultiblockNeededPositions();
		
		//all loaded contain mb and everything is valid
		return needed.stream().allMatch(loc->loaded.stream().anyMatch(pos->pos.equals(loc))&&
				validatePosWithLoadCheck(getWorld(), loc));
	}
	
	protected boolean validatePosWithLoadCheck(World world, PartType pos){
		return pos.getPos().isLoaded(getWorld())&&validatePos(world, pos);
	}
	
	protected World getWorld(){
		return owner.getWorld();
	}
	
	public void onCreate(){
		built=true;
		pending.clear();
	}
	
	public void onBreak(){
		built=false;
	}

	public abstract boolean canBePart(BlockPosM pos);
	
}
