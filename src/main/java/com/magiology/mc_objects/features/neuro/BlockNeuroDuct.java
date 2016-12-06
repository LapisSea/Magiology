package com.magiology.mc_objects.features.neuro;

import com.magiology.forge.events.TickEvents;
import com.magiology.mc_objects.BlockStates.IPropertyM;
import com.magiology.mc_objects.BlockStates.PropertyBoolM;
import com.magiology.util.interf.ISidedConnection;
import com.magiology.util.objs.BlockSides;
import com.magiology.util.objs.block_bounds.MultiBlockBounds.PipeStyleBlockBounds;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
		
//		AxisAlignedBB[] boxes=new AxisAlignedBB[64];
//
//		Vec3M min=new Vec3M(), max=new Vec3M();
//
//		for(int id=0;id<64;id++){
//			min.set(6/16F, 6/16F, 6/16F);
//			max.set(10/16F, 10/16F, 10/16F);
//
//			for(int i=0;i<6;i++){
//				if(((id>>i)&1)==1){
//					EnumFacing side=EnumFacing.getFront(i);
//					(side.getAxisDirection()==AxisDirection.POSITIVE?max:min).addSelf(new Vec3M(side.getDirectionVec()).mul(6/16F));
//				}
//			}
//
//			boxes[id]=new AxisAlignedBB(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
//		}
//		setBlockBounds(new StateDependantBlockBounds(state->{
//
//			switch(STRAIGHT.get(state)){
//			case 0:return 0b000011;
//			case 1:return 0b001100;
//			case 2:return 0b110000;
//			}
//
//			int boxId=0;
//			if(CONNECTIONS[0].get(state))boxId|=0b000000_1;
//			if(CONNECTIONS[1].get(state))boxId|=0b00000_10;
//			if(CONNECTIONS[2].get(state))boxId|=0b0000_100;
//			if(CONNECTIONS[3].get(state))boxId|=0b000_1000;
//			if(CONNECTIONS[4].get(state))boxId|=0b00_10000;
//			if(CONNECTIONS[5].get(state))boxId|=0b0_100000;
//
//			return boxId;
//		},boxes));
		
		setBlockBounds(new PipeStyleBlockBounds(CONNECTIONS,STRAIGHT, 0b1000000,p*4));
	}
	
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn){
		super.neighborChanged(state, world, pos, blockIn);
		updateBlockState(state, world, pos);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		super.onBlockAdded(world, pos, state);
		updateBlockState(state, world, pos);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		IBlockState state=super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		for(PropertyBoolM bol:CONNECTIONS){
			state=bol.set(state, false);
		}
		state=HAS_CONTROLLER.set(state, false);
		state=STRAIGHT.set(state, 3);
		
		return state;
	}
	
//	(IBlockState state, World world, BlockPos pos){
//		IBlockState newState=calcBlockState(state, world, pos);
//
//		if(newState==null){
//			if(!world.isRemote)TickEvents.nextTick(false, ()->updateBlockState(state, world, pos));
//		}
//		else if(newState!=state)world.setBlockState(pos, newState, 2);
//	}
	
	public void updateBlockState(IBlockState state, World world, BlockPos pos){
		TileEntityNeuroDuct tile=getTile(world, pos);
		
		for(int i=0;i<6;i++){
			EnumFacing side=EnumFacing.getFront(i);
			BlockPos pos1=tile.getPos().offset(side);
			TileEntity tile1=world.getTileEntity(pos1);
			if(!world.isBlockLoaded(pos1)){
				TickEvents.nextTick(()->updateBlockState(state, world, pos));
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
		boolean hasCtrl=tile.getControllerSecure()!=null;
		
		setData(world, pos, state, 3, null, CONNECTION_HANDLER, hasCtrl);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos){
		// TODO Auto-generated method stub
		return super.getCollisionBoundingBox(blockState, worldIn, pos);
	}
	
	public static void setData(World world, BlockPos pos, IBlockState target, int straight, boolean[] sides, BlockSides s, boolean hasCtrl){
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
		if(newData!=target)world.setBlockState(pos, newData);
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
