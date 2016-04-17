package com.magiology.util.utilclasses.math;

import java.util.HashSet;
import java.util.Set;

import com.magiology.util.utilclasses.UtilM.U;

public class ObjectCalculator{
	private static final char mul='*',div='/',add='+',sub='-';
	
	public static interface Calculator{
		public Object calc(Object left, Object right);
	}
	
	public static Calculator getCalculator(final Object left,final Object right, final char type){
		Class lc=left.getClass(),rc=right.getClass();
		boolean arrayL=U.isArray(lc),arrayR=U.isArray(rc);
		if(arrayL||arrayR)return arrayCalculator(left, right, type, arrayL, arrayR);
		else return normalCalculator(left, right, type);
	}
	private static Calculator arrayCalculator(final Object left,final Object right, final char type, final boolean arrayL, final boolean arrayR){
		
		if(arrayL&&arrayR){
			if(left instanceof float[]){
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
						return ArrayMath.calc((float[])left, (float[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (int[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (double[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (long[])right, type);
				}};
			}else if(left instanceof int[]){
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (int[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (float[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (double[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (long[])right, type);
				}};
			}else if(left instanceof double[]){
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (double[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (float[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (int[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (long[])right, type);
				}};
			}else if(left instanceof long[]){
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (long[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (int[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (float[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (double[])right, type);
				}};
			}
		}else if(arrayL&&!arrayR){
			if(left instanceof float[]){
				if(right instanceof Float)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (Float)right, type);
				}};
				if(right instanceof Integer)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (Integer)right, type);
				}};
				if(right instanceof Double)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (Double)right, type);
				}};
				if(right instanceof Long)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((float[])left, (Long)right, type);
				}};
			}
			else if(left instanceof int[]){
				if(right instanceof Integer)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (Integer)right, type);
				}};
				if(right instanceof Float)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (Float)right, type);
				}};
				if(right instanceof Double)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (Double)right, type);
				}};
				if(right instanceof Long)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((int[])left, (Long)right, type);
				}};
			}
			else if(left instanceof double[]){
				if(right instanceof Double)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (Double)right, type);
				}};
				if(right instanceof Float)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (Float)right, type);
				}};
				if(right instanceof Integer)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (Integer)right, type);
				}};
				if(right instanceof Long)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((double[])left, (Long)right, type);
				}};
			}
			else if(left instanceof long[]){
				if(right instanceof Long)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (Long)right, type);
				}};
				if(right instanceof Float)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (Float)right, type);
				}};
				if(right instanceof Integer)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (Integer)right, type);
				}};
				if(right instanceof Double)return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((long[])left, (Double)right, type);
				}};
			}
		}else if(!arrayL&&arrayR){
			if(left instanceof Float){
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Float)left, (float[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Float)left, (int[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Float)left, (double[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Float)left, (long[])right, type);
				}};
			}
			else if(left instanceof Integer){
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Integer)left, (int[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Integer)left, (float[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Integer)left, (double[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Integer)left, (long[])right, type);
				}};
			}
			else if(left instanceof Double){
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Double)left, (double[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Double)left, (float[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Double)left, (int[])right, type);
				}};
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Double)left, (long[])right, type);
				}};
			}
			else if(left instanceof Long){
				if(right instanceof long[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Long)left, (long[])right, type);
				}};
				if(right instanceof float[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Long)left, (float[])right, type);
				}};
				if(right instanceof int[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Long)left, (int[])right, type);
				}};
				if(right instanceof double[])return new Calculator(){@Override public Object calc(Object left, Object right){
					return ArrayMath.calc((Long)left, (double[])right, type);
				}};
			}
		}
		
		throw new IllegalStateException("ObjectCalculator has taken unsuported or invalid object type! "+left.getClass()+" or/and "+right.getClass());
	}
	private static Calculator normalCalculator(final Object left,final Object right, final char type){
		if(left instanceof Float){
			if(right instanceof Float){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)*((Float)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)/((Float)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)+((Float)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)-((Float)right);
				}};
			}
			if(right instanceof Integer){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)*((Integer)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
//					Util.printInln(left,"/",right,"=",((Float)left)/((Integer)right));
					return((Float)left)/((Integer)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)+((Integer)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)-((Integer)right);
				}};
			}
			if(right instanceof Double){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)*((Double)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)/((Double)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)+((Double)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)-((Double)right);
				}};
			}
			if(right instanceof Long){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)*((Long)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)/((Long)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)+((Long)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left)-((Long)right);
				}};
			}
			if(right instanceof String){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Float)left).floatValue()+((String)right);
				}};
			}
		}else if(left instanceof Integer){
			if(right instanceof Integer){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)*((Integer)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)/((Integer)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)+((Integer)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)-((Integer)right);
				}};
			}
			if(right instanceof Float){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)*((Float)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)/((Float)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)+((Float)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)-((Float)right);
				}};
			}
			if(right instanceof Double){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)*((Double)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)/((Double)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)+((Double)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)-((Double)right);
				}};
			}
			if(right instanceof Long){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)*((Long)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)/((Long)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)+((Long)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Integer)left)-((Long)right);
				}};
			}
			if(right instanceof String){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return(left)+((String)right);
				}};
			}
		}else if(left instanceof Double){
			if(right instanceof Double){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)*((Double)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)/((Double)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)+((Double)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)-((Double)right);
				}};
			}
			if(right instanceof Float){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)*((Float)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)/((Float)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)+((Float)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)-((Float)right);
				}};
			}
			if(right instanceof Integer){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)*((Integer)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)/((Integer)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)+((Integer)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)-((Integer)right);
				}};
			}
			if(right instanceof Long){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)*((Long)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)/((Long)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)+((Long)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Double)left)-((Long)right);
				}};
			}
			if(right instanceof String){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return(left)+((String)right);
				}};
			}
		}else if(left instanceof Long){
			if(right instanceof Long){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)*((Long)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)/((Long)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)+((Long)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)-((Long)right);
				}};
			}
			if(right instanceof Float){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)*((Float)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)/((Float)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)+((Float)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)-((Float)right);
				}};
			}
			if(right instanceof Integer){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)*((Integer)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)/((Integer)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)+((Integer)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)-((Integer)right);
				}};
			}
			if(right instanceof Double){
				if(type==mul)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)*((Double)right);
				}};
				if(type==div)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)/((Double)right);
				}};
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)+((Double)right);
				}};
				if(type==sub)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((Long)left)-((Double)right);
				}};
			}
			if(right instanceof String){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return(left)+((String)right);
				}};
			}
		}else if(left instanceof String){
			if(right instanceof String){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+((String)right);
				}};
			}
			if(right instanceof Long){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+(right);
				}};
			}
			if(right instanceof Float){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+(right);
				}};
			}
			if(right instanceof Integer){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+(right);
				}};
			}
			if(right instanceof Double){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+(right);
				}};
			}
			if(right instanceof Boolean){
				if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
					return((String)left)+(right);
				}};
			}
		}else if(left instanceof Boolean){
			if(type==add)return new Calculator(){@Override public Object calc(Object left, Object right){
				return(left)+((String)right);
			}};
		}
		
		throw new IllegalStateException("ObjectCalculator has taken unsuported or invalid object type! "+left.getClass()+" or/and "+right.getClass());
	}
	
	
	
	
	private static Set<Class> SupportedTypes;
	static{
		SupportedTypes=new HashSet<Class>();
		SupportedTypes.add(Boolean.class);
//	  SupportedTypes.add(Character.class);
//	  SupportedTypes.add(Byte.class);
//	  SupportedTypes.add(Short.class);
		SupportedTypes.add(Integer.class);
		SupportedTypes.add(Long.class);
		SupportedTypes.add(Float.class);
		SupportedTypes.add(Double.class);
		SupportedTypes.add(String.class);
		SupportedTypes.add(new int[0].getClass());
		SupportedTypes.add(new long[0].getClass());
		SupportedTypes.add(new float[0].getClass());
		SupportedTypes.add(new double[0].getClass());
		SupportedTypes.add(new String[0].getClass());
//	  SupportedTypes.add(Void.class);
	}
	
	private static boolean isSupported(Class clazz){
		return SupportedTypes.contains(clazz);
	}
}
