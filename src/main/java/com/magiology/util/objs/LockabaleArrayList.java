package com.magiology.util.objs;

import java.util.ArrayList;
import java.util.Collection;

public class LockabaleArrayList<E>extends ArrayList<E>{
	
	private boolean locked=false;
	
	public LockabaleArrayList(){
		super();
	}
	public LockabaleArrayList(Collection<? extends E> c){
		super(c);
	}
	
	public void lock(){
		locked=true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c){
		lockList();
		return super.addAll(c);
	}
	
	@Override
	public E remove(int index){
		lockList();
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o){
		lockList();
		return super.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		lockList();
		return super.removeAll(c);
	}
	
	@Override
	public boolean removeIf(java.util.function.Predicate<? super E> filter){
		lockList();
		return super.removeIf(filter);
	}
	
	@Override
	public boolean add(E e){
		lockList();
		return super.add(e);
	}
	
	@Override
	public void add(int index, E element){
		lockList();
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		lockList();
		return super.addAll(index, c);
	}
	
	protected void lockList(){
		if(locked)throw new IllegalAccessError("This list is locked");
	}
}
