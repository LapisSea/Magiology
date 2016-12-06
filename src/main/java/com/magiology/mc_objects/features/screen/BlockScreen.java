package com.magiology.mc_objects.features.screen;

import com.magiology.mc_objects.BlockStates;
import com.magiology.mc_objects.BlockStates.PropertyBoolM;
import com.magiology.mc_objects.BlockStates.PropertyDirectionM;
import com.magiology.mc_objects.items.ItemMatterJumper;
import com.magiology.mc_objects.items.ItemMatterJumper.MatterJumperMode;
import com.magiology.util.m_extensions.BlockContainerM;
import com.magiology.util.m_extensions.BlockPosM;
import com.magiology.util.objs.block_bounds.StateDependantBlockBounds;
import com.magiology.util.objs.vec.Vec2FM;
import com.magiology.util.statics.UtilM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerM<TileEntityScreen>{
	
	public static final PropertyDirectionM	ROT		=BlockStates.SAVE_ROTATION_FULL_3BIT;
	public static final PropertyBoolM		ACTIVE	=BlockStates.saveableBooleanProp("active");
	
	public BlockScreen(){
		super(Material.IRON, ()->new TileEntityScreen(), ROT, ACTIVE);
		
		setBlockBounds(new StateDependantBlockBounds(
			state->ROT.get(state).getIndex()/2,
			
			new AxisAlignedBB(0  ,p*5,0  ,1    ,1-p*5,1    ),
			new AxisAlignedBB(0  ,0  ,p*5,1    ,1    ,1-p*5),
			new AxisAlignedBB(p*5,0  ,0  ,1-p*5,1    ,1    )
		));
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
		return ROT.set(getDefaultState(), facing);
	}
	
	public static EnumFacing getRotation(IBlockState state){
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
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		TileEntityScreen tile=new BlockPosM(pos).getTile(world, TileEntityScreen.class);
		if(tile==null) return false;
		if(UtilM.isItemInStack(ItemMatterJumper.class, heldItem)){
			MatterJumperMode mode=ItemMatterJumper.getMode(heldItem);
			
			if(mode==MatterJumperMode.WRENCH){
				if(!tile.hasBrain()){
					tile.formMultiblock(pos);
					return true;
				}
			}
			
		}else if(tile.hasBrain()){
			TileEntityScreen brain=tile.getBrain();
			brain.onClick(calcScreenPos(tile, hitX, hitY, hitZ));
			
			return true;
		}
		
		return false;
	}
	
	public static Vec2FM calcScreenPos(TileEntityScreen tile, float hitX, float hitY, float hitZ){
		
		TileEntityScreen brain=tile.getBrain();
		int pixels=64;
		Vec2FM offset=new Vec2FM(brain.getPositions().get(tile.getMbId())).mulSelf(pixels, -pixels);
		switch(brain.getRotation()){
		case NORTH:{
			offset.y+=(1-hitY)*pixels;
			offset.x+=(1-hitX)*pixels;
		}
		break;
	
		default:
		break;
		}
		
		return offset;
	}
}
