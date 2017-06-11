package com.magiology.util.interf;

import com.magiology.core.registry.init.TileEntityRegistry;
import com.magiology.forge.events.TickEvents;
import com.magiology.util.interf.Locateable.LocateableBlockM;
import com.magiology.util.interf.UnloadSafeLink.LoadLink.LoadLinkStatus;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.ArrayList_ModifyHook;
import com.magiology.util.statics.UtilM;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * <p>This interface should be implemented exclusively on {@link TileEntityM}!</p>
 * <p>It is is used to provide a pointer to another block with safe handling of unloaded blocks (main use for multiblocks)</p>
 *
 * @author LapisSea
 */
public interface UnloadSafeLink extends Worldabale, LocateableBlockM{
	
	static final String DEFAULT_KEY="UnloadSafe";
	
	abstract class LoadLink implements LocateableBlockM{
		
		public enum LoadLinkStatus{
			LOADED(true, "load"),
			UNLOADED(false, "unLoad"),
			WAITING_FOR_RESPONSE(false, "waitFor"),
			UNKNOWN(false, "unknown");
			
			public final  boolean loaded;
			private final String  sh;
			
			private LoadLinkStatus(boolean loaded, String sh){
				this.loaded=loaded;
				this.sh=sh;
			}
		}
		
		private final BlockPosM      pos;
		public final  UnloadSafeLink owner;
		private       LoadLinkStatus status;
		protected     String         id;
		public final  String         category;
		
		public LoadLink(BlockPosM pos, UnloadSafeLink owner, LoadLinkStatus status, String id, String category){
			this.pos=pos;
			this.owner=owner;
			this.status=status;
			this.id=id;
			this.category=category;
		}
		
		public abstract boolean checkType();
		
		public NBTTagCompound sterilise(NBTTagCompound compound){
			compound.setInteger("t", this instanceof LoadLinkTile?1:0);
			compound.setLong("pos", pos.toLong());
			if(!id.isEmpty()) compound.setString("id", id);
			return compound;
		}
		
		@Override
		public String toString(){
			return "["+pos.toString()+", "+getStatus().sh+"]";
		}
		
		@Override
		public int hashCode(){
			return pos.hashCode();
		}
		
		@Override
		public boolean equals(Object obj){
			return obj instanceof LoadLink&&equals((LoadLink)obj);
		}
		
		public boolean equals(LoadLink obj){
			if(obj==this) return true;
			return obj.pos.equals(pos);
		}

		@Override
		public BlockPosM getPos(){
			return pos;
		}
		
		public static LoadLink desterilise(NBTTagCompound compound, UnloadSafeLink owner, String category){
			BlockPosM pos=new BlockPosM(compound.getLong("pos"));
			String id=compound.getString("id");
			LoadLink l=null;
			switch(compound.getInteger("t")){
			case 0:
				l=new LoadLinkBlock(pos, owner, LoadLinkStatus.UNLOADED, id, category);
				break;
			case 1:
				l=new LoadLinkTile(pos, owner, LoadLinkStatus.UNLOADED, id, category);
				break;
			default:
				return null;
			}
			return l;
		}
		
		@Override
		public LoadLink clone(){
			return null;
		}
		
		public LoadLinkStatus getStatus(){
			return status;
		}
		
		public void setStatus(LoadLinkStatus status){
			if(this.status==status) return;
			
			boolean loadChange=status.loaded!=this.status.loaded;
			this.status=status;
			
			if(loadChange) owner.markLinksDirty();
		}
		
		public UnloadSafeLink getPointed(){
			return pos.getTile(owner.getWorld(), UnloadSafeLink.class);
		}
		
	}
	
	class LoadLinkBlock extends LoadLink{
		
		public LoadLinkBlock(BlockPosM pos, UnloadSafeLink owner, LoadLinkStatus status, Block block, String category){
			super(pos, owner, status, block.getUnlocalizedName(), category);
		}
		
		public LoadLinkBlock(BlockPosM pos, UnloadSafeLink owner, LoadLinkStatus status, String id, String category){
			super(pos, owner, status, id, category);
		}
		
		@Override
		public boolean checkType(){
			if(!owner.hasWorld()) return false;
			if(!getPos().isLoaded(owner.getWorld())) return false;
			if(id==null||id.isEmpty()) return true;
			return owner.getWorld().getBlockState(getPos()).getBlock().getUnlocalizedName().equals(id);
		}
		
		@Override
		public LoadLinkBlock clone(){
			return new LoadLinkBlock(getPos(), owner, getStatus(), id, category);
		}
		
