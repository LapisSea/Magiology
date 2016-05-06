package com.magiology.handlers;

import com.magiology.client.gui.container.ArmorContainer;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.client.gui.container.ContainerEmpty;
import com.magiology.client.gui.container.ISidedPowerInstructorContainer;
import com.magiology.client.gui.container.UpgradeContainer;
import com.magiology.client.gui.gui.GuiArmor;
import com.magiology.client.gui.gui.GuiCenterContainer;
import com.magiology.client.gui.gui.GuiHoloObjectEditor;
import com.magiology.client.gui.gui.GuiHologramProjectorMain;
import com.magiology.client.gui.gui.GuiISidedPowerInstructor;
import com.magiology.client.gui.gui.GuiJSProgramEditor;
import com.magiology.client.gui.gui.GuiUpgrade;
import com.magiology.core.Magiology;
import com.magiology.core.init.MGui;
import com.magiology.mcobjects.tileentityes.corecomponents.powertiles.TileEntityPow;
import com.magiology.mcobjects.tileentityes.hologram.TileEntityHologramProjector;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.RayTracer;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.vectors.Pos;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class GuiHandlerM implements IGuiHandler{
	
	public static void openGui(EntityPlayer player, int modGuiId, BlockPos pos){
		openGui(player, Magiology.getMagiology(), modGuiId, pos);
	}

	public static void openGui(EntityPlayer player, int modGuiId, int x, int y, int z){
		openGui(player, Magiology.getMagiology(), modGuiId, x,y,z);
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, BlockPos pos){
		openGui(player, mainModClassInstance, modGuiId, pos.getX(),pos.getY(),pos.getZ());
	}
	
	public static void openGui(EntityPlayer player, Object mainModClassInstance, int modGuiId, int x,int y,int z){
		if(U.isRemote(player))return;
		FMLNetworkHandler.openGui(player, mainModClassInstance, modGuiId, player.getEntityWorld(), x,y,z);
	}
	
	@Override public Object getClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetClientGuiElement(ID, player, world, x,y,z);}
	public GuiContainer GetClientGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x,y,z));
		RayTraceResult hit=RayTracer.rayTrace(player,4, 1);
		hit.sideHit.getIndex();
		
		switch (ID){
		case MGui.GuiUpgrade:
			if(tile instanceof TileEntityPow)		 
				return new GuiUpgrade(player.inventory, (TileEntityPow)tile);
		case MGui.GuiArmor:
			return new GuiArmor(player, player.inventory.armorInventory);
		case MGui.GuiISidedPowerInstructor:										  
			return new GuiISidedPowerInstructor(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
				return new GuiHoloObjectEditor(player, (TileEntityHologramProjector)tile,((TileEntityHologramProjector)tile).lastPartClicked);
		case MGui.HologramProjectorMainGui:
			if(tile instanceof TileEntityHologramProjector)
				return new GuiHologramProjectorMain(player, (TileEntityHologramProjector)tile);
		case MGui.CommandCenterGui:
			if(tile instanceof TileEntityNetworkProgramHolder)
				return new GuiCenterContainer(player, (TileEntityNetworkProgramHolder)tile);
		case MGui.JSProgramEditor:
			return GuiJSProgramEditor.New(player,new Pos(x,y,z));
		}
		PrintUtil.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}
	@Override public Object getServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){return GetServerGuiElement(ID, player, world, x,y,z);}
	public Container GetServerGuiElement(int ID, EntityPlayer player, World world,int x, int y, int z){
		TileEntity tile=world.getTileEntity(new Pos(x, y, z));
		switch (ID){
		case MGui.GuiUpgrade:			  if(tile instanceof TileEntityPow)		 
			return new UpgradeContainer(player.inventory, (TileEntityPow)tile);
		case MGui.GuiArmor:														  
			return new ArmorContainer(player, player.inventory.armorInventory);
		case MGui.GuiISidedPowerInstructor:										  
			return new ISidedPowerInstructorContainer(player, tile);
		case MGui.HologramProjectorObjectCustomGui:
			if(tile instanceof TileEntityHologramProjector&&((TileEntityHologramProjector)tile).lastPartClicked!=null)
			return new ContainerEmpty();
		case MGui.HologramProjectorMainGui:
			if(tile instanceof TileEntityHologramProjector)
				return new ContainerEmpty();
		case MGui.CommandCenterGui:
			if(tile instanceof TileEntityNetworkProgramHolder)
				return new CommandCenterContainer(player, (TileEntityNetworkProgramHolder)tile);
		case MGui.JSProgramEditor:
			return new ContainerEmpty();
		}
		PrintUtil.println("[WARNING] Gui on "+(world.isRemote?"client":"server")+"\tat X= "+x+"\tY= "+y+"\tZ= "+z+"\t has failed to open!");
		return null;
	}
}
