package com.magiology.client.render.itemrender;

//public class ItemRendererPants42 implements IItemRenderer {
//	ModelPants42 model=new ModelPants42();
//
//	public ItemRendererPants42(){}
//	
//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return true;
//	}
//
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
//		return true;
//	}
//	
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
//		double xoffset=0,yoffset=0,zoffset=0,xRotation=0,yRotation=0,zRotation=0;
//		
//		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
//			xoffset=0.5;
//			yoffset=0.8;
//			zoffset=0.5;
//			yRotation=110;
//		}
//		yoffset+=0.7;
//		OpenGLM.enableCull();
//		OpenGLM.disableCull();
//		OpenGLM.translate( xoffset,  yoffset,  zoffset);
//		OpenGLM.rotate(180, 1, 0, 0);
//		double scale=0.7;
//		OpenGLM.scale(scale,scale,scale);
////		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON||type==ItemRenderType.INVENTORY)model.shouldFollowThePlayer=false;
////		else model.shouldFollowThePlayer=true;
//		
//		OpenGLM.rotate(xRotation,1,0,0);
//		OpenGLM.rotate(yRotation,0,1,0);
//		OpenGLM.rotate(zRotation,0,0,1);
//		OpenGLM.pushMatrix();
//		model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//		OpenGLM.popMatrix();
//		OpenGLM.rotate(zRotation,0,0,1);
//		OpenGLM.rotate(yRotation,0,1,0);
//		OpenGLM.rotate(xRotation,1,0,0);
//		
//		
//		OpenGLM.scale(1/scale,1/scale,1/scale);
//		OpenGLM.rotate(-180, 1, 0, 0);
//		OpenGLM.translate(-xoffset, -yoffset, -zoffset);
//		OpenGLM.enableCull();
//	}
//	
//	
//
//}




