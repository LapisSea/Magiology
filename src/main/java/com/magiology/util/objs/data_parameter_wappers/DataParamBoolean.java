package com.magiology.util.objs.data_parameter_wappers;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;

public class DataParamBoolean<Ent extends Entity> extends DataParamBase<Boolean,Ent>{
	
	public DataParamBoolean(Class<Ent> container){
		super(container, DataSerializers.BOOLEAN);
	}
	
	@Override
	public Boolean get(Ent entity){
		return super.get(entity);
	}
	
	@Override
	public void set(Ent entity, Boolean t){
		super.set(entity, t);
	}
	
	@Override
	public void register(Ent entity, Boolean startValue){
		super.register(entity, startValue);
	}
	
	public void set(Ent entity, boolean t){
		super.set(entity, t);
	}
	
	public void register(Ent entity, boolean startValue){
		super.register(entity, startValue);
	}
}
