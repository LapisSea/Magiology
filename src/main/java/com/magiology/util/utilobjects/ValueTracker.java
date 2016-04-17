package com.magiology.util.utilobjects;

public class ValueTracker<T>{
	private T oldValue;
	private Runnable onChange;
	private boolean useEquals;
	
	public ValueTracker(Runnable onChange,boolean useEquals){
		this.onChange=onChange;
		this.useEquals=useEquals;
	}
	public void updateValue(T newValue){
		if(useEquals?(!newValue.equals(oldValue)):(newValue!=oldValue)){
			onChange.run();
		}
		oldValue=newValue;
	}
}
