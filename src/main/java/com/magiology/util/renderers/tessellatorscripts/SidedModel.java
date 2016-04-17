package com.magiology.util.renderers.tessellatorscripts;

import com.magiology.util.renderers.VertexModel;
import com.magiology.util.utilobjects.DoubleObject;
import com.magiology.util.utilobjects.vectors.Vec3M;

public class SidedModel{
	
	public VertexModel[][] compiledModels;
	private int curentSide;
	public VertexModel[][] models=new VertexModel[6][0];
	
	public final Vec3M[] rotations={
		new Vec3M( 90,   0, 0),
		new Vec3M(-90,   0, 0),
		new Vec3M(  0, 180, 0),
		new Vec3M(  0,   0, 0),
		new Vec3M(  0, -90, 0),
		new Vec3M(  0,  90, 0)
	};
	
	private boolean shouldCompile;
	
	public SidedModel(DoubleObject<VertexModel[],int[]>... modelsFormat){
		set(modelsFormat);
	}
	
	
	private void compile(){
		
		compiledModels=new VertexModel[models.length][0];
		
		for(int i=0;i<compiledModels.length;i++){
			compiledModels[i]=new VertexModel[models[i].length];
			for(int j=0;j<compiledModels[i].length;j++){
				
				VertexModel buff=new VertexModel();
				
				VertexModel buffer=models[i][j].exportToNormalisedVertexBufferModel();
				
				buffer.rotateAt(0.5, 0.5, 0.5, rotations[i].x, rotations[i].y, rotations[i].z);
				buffer.transformAndSaveTo(buff);
//				buff.recalculateNormals();
				compiledModels[i][j]=buff;
				compiledModels[i][j].setRenderer(models[i][j].getRenderer());
				
				
				
				compiledModels[i][j].glStateCell=models[i][j].glStateCell;
				
			}
		}
		shouldCompile=false;
	}
	public void draw(boolean[] sides){
		if(shouldCompile)compile();
		for(int i=0;i<compiledModels.length;i++){
			VertexModel[] modelArray=compiledModels[i];
			if(sides[i]&&modelArray!=null){
				curentSide=i;
				for(VertexModel model:modelArray){
					if(model!=null){
						model.draw();
					}
				}
			}
		}
	}
	
	public VertexModel[] get(int id){
		return models[id];
	}
	public int getCurentSide(){
		return curentSide;
	}
	public void set(DoubleObject<VertexModel[],int[]>... modelsFormat){
		for(DoubleObject<VertexModel[], int[]> i:modelsFormat)for(int j:i.obj2)set(j, i.obj1);
	}
	
	public void set(int id, VertexModel... model){
		models[id]=model;
		shouldCompile=true;
	}
}
