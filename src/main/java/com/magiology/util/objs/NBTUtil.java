package com.magiology.util.objs;

import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

public class NBTUtil{
	public static NBTTagCompound createNBT(ItemStack a){
		if(!a.hasTagCompound())a.setTagCompound(new NBTTagCompound());
		return getNBT(a);
	}
	public static boolean getBoolean(ItemStack stack, String key){
		return getNBT(stack).getBoolean(key);
	}
	public static byte getByte(ItemStack stack, String key){
		return getNBT(stack).getByte(key);
	}
	public static double getDouble(ItemStack stack, String key){
		return getNBT(stack).getDouble(key);
	}
	public static float getFloat(ItemStack stack, String key){
		return getNBT(stack).getFloat(key);
	}
	public static int getInt(ItemStack stack, String key){
		return hasNBT(stack)?getNBT(stack).getInteger(key):0;
	}
	public static long getLong(ItemStack stack, String key){
		return getNBT(stack).getLong(key);
	}
	public static NBTTagCompound getNBT(ItemStack stack){
		return stack.getTagCompound();
	}
	public static short getShort(ItemStack stack, String key){
		return getNBT(stack).getShort(key);
	}
	public static String getString(ItemStack stack, String key){
		return getNBT(stack).getString(key);
	}
	public static boolean hasKey(ItemStack stack, String key){
		return hasNBT(stack)&&getNBT(stack).hasKey(key);
	}
	public static boolean hasNBT(ItemStack stack){
		return stack!=null&&getNBT(stack)!=null;
	}
	public static ItemStack[] loadItemsFromNBT(NBTTagCompound NBTTC,String baseName,ItemStack[] stacks){
		int NumberOfSlots=stacks.length;
		NBTTagList list= NBTTC.getTagList(baseName+"Slots", 10);
		stacks=new ItemStack[NumberOfSlots];
		for(int i=0;i<list.tagCount();i++){
			NBTTagCompound item=list.getCompoundTagAt(i);
			byte b=item.getByte(baseName);
			if(b>=0&&b<stacks.length){
				stacks[b]=ItemStack.loadItemStackFromNBT(item);
			}
		}
		return stacks;
	}
	public static void removeTag(ItemStack stack, String key){
		if(hasNBT(stack))getNBT(stack).removeTag(key);
	}
	public static void saveItemsToNBT(NBTTagCompound NBTTC,String baseName,ItemStack[] stacks){
		NBTTagList list= new NBTTagList();
		for(int i=0;i<stacks.length;i++){
			if(stacks[i]!=null){
				NBTTagCompound item=new NBTTagCompound();
				item.setByte(baseName, (byte)i);
				stacks[i].writeToNBT(item);
				list.appendTag(item);
			}
		}
		NBTTC.setTag(baseName+"Slots", list);
	}
	public static void setBoolean(ItemStack stack, String key, boolean keyValue){
		createNBT(stack).setBoolean(key, keyValue);
	}
	public static void setByte(ItemStack stack, String key, byte keyValue){
		createNBT(stack).setByte(key, keyValue);
	}
	public static void setDouble(ItemStack stack, String key, double keyValue){
		createNBT(stack).setDouble(key, keyValue);
	}
	
	public static void setFloat(ItemStack stack, String key, float keyValue){
		createNBT(stack).setFloat(key, keyValue);
	}
	public static void setInt(ItemStack stack, String key, int keyValue){
		createNBT(stack).setInteger(key, keyValue);
	}
	public static void setLong(ItemStack stack, String key, long keyValue){
		createNBT(stack).setLong(key, keyValue);
	}
	public static void setShort(ItemStack stack, String key, short keyValue){
		createNBT(stack).setShort(key, keyValue);
	}
	public static void setString(ItemStack stack, String key, String keyValue){
		createNBT(stack).setString(key, keyValue);
	}
	public static void setUUID(ItemStack stack, String key, UUID keyValue){
		NBTTagCompound nbt=createNBT(stack),wrap=new NBTTagCompound();
		wrap.setLong("0", keyValue.getMostSignificantBits());
		wrap.setLong("1", keyValue.getLeastSignificantBits());
		nbt.setTag(key, wrap);
	}
	public static UUID getUUID(ItemStack stack, String key){
		NBTTagCompound nbt=createNBT(stack);
		if(!nbt.hasKey(key))return null;
		NBTTagCompound wrap=nbt.getCompoundTag(key);
		return new UUID(wrap.getLong("0"), wrap.getLong("1"));
	}
	
	public static boolean initUUID(ItemStack stack, String key, UUID keyValue){
		NBTTagCompound nbt=createNBT(stack);
		if(!nbt.hasKey(key)){
			setUUID(stack, key, keyValue);
			return true;
		}
		return false;
	}
	public static boolean initBoolean(ItemStack stack, String key, boolean keyValue){
		NBTTagCompound nbt=createNBT(stack);
		if(!nbt.hasKey(key)){
			setBoolean(stack, key, keyValue);
			return true;
		}
		return false;
	}
}
