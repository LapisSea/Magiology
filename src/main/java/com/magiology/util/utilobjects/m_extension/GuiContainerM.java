package com.magiology.util.utilobjects.m_extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiContainerM extends GuiContainer{
	
	protected boolean superClicked=false;
	public List<GuiTextField> textFieldList=new ArrayList<GuiTextField>();
	public GuiContainerM(Container container){
		super(container);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
		for(GuiTextField textField:textFieldList)textField.drawTextBox();
	}
	
	@Override
	public void initGui(){
		textFieldList.clear();
		super.initGui();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		boolean next=true;
		for(GuiTextField textField:textFieldList){
			if(next)next=!textField.textboxKeyTyped(typedChar, keyCode);
			else break;
		}
		if(next)super.keyTyped(typedChar, keyCode);
		superClicked=next;
	}
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)throws IOException{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(GuiTextField textField:textFieldList)textField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	@Override
	public void onGuiClosed(){
		textFieldList.clear();
		super.onGuiClosed();
	}
	@Override
	public void updateScreen(){
		for(GuiTextField textField:textFieldList)textField.updateCursorCounter();
	}
}
