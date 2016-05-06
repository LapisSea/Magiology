package com.magiology.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import com.magiology.client.gui.GuiUpdater.Updateable;
import com.magiology.client.gui.guiutil.container.FakeContainer;
import com.magiology.client.gui.guiutil.container.OnlyShiftClickSlot;
import com.magiology.client.gui.guiutil.container.UpgItemContainer;
import com.magiology.util.utilclasses.PrintUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class ArmorContainer extends Container implements Updateable{
	
	FakeContainer armorSlots;
	boolean firstUpdate=false;
	public UpgItemContainer[] InventoryArmorContainers=new UpgItemContainer[4];
	public List[] InventorySlots = {new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
	public List inventorySlotsAllTheTime = new ArrayList();
	int lastPos=-100;
	ItemStack[] p42;
	InventoryPlayer pInventory;
	EntityPlayer player;
	
	public double sliderPos,sliderSpeed,sliderVantedPos;
	public int sliderPosId=0,lastSliderPosId=0;
	
	public ArmorContainer(EntityPlayer player,ItemStack[] armorInventory){
		this.player=player;
		pInventory=player.inventory;
		p42=armorInventory;
		armorSlots=new FakeContainer(p42, 64);
		for(int i=0;i<3;i++){for(int j=0;j<9;j++){this.addSlotToList(new Slot(pInventory,9+j+9*i,8+j*18,84+18*i),inventorySlotsAllTheTime);}}
		for(int i=0;i<9;i++)this.addSlotToList(new Slot(pInventory,i,8+i*18,142),inventorySlotsAllTheTime);
		for(int i=0;i<armorSlots.getSizeInventory();i++)this.addSlotToList(new OnlyShiftClickSlot(armorSlots,i,11,55-i*16),inventorySlotsAllTheTime);
		//adding stuff to InventorySlots for armor
		for(int a=0;a<armorInventory.length;a++){
//			Item item=armorInventory[a]!=null?armorInventory[a].getItem():null;
//			ItemStack stackMain=armorInventory[a];
//			if(item!=null&&item instanceof UpgItem){
//				UpgItem upgItem=(UpgItem)item;
//				int numberOfSlots=upgItem.getInventorySize();
//				InventoryArmorContainers[a]=new UpgItemContainer(stackMain);
//				// this is a not so necessary steep to automatically set slider to a valid item
//				{
//					sliderPos=-a*16;
//					sliderVantedPos=sliderPos;
//				}
//				for(int b=0;b<numberOfSlots;b++){
//					int overrideNumberOfSlots=numberOfSlots;
//					if(overrideNumberOfSlots>4)overrideNumberOfSlots=4;
//					if(b<=3)this.addSlotToList(new UpgItemSlot(InventoryArmorContainers[a],b,132+b*18-(overrideNumberOfSlots*9), 21-(numberOfSlots>overrideNumberOfSlots?9:0)),InventorySlots[a]);
//					if(b>=4&&numberOfSlots>overrideNumberOfSlots){
//						this.addSlotToList(new UpgItemSlot(InventoryArmorContainers[a],b, 123, 30),InventorySlots[a]);
//					}
//				}
//			}
		}
		reformatSlots(0);
		refreshStacksList();
		
		while(inventorySlots.size()<40+Math.max(InventorySlots[0].size(), Math.max(InventorySlots[1].size(), Math.max(InventorySlots[2].size(), InventorySlots[3].size())))){
			inventoryItemStacks.add((ItemStack)null);
			inventorySlots.add(new OnlyShiftClickSlot(pInventory, 0, -1000, -1000));
		}
		detectAndSendChanges();
	}
	protected Slot addSlotToList(Slot slot,List list){
		slot.slotNumber=list.size();
		list.add(slot);
		return slot;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player){super.onContainerClosed(player);}
	
	@Override
	public void putStackInSlot(int id, ItemStack stack){
		refreshSlotLists();
		this.getSlot(id).putStack(stack);
		refreshSlotLists();
	}
	
	public void reformatSlots(int item){
		inventorySlots.clear();
		if(!inventorySlotsAllTheTime.isEmpty())for(Object slot:inventorySlotsAllTheTime){
			if(slot!=null)addSlotToList((Slot)slot,inventorySlots);
			else PrintUtil.printlnEr("Slot is null! On container "+item+" This is an error!");
		}
		if(InventoryArmorContainers[item]!=null)for(Object slot:InventorySlots[item]){
			if(slot!=null)addSlotToList((Slot)slot,inventorySlots);
			else PrintUtil.printlnEr("Slot is null! On container "+item+" This is an error!");
		}
	}
	public void refreshSlotLists(){
		reformatSlots(sliderPosId);
		refreshStacksList();
		detectAndSendChanges();
	}
	public void refreshStacksList(){
		inventoryItemStacks.clear();
		for(int a=0;a<inventorySlots.size();a++){
			Slot slot=inventorySlots.get(a);
			inventoryItemStacks.add((slot)==null?(ItemStack)null:slot.getStack());
		}
	}
	
	@Override
	public ItemStack func_184996_a(int slotid, int x, ClickType y, EntityPlayer player){
		PrintUtil.println(slotid);
		int id=slotid-36;
		if(id>=0&&id<=3)sliderVantedPos=-id*16;
		while(inventorySlots.size()>inventoryItemStacks.size()){
			inventoryItemStacks.add((ItemStack)null);
		}
		refreshSlotLists();
		
		if(slotid>=0&&slotid<inventoryItemStacks.size()&&inventoryItemStacks.size()==inventorySlots.size()){
			Slot slot =this.inventorySlots.get(slotid);
			
//			UtilM.println("Side: "+(player.worldObj.isRemote?"CLIENT":"SERVER")+", clicked slot id: "+slotid+", "+(slot!=null?("slot number: "+slot.slotNumber+", Stack in slot: "+(slot.getStack()!=null?slot.getStack().getDisplayName():"null")+", Inventory name of the slot: "+slot.inventory.getInventoryName()):"slot is null")+"\n");
//			if(!player.worldObj.isRemote)UtilM.println("\n");
			if(slot instanceof OnlyShiftClickSlot)return null;
			ItemStack result=super.func_184996_a(slotid, x, y, player);
			
			refreshSlotLists();
			
			return result;
		}
		
		return null;
	}
	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID){
		
		int player1Start=0,player1EndW2Start=27,player2EndWArmorStart=36,armorEndWOptionalInvStart=40,optionalInvEnd=inventorySlots.size();
		
		ItemStack itemstack=null;
		{
			Slot slot = this.inventorySlots.get(slotID);
			if(slot!=null&&slot.getHasStack()){
				ItemStack itemstack1=slot.getStack();
				itemstack=itemstack1.copy();
				if(slotID>=armorEndWOptionalInvStart){
					if(!mergeItemStack(itemstack1, player1EndW2Start, player2EndWArmorStart, false))if(!mergeItemStack(itemstack1, player1Start, player1EndW2Start, false))return null;
					slot.onSlotChange(itemstack1, itemstack);
				}else if(slotID<armorEndWOptionalInvStart){
					boolean isAviable=false;
					{//fix if isItemValid()-----
//						ItemStack container1=this.p42[sliderPosId];
//						UpgItem item1=null;
//						if(container1!=null&&container1.getItem() instanceof UpgItem)item1=((UpgItem)container1.getItem());
//						//if selected item by slider is UpgItem than run this code
//						if(item1!=null){
//							int posId=0;
//							ItemStack[] inv=item1.getStacks(container1);
//							for(int i=0;i<item1.getInventorySize();i++)if(inv[i]==null){posId=i;i=item1.getInventorySize();}
//							//ok now the slot is/isn't found and we are double checking for null so we prevent errors if none of the stacks are null
//							if(inv[posId]==null){
//								if(InventoryArmorContainers[sliderPosId]!=null&&((UpgItemSlot)inventorySlots.get(posId+40)).isItemValid(itemstack.copy()))isAviable=true;
//							}
//						}
					}//-------------------------
					if(isAviable?!mergeItemStack(itemstack1, armorEndWOptionalInvStart, optionalInvEnd, false):true){
						if(slotID>=player1EndW2Start&&slotID<=player2EndWArmorStart){
						if(!mergeItemStack(itemstack1, player1Start, player1EndW2Start, false))return null;}
						else if(!mergeItemStack(itemstack1, player1EndW2Start, player2EndWArmorStart, false))return null;
						if(itemstack1.stackSize==0)slot.putStack((ItemStack)null);
						else slot.onSlotChanged();
						return null;
					}
					slot.onSlotChange(itemstack1, itemstack);
				}
				slot.onSlotChange(itemstack1, itemstack);
				if(itemstack1.stackSize==0)slot.putStack((ItemStack)null);
				else					   slot.onSlotChanged();
				if(itemstack1.stackSize==itemstack.stackSize)return null;
				slot.onPickupFromSlot(player, itemstack1);
			}
		}
		
		return itemstack;
		
	}
	
	@Override
	public void update(){
		for(int a=0;a<3;a++){
			if(sliderPos>sliderVantedPos)sliderSpeed-=0.1;
			else sliderSpeed+=0.1;
			sliderSpeed*=0.9+(a<2?0:0.05);
			sliderPos+=sliderSpeed;
		}
		sliderPos=Math.round(sliderPos*100);sliderPos/=100;
		lastSliderPosId=sliderPosId;
		sliderPosId=(int)Math.round(Math.abs(sliderPos/16));
		
		if(!firstUpdate){
			firstUpdate=true;
			refreshSlotLists();
		}
		if(sliderPosId!=lastSliderPosId)refreshSlotLists();
	}
	
	
}
