package com.magiology.mc_objects.features.neuro;

import com.magiology.client.rendering.highlight.extras.MultiBlockBoxHighlight;
import com.magiology.forge.events.TickEvents;
import com.magiology.util.interf.ISidedConnection;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.BlockSides;
import com.magiology.util.objs.BlockStates.IPropertyM;
import com.magiology.util.objs.BlockStates.PropertyBoolM;
import com.magiology.util.objs.block_bounds.MultiBlockBounds.PipeStyleBlockBounds;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockNeuroDuct extends BlockNeuroBase<TileEntityNeuroDuct>{
	
	protected static final BlockSides	CONNECTION_HANDLER	=new BlockSides();
	private static BlockNeuroDuct		instance;
	
	public static BlockNeuroDuct get(){
		return instance;
	}
	
	public BlockNeuroDuct(){
		super(Material.IRON, ()->new TileEntityNeuroDuct(), UtilM.mixedToArray(IPropertyM.class, HAS_CONTROLLER, STRAIGHT, CONNECTIONS));
		instance=this;
		
		PipeStyleBlockBounds bounds=new PipeStyleBlockBounds(CONNECTIONS,STRAIGHT, 0b1000000, p*4, this::onBoxHighlight);
		if(UtilM.isRemote()){
			bounds.getHighlightRenderer().addListener(new MultiBlockBoxHighlight());
		}
		
		setBlockBounds(bounds);
		
		IBlockState state=STRAIGHT.set(HAS_CONTROLLER.set(getDefaultState(), false), 3);
		for(PropertyBoolM con:CONNECTIONS)state=con.set(state, false);
		setDefaultState(state);
	}
	
	protected void onBoxHighlight(RayTraceResult hit, World world){
		
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn){
		super.neighborChanged(state, world, pos, blockIn);
		updateBlockStateAndSet(state, world, pos);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		super.onBlockAdded(world, pos, state);
		updateBlockStateAndSet(state, world, pos);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntity tile=world.getTileEntity(pos.offset(side,-1));
		if(tile instanceof NeuroPart){
			NeuroPart part=(NeuroPart)tile;
			if(part.hasController()){
				getTile(world, pos).setController(part.getController());
				neighborChanged(state, world, pos, state.getBlock());
			}
		}
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		if(world instanceof World)updateBlockStateAndSet(world.getBlockState(pos), (World)world, pos);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack){
		updateBlockState(world, pos, null);
		IBlockState s=applyData(world, pos, super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack), 3, null, CONNECTION_HANDLER, false);
		return s;
	}
	
	public void updateBlockStateAndSet(IBlockState state, World world, BlockPos pos){
		
		updateBlockState(world, pos, ()->{
			TileEntityNeuroDuct tile=getTile(world, pos);
			if(tile==null)return;
			IBlockState newState=applyData(world, pos, state, 3, null, CONNECTION_HANDLER, tile.getControllerSecure()!=null);
			if(state!=newState)world.setBlockState(pos, newState);
		});
	}
	public void updateBlockState(World world, BlockPos pos,Runnable onEnd){
		TileEntityNeuroDuct tile=getTile(world, pos);
		for(int i=0;i<6;i++){
			EnumFacing side=EnumFacing.getFront(i);
			BlockPos pos1=pos.offset(side);
			TileEntity tile1=world.getTileEntity(pos1);
			if(!world.isBlockLoaded(pos1)||(tile1 instanceof TileEntityM&&!((TileEntityM)tile1).isNbtLoaded())){
				TickEvents.nextTick(()->updateBlockState(world, pos,onEnd));
				return;
			}
			boolean flag=tile1 instanceof NeuroPart;
			
			if(flag){
				NeuroPart pRaw=(NeuroPart)tile1;
				NeuroPart p=pRaw.getSelf();
				flag=p!=null;
				
				if(flag){
					flag=ISidedConnection.handshake(tile, pRaw);
					
					if(flag&&tile.getController()!=null&&p.getController()!=null){
						flag=tile.getController().getPos().equals(p.getController().getPos());
					}
				}
				
				//				flag=
				//				if(&&tile.getController()!=null&&p!=null&&p.getController()!=null){
				//					flag=tile.getController().getPos().equals(p.getController().getPos());
				//				}
			}
			
			CONNECTION_HANDLER.setSide(i, flag);
		}
		if(onEnd!=null)onEnd.run();
	}
	
	public static IBlockState applyData(World world, BlockPos pos, IBlockState target, int straight, boolean[] sides, BlockSides s, boolean hasCtrl){
		if(s!=null)straight=s.getStraight();
		
		IBlockState newData=target;
		boolean hasStraight=straight!=3;
		for(int i=0;i<6;i++){
			
			boolean flag=s!=null?s.getSideNotStraight(i):hasStraight&&sides[i];
			if(flag!=CONNECTIONS[i].get(newData)){
				newData=CONNECTIONS[i].set(newData, flag);
			}
		}
		
		if(straight!=STRAIGHT.get(newData)){
			newData=STRAIGHT.set(newData, straight);
		}
		
//		LogUtil.println(HAS_CONTROLLER.get(newData),hasCtrl);
		
		if(HAS_CONTROLLER.get(newData)!=hasCtrl){
			newData=HAS_CONTROLLER.set(newData, hasCtrl);
		}
		return newData;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
}
