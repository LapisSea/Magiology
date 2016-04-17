package com.magiology.client.gui.gui;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.container.CommandCenterContainer;
import com.magiology.core.MReference;
import com.magiology.core.init.MItems;
import com.magiology.forgepowered.packets.packets.OpenProgramContainerInGui;
import com.magiology.mcobjects.tileentityes.network.TileEntityNetworkProgramHolder;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.NBTUtil;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCenterContainer extends GuiContainer implements Updateable{
	
	protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	ResourceLocation main= new ResourceLocation(MReference.MODID,"/textures/gui/GuiCenterContainer.png");
	
	public GuiCenterContainer(EntityPlayer player,TileEntityNetworkProgramHolder tile){
		super(new CommandCenterContainer(player,tile));
		this.xSize=176;
		this.ySize=166;
		
	}
	@Override
	protected void actionPerformed(GuiButton b){
		switch(b.id){
		case 0:{
			EntityPlayer player=UtilM.getThePlayer();
			if(player.openContainer instanceof CommandCenterContainer){
				CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
				int id=container.selectedSlotId+36;
				ItemStack stack=container.inventorySlots.get(id).getStack();
				if(stack!=null){
					UtilM.sendMessage(new OpenProgramContainerInGui(id));
					NBTUtil.createNBT(stack);
				}
			}
		}break;
		}
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float v1, int v2, int v3){
		TessUtil.bindTexture(main);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		buttonList.get(0).enabled=getSelectedSlotId()>=0;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int a,int b){
		if(getSelectedSlotId()>=0){
			TessUtil.bindTexture(widgetsTexPath);
			drawTexturedModalRect(getSelectedSlotId()%4*18+49, getSelectedSlotId()/4*18+3, 0, 22, 24, 24);
		}
	}
	
	private int getSelectedSlotId(){
		EntityPlayer player=UtilM.getThePlayer();
		if(player.openContainer instanceof CommandCenterContainer){
			CommandCenterContainer container=((CommandCenterContainer)player.openContainer);
			int id=container.selectedSlotId;
			if(!UtilM.isItemInStack(MItems.commandContainer, container.inventorySlots.get(id+36).getStack()))return -1;
			return id;
		}
		return -1;
	}
	
	@Override
	public void initGui(){
		super.initGui();
		buttonList.add(new GuiButton(0, guiLeft+11, guiTop+32, 34, 20, "Open"));
	}
	
	@Override
	public void update(){
		
		
		
	}
}