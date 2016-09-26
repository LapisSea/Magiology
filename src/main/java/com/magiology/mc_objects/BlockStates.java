package com.magiology.mc_objects;

import java.util.Collection;

import com.google.common.collect.Lists;

import net.minecraft.block.properties.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockStates{
	public static final PropertyDirectionM ROTATION_FULL_3BIT=new PropertyDirectionM("facing", Lists.newArrayList(EnumFacing.values()));
	
	public static final PropertyBoolM BOOLEAN_STATE=new PropertyBoolM("generic_boolean");
	public static final PropertyInteger INT_2BIT=new PropertyIntegerM("2bit_int",2),
										INT_3BIT=new PropertyIntegerM("3bit_int",3),
										INT_4BIT=new PropertyIntegerM("4bit_int",4);
	
	private static interface BitSerializable<T extends Comparable<T>>{
		int write(IBlockState src);
		T read(int src);
		
		int getBitCount();
		
		public T get(IBlockState state);
		public IBlockState set(IBlockState src, T value);
	}
	
	public static class PropertyDirectionM extends PropertyDirection implements BitSerializable<EnumFacing>{
		private PropertyDirectionM(String name,Collection<EnumFacing> values){
			super(name,values);
		}
		@Override
		public EnumFacing get(IBlockState state){
			EnumFacing var=state.getValue(this);
			return var==null?EnumFacing.DOWN:var;
		}
		@Override
		public IBlockState set(IBlockState src, EnumFacing value){
			return src.withProperty(this,value);
		}
		
		@Override
		public int write(IBlockState src){
			return get(src).getIndex();
		}
		@Override
		public EnumFacing read(int src){
			return EnumFacing.getFront(src);
		}
		@Override
		public int getBitCount(){
			return 3;
		}
		
	}
	public static class PropertyBoolM extends PropertyBool implements BitSerializable<Boolean>{
		
		public PropertyBoolM(String name){
			super(name);
		}
		
		@Override
		public Boolean get(IBlockState state){
			Boolean var=state.getValue(this);
			return var==null?false:var;
		}
		@Override
		public IBlockState set(IBlockState src, Boolean value){
			return src.withProperty(this,value);
		}
		
		@Override
		public int write(IBlockState src){
			return get(src)?1:0;
		}
		
		@Override
		public Boolean read(int src){
			return src==1;
		}

		@Override
		public int getBitCount(){
			return 1;
		}
	}
	public static class PropertyIntegerM extends PropertyInteger implements BitSerializable<Integer>{
		public final int bitCount;
		public PropertyIntegerM(String name,int bitCount){
			super(name, 0, (int)(Math.pow(2,bitCount)-1));
			this.bitCount=bitCount;
		}
		
		@Override
		public Integer get(IBlockState state){
			return state.getValue(this);
		}
		@Override
		public IBlockState set(IBlockState src,Integer value){
			return src.withProperty(this,value);
		}
		
		@Override
		public int write(IBlockState src){
			return get(src);
		}
		
		@Override
		public Integer read(int src){
			return src;
		}
		
		@Override
		public int getBitCount(){
			return bitCount;
		}
		
	}
	
	
	public static class BlockStateParser{
		
		private final BitSerializable[] values;
		
		public BlockStateParser(BitSerializable[] values){
			if(values.length>4)throw new IllegalArgumentException("To big ammount of states!");
			int totalSize=0;
			for(BitSerializable value:values){
				totalSize+=value.getBitCount();
			}
			if(totalSize>4)throw new IllegalArgumentException("Can't store that much data! - 4 bits max, tried to use "+totalSize+" bits");
			this.values=values;
		}
		
		public IBlockState parseBits(IBlockState destination, int src){
			
			int offset=0;
			for(BitSerializable value:values){
				int size=value.getBitCount();
				
				int valueBits=cutBits(src,offset,size);
				
				destination=destination.withProperty((IProperty)value,value.read(valueBits));
				offset+=size;
			}
			return destination;
		}
		
		public int parseValues(IBlockState src){
			int bits=0b0000;
			
			int offset=0;
			for(BitSerializable value:values){
				bits=fillBits(bits,value.write(src),offset);
				offset+=value.getBitCount();
			}
			
			return bits;
		}
		private int fillBits(int src,int inserting,int offset){
			return src|(inserting<<offset);
		}
		private int cutBits(int src,int offset,int size){
			int andBits=0;
			for(int j=0;j<size;j++){
				andBits+=Math.pow(2,j);
			}
			
			return (src>>offset)&andBits;
		}
		
	}
}
