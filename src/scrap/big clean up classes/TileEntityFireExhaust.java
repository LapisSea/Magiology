package com.magiology.mcobjects.tileentityes;

import com.magiology.core.init.MBlocks;
import com.magiology.util.utilclasses.UtilM.U;
import com.magiology.util.utilobjects.SlowdownUtil;
import com.magiology.util.utilobjects.m_extension.TileEntityM;

import net.minecraft.util.ITickable;

public class TileEntityFireExhaust extends TileEntityM implements ITickable{
	
	
	SlowdownUtil optimizer=new SlowdownUtil(5);
	public TileEntityFireExhaust(){}
	
	
	@Override
	public void update(){
		if(optimizer.isTimeWithAddProgress()){
			if(U.getBlock(worldObj, pos.add(0, -4, 0))!=MBlocks.OreStructureCore){
//				Get.Render.ER().addBlockDestroyEffects(pos, H.getBlock(worldObj, pos), 0);
				worldObj.setBlockToAir(pos);
				worldObj.setTileEntity(pos, null);
			}
		}
	}
	
}
