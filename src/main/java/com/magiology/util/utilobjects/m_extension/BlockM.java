package com.magiology.util.utilobjects.m_extension;

import java.util.HashMap;
import java.util.Map;

import com.magiology.core.MReference;
import com.magiology.util.utilclasses.Get;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockM extends Block{
	public static final float p=1F/16F;

	//#fucka youuu json models!
	public static Map<Block, ResourceLocation> modelsInit=new HashMap<Block, ResourceLocation>();
	
	public static void registerBlockModels(){
		for(Block block:modelsInit.keySet()){ 
			Item itemBlock=Item.getItemFromBlock(block);
			try{
				Get.Render.RI().getItemModelMesher().register(itemBlock, 0, new ModelResourceLocation(modelsInit.get(block), "inventory"));
			} catch (Exception e){
				PrintUtil.println("failed!",block,itemBlock,Get.Render.RI());
				e.printStackTrace();
				UtilM.exit(404);
			}
		}
//		modelsInit.clear();
	}
	
	protected AxisAlignedBB boundingBox=new AxisAlignedBB(p*6, p*6, p*6, p*10, p*10, p*10);
	
	public void setBlockBounds(double x1, double y1, double z1, double x2, double y2, double z2){
		setBlockBounds(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
	}
	public void setBlockBounds(AxisAlignedBB box){
		this.boundingBox=box;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boundingBox;
	}
	
	
	public BlockM(Material material){
		super(material);
	}
	public BlockM setBlockTextureName(){
		return setBlockTextureName(MReference.MODID+":"+getUnlocalizedName().substring(5));
	}
	
	public BlockM setBlockTextureName(String name){
		modelsInit.put(this, new ResourceLocation(name));
		return this;
	}
	
	@Override
	public BlockM setCreativeTab(CreativeTabs tab){
		return (BlockM)super.setCreativeTab(tab);
	}
}
