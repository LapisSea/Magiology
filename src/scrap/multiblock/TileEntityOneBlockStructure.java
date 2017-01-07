package com.magiology.mc_objects.tile.multiblock;

import java.util.ArrayList;
import java.util.List;

import com.magiology.core.registry.init.ParticlesM;
import com.magiology.forge.events.TickEvents;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public abstract class TileEntityOneBlockStructure<T extends TileEntityOneBlockStructure<T,MultiblockDataType>, MultiblockDataType extends MultiblockData<T>>extends TileMultiblock<T, MultiblockDataType>{
	
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
	
	public static final String BRAIN_CATEGORY="br", MULTIBLOCK_CATEGORY="mb";
	
	private T					brain		=null;
	private MultiblockState		state		=MultiblockState.UNKNOW;
	
	protected abstract boolean isMultiBlockValid();
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		super.readFromNbtWithWorld(compound);
		
		updateBrainFromLink();
		markLinksDirty();
	}
	
	@Override
	public void update(){
		super.update();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
		return oldState.getBlock()!=newSate.getBlock();
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
	
	protected void setToBrain(){
		setBrain((T)this);
	}
	
	protected void unsetBrain(){
		setBrain(null);
	}
	
	protected void updateBrainFromLink(){
		LinkCategory brainLink=getBrainCategory();
		
		if(brainLink.isEmpty()){
			setBrain(null);
			return;
		}
		
		Link l=brainLink.get(0);
		
		if(!l.getStatus().loaded){
			setBrain(null);
			return;
		}
		
		TileEntity t=UtilM.getTile(worldObj, l.getPoint());
		if(t==this){
			setBrain((T)this);
			return;
		}
		
		if(getBrain()==t) return;
		if(t==null) setBrain(null);
		else if(t.getClass()==getClass()){
			T br=(T)t;
			br.handshake(l, BRAIN_CATEGORY);
		}
		
	}
	
	public T getBrain(){
		return brain;
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
	
	
	@Override
	protected void updateLinks(){
		ParticlesM.MESSAGE.spawn(new Vec3M(getPos()).add(0.5).addZ(1).addX(-1), new Vec3M(0,0,0.01), 3/16F, 40, ColorM.WHITE, getMbCategory().toString());
		getMbCategory().onAllLoad=()->{
			Class<T> c=(Class<T>)this.getClass();
			List<T> tiles=CollectionConverter.convLi(getMbCategory(), c, l->l.getPoint().getTile(this, c));
			tiles.removeIf(t->t!=null);
			setMultiblockParts(tiles);
		};
		super.updateLinks();
	}
	
	protected void breakMultiblock(){
		if(!hasBrain()) return;
		if(!isBrain()){
			getBrain().breakMultiblock();
			return;
		}
		TickEvents.nextTick(false, ()->getMultiblock().parts.forEach(T::unsetBrain));
		
	}
	
	public LinkCategory getMbCategory(){
		return getCategory(MULTIBLOCK_CATEGORY);
	}
	
	public LinkCategory getBrainCategory(){
		return getCategory(BRAIN_CATEGORY);
	}
	
	protected MultiblockDataType createMutliblockObj(){
		return (MultiblockDataType)new MultiblockData<T>();
	}
	
	//TODO: MULTIBLOCK CONSTRUCTION (link)
	public void constructMultiblock(Vec3i clickedPos){
		if(client()) return;
		
		setToBrain();
		
		List<T> parts=new ArrayList<>();
		getPossibleConstruction(parts);
		
		//trim invalid positions
		filter(parts, clickedPos);
		
		setMultiblockParts(parts);
		
	}
	
	protected void setMultiblockParts(List<T> parts){
		List<T> multiblock=getMultiblock().parts;
		multiblock.clear();
		multiblock.addAll(parts);
		
		LinkCategory mb=getMbCategory();
		mb.clear();
		multiblock.forEach(p->{
			p.setBrain(this.getBrain());
			mb.add(new Link(this.getPos(), p.getPos()));
		});
		markLinksDirty();
	}
	
	protected void getPossibleConstruction(List<T> parts){
		/*get connected*/{
			parts.add((T)this);
			int size=0;
			
			while(size<parts.size()){
				size=parts.size();
				
				for(int i=0;i<parts.size();i++){
					UtilM.getTileSides(parts.get(i), getClass()).stream().filter(t->!t.hasBrain()&&!parts.contains(t)).forEach(t->parts.add((T)t));
				}
			}
		}
	}
	
	protected abstract void filter(List<T> multiblock, Vec3i clickedPos);
}
