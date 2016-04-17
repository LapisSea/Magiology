package com.magiology.api.lang.bridge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.magiology.api.lang.ICommandInteract;
import com.magiology.api.lang.program.ProgramCommon;

public class CommandInteractorList extends ArrayList<ICommandInteract>{
	private boolean ableToEdit=false;
	public CommandInteractorList(Collection<ICommandInteract> commandInteractors){
		ableToEdit=true;
		this.addAll(commandInteractors);
		ableToEdit=false;
	}
	
	@Override
	public boolean add(ICommandInteract e){
		if(!ableToEdit)return false;
		return super.add(e);
	}
	@Override
	public void add(int index, ICommandInteract element){
		if(!ableToEdit)return;
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(Collection<? extends ICommandInteract> c){
		if(!ableToEdit)return false;
		return super.addAll(c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends ICommandInteract> c){
		if(!ableToEdit)return false;
		return super.addAll(index, c);
	}
	
	@Override
	public void clear(){
		if(!ableToEdit)return;
		super.clear();
	}
	public CommandInteractorList extractByName(String name){
		List<ICommandInteract> extract=new ArrayList<ICommandInteract>();
		for(ICommandInteract i:this)if(i!=null&&i.getName()!=null&&i.getName().equals(name))extract.add(i);
		return new CommandInteractorList(extract);
	}
	public CommandInteractorList extractByType(String type){
		List<ICommandInteract> extract=new ArrayList<ICommandInteract>();
		for(ICommandInteract i:this)if(i.getClass().getSimpleName().startsWith(type))extract.add(i);
		return new CommandInteractorList(extract);
	}
	public Object getByName(String name){
		if(name==null||name.isEmpty())ProgramCommon.JSNull("Cannot search for empty name!");
		for(ICommandInteract com:this)if(com!=null&&com.getName()!=null&&com.getName().equals(name))return com;
		return ProgramCommon.JSNull("No command interactor with name "+name);
	}
	public Object getByType(String type){
		if(type==null||type.isEmpty())ProgramCommon.JSNull("Cannot search for empty type!");
		for(ICommandInteract com:this)if(com!=null&&com.getClass().getSimpleName().startsWith(type))return com;
		return ProgramCommon.JSNull("No command interactor with type "+type);
	}
	@Override
	public ICommandInteract remove(int index){
		if(!ableToEdit)return null;
		return super.remove(index);
	}
	@Override
	public boolean remove(Object o){
		if(!ableToEdit)return false;
		return super.remove(o);
	}
	@Override
	public boolean removeAll(Collection<?> c){
		if(!ableToEdit)return false;
		return super.removeAll(c);
	}
	@Override
	public boolean removeIf(Predicate<? super ICommandInteract> filter){
		if(!ableToEdit)return false;
		return super.removeIf(filter);
	}
	@Override
	protected void removeRange(int fromIndex, int toIndex){
		if(!ableToEdit)return;
		super.removeRange(fromIndex, toIndex);
	}
}
