package com.magiology.util.objs;

import com.google.common.collect.Lists;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.List;

public class BlockStates{
	
	public static final PropertyDirectionM SAVE_ROTATION_FULL_3BIT=saveableFull3BitRotationProp("facing"), NOSAVE_ROTATION_FULL_3BIT=full3BitRotationProp("facing");
	
	public static PropertyDirectionM saveableFull3BitRotationProp(String name){
		return new PropertyDirectionM(name, Lists.newArrayList(EnumFacing.values()), true);
	}
	
	public static PropertyDirectionM full3BitRotationProp(String name){
		return new PropertyDirectionM(name, Lists.newArrayList(EnumFacing.values()), false);
	}
	
	public static PropertyBoolM saveableBooleanProp(String name){
		return new PropertyBoolM(name, true);
	}
	
	public static PropertyBoolM booleanProp(String name){
		return new PropertyBoolM(name, false);
	}
	
	public static PropertyIntegerM saveableIntProp(String name, int bitCount){
		if(bitCount>4) throw new IllegalArgumentException("Can't store that much data! - 4 bits max, tried to use "+bitCount+" bits");
		return new PropertyIntegerM(name, bitCount, true);
	}
	
	public static PropertyIntegerM intProp(String name, int bitCount){
		return new PropertyIntegerM(name, bitCount, false);
	}
	
	public static PropertyIntegerM saveableIntProp(String name, int min, int max){
		if(max>15) throw new IllegalArgumentException("Can't store that much data! - max value = 15, tried to use "+max+" bits");
		return new PropertyIntegerM(name, min, max, true);
	}
	
	public static PropertyIntegerM intProp(String name, int min, int max){
		return new PropertyIntegerM(name, min, max, false);
	}
	
	public static interface IPropertyM<T extends Comparable<T>> extends IProperty<T>{
		
		int write(IBlockState src);
		
		T read(int src);
		
		int getBitCount();
		
		T get(IBlockState state);
		
		IBlockState set(IBlockState src, T value);
		
		boolean isSaveable();
	}
	
	public static class PropertyDirectionM extends PropertyDirection implements IPropertyM<EnumFacing>{
		
		private final boolean saveable;
		
		private PropertyDirectionM(String name, Collection<EnumFacing> values, boolean saveable){
			super(name, values);
			this.saveable=saveable;
		}
		
		@Override
		public EnumFacing get(IBlockState state){
			EnumFacing var=state.getValue(this);
			return var==null?EnumFacing.DOWN:var;
		}
		
		@Override
		public IBlockState set(IBlockState src, EnumFacing value){
			if(get(src)==value) return src;
			return src.withProperty(this, value);
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
		
		@Override
		public boolean isSaveable(){
			return saveable;
		}
		
	}
	
	public static class PropertyBoolM extends PropertyBool implements IPropertyM<Boolean>{
		
		private final boolean saveable;
		
		private PropertyBoolM(String name, boolean saveable){
			super(name);
			this.saveable=saveable;
		}
		
		@Override
		public Boolean get(IBlockState state){
			Boolean var=state.getValue(this);
			return var==null?false:var;
		}
		
		@Override
		public IBlockState set(IBlockState src, Boolean value){
			if(get(src).booleanValue()==value.booleanValue()) return src;
			return src.withProperty(this, value);
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
		
		@Override
		public boolean isSaveable(){
			return saveable;
		}
	}
	
	public static class PropertyIntegerM extends PropertyInteger implements IPropertyM<Integer>{
		
		private final boolean saveable;
		public final  int     bitCount;
		
		private PropertyIntegerM(String name, int min, int max, boolean saveable){
			super(name, min, max);
			
			//1 = 1, 2 = 3, 3 = 5, 4 = 15
			if(max>5) bitCount=4;
			else if(max>3) bitCount=3;
			else if(max>1) bitCount=2;
			else bitCount=1;
			
			this.saveable=saveable;
		}
		
		private PropertyIntegerM(String name, int bitCount, boolean saveable){
			super(name, 0, (int)(Math.pow(2, bitCount)-1));
			this.bitCount=bitCount;
			this.saveable=saveable;
		}
		
		@Override
		public Integer get(IBlockState state){
			return state.getValue(this);
		}
		
		@Override
		public IBlockState set(IBlockState src, Integer value){
			if(get(src).intValue()==value.intValue()) return src;
			return src.withProperty(this, value);
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
		
		@Override
		public boolean isSaveable(){
			return saveable;
		}
	}
	
	public static class BlockStateParser{
		
		private final IPropertyM[] values;
		
		public BlockStateParser(IPropertyM[] values){
			List<IPropertyM> valuesList=Lists.newArrayList(values);
			valuesList.removeIf(v->!v.isSaveable());
			values=valuesList.toArray(new IPropertyM[valuesList.size()]);
			
			if(values.length>4) throw new IllegalArgumentException("To big ammount of states!");
			int totalSize=0;
			for(IPropertyM value : values){
				totalSize+=value.getBitCount();
			}
			if(totalSize>4) throw new IllegalArgumentException("Can't store that much data! - 4 bits max, tried to use "+totalSize+" bits");
			this.values=values;
		}
		
		public IBlockState parseBits(IBlockState destination, int src){
			
			int offset=0;
			for(IPropertyM value : values){
				int size=value.getBitCount();
				
				int valueBits=cutBits(src, offset, size);
				
				destination=destination.withProperty((IProperty)value, value.read(valueBits));
				offset+=size;
			}
			return destination;
		}
		
		public int parseValues(IBlockState src){
			int bits=0b0000;
			
			int offset=0;
			for(IPropertyM value : values){
				bits=fillBits(bits, value.write(src), offset);
				offset+=value.getBitCount();
			}
			
			return bits;
		}
		
		private int fillBits(int src, int inserting, int offset){
			return src|(inserting<<offset);
		}
		
		private int cutBits(int src, int offset, int size){
			int andBits=0;
			for(int j=0; j<size; j++){
				andBits+=Math.pow(2, j);
			}
			
			return (src>>offset)&andBits;
		}
		
	}
}
