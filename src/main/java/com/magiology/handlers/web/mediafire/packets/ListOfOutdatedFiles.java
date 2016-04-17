package com.magiology.handlers.web.mediafire.packets;

import java.io.Serializable;
import java.util.ArrayList;

public class ListOfOutdatedFiles implements Serializable{
	private static final long serialVersionUID=3452358;
	
	public ArrayList<String> outdatedFiles;
	
	public ListOfOutdatedFiles(ArrayList<String> outdatedFiles){
		this.outdatedFiles=outdatedFiles;
	}	
}
