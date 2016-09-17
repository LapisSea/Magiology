package com.magiology.util.statics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.magiology.util.interf.ObjectConverter;
import com.magiology.util.objs.DoubleObject;

public class CollectionBuilder{

	public static <K,V> Map<K,V> buildMap(Map<K, V> map, ObjectConverter<Object[], DoubleObject<K, V>> builder, int dataChunkSize, Object...data){
		if(data.length==0)return map;
		if(data.length%dataChunkSize!=0)throw new IllegalArgumentException("Incorrect number of data objects!");
		
		Object[] chunk=new Object[dataChunkSize];
		int tracker=0;
		for(int i=0,j=data.length/dataChunkSize;i<j;i++){
			for(int k=0;k<chunk.length;k++){
				chunk[k]=data[tracker++];
			}
			DoubleObject<K, V> entrty=builder.convert(chunk);
			map.put(entrty.obj1, entrty.obj2);
		}
		
		return map;
	}
	public static <K,V> Map<K,V> buildMap(Map<K, V> map,Object...data){
		return buildMap(new HashMap<>(), (ObjectConverter<Object[], DoubleObject<K, V>>)(in)->{
			return new DoubleObject<K, V>((K)in[0], (V)in[1]);
		}, 2, data);
	}
	
	public static <U,T extends Collection<U>> T build(T col, ObjectConverter<Object[], U> builder, int dataChunkSize, Object...data){
		if(data.length==0)return col;
		if(data.length%dataChunkSize!=0)throw new IllegalArgumentException("Incorrect number of data objects!");
		
		Object[] chunk=new Object[dataChunkSize];
		int tracker=0;
		for(int i=0,j=data.length/dataChunkSize;i<j;i++){
			for(int k=0;k<chunk.length;k++){
				chunk[k]=data[tracker++];
			}
			col.add(builder.convert(chunk));
		}
		
		return col;
	}
	
}
