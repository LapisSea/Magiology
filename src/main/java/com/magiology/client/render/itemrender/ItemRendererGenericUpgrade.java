package com.magiology.client.render.itemrender;

//public class ItemRendererGenericUpgrade implements IItemRenderer {
//	Minecraft mc=U.getMC();
//	ResourceLocation texture=null;
//	ItemRenderer IR=new ItemRenderer(mc);
//	
//	public ItemRendererGenericUpgrade(){}
//	
//	@Override
//	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return type!=ItemRenderType.INVENTORY;
//	}
//	
//	@Override
//	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper){
//		return true;
//	}
//	
//	@Override
//	public void renderItem(ItemRenderType type, ItemStack is, Object... data){
//		texture=Textures.getResource(MReference.MODID,"/textures/items/"+((GenericItemUpgrade)is.getItem()).UT.toString()+"Upgrades.png");
//		U.getMC().renderEngine.bindTexture(texture);
//		if(type==ItemRenderType.EQUIPPED_FIRST_PERSON||type==ItemRenderType.EQUIPPED){
//			double x=0,y=0,z=0,xr=0,yr=0,zr=0;
//			if(type==ItemRenderType.EQUIPPED){
////				double angle=((float)H.getMC().theWorld.getTotalWorldTime()%180)*2;
//				xr=90;
//				yr=45;
//				zr=90;
//				x=0.3;
//				y=-0.9;
//				z=0.4;
//			}else if(type==ItemRenderType.EQUIPPED_FIRST_PERSON){
//				x=0;
//				y=0.5;
//				z=0.6;
//			}
//			OpenGLM.rotate(xr, 1, 0, 0);
//			OpenGLM.rotate(yr, 0, 1, 0);
//			OpenGLM.rotate(zr, 0, 0, 1);
//			OpenGLM.translate(x,y,z);
//			double time=U.getMC().theWorld.getTotalWorldTime()%180,angle=((time)*2-2)+(2)*RenderEvents.partialTicks;
//			OpenGLM.translate(0.5, 0, 0);
//			OpenGLM.rotate(angle, 0, 1, 0);
//			OpenGLM.translate(-0.5, 0, 0);
//			render(is);
//			OpenGLM.translate(0.5, 0, 0);
//			OpenGLM.rotate(-angle, 0, 1, 0);
//			OpenGLM.translate(-0.5, 0, 0);
//			OpenGLM.translate(x, -y, z);
//			OpenGLM.rotate(zr, 0, 0, 1);
//			OpenGLM.rotate(yr, 0, 1, 0);
//			OpenGLM.rotate(xr, 1, 0, 0);
//		}else if(type==ItemRenderType.ENTITY){
//			double p=1.0/16.0;
//			double transx=p*8,transy=0.2,transz=-p/2;
//			
//			OpenGLM.translate(-transx, -transy, -transz);
//			render(is);
//			OpenGLM.translate(transx, transy, transz);
//		}
//	}
//	public void render(ItemStack is){
//		OpenGLM.depthMask(true);
//		OpenGLM.disableBlend();
//		GL11U.allOpacityIs(false);
//		float width=0.0625F;
//		OpenGLM.pushMatrix();
//		if(((GenericItemUpgrade)is.getItem()).UT.toString().equals("Speed")){
//			width*=0.5;
//			OpenGLM.translate(0, 0, -width/2);
//		}
//		Render.RI().renderItemModel(is);
//		OpenGLM.popMatrix();
//		OpenGLM.depthMask(false);
//		OpenGLM.enableBlend();
//		GL11U.blendFunc(2);
//		GL11U.allOpacityIs(false);
//		GL11.glDisable(GL11.GL_ALPHA_TEST);
//		OpenGLM.disableCull();
//		OpenGLM.color(1, 1, 1, 0.2);
//		Render.RI().renderItemModel(is);
//		OpenGLM.color(1, 1, 1, 1);
//		OpenGLM.disableBlend();
//		GL11.glEnable(GL11.GL_ALPHA_TEST);
//		OpenGLM.enableCull();
//		OpenGLM.depthMask(true);
//		GL11U.allOpacityIs(true);
//	}
//	
//	
//}




