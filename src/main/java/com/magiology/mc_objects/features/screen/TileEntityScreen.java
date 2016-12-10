package com.magiology.mc_objects.features.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.magiology.handlers.TileEntityOneBlockStructure;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.mc_objects.features.neuro.NeuroInterface;
import com.magiology.mc_objects.features.neuro.NeuroPart;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroController;
import com.magiology.util.interf.ISidedConnection;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.Structure;
import com.magiology.util.statics.Structure.Plane;
import com.magiology.util.statics.UtilM;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityScreen extends TileEntityOneBlockStructure<TileEntityScreen> implements NeuroInterface<TileEntityScreen>{
	
	private static TileEntityScreen highlightedTile;
	private TileEntityNeuroController controller;
	private List<Vec2i> positions;
	private Vec2i size2d;
	
	public static void setHighlighted(TileEntityScreen tile, float hitX,float hitY,float hitZ){
		TileEntityScreen brain=null;
		if(tile!=null){
			if(tile.hasBrain())brain=tile.getBrain();
		}
		
		if(highlightedTile==tile){
			if(brain!=null)brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			return;
		}
		
		if(highlightedTile!=null){
			if(tile==null&&highlightedTile.hasBrain())highlightedTile.getBrain().onExit();
		}else if(brain!=null){
			brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			brain.onEnter();
		}
		highlightedTile=tile;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean screenDirty=true;
	@SideOnly(Side.CLIENT)
	public TemporaryFrame screenTexture;
	private int mbId=-1;
	public Vec2FM click=new Vec2FM(-1,-1),highlight=new Vec2FM(-1,-1);
	public boolean highlighted;
	
	public int getMbId(){
		if(hasBrain()){
			if(mbId==-1)mbId=getBrain().getMultiblock().indexOf(this);
		}
		else if(mbId!=-1)mbId=-1;
		
		return mbId;
	}
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		readNeuroPartFromNbt(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		writeNeuroPartToNbt(compound);
		return super.writeToNBT(compound);
	}
	
	public EnumFacing getRotation(){
		return BlockScreen.getRot(getState());
	}
	@Override
	public void filterBlocks(List<TileEntityScreen> parts,Vec3i clickedPos){
		parts.removeIf(t->t.hasBrain());
		if(parts.isEmpty())return;
		TileEntityScreen clicked=new BlockPosM(clickedPos).getTile(worldObj, TileEntityScreen.class);
		Plane<TileEntityScreen> i=Structure.buildPlane(parts,clicked);
		
		parts.removeIf(t->i.parts.stream().noneMatch(p->p.tile==t));
	}
	@Override
	protected List<TileEntityScreen> explore(List<TileEntityScreen> parts){
		parts.add(this);
		EnumFacing rotation=getRotation();
		UtilM.getTileSides(getWorld(), getPos()).forEach(tile->{
			if(checkType(tile)){
				TileEntityScreen t=(TileEntityScreen)tile;
				if(t.getBrain()==null&&!parts.contains(t)&&rotation==t.getRotation())t.explore(parts);
			}
		});
		
		return parts;
	}
	@Override
	public boolean validateLoaded(TileEntity tile){
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	protected void initBrainObjects(List<TileEntityScreen> multiblock){
		
		try{
			Vec2i min=new Vec2i(Integer.MAX_VALUE,Integer.MAX_VALUE),max=new Vec2i(Integer.MIN_VALUE,Integer.MIN_VALUE);
			
			List<Vec3M> offsets=CollectionConverter.convLi(multiblock,Vec3M.class,t->new Vec3M(t.getPos().subtract(getPos())));
			
			boolean
				noneXOffset=offsets.stream().allMatch(b->b.getX()==0),
				noneYOffset=offsets.stream().allMatch(b->b.getY()==0);/*,
				noneZOffset=noneYOffset?offsets.stream().allMatch(b->b.getZ()==0):true;*/
			positions=CollectionConverter.convLi(offsets,Vec2i.class,offset->{
				
				Vec2FM offset2D_float=new Vec2FM();
				if(noneXOffset)offset.swizzleZY(offset2D_float);
				else if(noneYOffset)offset.swizzleXZ(offset2D_float);
				else offset.swizzleXY(offset2D_float);
				
				Vec2i offset2D=offset2D_float.toVec2i();
				
				if(min.x>offset2D.x||min.y>offset2D.y){
					min.x=offset2D.x;
					min.y=offset2D.y;
				}
				if(max.x<offset2D.x||max.y<offset2D.y){
					max.x=offset2D.x;
					max.y=offset2D.y;
				}
				
				return offset2D;
			});
			size2d=max.sub(min).add(1);
			
			IntStream minxs=positions.stream().mapToInt(off->off.x),minys=positions.stream().mapToInt(off->off.y);
			OptionalInt minxo=null,minyo=null;
			switch(getRotation()){
			case UP:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case DOWN:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case EAST:{
				minxo=minxs.max();
				minyo=minys.max();
			}break;
			case NORTH:{
				minxo=minxs.max();
				minyo=minys.max();
			}break;
			case SOUTH:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			case WEST:{
				minxo=minxs.min();
				minyo=minys.max();
			}break;
			}
			
			int minx=minxo.getAsInt(),miny=minyo.getAsInt();
			for(Vec2i off:positions){
				off.x-=minx;
				off.y-=miny;
			}
			switch(getRotation()){
			case EAST:for(Vec2i off:positions)off.x*=-1;break;
			case NORTH:for(Vec2i off:positions)off.x*=-1;break;
			case UP:for(Vec2i off:positions)off.y=1-size2d.y-off.y;break;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onMultiblockJoin(List<TileEntityScreen> multiblock,TileEntityScreen brain){
		mbId=multiblock.indexOf(this);
	}
	@Override
	protected void onMultiblockLeave(List<TileEntityScreen> multiblock){
		mbId=-1;
	}
	

	public void onClick(Vec2FM pos){
		click=pos;
	}
	public void onHighlight(Vec2FM pos){
		highlight=pos;
	}
	public void onEnter(){
		highlighted=true;
	}
	public void onExit(){
		highlighted=false;
	}
	
	@Override
	public TileEntityNeuroController getController(){
		return hasBrain()?isBrain()?controller:getBrain().getController():null;
	}

	@Override
	public void setController(TileEntityNeuroController controller){
		if(!hasBrain())return;
		if(isBrain())this.controller=controller;
		else getBrain().setController(controller);
	}

	@Override
	public TileEntityScreen getInterfaceCore(){
		return getBrain();
	}
	@Override
	public List<NeuroPart> getConnected(){
		if(!hasBrain())return new ArrayList<>();
		if(!isBrain())return getBrain().getConnected();
		
		List<NeuroPart> list=new ArrayList<>();
		getMultiblock().forEach(t->list.addAll(UtilM.getTileSides(t.getWorld(), new BlockPosM(t.getPos()), NeuroPart.class)));
		
		list.removeIf(p->{
			BlockPos pos=((TileEntity)p).getPos();
			EnumFacing face=getBrain().getRotation();
			return getMultiblock().stream().anyMatch(m->
				pos.equals(m.pos)||
				pos.equals(m.pos.offset(face))
			);
		});
//		for(NeuroPart p:list){
//			worldObj.setBlockState(((TileEntity)p).getPos().up(), Blocks.STONE.getDefaultState());
//		}
		
		return list;
	}
	@Override
	public boolean canConnect(ISidedConnection o){
		if(!(o instanceof TileEntity))return false;
		List<NeuroPart> conected=getConnected();
		
		if(o instanceof NeuroInterface){
			return ((NeuroInterface<NeuroInterface>)o).getWholeInterface().stream().anyMatch(i->conected.contains(i));
		}
		
		return getConnected().contains(o);
	}
	@Override
	protected void clearBrain(){
		positions=null;
		size2d=null;
	}
	public List<Vec2i> getPositions(){
		if(!hasBrain())return null;
		if(!isBrain())return getBrain().getPositions();
		return positions;
	}
	public Vec2i getSize2d(){
		if(!hasBrain())return null;
		if(!isBrain())return getBrain().getSize2d();
		return size2d;
	}
	@Override
	public Collection<TileEntityScreen> getWholeInterface(){
		if(!hasBrain())return new ArrayList<>();
		if(!isBrain())return getBrain().getWholeInterface();
		return getMultiblock();
	}
}
