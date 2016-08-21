package com.magiology.util.statics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.magiology.util.interf.ObjectConverterThrows;

public class CollectionConverter{

	public static<T,Col extends List<T>,result> result[] convAr(Col collection,Class<result> resultType, ObjectConverterThrows<T,result>  action){
		result[] resultAr=(result[])Array.newInstance(resultType, collection.size());
		for(int i=0;i<resultAr.length;i++){
			try{
				resultAr[i]=action.convert(collection.get(i));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return resultAr;
	}
	public static<T,result> result[] convAr(T[] collection,Class<result> resultType,ObjectConverterThrows<T,result> action){
		result[] resultAr=(result[])Array.newInstance(resultType, collection.length);
		for(int i=0;i<resultAr.length;i++){
			try{
				resultAr[i]=action.convert(collection[i]);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return resultAr;
	}
	public static<result> List<result> convLi(char[] collection, Class<result> resultType, ObjectConverterThrows<Character,result>  action){
		List<result> res=new ArrayList<>();
		for(char element:collection){
			try{
				res.add(action.convert(element));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return res;
	}
	public static<T,result> List<result> convLi(T[] collection, Class<result> resultType, ObjectConverterThrows<T,result>  action){
		List<result> res=new ArrayList<>(collection.length);
		for(int i=0;i<collection.length;i++){
			try{
				res.set(i, action.convert(collection[i]));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		return res;
	}
	public static<T,Col extends List<T>,result> List<result> convLi(Col collection,Class<result> resultType, ObjectConverterThrows<T,result>  action){
		List<result> res=new ArrayList<>();
		for(T obj:collection){
			try{
				res.add(action.convert(obj));
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		};
		return res;
	}

}
