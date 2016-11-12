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
		if(locked)return false;
		return super.addAll(c);
	}
	
	@Override
	public E remove(int index){
		if(locked)return null;
		return super.remove(index);
	}
	
	@Override
	public boolean remove(Object o){
		if(locked)return false;
		return super.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		if(locked)return false;
		return super.removeAll(c);
	}
	
	@Override
	public boolean removeIf(java.util.function.Predicate<? super E> filter){
		if(locked)return false;
		return super.removeIf(filter);
	}
	
	@Override
	public boolean add(E e){
		if(locked)return false;
		return super.add(e);
	}
	
	@Override
	public void add(int index, E element){
		if(locked)return;
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		if(locked)return false;
		return super.addAll(index, c);
	}
	
	@Override
	protected void removeRange(int fromIndex, int toIndex){
		if(locked)return;
		super.removeRange(fromIndex, toIndex);
	}
}
