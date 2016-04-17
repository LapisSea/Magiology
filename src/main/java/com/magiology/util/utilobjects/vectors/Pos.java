package com.magiology.util.utilobjects.vectors;

import com.magiology.util.utilobjects.m_extension.BlockPosM;

public class Pos extends BlockPosM{
	public Pos(){
		this(0,1000,0);
	}
	public Pos(int x, int y, int z){
		super(x, y, z);
	}

}
