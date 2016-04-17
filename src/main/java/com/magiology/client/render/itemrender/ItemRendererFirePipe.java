package com.magiology.client.render.itemrender;

//public class ItemRendererFirePipe implements IItemRenderer {
//	
//	private final float p= 1F/16F;
//	private final float tW=1F/112F;
//	private final float tH=1F/16F;
//	private final float tWC=1F/4F;
//	private final float tHC=1F/40F;
//	
//	VertixBuffer buf=TessUtil.getVB();
//	
//	public ItemRendererFirePipe(){}
//	
//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return true;
//	}
//
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
//		return helper!=ItemRendererHelper.ENTITY_BOBBING&&helper!=ItemRendererHelper.ENTITY_ROTATION;
//	}
//	
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
//		OpenGLM.pushMatrix();
//		float x=0;
//		float y=0;
//		float z=0;
//		float xr=0;
//		float yr=0;
//		float zr=0;
//		float scale=1;
//		
//		if(ItemRenderType.ENTITY == type){
//			xr=90;
//			z=0.75F;
//			y=-1.05F;
//			x=-0.75F;
//			scale=1.5F;
//		}
//		else if(ItemRenderType.EQUIPPED_FIRST_PERSON == type){
//			x=-0.1F;
//			z=-0.1F;
//			xr=-5;
//			zr=-5;
//			y=0.5F;
//		}
//		else if(ItemRenderType.EQUIPPED == type){
//			scale=1.2F;
//			x=1F;
//			y=-0.2F;
//			z=1F;
//			xr=60;
//			yr=30;
//			zr=-60;
//		}
//		else if(ItemRenderType.INVENTORY == type){
//			y=-0.1F;
//			scale=1.4F;
//		}
//		
//		OpenGLM.enableLighting();
//		OpenGLM.enableCull();
//		OpenGLM.translate(x,y,z);
//		OpenGLM.rotate(-xr, 1, 0, 0);OpenGLM.rotate(-yr, 0, 1, 0);OpenGLM.rotate(-zr, 0, 0, 1);
//		OpenGLM.scale(scale, scale, scale);
//		
//		drawConector(EnumFacing.DOWN);
//		drawConector(EnumFacing.UP);
//		drawCore();
//		
//		OpenGLM.popMatrix();
//	}
//	
//	public void drawCore(){
//		TessUtil.bindTexture(Textures.firePipeCore);
//		buf.addVertexWithUV(p*6, p*10, p*6, tWC*4, tHC*4);
//		buf.addVertexWithUV(p*6, p*6,  p*6, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*6, p*6,  p*10,tWC*0, tHC*0);
//		buf.addVertexWithUV(p*6, p*10, p*10,tWC*0, tHC*4);
//		
//		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4);
//		buf.addVertexWithUV(p*10, p*6,  p*10, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*10, p*6,  p*6, tWC*0, tHC*0);
//		buf.addVertexWithUV(p*10, p*10, p*6, tWC*0, tHC*4);
//		
//		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4);
//		buf.addVertexWithUV(p*6, p*6 , p*10, tWC*0, tHC*0);
//		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4);
//		
//		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*4);
//		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*6, p*6 , p*6, tWC*0, tHC*0);
//		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*4);
//		
//		buf.addVertexWithUV(p*10, p*10, p*10, tWC*4, tHC*4);
//		buf.addVertexWithUV(p*10, p*10, p*6, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*6, p*10, p*6, tWC*0, tHC*0);
//		buf.addVertexWithUV(p*6, p*10, p*10, tWC*0, tHC*4);
//			
//		buf.addVertexWithUV(p*6, p*6, p*10, tWC*0, tHC*4);
//		buf.addVertexWithUV(p*6, p*6, p*6, tWC*0, tHC*0);
//		buf.addVertexWithUV(p*10, p*6, p*6, tWC*4, tHC*0);
//		buf.addVertexWithUV(p*10, p*6, p*10, tWC*4, tHC*4);
//		buf.draw();
//	}
//	
//	public void drawConector(EnumFacing dir){
//		OpenGLM.pushMatrix();
//		TessUtil.bindTexture(Textures.FirePipeConection);
//		
//		OpenGLM.translate(0.5F, 0.5F, 0.5F);
//		if(dir.equals(EnumFacing.WEST)){}
//		else if (dir.equals(EnumFacing.UP)){OpenGLM.rotate(-90, 0, 0, 1);}
//		else if (dir.equals(EnumFacing.DOWN)){OpenGLM.rotate(90, 0, 0, 1);}
//		else if (dir.equals(EnumFacing.SOUTH)){OpenGLM.rotate(90, 0, 1, 0);}
//		else if (dir.equals(EnumFacing.EAST)){OpenGLM.rotate(-180, 0, 1, 0);}
//		else if (dir.equals(EnumFacing.NORTH)){OpenGLM.rotate(-90, 0, 1, 0);}
//		OpenGLM.translate(-0.5F, -0.5F, -0.5F);
//
//		buf.addVertexWithUV(p*0, p*9.5,  p*9.5,tW*24.5, tH*16);
//		buf.addVertexWithUV(p*0, p*6.5,  p*9.5,tW*24.5, tH*0);
//		buf.addVertexWithUV(p*6, p*6.5,  p*9.5,tW*0,	tH*0);
//		buf.addVertexWithUV(p*6, p*9.5,  p*9.5,tW*0,	tH*16);
//
//		buf.addVertexWithUV(p*6, p*9, p*9  ,tW*0,   tH*0);
//		buf.addVertexWithUV(p*6, p*7, p*9 ,tW*0,   tH*16);
//		buf.addVertexWithUV(p*0, p*7, p*9, tW*24.5,tH*16);
//		buf.addVertexWithUV(p*0, p*9, p*9,  tW*24.5,tH*0);
//		
//		
//		buf.addVertexWithUV(p*6, p*9.5, p*6.5  ,tW*0,   tH*0);
//		buf.addVertexWithUV(p*6, p*6.5, p*6.5 ,tW*0,   tH*16);
//		buf.addVertexWithUV(p*0, p*6.5, p*6.5, tW*24.5,tH*16);
//		buf.addVertexWithUV(p*0, p*9.5, p*6.5,  tW*24.5,tH*0);
//		
//		buf.addVertexWithUV(p*0, p*9,  p*7,tW*24.5, tH*16);
//		buf.addVertexWithUV(p*0, p*7,  p*7,tW*24.5, tH*0);
//		buf.addVertexWithUV(p*6, p*7,  p*7,tW*0,	tH*0);
//		buf.addVertexWithUV(p*6, p*9,  p*7,tW*0,	tH*16);
//		
//		
//		buf.addVertexWithUV(p*6, p*9.5, p*9.5, tW*0,	tH*16);
//		buf.addVertexWithUV(p*6, p*9.5, p*6.5, tW*0,	tH*0);
//		buf.addVertexWithUV(p*0, p*9.5, p*6.5, tW*24.5, tH*0);
//		buf.addVertexWithUV(p*0, p*9.5, p*9.5, tW*24.5, tH*16);
//
//		buf.addVertexWithUV(p*0, p*9, p*9, tW*24.5, tH*16);
//		buf.addVertexWithUV(p*0, p*9, p*7, tW*24.5, tH*0);
//		buf.addVertexWithUV(p*6, p*9, p*7, tW*0,	tH*0);
//		buf.addVertexWithUV(p*6, p*9, p*9, tW*0,	tH*16);
//		
//
//		buf.addVertexWithUV(p*0, p*6.5, p*9.5, tW*24.5, tH*16);
//		buf.addVertexWithUV(p*0, p*6.5, p*6.5, tW*24.5, tH*0);
//		buf.addVertexWithUV(p*6, p*6.5, p*6.5, tW*0,	tH*0);
//		buf.addVertexWithUV(p*6, p*6.5, p*9.5, tW*0,	tH*16);
//
//		buf.addVertexWithUV(p*6, p*7, p*9, tW*0,	tH*16);
//		buf.addVertexWithUV(p*6, p*7, p*7, tW*0,	tH*0);
//		buf.addVertexWithUV(p*0, p*7, p*7, tW*24.5, tH*0);
//		buf.addVertexWithUV(p*0, p*7, p*9, tW*24.5, tH*16);
//		buf.draw();
//		
//		TessUtil.bindTexture(Textures.FirePipeConectionEnd);
//		
//		buf.addVertexWithUV(p*0, p*9.5, p*9.5, 1, 1);
//		buf.addVertexWithUV(p*0, p*9.5, p*6.5, 1, 0);
//		buf.addVertexWithUV(p*0, p*6.5, p*6.5, 0, 0);
//		buf.addVertexWithUV(p*0, p*6.5, p*9.5, 0, 1);
//		buf.draw();
//		OpenGLM.popMatrix();
//	}
//
//}




