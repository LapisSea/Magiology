package com.magiology.api.power;

import com.magiology.util.utilclasses.PrintUtil;
import com.magiology.util.utilclasses.UtilM;

public class SixSidedBoolean{
	public static enum Modifier{
		Exclude	(false,false,false),
		First6False(true, false,true ),
		First6True (true, true, true ),
		Include	(false,false,false),
		Last6False (true, false,false),
		Last6True  (true, true, false)
		;
		public final boolean isAStarter,type,first6OrLast6;
		private Modifier(boolean isAStarter,boolean type,boolean first6OrLast6){
			this.isAStarter=isAStarter;
			this.type=type;
			this.first6OrLast6=first6OrLast6;
		}
	}
	public static SixSidedBoolean lastGen=new SixSidedBoolean();
	/**
	 * example structure (Modifier.First6True,Modifier.Exclude,0,5,Modifier.Last6False,Modifier.Include,1,3)
	 * Rules:
	 * 1. First object for input can't be an int or Exclude or Include
	 * 2. After a starting Modifier has to go an Include or Exclude
	 * 3. After an Include or Exclude has to go at least one number that is positive and smaller than 6 (you can use -1 if you don't want to add any numbers)
	 * 4. Rules before this one can be repeated
	 */
	/*
	 * Here is some example code. If input is correct this will print out 12 booleans in a correct fashion!
	 * Oh and by the way this is documented so good because this function is very complex so if I (the developer) find a bug over here I can understand what the f*** I wrote! ;)
	 * (And I am pretty sure that I will never figure out how this thing works because I forgot eeeeverything about it)
	 * 
	 	SixSidedBoolean test=SixSidedBoolean.create(Modifier.First6True,Modifier.Exclude,0,5,Modifier.Last6False,Modifier.Include,1,3);
		if(test!=null){
			for(int a=0;a<test.sides.length;a++){
				Helper.println(test.sides[a]);
				if(a==5)Helper.println("--");
			}
			Helper.println("-----");
		}
	 */
	public static <generic> SixSidedBoolean create(generic... modifiers){
		return lastGen=create(null,modifiers);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static <generic> SixSidedBoolean create(Object javaTrickFor2SameFunctions,generic... modifiers){
		//checking if all objects are a valid type
		for(generic modifier:modifiers){
			if(modifier instanceof Modifier||modifier instanceof Integer);
			else{
				PrintUtil.println("An invalid input was detected!");
				return null;
			}
		}
		SixSidedBoolean result=new SixSidedBoolean();
		Modifier includeExclude=null,starter=null;
		
		try{
			//for loop: reading all Modifier clusters
			for(int id=0;id<modifiers.length;id++){
				generic modifier=modifiers[id];
				
				//detecting possible start input error
				if(modifier instanceof Integer||!isAStarter(modifier)){
					PrintUtil.println("Modifier cluster starter is invalid!");
					return null;
				}else{
					starter=(Modifier)modifiers[id];
					id++;
					modifier=modifiers[id];
				}
				
				
				//detecting possible input error after a starter modifier
				if(isAStarter(modifier)){
					PrintUtil.println("Invalid include/exclude input!");
					return null;
				}else{
					includeExclude=(Modifier)modifier;
					id++;
					modifier=modifiers[id];
				}
				
				Integer[] nots=new Integer[]{-1,-1,-1,-1,-1,-1};
				int notsId=0;
				boolean var1=true;
				//reading numbers after include/exclude for inverting
				while(id<modifiers.length&&var1){
					modifier=modifiers[id];
					try{
						int i=(Integer)modifier;
						if(i<0||i>=6);
						else nots[notsId]=i;
						
						notsId++;
						id++;
					}catch(Exception e){
						var1=false;
					}
				}
				
				//actually using gathered data from the correctly formated set of objects (First6True,Exclude,1,5,2...)
				
				for(int a=0;a<6;a++){
					result.sides[a+(starter.first6OrLast6?0:6)]=
					(UtilM.isInArray(a, nots)?//{
						includeExclude==Modifier.Exclude:includeExclude==Modifier.Include)?//{
							!starter.type:starter.type;
						//}
					//}
					
				}
				
				id--;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	private static boolean isAStarter(Object modifier){
		if(modifier instanceof Modifier){
			return ((Modifier)modifier).isAStarter;
		}
		return false;
	}
	public boolean[] sides=new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false};
	public SixSidedBoolean(){}
	public SixSidedBoolean(boolean b1,boolean b2,boolean b3,boolean b4,boolean b5,boolean b6,boolean b7,boolean b8,boolean b9,boolean b10,boolean b11,boolean b12){
		sides=new boolean[]{b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12};
	}
}
