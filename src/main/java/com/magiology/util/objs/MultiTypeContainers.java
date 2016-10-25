package com.magiology.util.objs;

import com.magiology.util.statics.UtilM;

public class MultiTypeContainers{
	public static abstract class MultiTypeContainerX{
		
		public MultiTypeContainerX(){
			
		}
		
		private Object get(int id){
			switch(id){
			case 0:return ((MultiTypeContainer1)this).get1();
			case 1:return ((MultiTypeContainer2)this).get2();
			case 2:return ((MultiTypeContainer3)this).get3();
			case 3:return ((MultiTypeContainer4)this).get4();
			case 4:return ((MultiTypeContainer5)this).get5();
			case 5:return ((MultiTypeContainer6)this).get6();
			}
			return null;
		}
		public int getCount(){
			return 0;
		}
		@Override
		public String toString(){
			StringBuilder result=new StringBuilder("{");
			for(int i=0;i<getCount();i++){
				result.append("obj").append(i+1).append("=").append(UtilM.toString(get(i)));
				if(i+1<getCount())result.append(", ");
			}
			return result.toString();
		}
	}
	public static abstract class MultiTypeContainer1<T1> extends MultiTypeContainerX{
		
		public MultiTypeContainer1(){
			
		}
		
		public abstract T1 get1();
		public abstract void set1(T1 t1);
		@Override
		public int getCount(){
			return 1;
		}
	}
	public static abstract class MultiTypeContainer2<T1,T2> extends MultiTypeContainer1<T1>{
		public abstract T2 get2();
		public abstract void set2(T2 t2);
		@Override
		public int getCount(){
			return 2;
		}
	}
	public static abstract class MultiTypeContainer3<T1,T2,T3> extends MultiTypeContainer2<T1,T2>{
		public abstract T3 get3();
		public abstract void set3(T3 t3);
		@Override
		public int getCount(){
			return 3;
		}
	}
	public static abstract class MultiTypeContainer4<T1,T2,T3,T4> extends MultiTypeContainer3<T1,T2,T3>{
		public abstract T4 get4();
		public abstract void set4(T4 t4);
		@Override
		public int getCount(){
			return 4;
		}
	}
	public static abstract class MultiTypeContainer5<T1,T2,T3,T4,T5> extends MultiTypeContainer4<T1,T2,T3,T4>{
		public abstract T5 get5();
		public abstract void set5(T5 t5);
		@Override
		public int getCount(){
			return 5;
		}
	}
	public static abstract class MultiTypeContainer6<T1,T2,T3,T4,T5,T6> extends MultiTypeContainer5<T1,T2,T3,T4,T5>{
		public abstract T6 get6();
		public abstract void set6(T6 t6);
		@Override
		public int getCount(){
			return 6;
		}
	}
	
}
