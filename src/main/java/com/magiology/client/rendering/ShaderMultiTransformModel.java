package com.magiology.client.rendering;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Model designed for fast rendering with partos moving in a single draw.
 * @author LapisSea
 *
 */
public abstract class ShaderMultiTransformModel{
	
	protected int modelId=-1;
	
	
	public int getModelListId(){
		return modelId;
	}
	
	public abstract int generateModel();
	public abstract int[] getMatrixIds();
	public abstract Matrix4f[] getMatrices();
}
