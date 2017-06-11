package com.magiology.mc_objects.features.screen;

import com.magiology.core.registry.init.BlocksM;
import com.magiology.core.registry.init.ParticlesM;
import com.magiology.forge.events.TickEvents;
import com.magiology.forge.networking.UpdateTileNBTPacket;
import com.magiology.handlers.frame_buff.TemporaryFrame;
import com.magiology.mc_objects.features.neuro.NeuroInterface;
import com.magiology.mc_objects.features.neuro.NeuroPart;
import com.magiology.mc_objects.features.neuro.TileEntityNeuroController;
import com.magiology.mc_objects.tile.MultiblockHandler;
import com.magiology.mc_objects.tile.TileEntityMultiblock;
import com.magiology.util.interf.ISidedConnection;
import com.magiology.util.interf.Locateable;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec2i;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.CollectionConverter;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.Structure;
import com.magiology.util.statics.Structure.Plane;
import com.magiology.util.statics.UtilM;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class TileEntityScreen extends TileEntityMultiblock<TileEntityScreen,TileEntityScreen> implements NeuroInterface<TileEntityScreen>{
	
	private static TileEntityScreen highlightedTile;
	
	public static void setHighlighted(TileEntityScreen tile, float hitX, float hitY, float hitZ){
		TileEntityScreen brain=null;
		if(tile!=null){
			if(tile.hasBrain()) brain=tile.getBrain();
		}
		
		if(highlightedTile==tile){
			if(brain!=null) brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			return;
		}
		
		if(highlightedTile!=null){
			if(tile==null&&highlightedTile.hasBrain()) highlightedTile.getBrain().onExit();
		}else if(brain!=null){
			brain.onHighlight(BlockScreen.calcScreenPos(tile, hitX, hitY, hitZ));
			brain.onEnter();
		}
		highlightedTile=tile;
	}
	
	
	private TileEntityNeuroController controller;
	@SideOnly(Side.CLIENT)
	public boolean screenDirty=true;
	@SideOnly(Side.CLIENT)
	public TemporaryFrame screenTexture;
	private int    mbId =-1;
	public  Vec2FM click=new Vec2FM(-1, -1), highlight=new Vec2FM(-1, -1);
	public boolean highlighted;
	
	public class ScreenMultiblockHandler extends MultiblockHandler<TileEntityScreen,TileEntityScreen>{
		
		public ScreenMultiblockHandler(TileEntityScreen owner){
			super(owner);
		}
		
		@Override
		public List<TileEntityScreen> getMultiblockNeededPositions(){
			if(loaded.isEmpty()) return new ArrayList<>();
			
			List<TileEntityScreen> parts=new ArrayList<>(loaded);
			
			TileEntityScreen clicked=TileEntityScreen.this;
			Plane<TileEntityScreen> i=Structure.buildPlane(loaded, clicked);
			
			parts.removeIf(t->i.parts.stream().noneMatch(p->p.tile==t));
			
			return parts;
		}
		
		@Override
		protected boolean validatePos(World world, TileEntityScreen pos){
			return pos!=null&&(!pos.hasBrain()||pos.getBrain()==getBrain());
		}
		
		@Override
		public void addBlockPos(BlockPosM pos){
			pos.getTile(world, TileEntityScreen.class, t->addPos(t));
		}
		
		@Override
		public boolean canBePart(BlockPosM pos){
			return UtilM.getBlock(world, pos)==BlocksM.SCREEN;
		}
		
		@Override
		public void onCreate(){
			super.onCreate();
			
			loaded.forEach(l->{
				l.setBrain(TileEntityScreen.this);
				ParticlesM.MESSAGE.spawn(new Vec3M(l.getPos()).add(-0.5, 0.4, 0.5), new Vec3M(-0.01, 0, 0), 2/16F, 32, ColorM.GREEN, "Block accepted!");
			});
			gen();
		}
		
		private List<Vec2i> positions;
		private Vec2i       size2d;
		
		public List<Vec2i> getPositions(){
			if(positions==null)gen();
			return positions;
		}
		
		public Vec2i getSize2d(){
			if(size2d==null)gen();
			return size2d;
		}
		
		@SuppressWarnings("incomplete-switch")
		private void gen(){
			
			runInMB(part->part.mbId=-1);
			
			PairM<List<Vec2i>,Vec2i> pair=gen2DPositionsAndSize(loaded, TileEntityScreen.this);
			positions=pair.obj1;
			
			IntStream minxs=positions.stream().mapToInt(off->off.x), minys=positions.stream().mapToInt(off->off.y);
			OptionalInt minxo=null, minyo=null;
			switch(getRotation()){
			case UP:{
				minxo=minxs.min();
				minyo=minys.max();
			}
			break;
			case DOWN:{
				minxo=minxs.min();
				minyo=minys.max();
			}
			break;
			case EAST:{
				minxo=minxs.max();
				minyo=minys.max();
			}
			break;
			case NORTH:{
				minxo=minxs.max();
				minyo=minys.max();
			}
			break;
			case SOUTH:{
				minxo=minxs.min();
				minyo=minys.max();
			}
			break;
			case WEST:{
				minxo=minxs.min();
				minyo=minys.max();
			}
			break;
			}
			size2d=pair.obj2;
			int minx=minxo.getAsInt(), miny=minyo.getAsInt();
			for(Vec2i off : positions){
				off.x-=minx;
				off.y-=miny;
			}
			switch(getRotation()){
			case EAST:
				for(Vec2i off : positions)
					off.x*=-1;
				break;
			case NORTH:
				for(Vec2i off : positions)
					off.x*=-1;
				break;
			case UP:
				for(Vec2i off : positions)
					off.y=1-size2d.y-off.y;
				break;
			}
		}
		
	}
	
	
	
	public int getMbId(){
		if(hasBrain()){
			TileEntityScreen brain=getBrain();
			ScreenMultiblockHandler mbHandler=brain.getHandler();
			List<TileEntityScreen> mb=mbHandler.loaded;
			if(mbId==-1||mbId>=mb.size()){
				mbId=mb.indexOf(this);
				if(server()&&mbId==-1){
					mbHandler.built=false;
					if(!mbHandler.pending.contains(this))mbHandler.addPos(this);
					mbHandler.tryToForm();
					UpdateTileNBTPacket.markForSync(brain);
				}
			}
		}else if(mbId!=-1) mbId=-1;
		return mbId;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
	}
	
	@Override
	protected void readFromNbtWithWorld(NBTTagCompound compound){
		super.readFromNbtWithWorld(compound);
		readNeuroPartFromNbt(compound);
//		if(server())TickEvents.tickUntil(this,()->{
//			try{
//				getMbId();
//				return false;
//			}catch(Exception e){
//				if(!getBrain().isInWorld()){
//					setBrain(null);
//					return false;
//				}
//				return true;
//			}
//		});
		BlocksM.SCREEN.updateBlockStateAndSet(getState(), world, getPos(), this);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		writeNeuroPartToNbt(getTileData());
		super.writeToNBT(compound);
		return compound;
	}
	
	public EnumFacing getRotation(){
		return BlockScreen.getRot(getState());
	}
	
	public static PairM<List<Vec2i>,Vec2i> gen2DPositionsAndSize(List<? extends Locateable<? extends BlockPos>> parts, Locateable<? extends BlockPos> main){
		Vec2i min=new Vec2i(Integer.MAX_VALUE, Integer.MAX_VALUE), max=new Vec2i(Integer.MIN_VALUE, Integer.MIN_VALUE);
		
		List<Vec3M> offsets=CollectionConverter.convLi(parts, Vec3M.class, t->new Vec3M(t.getPos().subtract(main.getPos())));
		boolean noneXOffset=offsets.stream().allMatch(b->b.getX()==0), noneYOffset=offsets.stream().allMatch(b->b.getY()==0);
		List<Vec2i> positions=CollectionConverter.convLi(offsets, Vec2i.class, offset->{
			
			Vec2FM offset2D_float=new Vec2FM();
			if(noneXOffset) offset.swizzleZY(offset2D_float);
			else if(noneYOffset) offset.swizzleXZ(offset2D_float);
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
		if(positions.isEmpty()) positions.add(new Vec2i(0, 0));
		
		Vec2i size2d=max.sub(min).add(1);
		
		return new PairM<>(positions, size2d);
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
		if(!hasBrain()) return;
		if(isBrain()) this.controller=controller;
		else getBrain().setController(controller);
	}
	
	@Override
	public List<NeuroPart> getConnected(){
		if(!hasBrain()) return new ArrayList<>();
		if(!isBrain()) return getBrain().getConnected();
		
		List<NeuroPart> list=new ArrayList<>();
		getHandler().loaded.forEach(t->list.addAll(UtilM.getTileSides(t.getWorld(), new BlockPosM(t.getPos()), NeuroPart.class)));
		
		list.removeIf(p->{
			BlockPos pos=((TileEntity)p).getPos();
			EnumFacing face=getBrain().getRotation();
			return getHandler().loaded.stream().anyMatch(m->pos.equals(m.getPos())||pos.equals(m.getPos().offset(face)));
		});
		//		for(NeuroPart p:list){
		//			worldObj.setBlockState(((TileEntity)p).getPos().up(), Blocks.STONE.getDefaultState());
		//		}
		
		return list;
	}
	
	@Override
	public boolean canConnect(ISidedConnection o){
		if(!(o instanceof TileEntity)) return false;
		List<NeuroPart> conected=getConnected();
		
		if(o instanceof NeuroInterface) return ((NeuroInterface<NeuroInterface>)o).getWholeInterface().stream().anyMatch(i->conected.contains(i));
		
		return getConnected().contains(o);
	}
	@Override
	public TileEntityScreen getInterfaceCore(){
		return getBrain();
	}
	
	public List<Vec2i> getPositions(){
		if(!hasBrain())return null;
		ScreenMultiblockHandler mb=getBrain().getHandler();
		if(mb==null) return null;
		return mb.getPositions();
	}
	
	public Vec2i getSize2d(){
		if(!hasBrain())return null;
		ScreenMultiblockHandler mb=getBrain().getHandler();
		if(mb==null) return null;
		return mb.getSize2d();
	}
	
	@Override
	public Collection<TileEntityScreen> getWholeInterface(){
		if(!hasBrain()) return new ArrayList<>();
		if(!isBrain()) return getBrain().getWholeInterface();
		return getHandler().loaded;
	}
	
	@Override
	protected MultiblockHandler createMultiblockHandler(){
		return new ScreenMultiblockHandler(this);
	}

	@Override
	public ScreenMultiblockHandler getHandler(){
		return (ScreenMultiblockHandler)super.getHandler();
	}
}
