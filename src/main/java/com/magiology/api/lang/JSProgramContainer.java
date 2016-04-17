package com.magiology.api.lang;

import com.magiology.api.lang.program.ProgramUsable;

import net.minecraft.item.ItemStack;

public interface JSProgramContainer{
	
	public int getId(ItemStack stack);
	
	public ProgramUsable getProgram(ItemStack stack);
	public void initID(ItemStack stack);
	
	public void setId(ItemStack stack, int content);
	
//	public static class Program{
//		
//		public String name,result,argsSrc;
//		public BlockPosM pos;
//		public int programId;
//		
//		public Program(String name, int programId, Vec3i pos){
//			this.name=name;
//			this.programId=programId;
//			this.pos=new BlockPosM(pos);
//		}
//		@Override
//		public String toString(){
//			return "Program{name: "+name+", result: "+result+", pos: "+pos+", program id: "+programId+"}";
//		}
//		public Object run(TileEntityNetworkProgramHolder holder, Object[] args, Object[] environment){
//			NetworkProgramHolderWrapper.setInstance(holder);
//			Object x=ProgramDataBase.run(programId, args, environment);
//			result=x+"";
//			return x;
//		}
//	}
}