		public void setId(Block block){
			id=block.getUnlocalizedName();
		}
	}
	
	class LoadLinkTile extends LoadLink{
		
		private static final Map<Class<? extends TileEntity>,String> classToNameMap;
		
		static{
			try{
				Field f=TileEntity.class.getDeclaredField("classToNameMap");
				f.setAccessible(true);
				classToNameMap=(Map<Class<? extends TileEntity>,String>)f.get(null);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		private static String getIdFromClass(TileEntity t){
			try{
				Field f=TileEntity.class.getDeclaredField("classToNameMap");
				f.setAccessible(true);
				for(Entry<Class<? extends TileEntity>,String> i : classToNameMap.entrySet()){
					if(i.getKey()==t.getClass()) return i.getValue();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		private static Class<? extends TileEntity> getTileFromId(String id, TileEntity fallback){
			Class<? extends TileEntity> t=TileEntityRegistry.getFromId(id);
			if(t==null){
				NBTTagCompound nbt=fallback.writeToNBT(new NBTTagCompound());
				if(nbt.getString("id")==id) t=fallback.getClass();
			}
			return t;
		}
		
		private Class<? extends TileEntity> idFast;
		
		public LoadLinkTile(BlockPosM pos, UnloadSafeLink owner, LoadLinkStatus status, TileEntity tile, String category){
			super(pos, owner, status, getIdFromClass(tile), category);
		}
		
		public LoadLinkTile(BlockPosM pos, UnloadSafeLink owner, LoadLinkStatus status, String id, String category){
			super(pos, owner, status, id, category);
		}
		
		@Override
		public boolean checkType(){
			if(!owner.hasWorld()) return false;
			if(!getPos().isLoaded(owner)) return false;
			TileEntity tile=getPos().getTile(owner);
			if(tile==null) return false;
			if(id==null||id.isEmpty()) return true;
			
			if(idFast==null) idFast=getTileFromId(id, tile);
			
			return idFast==tile.getClass();
		}
		
		@Override
		public LoadLinkTile clone(){
			return new LoadLinkTile(getPos(), owner, getStatus(), id, category);
		}
		
		public void setId(TileEntity tile){
			id=getIdFromClass(tile);
		}
	}
	
	class LinkCategory{
		
		private final UnloadSafeLink owner;
		public final  String         category;
		private final List<LoadLink> links;
		
		public LinkCategory(String category, UnloadSafeLink owner){
			this.category=category==null||category.isEmpty()?"all":category;
			this.owner=owner;
			links=new ArrayList_ModifyHook<>(owner::markLinksDirty);
		}
		
		public LoadLink get(int id){
			return links.get(id);
		}
		
		public List<LoadLink> getAll(){
			return links;
		}
		
		public void newLink(BlockPosM pos, Block blockType){
			if(posMatch(pos)) return;
			links.add(new LoadLinkBlock(pos, owner, LoadLinkStatus.UNKNOWN, blockType, category));
			UtilM.removeCollectionDuplicates(links);
		}
		
		public void newLink(BlockPosM pos, TileEntity tile){
			if(posMatch(pos)) return;
			links.add(new LoadLinkTile(pos, owner, LoadLinkStatus.UNKNOWN, tile, category));
			UtilM.removeCollectionDuplicates(links);
		}
		
		private boolean posMatch(BlockPos pos){
			return links.stream().anyMatch(l -> l.pos.equals(pos));
		}
		
		public void clear(){
			links.clear();
		}
		
		public boolean isEmpty(){
			return links.isEmpty();
		}
		
		public boolean hasLink(){
			return links.size()>0;
		}
		
		@Override
		public int hashCode(){
			return category.hashCode();
		}
		
		@Override
		public boolean equals(Object obj){
			if(!(obj instanceof LinkCategory)) return false;
			return category.equals(((LinkCategory)obj).category);
		}

		@Override
		public String toString(){
			return category+"="+links.toString();
		}
	}
	
	List<LinkCategory> getLinks();
	
	default LinkCategory getLinks(String category){
		String category0=category==null||category.isEmpty()?"all":category;
		
		LinkCategory links=getLinks().stream().filter(cat -> cat.category.equals(category0)).findFirst().orElse(null);
		if(links==null) getLinks().add(links=new LinkCategory(category, this));
		
		return links;
	}
	
	default void readSafeLinkFromNbt(NBTTagCompound compound){
		NBTTagCompound data=compound.getCompoundTag(DEFAULT_KEY);
		if(data.getSize()==0) return;
		
		List<LinkCategory> links=getLinks();
		data.getKeySet().forEach(categoryName -> {
			NBTTagCompound categoryData=data.getCompoundTag(categoryName);
			for(int i=0; i<categoryData.getSize(); i++){
				getLinks(categoryName).getAll().add(LoadLink.desterilise(categoryData.getCompoundTag(Integer.toString(i)), this, categoryName));
			}
		});
		if(hasWorld()) checkForChange();
	}
	
	default void writeSafeLinkToNbt(NBTTagCompound compound){
		Map<String,NBTTagCompound> categoryes=new HashMap<>();
		
		getLinks().forEach(cat -> {
			List<LoadLink> catList=getLinksToSave(cat);
			
			if(catList==null||catList.isEmpty()) return;
			
			NBTTagCompound categoryData=new NBTTagCompound();
			categoryes.put(cat.category, categoryData);
			
			catList.forEach(link -> categoryData.setTag(Integer.toString(categoryData.getSize()), link.sterilise(new NBTTagCompound())));
		});
		
		if(!categoryes.isEmpty()){
			NBTTagCompound data=new NBTTagCompound();
			categoryes.forEach(data::setTag);
			compound.setTag(DEFAULT_KEY, data);
		}
	}
	
	default List<LoadLink> getLinksToSave(LinkCategory category){
		return category.getAll();
	}
	
	default void updateLoadSafeLinks(){
		List<LoadLink> load=new ArrayList<>(), unload=new ArrayList<>();
		
		getLinks().forEach(category -> category.getAll().forEach(l -> (l.getStatus().loaded?load:unload).add(l)));
		
		load.stream().filter(l -> !l.checkType()).forEach(l -> l.setStatus(LoadLinkStatus.UNLOADED));
		
		unload.stream().filter(link -> getWorld().isBlockLoaded(link.pos)).forEach(link -> {
			
			LoadLinkStatus prevStatus=link.getStatus();
			
			BlockPosM pos=link.pos;
			
			TileEntity unsafeTile=pos.getTile(getWorld());
			//small possibility of being called when blocks are loaded and tiles are not.
			if(unsafeTile instanceof UnloadSafeLink){
				
				TileEntityM linked=(TileEntityM)unsafeTile;
				if(!linked.isNbtLoaded()){
					link.setStatus(LoadLinkStatus.WAITING_FOR_RESPONSE);
					return;//Ssssh he is still loading.
				}
				
				UnloadSafeLink linkedMate=(UnloadSafeLink)linked;
				//can you give me a link that points to me?
				LoadLink linkFromLinkedToThis=getLinkbyPos(pos);
				
				//Oh... so you are not interested in me... :(
				if(linkFromLinkedToThis==null){
					link.setStatus(LoadLinkStatus.UNLOADED);
					return;
				}
				//Hi!
				linkFromLinkedToThis.setStatus(LoadLinkStatus.LOADED);
				link.setStatus(LoadLinkStatus.LOADED);
				
			}else {
				link.setStatus(LoadLinkStatus.LOADED);
			}
			
		});
		TickEvents.nextTick(this::checkForChange);
	}
	
	default void notifyDestroy(){
		List<UnloadSafeLink> notify=new ArrayList<>();
		forEachLink(link -> {
			UnloadSafeLink pointedTo=link.getPointed();
			if(pointedTo!=null) notify.add(pointedTo);
		});
		notify.forEach(UnloadSafeLink::updateLoadSafeLinks);
	}
	
	default List<LoadLink> getLinkList(){
		List<LoadLink> links=new ArrayList<>();
		getLinks().forEach(v -> links.addAll(v.getAll()));
		return links;
	}
	
	default void forEachLink(Consumer<LoadLink> link){
		getLinks().forEach(v -> v.getAll().forEach(link));
	}
	
	default void destroyLink(BlockPosM pos){
		
		LoadLink l=getLinkbyPos(pos);
		
		if(l==null) return;
		for(LinkCategory e : getLinks()){
			if(e.getAll().remove(l)) break;
		}
	}
	
	default LoadLink getLinkbyPos(BlockPosM pos){
		LoadLink l=null;
		mainFor:
		for(LinkCategory e : getLinks()){
			for(LoadLink link : e.getAll()){
				if(link.pos.equals(pos)){
					l=link;
					break mainFor;
				}
			}
		}
		return l;
	}
	
	void onLinkedChange();
	
	void markLinksDirty();
	
	void checkForChange();
}
