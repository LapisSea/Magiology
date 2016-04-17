package com.magiology.core.init;

import com.magiology.core.MReference;
import com.magiology.mcobjects.blocks.HologramProjector;
import com.magiology.mcobjects.blocks.JSProgrammer;
import com.magiology.mcobjects.blocks.fire.FirePipe;
import com.magiology.mcobjects.blocks.network.NetworkCommandHolder;
import com.magiology.mcobjects.blocks.network.NetworkConductor;
import com.magiology.mcobjects.blocks.network.NetworkController;
import com.magiology.mcobjects.blocks.network.NetworkInterface;
import com.magiology.mcobjects.blocks.network.NetworkRouter;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MBlocks{

	//block references
	public static Block
		fireLamp,firePipe,
		fireMatrixTransferer,fireMatrixReceaver,hologramProjector,
		networkController,networkConductor,networkPointerContainer,networkInterface,networkCommandCenter,
		jSProgrammer;

	public static void blocksInit(){
		firePipe=init(new FirePipe().setBlockTextureName(MReference.MODID + ":" + "FirePipeIcon").setUnlocalizedName("FirePipe").setCreativeTab(MCreativeTabs.Whwmmt_power));
		hologramProjector=init(new HologramProjector().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("hologramProjector"));
		networkController=init(new NetworkController().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkController"));
		networkConductor=init(new NetworkConductor().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkConductor"));
		networkInterface=init(new NetworkInterface().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkInterface"));
		networkPointerContainer=init(new NetworkRouter().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkPointerContainer"));
		networkCommandCenter=init(new NetworkCommandHolder().setCreativeTab(MCreativeTabs.Whwmmt_core).setUnlocalizedName("networkCommandCenter"));
		jSProgrammer=init(new JSProgrammer().setCreativeTab(MCreativeTabs.Whwmmt_core));
	}

	static Block init(Block block){
		
		GameRegistry.registerBlock(block,block.getUnlocalizedName().substring("tile.".length(), block.getUnlocalizedName().length()));
		return block;
	}

	public static void preInit(){
		blocksInit();
	}
}
