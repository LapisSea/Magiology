package com.magiology.util.m_extensions;

import java.util.List;

import com.magiology.mc_objects.BlockStates.BlockStateParser;
import com.magiology.mc_objects.BlockStates.IPropertyM;
import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.objs.PairM;
import com.magiology.util.objs.block_bounds.BasicBlockBounds;
import com.magiology.util.objs.block_bounds.IBlockBounds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 
 * Reimagination of extremely efficient handling of: IProperty sterilization, bounding boxes, full block checking
 * 
 * @author LapisSea
 *
 */
public abstract class BlockM extends Block{
	
	public static final float				p				=1F/16F;
	private static IPropertyM[]				properties		=null;
	private static final BasicBlockBounds	FULL_CUBE_BOUNDS=new BasicBlockBounds();
	
	//======================================FAST_INTERFACES======================================\\
	private static interface StateSaver{
		
		IBlockState getStateFromMeta(BlockM instance, int meta);
		
		int getMetaFromState(BlockM instance, IBlockState state);
	}
	
	private static final StateSaver save=new StateSaver(){
		
		@Override
		public IBlockState getStateFromMeta(BlockM instance, int meta){
			return instance.parser.parseBits(instance.getDefaultState(), meta);
		}
		
		@Override
		public int getMetaFromState(BlockM instance, IBlockState state){
			return instance.parser.parseValues(state);
		}
	},
			noSave=new StateSaver(){
				
				@Override
				public IBlockState getStateFromMeta(BlockM instance, int meta){
					return instance.getDefaultState();
				}
				
				@Override
				public int getMetaFromState(BlockM instance, IBlockState state){
					return 0;
				}
			};
	//===========================================================================================\\
	
	private IBlockBounds					boundingBox	=FULL_CUBE_BOUNDS;
	protected PairM<AxisAlignedBB, Boolean>	boxes[];
	private final BlockStateParser			parser;
	private final StateSaver				saver;
	
	public BlockM(Material material, IPropertyM...properties){
		super(((ObjectReturn<Material>)()->{
			BlockM.properties=properties;
			return material;
		}).process());
		if(properties!=null&&properties.length>0) parser=new BlockStateParser(properties);
		else parser=null;
		saver=parser!=null?save:noSave;
		BlockM.properties=null;
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, properties);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return saver.getStateFromMeta(this, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return saver.getMetaFromState(this, state);
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end){
		return boundingBox.collisionRayTrace(blockState, worldIn, pos, start, end);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity){
		boundingBox.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boundingBox.getBoundingBox(state, source, pos);
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return boundingBox.isFullCube(state);
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
	
	public Item toItem(){
		return Item.getItemFromBlock(this);
	}
	
	public void setBlockBounds(IBlockBounds boundingBox){
		this.boundingBox=boundingBox;
	}
	
	public IBlockBounds getBoundingBox(){
		return boundingBox;
	}
}
