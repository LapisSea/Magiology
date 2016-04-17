package com.magiology.api.network;

import java.util.Iterator;
import java.util.List;

import com.magiology.api.SavableData;

public class Redstone implements SavableData{
	public boolean isStrong=false,on=false;
	public int strenght=0;
	
	public Redstone(){}
	public Redstone(boolean isStrong, boolean on, int strenght){
		this.isStrong=isStrong;
		this.on=on;
		this.strenght=strenght;
	}
	@Override
	public void readData(Iterator<Integer> integers, Iterator<Boolean> booleans, Iterator<Byte> bytes___, Iterator<Long> longs___,Iterator<Double> doubles_, Iterator<Float> floats__, Iterator<String> strings_, Iterator<Short> shorts__){
		strenght=integers.next();
		on=booleans.next();
		isStrong=booleans.next();
	}
	@Override
	public void writeData(List<Integer> integers, List<Boolean> booleans, List<Byte> bytes___, List<Long> longs___, List<Double> doubles_, List<Float> floats__, List<String> strings_, List<Short> shorts__){
		integers.add(strenght);
		booleans.add(on);
		booleans.add(isStrong);
	}
}