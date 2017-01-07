package com.magiology.util.objs;

import java.util.ArrayList;
import java.util.Collection;

public class NoNullArrayList<E> extends ArrayList<E>{

	public NoNullArrayList(){
		super();
	}

	public NoNullArrayList(Collection<? extends E> c){
		super(c);
	}

	public NoNullArrayList(int initialCapacity){
		super(initialCapacity);
	}

	@Override
	public boolean add(E e){
		return e!=null&&super.add(e);
	}
	@Override
	public void add(int index, E element){
		if(element!=null)super.add(index, element);
	}
	@Override
	public boolean addAll(Collection<? extends E> c){
		c=new ArrayList<>(c);
		c.removeIf(e->e==null);
		return super.addAll(c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends E> c){
		c=new ArrayList<>(c);
		c.removeIf(e->e==null);
		return super.addAll(index, c);
	}
	@Override
	public E set(int index, E element){
		if(element==null){
			remove(index);
			return null;
		}
		return super.set(index, element);
	}
}
