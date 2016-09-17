package com.magiology.util.objs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ModelRendererDummy extends ModelRenderer{
	
	public ModelRendererDummy(ModelBase model){
		super(model);
	}
	
	public abstract void renderHook();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(float scale){
		if(!this.isHidden){
			if(this.showModel){
				GlStateManager.pushMatrix();
				
				GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
				
				if(this.rotateAngleX==0.0F&&this.rotateAngleY==0.0F&&this.rotateAngleZ==0.0F){
					if(this.rotationPointX==0.0F&&this.rotationPointY==0.0F&&this.rotationPointZ==0.0F){
						renderHook();
						
						if(this.childModels!=null){
							for(int k=0; k<this.childModels.size(); ++k){
								this.childModels.get(k).render(scale);
							}
						}
					}else{
						GlStateManager.translate(this.rotationPointX*scale, this.rotationPointY*scale, this.rotationPointZ*scale);
						renderHook();
						
						if(this.childModels!=null){
							for(int j=0; j<this.childModels.size(); ++j){
								this.childModels.get(j).render(scale);
							}
						}
					}
				}else{
					GlStateManager.translate(this.rotationPointX*scale, this.rotationPointY*scale, this.rotationPointZ*scale);
					
					if(this.rotateAngleZ!=0.0F){
						GlStateManager.rotate(this.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
					}
					
					if(this.rotateAngleY!=0.0F){
						GlStateManager.rotate(this.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
					}
					
					if(this.rotateAngleX!=0.0F){
						GlStateManager.rotate(this.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
					}
					
					renderHook();
					
					if(this.childModels!=null){
						for(int i=0; i<this.childModels.size(); ++i){
							this.childModels.get(i).render(scale);
						}
					}
					
				}
				GlStateManager.popMatrix();
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void renderWithRotation(float scale){
		if(!this.isHidden){
			if(this.showModel){
				
				GlStateManager.pushMatrix();
				GlStateManager.translate(this.rotationPointX*scale, this.rotationPointY*scale, this.rotationPointZ*scale);
				
				if(this.rotateAngleY!=0.0F){
					GlStateManager.rotate(this.rotateAngleY*(180F/(float)Math.PI), 0.0F, 1.0F, 0.0F);
				}
				
				if(this.rotateAngleX!=0.0F){
					GlStateManager.rotate(this.rotateAngleX*(180F/(float)Math.PI), 1.0F, 0.0F, 0.0F);
				}
				
				if(this.rotateAngleZ!=0.0F){
					GlStateManager.rotate(this.rotateAngleZ*(180F/(float)Math.PI), 0.0F, 0.0F, 1.0F);
				}
				
				renderHook();
				GlStateManager.popMatrix();
			}
		}
	}
	
}
