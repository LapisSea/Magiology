package com.magiology.util.objs.data_parameter_wappers;

import com.magiology.util.statics.UtilM;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializers;

public class DataParamEnum<Type extends Enum, Ent extends Entity> extends DataParamBase<Byte,Ent>{
	
	protected final Type defaultEnum, values[];
	
	public DataParamEnum(Class<Ent> container, Type defult){
		super(container, DataSerializers.BYTE);
		defaultEnum=defult;
		values=(Type[])defult.getDeclaringClass().getEnumConstants();
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
	
	public Type getEnum(Ent entity){
		int id=get(entity);
		if(UtilM.isIdInArray(values, id)) return values[id];
		return defaultEnum;
	}
	
	public void setEnum(Ent entity, Type t){
		set(entity, (byte)t.ordinal());
	}
	
	public void register(Ent entity, Type startValue){
		register(entity, (byte)startValue.ordinal());
	}
}
