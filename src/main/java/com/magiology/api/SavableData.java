package com.magiology.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface SavableData{
	
	public static class SavableDataHandler{
		public static SavableData[] loadDataFromNBT(NBTTagCompound NBT,String baseName){
			NBTTagList list=NBT.getTagList(baseName+"Data", 10);
			int size=list.tagCount();
			SavableData[] data=new SavableData[size];
			for(int i=0;i<size;i++){
				NBTTagCompound savable=list.getCompoundTagAt(i);
				byte b=savable.getByte(baseName);
				if(b>=0&&b<data.length){
					data[b]=loadFromNBT(savable);
				}
			}
			return data;
		}
		public static List<SavableData> loadDataFromNBT(NBTTagCompound NBT,String baseName,Object... dummy){
			SavableData[] array=loadDataFromNBT(NBT, baseName);
			List<SavableData> result=new ArrayList<SavableData>();
			for(SavableData i:array)if(i!=null)result.add(i);
			return result;
		}
		public static SavableData loadFromNBT(NBTTagCompound NBT){
			SavableData result=null;
			try{
				result=(SavableData)Class.forName(NBT.getString("className")).newInstance();
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
			List<Integer> integers=new ArrayList<Integer>();
			List<Boolean> booleans=new ArrayList<Boolean>();
			List<Byte> bytes___=new ArrayList<Byte>();
			List<Long> longs___=new ArrayList<Long>();
			List<Double> doubles_=new ArrayList<Double>();
			List<Float> floats__=new ArrayList<Float>();
			List<String> strings_=new ArrayList<String>();
			List<Short> shorts__=new ArrayList<Short>();
			int intS=NBT.getInteger("intS");
			int bolS=NBT.getInteger("bolS");
			int bytS=NBT.getInteger("bytS");
			int lonS=NBT.getInteger("lonS");
			int douS=NBT.getInteger("douS");
			int floS=NBT.getInteger("floS");
			int strS=NBT.getInteger("strS");
			int shoS=NBT.getInteger("shoS");
			for(int i=0;i<intS;i++)integers.add(NBT.getInteger("int"+i));
			for(int i=0;i<bolS;i++)booleans.add(NBT.getBoolean("bol"+i));
			for(int i=0;i<bytS;i++)bytes___.add(NBT.getByte   ("byt"+i));
			for(int i=0;i<lonS;i++)longs___.add(NBT.getLong   ("lon"+i));
			for(int i=0;i<douS;i++)doubles_.add(NBT.getDouble ("dou"+i));
			for(int i=0;i<floS;i++)floats__.add(NBT.getFloat  ("flo"+i));
			for(int i=0;i<strS;i++)strings_.add(NBT.getString ("str"+i));
			for(int i=0;i<shoS;i++)shorts__.add(NBT.getShort  ("sho"+i));
			result.readData(integers.iterator(), booleans.iterator(), bytes___.iterator(), longs___.iterator(), doubles_.iterator(), floats__.iterator(), strings_.iterator(), shorts__.iterator());
			return result;
		}
		public static void saveDataToNBT(List<SavableData> data,NBTTagCompound NBT,String baseName){
			SavableData[] dataArray={};
			for(SavableData i:data)dataArray=ArrayUtils.add(dataArray, i);
			saveDataToNBT(dataArray, NBT, baseName);
		}
		
		public static void saveDataToNBT(SavableData[] data,NBTTagCompound NBT,String baseName){
			NBTTagList list=new NBTTagList();
			
			for(int i=0;i<data.length;i++){
				if(data[i]!=null){
					NBTTagCompound savable=new NBTTagCompound();
					savable.setByte(baseName, (byte)i);
					saveToNBT(data[i], savable);
					list.appendTag(savable);
				}
			}
			NBT.setTag(baseName+"Data", list);
		}
		public static void saveToNBT(SavableData data,NBTTagCompound NBT){
			List<Integer> integers=new ArrayList<Integer>();
			List<Boolean> booleans=new ArrayList<Boolean>();
			List<Byte>	bytes___=new ArrayList<Byte>();
			List<Long>	longs___=new ArrayList<Long>();
			List<Double>  doubles_=new ArrayList<Double>();
			List<Float>   floats__=new ArrayList<Float>();
			List<String>  strings_=new ArrayList<String>();
			List<Short>   shorts__=new ArrayList<Short>();
			data.writeData(integers, booleans, bytes___, longs___, doubles_, floats__, strings_, shorts__);
			NBT.setString("className", data.getClass().getName());
			if(integers.size()>0)NBT.setInteger("intS", integers.size());
			if(booleans.size()>0)NBT.setInteger("bolS", booleans.size());
			if(bytes___.size()>0)NBT.setInteger("bytS", bytes___.size());
			if(longs___.size()>0)NBT.setInteger("lonS", longs___.size());
			if(doubles_.size()>0)NBT.setInteger("douS", doubles_.size());
			if(floats__.size()>0)NBT.setInteger("floS", floats__.size());
			if(strings_.size()>0)NBT.setInteger("strS", strings_.size());
			if(shorts__.size()>0)NBT.setInteger("shoS", shorts__.size());
			for(int i=0;i<integers.size();i++)NBT.setInteger("int"+i, integers.get(i));
			for(int i=0;i<booleans.size();i++)NBT.setBoolean("bol"+i, booleans.get(i));
			for(int i=0;i<bytes___.size();i++)NBT.setByte   ("byt"+i, bytes___.get(i));
			for(int i=0;i<longs___.size();i++)NBT.setLong   ("lon"+i, longs___.get(i));
			for(int i=0;i<doubles_.size();i++)NBT.setDouble ("dou"+i, doubles_.get(i));
			for(int i=0;i<floats__.size();i++)NBT.setFloat  ("flo"+i, floats__.get(i));
			for(int i=0;i<strings_.size();i++)NBT.setString ("str"+i, strings_.get(i));
			for(int i=0;i<shorts__.size();i++)NBT.setShort  ("sho"+i, shorts__.get(i));
		}
	}
	public void readData(Iterator<Integer> integers,Iterator<Boolean> booleans,Iterator<Byte> bytes___,Iterator<Long> longs___,Iterator<Double> doubles_,Iterator<Float> floats__,Iterator<String> strings_,Iterator<Short> shorts__);
	
	public void writeData(List<Integer> integers,List<Boolean> booleans,List<Byte> bytes___,List<Long> longs___,List<Double> doubles_,List<Float> floats__,List<String> strings_,List<Short> shorts__);
}
