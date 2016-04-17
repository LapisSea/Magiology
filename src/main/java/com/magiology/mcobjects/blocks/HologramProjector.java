package com.magiology.mcobjects.blocks;

import com.magiology.core.init.MGui;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.util.utilobjects.m_extension.BlockContainerM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HologramProjector extends BlockContainerM{
	private float p=1F/16F;

	public HologramProjector(){
		super(Material.iron);
		setBlockBounds(p, 0, p, 1-p, p*10, 1-p);
	}
	
	@Override
	public TileEntity createNewTileEntity(World w, int i){
		return new TileEntityHologramProjector();
	}
	@Override
	public int getRenderType(){
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube() {return false;}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		GuiHandlerM.openGui(player, MGui.HologramProjectorMainGui, pos);
		return true;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos){
		setBlockBounds(p, 0, p, 1-p, p*10, 1-p);
	}
	
	
	
}
