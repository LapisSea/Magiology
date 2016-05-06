package com.magiology.core.init;

import com.magiology.client.render.tilerender.RenderFireLamp;
import com.magiology.client.render.tilerender.RenderFirePipe;
import com.magiology.client.render.tilerender.RenderHologramProjector;
import com.magiology.client.render.tilerender.RenderMultiBox;
import com.magiology.client.render.tilerender.network.RenderNetworkConductor;
import com.magiology.client.render.tilerender.network.RenderNetworkController;
import com.magiology.client.render.tilerender.network.RenderNetworkInterface;
import com.magiology.client.render.tilerender.network.RenderNetworkRouter;
import com.magiology.mcobjects.tileentityes.TileEntityFireLamp;
import com.magiology.mcobjects.tileentityes.TileEntityFirePipe;
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

	public static void init(){
		register(TileEntityFireLamp.class);
		register(TileEntityFirePipe.class);
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
		bindTileWithRenderer(TileEntityFireLamp.class,			   new RenderFireLamp());
		bindTileWithRenderer(TileEntityFirePipe.class,			   new RenderFirePipe());
		bindTileWithRenderer(TileEntityHologramProjector.class,	  new RenderHologramProjector());
		bindTileWithRenderer(TileEntityNetworkConductor.class,	   new RenderNetworkConductor());
		bindTileWithRenderer(TileEntityNetworkController.class,	  new RenderNetworkController());
		bindTileWithRenderer(TileEntityNetworkInterface.class,	   new RenderNetworkInterface());
		bindTileWithRenderer(TileEntityNetworkRouter.class,new RenderNetworkRouter());
		bindTileWithRenderer(TileEntityNetworkProgramHolder.class,   new RenderMultiBox());
	}
	private static<T extends TileEntityM> void register(Class<T> clazz){
		String name=clazz.getSimpleName().substring("TileEntity".length());
		GameRegistry.registerTileEntity(clazz, "TE"+name);
	}
	public static void bindTileWithRenderer(Class<?extends TileEntity>tileEntityClass, TileEntitySpecialRenderer specialRenderer){
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass,specialRenderer);
	}

}
