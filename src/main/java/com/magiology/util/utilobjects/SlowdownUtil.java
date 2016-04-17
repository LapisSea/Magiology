package com.magiology.util.utilobjects;

public class SlowdownUtil{
	
	public int lenght,progress=0;

	public SlowdownUtil(int lenght){
		this.lenght=lenght;
	}
	
	public void addProgress(){
		progress++;
	}
	
	public void ChangeLenght(int a){
		lenght=a;
	}
	
	public boolean isTime(){
		boolean result;
		result=lenght<=progress?true:false;
		if(result)RessetProgress();
		return result;
	}
	
	public boolean isTimeWithAddProgress(){
		addProgress();
		return isTime();
	}
	
	public void RessetProgress(){
		progress=0;
	}
	
}
