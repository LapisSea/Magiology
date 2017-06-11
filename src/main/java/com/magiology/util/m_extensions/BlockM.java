package com.magiology.util.m_extensions;

import com.magiology.util.interf.ObjectProcessorDouble;
import com.magiology.util.objs.BlockStates.BlockStateParser;
import com.magiology.util.objs.BlockStates.IPropertyM;
import com.magiology.util.objs.block_bounds.BasicBlockBounds;
import com.magiology.util.objs.block_bounds.IBlockBounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 *
 * Reimagination of extremely efficient handling of: IProperty sterilization, bounding boxes, full block checking
 *
 * @author LapisSea
 *
 */
public abstract class BlockM extends Block{
	
	public static final  float            p                =1F/16F;
	private static final BasicBlockBounds FULL_CUBE_BOUNDS =new BasicBlockBounds();
	private static       IPropertyM[]     STATIC_PROPERTIES=null;
	
	/**
	 * A syntax hack that stores IPropertyM[] in STATIC_PROPERTIES in order to use them in construction of BlockStateContainer.<br>
	 * Should not be used anywhere else. 
	 */
	private static final ObjectProcessorDouble<Material,IPropertyM[]> SET_STATIC_PROPERTIES=(material, properties)->{
		BlockM.STATIC_PROPERTIES=properties;
		return material;
	};
	
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
	}, noSave                           =new StateSaver(){
		
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
	
	private IBlockBounds blockBounds=FULL_CUBE_BOUNDS;
	private final BlockStateParser parser;
	private final StateSaver       saver;
	
	public BlockM(Material material, IPropertyM... properties){
		super(SET_STATIC_PROPERTIES.process(material, properties));
		
		if(properties!=null&&properties.length>0){
			parser=new BlockStateParser(properties);
			saver=save;
		}else{
			parser=null;
			saver=noSave;
		}
	}
	
	@Override
	protected BlockStateContainer createBlockState(){
		BlockStateContainer stateContainer=new BlockStateContainer(this, STATIC_PROPERTIES);
		STATIC_PROPERTIES=null;
		return stateContainer;
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
		return blockBounds.collisionRayTrace(blockState, worldIn, pos, start, end);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean flag){
		blockBounds.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds.getBoundingBox(state, source, pos);
	}
	
	@Override
	public boolean isFullCube(IBlockState state){
		return blockBounds.isFullCube(state);
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
	
	public Item toItem(){
		return Item.getItemFromBlock(this);
	}
	
	public void setBlockBounds(IBlockBounds blockBounds){
		this.blockBounds=blockBounds;
	}
	
	public IBlockBounds getBlockBounds(){
		return blockBounds;
	}
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ){}
}
