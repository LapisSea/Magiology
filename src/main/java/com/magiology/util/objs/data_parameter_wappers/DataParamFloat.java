package com.magiology.util.objs.data_parameter_wappers;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;

public class DataParamFloat<Ent extends Entity> extends DataParamBase<Float,Ent>{
	
	public DataParamFloat(Class<Ent> container){
		super(container, DataSerializers.FLOAT);
	}

	@Override
	public Float get(Ent entity){
		return super.get(entity);
	}

	@Override
	public void set(Ent entity, Float t){
		super.set(entity, t);
	}

	@Override
	public void register(Ent entity, Float startValue){
		super.register(entity, startValue);
	}

	public void set(Ent entity, float t){
		super.set(entity, t);
	}

	public void register(Ent entity, float startValue){
		super.register(entity, startValue);
	}
}
