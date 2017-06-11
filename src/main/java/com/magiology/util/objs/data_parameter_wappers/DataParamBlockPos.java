package com.magiology.util.objs.data_parameter_wappers;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;

public class DataParamBlockPos<Ent extends Entity> extends DataParamBase<BlockPos,Ent>{
	
	public DataParamBlockPos(Class<Ent> container){
		super(container, DataSerializers.BLOCK_POS);
	}

	@Override
	public BlockPos get(Ent entity){
		return super.get(entity);
	}

	@Override
	public void set(Ent entity, BlockPos t){
		super.set(entity, t);
	}

	@Override
	public void register(Ent entity, BlockPos startValue){
		super.register(entity, startValue);
	}
}
