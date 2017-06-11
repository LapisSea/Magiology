package com.magiology.util.statics;

import com.magiology.util.interf.ObjectConverter;
import com.magiology.util.objs.PairM;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionBuilder{
	
	public static <K, V> Map<K,V> buildMap(Map<K,V> map, ObjectConverter<Object[],PairM<K,V>> builder, int dataChunkSize, Object... data){
		if(data.length==0) return map;
		if(data.length%dataChunkSize!=0) throw new IllegalArgumentException("Incorrect number of data objects!");
		
		Object[] chunk=new Object[dataChunkSize];
		int tracker=0;
		for(int i=0, j=data.length/dataChunkSize; i<j; i++){
			for(int k=0; k<chunk.length; k++){
				chunk[k]=data[tracker++];
			}
			PairM<K,V> entrty=builder.convert(chunk);
			map.put(entrty.obj1, entrty.obj2);
		}
		
		return map;
	}
	
	public static <K, V> Map<K,V> buildMap(Map<K,V> map, Object... data){
		return buildMap(new HashMap<>(), (ObjectConverter<Object[],PairM<K,V>>)(in)->{
			return new PairM<K,V>((K)in[0], (V)in[1]);
		}, 2, data);
	}
	
	public static <U, T extends Collection<U>, L> T build(T col, ObjectConverter<L[],U> builder, int dataChunkSize, L... data){
		if(data.length==0) return col;
		if(data.length%dataChunkSize!=0) throw new IllegalArgumentException("Incorrect number of data objects!");
		
		L[] chunk=(L[])Array.newInstance(data[0].getClass(), dataChunkSize);
		int tracker=0;
		for(int i=0, j=data.length/dataChunkSize; i<j; i++){
			for(int k=0; k<chunk.length; k++){
				chunk[k]=data[tracker++];
			}
			col.add(builder.convert(chunk));
		}
		
		return col;
	}
	
}
