package com.magiology.util.utilobjects;

import java.util.ArrayList;

import com.magiology.mcobjects.tileentityes.hologram.HoloObject;
import com.magiology.util.utilclasses.PrintUtil;

public class ArrayListLog<T> extends ArrayList<HoloObject> {
	@Override
	public void clear(){
		PrintUtil.printStackTrace();
		PrintUtil.println(this);
		super.clear();
	}
}
