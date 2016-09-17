package com.magiology.util.objs.data_parameter_wappers;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;

public class DataParamByte<Ent extends Entity>extends DataParamBase<Byte, Ent>{
	
	
	public DataParamByte(Class<Ent> container){
		super(container, DataSerializers.BYTE);
	}
	@Override
	public Byte get(Ent entity){
		return super.get(entity);
	}
	@Override
	public void set(Ent entity, Byte t){
		super.set(entity, t);
	}
	@Override
	public void register(Ent entity, Byte startValue){
		super.register(entity, startValue);
	}
	
	public void set(Ent entity, byte t){
		super.set(entity, t);
	}
	public void register(Ent entity, byte startValue){
		super.register(entity, startValue);
	}
}
