package com.magiology.api.lang.program;

import java.io.Serializable;

public class ProgramSerializable implements Serializable{
	
	public CharSequence programName,src;
	
	public ProgramSerializable(CharSequence programName, CharSequence src){
		this.programName=programName;
		this.src=src;
	}
}
