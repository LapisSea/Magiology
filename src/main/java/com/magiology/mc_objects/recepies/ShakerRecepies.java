package com.magiology.mc_objects.recepies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.magiology.mc_objects.features.dimension_stabiliser.BlockDimensionStabiliser;
import com.magiology.mc_objects.items.SimpleItems.ItemMistyPowder;

import net.minecraft.item.ItemStack;

public class ShakerRecepies{
	
	private static final ShakerRecepies instance=new ShakerRecepies();
	public static ShakerRecepies get(){
		return instance;
	}
	
	private final List<Recepie> smeltingList=new ArrayList<>();
	private ShakerRecepies(){
		add(new ItemStack(ItemMistyPowder.get()),2,new ItemStack(BlockDimensionStabiliser.get()));
	}
	
	public List<Recepie> getAll(){
		return smeltingList;
	}
	
	private void add(ItemStack input,int xp, ItemStack...output){
		add(new ItemStack[]{input},xp,output);
	}
	private void add(ItemStack[] input,int xp, ItemStack...output){
		smeltingList.add(new Recepie(input,xp,output));
	}
	
	public class Recepie{
		private int xp;
		public final List<ItemStack> input, output;
		
		public Recepie(ItemStack[] input,int xp,ItemStack[] output){
			this.xp=xp;
			this.input=Arrays.asList(input);
			this.output=Arrays.asList(output);
			this.input.forEach(s->{
				if(s.stackSize==0)s.stackSize=1;
			});
		}
	}
}
