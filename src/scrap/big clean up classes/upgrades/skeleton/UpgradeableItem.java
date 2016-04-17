package com.magiology.mcobjects.items.upgrades.skeleton;

import static com.magiology.util.utilclasses.FontEffectUtil.*;

import java.util.List;

import com.magiology.mcobjects.items.GenericItemUpgrade;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades;
import com.magiology.mcobjects.items.upgrades.RegisterItemUpgrades.Container;
import com.magiology.util.utilclasses.UtilM;
import com.magiology.util.utilobjects.NBTUtil;
import com.magiology.util.utilobjects.m_extension.ItemM;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class UpgradeableItem extends ItemM implements UpgItem{
	public static String slotNBT="thatSlot";
	public Container container=null;
	public int NumberOfContainerSlots=0;
//	public ItemStack[] containerItems=null;
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player){
		NBTUtil.createNBT(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,List list, boolean par4){
		if(itemStack.getTagCompound()!=null){
			if(GuiScreen.isShiftKeyDown()){
ItemStack[] stacks=this.getStacks(itemStack);
				for(int b=0;b<stacks.length;b++){
					ItemStack a=stacks[b];
					list.add((a!=null?GREEN:RED+""+UNDERLINE)+""+BOLD+""+(1+b)+(a!=null?"":UNDERLINE)+"."+RESET+" "+YELLOW+(a!=null?"":UNDERLINE)+"Slot:"+RESET+" "+AQUA+(a!=null?a.getDisplayName():RED+"empty"));
				}
			}else list.add(AQUA+""+UNDERLINE+"<<Press SHIFT>>");
		}else list.add("<No NBT on stack>");
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5){
		NBTUtil.createNBT(itemStack);
	}
	@Override
	public void initUpgrade(Container containe){
		container=containe;
		NumberOfContainerSlots=container.getNumberOfTypes();
	}

	@Override
	public Container getContainer(){
		return this.container;
	}
	@Override
	public ItemStack[] getStacks(ItemStack itemStack){
		if(!(itemStack.getItem()instanceof UpgItem)||!itemStack.hasTagCompound())return null;
		ItemStack[] result=new ItemStack[getInventorySize()];
		NBTTagCompound nbt=itemStack.getTagCompound();
		result=UtilM.loadItemsFromNBT(nbt, slotNBT, result);
		return result;
	}
	@Override
	public void setStacks(ItemStack itemStack,ItemStack[] itemStacks){
		UtilM.saveItemsToNBT(itemStack.getTagCompound(), slotNBT, itemStacks);
	}
	@Override
	public int getInventorySize(){
		return this.container.getNumberOfTypes();
	}
	//I didn't made for now this because for that I have to write a lot of Helper code and I don't know if they are necessary. :(
	@Deprecated
	@Override
	public ItemStack getStack(ItemStack itemStack,int id){
//		return Helper.loadItemsFromNBT(itemStack.getTagCompound(), slotNBT, new ItemStack[1])[0];
		return null;
	}
	@Deprecated
	@Override
	public boolean setStack(ItemStack itemStack, int id){
		return false;
	}

	@Override
	public int hasUpgrade(ItemStack itemStack, Item item){
		if(itemStack==null)return -1;
		if(itemStack.getItem() instanceof UpgItem&&item instanceof GenericItemUpgrade){
			UpgItem upgContainer=((UpgItem)itemStack.getItem());
			ItemStack[] stacks=upgContainer.getStacks(itemStack);
			for(ItemStack a:stacks){
				if(a!=null){
					Item theItem=a.getItem();
					if(RegisterItemUpgrades.isItemUpgrade(theItem)&&RegisterItemUpgrades.getItemUpgradeID(theItem)==RegisterItemUpgrades.getItemUpgradeID(item)){
						return RegisterItemUpgrades.getItemUpgradeLevel(theItem);
					}
				}
			}
		}
		return -1;
	}
}
