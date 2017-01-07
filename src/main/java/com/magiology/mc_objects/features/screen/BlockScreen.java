package com.magiology.mc_objects.features.screen;

import org.lwjgl.util.vector.Matrix4f;

import com.magiology.core.registry.init.ItemsM;
import com.magiology.mc_objects.items.ItemMatterJumper;
import com.magiology.mc_objects.items.ItemMatterJumper.MatterJumperMode;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.m_extensions.BlockContainerM.MixedRender;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.m_extensions.TileEntityM;
import com.magiology.util.objs.BlockStates;
import com.magiology.util.objs.BlockStates.IPropertyM;
import com.magiology.util.objs.BlockStates.PropertyBoolM;
import com.magiology.util.objs.BlockStates.PropertyDirectionM;
import com.magiology.util.objs.block_bounds.StateDependantBlockBounds;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.GeometryUtil;
import com.magiology.util.statics.UtilM;
import com.magiology.util.statics.math.MatrixUtil;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerM<TileEntityScreen> implements MixedRender{
	
	public static final PropertyDirectionM	ROT			=BlockStates.SAVE_ROTATION_FULL_3BIT;
	public static final PropertyBoolM		ACTIVE		=BlockStates.saveableBooleanProp("active");
	public static final PropertyBoolM[]		SIDE_EDGE	={BlockStates.booleanProp("top"),BlockStates.booleanProp("bottom"),BlockStates.booleanProp("left"),BlockStates.booleanProp("right")};
	
	public BlockScreen(){
		super(Material.IRON, ()->new TileEntityScreen(), UtilM.mixedToArray(IPropertyM.class, ROT, ACTIVE, SIDE_EDGE));
		
		setBlockBounds(new StateDependantBlockBounds(
				state->ROT.get(state).getIndex()/2,
				
				new AxisAlignedBB(0, p*5, 0, 1, 1-p*5, 1),
				new AxisAlignedBB(0, 0, p*5, 1, 1, 1-p*5),
				new AxisAlignedBB(p*5, 0, 0, 1-p*5, 1, 1)));
		
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		TileEntityScreen tile=getTile(world, pos);
		if(tile==null) return;
		updateBlockStateAndSet(state, world, pos, tile);
	}
	
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ){
		world.setBlockState(pos, ROT.set(state, side));
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot){
		if(ACTIVE.get(state)) return state;
		return ROT.set(state, rot.rotate(getRot(state)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror){
		if(ACTIVE.get(state)) return state;
		return ROT.set(state, mirror.mirror(getRot(state)));
	}
	
	public static EnumFacing getRot(IBlockState state){
		return ROT.get(state);
	}
	
	public static boolean isActive(IBlockState state){
		return ACTIVE.get(state);
	}
	
	public static IBlockState setActive(IBlockState state, boolean active){
		return ACTIVE.set(state, active);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.MODEL;
	}
	
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		TileEntityScreen tile=getTile(world, pos);
		if(tile==null) return;
		tile.updateMultiblock();
		updateBlockStateAndSet(state, world, pos, tile);
		
	}
	
	void updateBlockStateAndSet(IBlockState state, World world, BlockPos pos, TileEntityScreen tile){
		IBlockState newState=state;
		
		Matrix4f rot=MatrixUtil.createMatrix(pos).rotate(GeometryUtil.rotFromFacing(tile.getRotation())).finish();
		
		for(int i=0;i<4;i++)
			newState=checkEdge(world, rot, newState, i);
		
		if(state!=newState) world.setBlockState(pos, newState);
	}
	
	private IBlockState checkEdge(World world, Matrix4f rot, IBlockState state, int id){
		
		Vec3M side=new Vec3M(id==2?-1:id==3?1:0, id==0?1:id==1?-1:0, 0).transformSelf(rot);
		
		BlockPos topPos=new BlockPosM(side);
		IBlockState topBlock=world.getBlockState(topPos);
		
		boolean hasSide=topBlock.getBlock().getClass()==BlockScreen.class;
		
		return SIDE_EDGE[id].set(state, !hasSide);
	}
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntityScreen tile=getTile(world, pos);
		if(tile==null) return false;
		
		ItemStack heldItem=player.getHeldItem(hand);
		
		//LogUtil.println(tile.getMbCategory());
		if(UtilM.isItemInStack(ItemsM.MATTER_JUMPER, heldItem)){
			MatterJumperMode mode=ItemMatterJumper.getMode(heldItem);
			
			if(mode==MatterJumperMode.WRENCH){
				if(!tile.hasBrain()){
					tile.buildMultiblock();
					return true;
				}
			}
			return false;
		}
		if(tile.hasBrain()){
			try{
				if(side!=tile.getRotation()) return false;
				TileEntityScreen brain=tile.getBrain();
				brain.onClick(calcScreenPos(tile, hitX, hitY, hitZ));
			}catch(Exception e){
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}
	
	public static Vec2FM calcScreenPos(TileEntityScreen tile, float hitX, float hitY, float hitZ){
		
		TileEntityScreen brain=tile.getBrain();
		int pixels=64;
		int id=tile.getMbId();
		if(id==-1) return new Vec2FM();
		Vec2FM offset=new Vec2FM(tile.getPositions().get(id)).mulSelf(pixels, -pixels);
		switch(brain.getRotation()){
		case NORTH:{
			offset.x+=(1-hitX)*pixels;
			offset.y+=(1-hitY)*pixels;
		}
		break;
		case UP:{
			offset.x+=(0+hitX)*pixels;
			offset.y+=(0+hitZ)*pixels;
		}
		break;
		case EAST:{
			offset.x+=(1-hitZ)*pixels;
			offset.y+=(1-hitY)*pixels;
		}
		break;
		case SOUTH:{
			offset.x+=(0+hitX)*pixels;
			offset.y+=(1-hitY)*pixels;
		}
		break;
		case WEST:{
			offset.x+=(0+hitZ)*pixels;
			offset.y+=(1-hitY)*pixels;
		}
		break;
		case DOWN:{
			offset.x+=(0+hitX)*pixels;
			offset.y+=(1-hitZ)*pixels;
		}
		break;
		}
		
		return offset;
	}
}
