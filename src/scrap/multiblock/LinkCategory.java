package com.magiology.mc_objects.tile.multiblock;

import com.magiology.util.m_extensions.BlockPosM;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class LinkCategory extends ArrayList<Link>{
	
	public final String name;
	public    Runnable onAllLoad=() -> {};
	public    Runnable onChange =() -> {};
	protected int      prevHash =hashCode();
	public boolean removeLinkWhenLoadedAndlost=true, allLoad, prevAllLoad;
	
	private final TileMultiblock parent;
	
	public LinkCategory(TileMultiblock parent, String name, NBTTagCompound positions){
		this(parent, name);
		positions.getKeySet().forEach(k -> add(new Link(parent.getPos(), new BlockPosM(positions.getLong(k)))));
	}
	
	public LinkCategory(TileMultiblock parent, String name){
		this.name=name;
		this.parent=parent;
		parent.markLinksDirty();
	}
	
	public void writeTo(NBTTagCompound nbt){
		NBTTagCompound positions=new NBTTagCompound();
		forEach(link -> positions.setLong(String.valueOf(positions.getSize()), link.getPoint().toLong()));
		nbt.setTag(name, positions);
	}
	
	@Override
	public boolean add(Link e){
		if(contains(e)) return false;
		if(super.add(e)) return true;
		parent.markLinksDirty();
		return false;
	}
	
	@Override
	public boolean addAll(Collection<? extends Link> c){
		c=new ArrayList<>(c);
		c.removeIf(this::contains);
		if(c.isEmpty()) return false;
		if(!super.addAll(c)) return false;
		parent.markLinksDirty();
		return true;
	}
	
	@Override
	public boolean remove(Object o){
		if(!super.remove(o)) return false;
		parent.markLinksDirty();
		return true;
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		if(!super.removeAll(c)) return false;
		parent.markLinksDirty();
		return true;
	}
	
	@Override
	public boolean retainAll(Collection<?> c){
		if(!super.retainAll(c)) return false;
		parent.markLinksDirty();
		return true;
	}
	
	@Override
	public boolean removeIf(Predicate<? super Link> filter){
		if(!super.removeIf(filter)) return false;
		parent.markLinksDirty();
		return true;
	}
	
	@Override
	public void clear(){
		parent.markLinksDirty();
		super.clear();
	}
	
	public Link get(Vec3i pos){
		return stream().filter(l -> l.getPoint().equals(pos)).findFirst().orElse(null);
	}
	
	public void posWentBad(BlockPos pos){
		stream().filter(l -> l.getPoint().equals(pos)).forEach(l -> l.setStatus(LinkStatus.UNLOADED));
	}

	public void handshake(Link link){
		if(parent.isNbtLoaded()) ;//TODO
		
		Link found=get(link.getSrcPos());
		if(found==null){
			link.setStatus(LinkStatus.UNLOADED);
		}else {
			found.setStatus(LinkStatus.LOADED);
			link.setStatus(LinkStatus.LOADED);
		}
		parent.markLinksDirty();
	}
	
}
