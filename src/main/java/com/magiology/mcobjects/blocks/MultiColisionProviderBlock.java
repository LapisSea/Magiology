package com.magiology.mcobjects.blocks;

import java.util.List;

import com.magiology.mcobjects.tileentityes.corecomponents.UpdateableTile.UpdateablePipeHandler;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.CollisionBox;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProvider;
import com.magiology.mcobjects.tileentityes.corecomponents.multibox.MultiColisionProviderHandler;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.m_extension.BlockContainerM;
import com.magiology.util.utilobjects.vectors.Vec3M;
import com.magiology.util.utilobjects.vectors.physics.real.GeometryUtil;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class MultiColisionProviderBlock extends BlockContainerM{

	protected MultiColisionProviderBlock(Material material){
		super(material);
		if(!(createNewTileEntity(null, 0) instanceof MultiColisionProvider))throw new IllegalStateException("BlockContainerMultiColision has to be provided with a TileEntity that implements MultiColisionProvider class!");
		setUnlocalizedName(getClass().getSimpleName());
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity){
		for(CollisionBox box:MultiColisionProviderHandler.getBoxes((MultiColisionProvider)world.getTileEntity(pos))){
			setBlockBounds((float)box.box.minX,(float)box.box.minY,(float)box.box.minZ,(float)box.box.maxX,(float)box.box.maxY,(float)box.box.maxZ);
			super.addCollisionBoxToList(state,world,pos,mask,list,collidingEntity);
		}
	}
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World w, BlockPos pos, Vec3d startVec, Vec3d endVec){
		return MultiColisionProviderHandler.handleRayTracing((TileEntity&MultiColisionProvider)w.getTileEntity(pos), startVec, endVec);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.INVISIBLE;
	}
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side,float hitX,float hitY,float hitZ){
		DoubleObject<Vec3d,Vec3d> look=GeometryUtil.getStartEndLook(player);
		collisionRayTrace(state, world,pos,look.obj1,look.obj2);
		MultiColisionProvider tile=(MultiColisionProvider)world.getTileEntity(pos);
		Vec3M hit=new Vec3M(hitX,hitY,hitZ);
		if(tile.onNormalHit(player, player.getHeldItemMainhand(), true, tile.getPointedBoxID(), side, hit))return true;
		
		boolean ghost=false;
		EnumFacing side2=side.getOpposite();
		for(int id:tile.getGhostHits()){
			if(tile.onGhostHit(player, player.getHeldItemMainhand(), true, id, side2, hit)){
				ghost=true;
				break;
			}
		}
		
		return ghost;
	}
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		UpdateablePipeHandler.updatein3by3(world,pos);
		UpdateablePipeHandler.updatePipe(world,pos);
		UpdateablePipeHandler.updatein3by3(world,pos);
		UpdateablePipeHandler.updatePipe(world,pos);
	}
	
	@Override
	public void onBlockClicked(World world,BlockPos pos,EntityPlayer player){
		DoubleObject<Vec3d,Vec3d> look=GeometryUtil.getStartEndLook(player);
		RayTraceResult hitObj=collisionRayTrace(world.getBlockState(pos), world,pos,look.obj1,look.obj2);
		EnumFacing side=hitObj.sideHit;
		Vec3M hit=Vec3M.conv(hitObj.hitVec).sub(pos);
				
		MultiColisionProvider tile=(MultiColisionProvider)world.getTileEntity(pos);
		if(tile.onNormalHit(player, player.getHeldItemMainhand(), false, tile.getPointedBoxID(), side, hit))return;
		
		EnumFacing side2=side.getOpposite();
		for(int id:tile.getGhostHits()){
			if(tile.onGhostHit(player, player.getHeldItemMainhand(), false, id, side2, hit))break;
		}
	}
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		UpdateablePipeHandler.updatein3by3((World)world,pos);
		UpdateablePipeHandler.updatePipe((World)world,pos);
		UpdateablePipeHandler.updatein3by3((World)world,pos);
		UpdateablePipeHandler.updatePipe((World)world,pos);
	}
	public RayTraceResult superCollisionRayTrace(World w, BlockPos pos, Vec3d startVec, Vec3d endVec){
		return super.collisionRayTrace(w.getBlockState(pos), w, pos, startVec, endVec);
	}
}
