package com.magiology.util.objs;

import java.util.ArrayList;
import java.util.Collection;

import joptsimple.internal.Objects;

public class ArrayList_ModifyHook<E>extends ArrayList<E>{
	
	private Runnable hook;

	public ArrayList_ModifyHook(){
		super();
		this.hook=()->{};
	}
	public ArrayList_ModifyHook(Runnable hook){
		super();
		Objects.ensureNotNull(this.hook=hook);
	}
	public ArrayList_ModifyHook(Collection<? extends E> c){
		super(c);
		this.hook=()->{};
	}
	public ArrayList_ModifyHook(Collection<? extends E> c,Runnable hook){
		super(c);
		Objects.ensureNotNull(this.hook=hook);
	}
	
	protected void setHook(Runnable hook){
		Objects.ensureNotNull(this.hook=hook);
	}
	
	protected void callHook(){
		hook.run();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c){
		if(super.addAll(c)){
			callHook();
			return true;
		}
		return false;
	}
	
	@Override
	public E remove(int index){
		E e=super.remove(index);
		callHook();
		return e;
	}
	
	@Override
	public boolean remove(Object o){
		
		if(super.remove(o)){
			callHook();
			return true;
		}
		return false;
		
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		if(super.removeAll(c)){
			callHook();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeIf(java.util.function.Predicate<? super E> filter){
		if(super.removeIf(filter)){
			callHook();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean add(E e){
		if(super.add(e)){
			callHook();
			return true;
		}
		return false;
	}
	
	@Override
	public void add(int index, E element){
		super.add(index, element);
		callHook();
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		if(super.addAll(index, c)){
			callHook();
			return true;
		}
		return false;
	}
	@Override
	public E set(int index, E element){
		E e=super.set(index, element);
		callHook();
		return e;
	}
}
