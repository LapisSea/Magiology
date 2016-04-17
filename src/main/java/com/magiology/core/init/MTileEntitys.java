package com.magiology.core.init;

import com.magiology.client.render.tilerender.RenderBFCPowerOut;
import com.magiology.client.render.tilerender.RenderBateryL1;
import com.magiology.client.render.tilerender.RenderBateryL100;
import com.magiology.client.render.tilerender.RenderBateryL2;
import com.magiology.client.render.tilerender.RenderBateryL3;
import com.magiology.client.render.tilerender.RenderFireLamp;
import com.magiology.client.render.tilerender.RenderFireMatrixReceaver;
import com.magiology.client.render.tilerender.RenderFireMatrixTransferer;
import com.magiology.client.render.tilerender.RenderFirePipe;
import com.magiology.client.render.tilerender.RenderHologramProjector;
import com.magiology.client.render.tilerender.RenderMultiBox;
import com.magiology.client.render.tilerender.network.RenderNetworkConductor;
import com.magiology.client.render.tilerender.network.RenderNetworkController;
import com.magiology.client.render.tilerender.network.RenderNetworkInterface;
import com.magiology.client.render.tilerender.network.RenderNetworkRouter;
import com.magiology.mcobjects.tileentityes.TileEntityBFCPowerOut;
import com.magiology.mcobjects.tileentityes.TileEntityBigFurnaceCore;
import com.magiology.mcobjects.tileentityes.TileEntityControlBlock;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixReceaver;
import com.magiology.mcobjects.tileentityes.TileEntityFireMatrixTransferer;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL1;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL100;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL2;
import com.magiology.mcobjects.tileentityes.baterys.TileEntityBateryL3;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkConductor;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkController;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkInterface;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkRouter;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MTileEntitys{

	public static void bindTileWRender(Class<?extends TileEntity>tileEntityClass, TileEntitySpecialRenderer specialRenderer){
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass,specialRenderer);
	}
	public static void init(){
		register(TileEntityFireLamp.class);
		register(TileEntityFirePipe.class);
		register(TileEntityBigFurnaceCore.class);
		register(TileEntityBateryL1.class);
		register(TileEntityBateryL2.class);
		register(TileEntityBateryL3.class);
		register(TileEntityBateryL100.class);
		register(TileEntityBFCPowerOut.class);
		register(TileEntityControlBlock.class);
		register(TileEntityFireMatrixTransferer.class);
		register(TileEntityFireMatrixReceaver.class);
		register(TileEntityHologramProjector.class);
		register(TileEntityNetworkController.class);
		register(TileEntityNetworkConductor.class);
		register(TileEntityNetworkInterface.class);
		register(TileEntityNetworkRouter.class);
		register(TileEntityNetworkProgramHolder.class);
	}
	public static void initCustomRenderers(){
//		RenderFireLampISBRH a=new RenderFireLampISBRH(RenderingRegistry.getNextAvailableRenderId());
//		ISBRH.registerBlockRender(MBlocks.FireLamp, a);
//		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MBlocks.FireLamp),a);
		
	}
	@SideOnly(Side.CLIENT)
	public static void initRenders(){
		bindTileWRender(TileEntityFireLamp.class,			   new RenderFireLamp());
		bindTileWRender(TileEntityFirePipe.class,			   new RenderFirePipe());
		bindTileWRender(TileEntityBateryL1.class,			   new RenderBateryL1());
		bindTileWRender(TileEntityBateryL2.class,			   new RenderBateryL2());
		bindTileWRender(TileEntityBateryL3.class,			   new RenderBateryL3());
		bindTileWRender(TileEntityBateryL100.class,			 new RenderBateryL100());
		bindTileWRender(TileEntityBFCPowerOut.class,			new RenderBFCPowerOut());
		bindTileWRender(TileEntityFireMatrixTransferer.class,   new RenderFireMatrixTransferer());
		bindTileWRender(TileEntityFireMatrixReceaver.class,	 new RenderFireMatrixReceaver());
		bindTileWRender(TileEntityHologramProjector.class,	  new RenderHologramProjector());
		bindTileWRender(TileEntityNetworkConductor.class,	   new RenderNetworkConductor());
		bindTileWRender(TileEntityNetworkController.class,	  new RenderNetworkController());
		bindTileWRender(TileEntityNetworkInterface.class,	   new RenderNetworkInterface());
		bindTileWRender(TileEntityNetworkRouter.class,new RenderNetworkRouter());
		bindTileWRender(TileEntityNetworkProgramHolder.class,   new RenderMultiBox());
	}
	private static<T extends TileEntityM> void register(Class<T> clazz){
		String name=clazz.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(clazz, "TE"+name);
	}

}
