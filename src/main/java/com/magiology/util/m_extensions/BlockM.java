package com.magiology.util.m_extensions;

import com.magiology.mc_objects.BlockStates.*;
import com.magiology.util.interf.ObjectReturn;
import com.magiology.util.objs.PairM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockAccess;

/**
 * 
 * Reimagination of extremely efficient handling of: IProperty sterilization, bounding boxes, full block checking
 * 
 * @author LapisSea
 *
 */
public abstract class BlockM extends Block{
	
	public static final float p=1F/16F;
	
	private static IPropertyM[] properties;
	private static StateSaver save=new StateSaver(),noSave=new StateNotSaver();
	private static BoundsGetter singleBox=new BoundsGetter(),multiBox=new BoundsGetterMulti();
	
	protected AxisAlignedBB boundingBox=new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	protected PairM<AxisAlignedBB,Boolean> boxes[];
	
	private final BlockStateParser parser;
	private final StateSaver saver;
	private BoundsGetter boxGetter=singleBox;
	
	protected static class StateSaver{
		private StateSaver(){}
		
		protected IBlockState getStateFromMeta(BlockM instance, int meta){
			return instance.parser.parseBits(instance.getDefaultState(),meta);
		}
		protected int getMetaFromState(BlockM instance, IBlockState state){
			return instance.parser.parseValues(state);
		}
	}
	protected final static class StateNotSaver extends StateSaver{
		@Override
		protected IBlockState getStateFromMeta(BlockM instance,int meta){
			return instance.getDefaultState();
		}
		@Override
		protected int getMetaFromState(BlockM instance,IBlockState state){
			return 0;
		}
	}
	protected static class BoundsGetter{
		private BoundsGetter(){}
		
		protected AxisAlignedBB getBoundingBox(BlockM instance,IBlockState state, IBlockAccess source, BlockPos pos){
			return instance.boundingBox;
		}
	}
	protected static class BoundsGetterMulti extends BoundsGetter{
		private BoundsGetterMulti(){}
		
		@Override
		protected AxisAlignedBB getBoundingBox(BlockM instance,IBlockState state, IBlockAccess source, BlockPos pos){
			PairM<AxisAlignedBB,Boolean> box=instance.boxes[instance.chooseBox(state)];
			instance.fullBlock=box.obj2;
			return box.obj1;
		}
	}
	
	public BlockM(Material material,IPropertyM...properties){
		super(((ObjectReturn<Material>)()->{
			BlockM.properties=properties;
			return material;
		}).process());
		if(properties!=null&&properties.length>0)parser=new BlockStateParser(properties);
		else parser=null;
		saver=parser!=null?save:noSave;
		BlockM.properties=null;
	}
	
	
	@Override
	protected BlockStateContainer createBlockState(){
		return new BlockStateContainer(this, properties);
	}
	
	
	public void setBlockBounds(double x1, double y1, double z1, double x2, double y2, double z2){
		setBlockBounds(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		return saver.getStateFromMeta(this,meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state){
		return saver.getMetaFromState(this,state);
	}
	
	
	public void setBlockBounds(AxisAlignedBB box){
		if(this.boxes!=null)throw new IllegalArgumentException("Single box is disabled!");
		this.boundingBox=box;
		this.fullBlock=
				box.minX==0&&box.maxX==1&&
				box.minY==0&&box.maxY==1&&
				box.minZ==0&&box.maxZ==1;
	}
	public void setPossibleBlockBounds(AxisAlignedBB...boxes){
		if(boxes==null)throw new IllegalArgumentException("Boxes canot be null!");
		if(boxes.length==0)throw new IllegalArgumentException("Boxes canot be empty!");
		if(this.boxes!=null)throw new IllegalArgumentException("Boxes are already set!");
		
		this.boxes=new PairM[boxes.length];
		for(int i=0;i<boxes.length;i++){
			AxisAlignedBB box=boxes[i];
			this.boxes[i]=new PairM<>(box,
					box.minX==0&&box.maxX==1&&
					box.minY==0&&box.maxY==1&&
					box.minZ==0&&box.maxZ==1);
		}
		boxGetter=multiBox;
		this.boundingBox=null;
	}
	
	protected int chooseBox(IBlockState state){
		throw new IllegalStateException("Function chooseBox is not overriden in: "+this.getClass().getName()+" and boxes are set!");
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boxGetter.getBoundingBox(this,state,source,pos);
	}
	@Override
	public boolean isFullCube(IBlockState state){
		return this.fullBlock;
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
	
}
