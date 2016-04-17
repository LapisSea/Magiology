package com.magiology.mcobjects.blocks;

import com.magiology.core.init.MGui;
import com.magiology.core.init.MItems;
import com.magiology.handlers.GuiHandlerM;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.m_extension.BlockM;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JSProgrammer extends BlockM{

	public JSProgrammer(){
		super(Material.iron);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if(UtilM.isItemInStack(MItems.commandContainer, player.getHeldItemMainhand())){
			GuiHandlerM.openGui(player, MGui.JSProgramEditor, pos);
			return true;
		}
		return false;
	}
	
}
