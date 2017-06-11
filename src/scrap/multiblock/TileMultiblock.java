package com.magiology.mc_objects.tile.multiblock;

import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.interf.IBlockBreakListener;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityMTickable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public abstract class TileMultiblock<T extends TileMultiblock<T,MultiblockDataType>, MultiblockDataType extends MultiblockData<T>> extends TileEntityMTickable implements IBlockBreakListener{
	
	public static enum MultiblockState{
		/**
		 * multiblock is valid and fully loaded
		 */
		COMPLETE(true),
		/**
		 * multiblock may be valid but it is partially loaded on chunk boarders
		 */
		CONSTRUCTING(false),
		/**
		 * multiblock is not complete
		 */
		UNCOMPLETE(false),
		/**
		 * inital value and the actual state should be generated/loaded shortly
		 */
		UNKNOW(false);
		
		public final boolean complete;
		
		private MultiblockState(boolean complete){
			this.complete=complete;
		}
	}
	
	public static final String BRAIN_CATEGORY="br", MULTIBLOCK_CATEGORY="mb", POSITIONS_MARKER="pos", NAME_MARKER="name";
	
	private T               brain=null;
	private MultiblockState state=MultiblockState.UNKNOW;
	
	private final Map<String,LinkCategory> categories=new HashMap();
	private boolean linksDirty;
	private MultiblockDataType multiblock=null;
	
	//"links":{"name1":{144141721,562547247,2457,245725472457,24745},"name2":...}
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		NBTTagCompound links=compound.getCompoundTag("links");
		categories.clear();
		links.getKeySet().forEach(k -> categories.put(k, new LinkCategory(this, k, links.getCompoundTag(k))));
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		cleanLinks();
	}
	
	@Override
	protected boolean nbtUsingWorld(NBTTagCompound compound){
		return true;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		updateLinks();
		NBTTagCompound links=new NBTTagCompound();
		categories.forEach((name, cat) -> cat.writeTo(links));
		compound.setTag("links", links);
		super.writeToNBT(compound);
		
		return compound;
	}
	
	public void handshake(Link link, String targetCategory){
		LinkCategory cat=getCategory(targetCategory);
		cat.handshake(link);
	}
	
	@Override
	public void update(){
		cleanLinks();
	}
	
	@Override
	public void onBroken(World world, BlockPos pos, IBlockState state){
		HashSet<BlockPosM> notify=new HashSet<>();
		categories.values().forEach(cat -> cat.forEach(l -> notify.add(l.getSrcPos())));
		notify.forEach(p -> p.getTile(world, TileMultiblock.class, mb -> mb.posWentBad(p)));
	}
	
	public void posWentBad(BlockPos pos){
		categories.values().forEach(cat -> cat.posWentBad(pos));
	}
	
	public LinkCategory getCategory(String name){
		LinkCategory cat=categories.get(name);
		if(cat==null) categories.put(name, cat=new LinkCategory(this, name));
		return cat;
	}
	
	protected void updateLinks(){
		
		List<Link> toRemove=new ArrayList<>();
		
		categories.values().forEach(cat -> {
			
			boolean change=false, allLoad=true;
			
			for(Link link : cat){
				if(!link.getStatus().loaded) allLoad=false;
				
				boolean lastLoad=link.getPrevStatus().loaded;
				if(link.change()){
					if(cat.removeLinkWhenLoadedAndlost&&lastLoad) toRemove.add(link);
					change=true;
				}
			}
			
			if(toRemove.size()>0){
				cat.removeAll(toRemove);
				toRemove.clear();
			}
			
			int newHash=cat.hashCode();
			if(newHash!=cat.prevHash) change=true;
			cat.prevHash=newHash;
			
			if(allLoad) allLoad=cat.size()>0;
			
			if(change){
				cat.onChange.run();
				cat.prevAllLoad=cat.allLoad;
				cat.allLoad=allLoad;
				if(allLoad) cat.onAllLoad.run();
			}
		});
	}
	
	protected void cleanLinks(){
		if(!linksDirty) return;
		linksDirty=false;
		updateLinks();
	}
	
	public void markLinksDirty(){
		linksDirty=true;
	}
	
	public T getBrain(){
		return brain;
	}
	
	public LinkCategory getMbCategory(){
		return getCategory(MULTIBLOCK_CATEGORY);
	}
	
	public LinkCategory getBrainCategory(){
		return getCategory(BRAIN_CATEGORY);
	}
	
	protected void setBrain(T brain){
		if(this.brain==brain) return;
		this.brain=brain;
		
		LinkCategory brainPointer=getCategory(BRAIN_CATEGORY);
		brainPointer.clear();
		
		if(brain!=null) brainPointer.add(new Link(this.getPos(), brain.getPos()));
		else getMbCategory().clear();
		
		UpdateTileNBTPacket.markForSync(this);
		markLinksDirty();
	}
	
	public boolean hasBrain(){
		return getBrain()!=null;
	}
	
	public boolean isBrain(){
		return getBrain()==this;
	}
	
	public MultiblockDataType getMultiblock(){
		if(!hasBrain()) return multiblock=null;
		if(!isBrain()) return getBrain().getMultiblock();
		
		if(multiblock==null){
			multiblock=createMutliblockObj();
			UpdateTileNBTPacket.markForSync(this);
		}
		
		return multiblock;
	}
}
