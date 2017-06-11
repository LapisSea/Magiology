package com.magiology.util.objs.data_parameter_wappers;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.EntityDataManager;

public class DataParamBase<Type, Ent extends Entity>{
	
	protected final DataParameter<Type> parm;
	protected boolean isRegistered=false;
	
	public DataParamBase(Class<Ent> container, DataSerializer<Type> serializer){
		parm=EntityDataManager.createKey(container, serializer);
	}
	
	public void register(Ent entity, Type startValue){
		entity.getDataManager().register(parm, startValue);
	}
	
	public DataParameter<Type> getParm(){
		return parm;
	}
	
	public void set(Ent entity, Type t){
		entity.getDataManager().set(parm, t);
	}
	
	public Type get(Ent entity){
		return entity.getDataManager().get(parm);
	}
}
