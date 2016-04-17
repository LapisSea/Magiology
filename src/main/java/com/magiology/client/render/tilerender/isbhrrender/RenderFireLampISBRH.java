package com.magiology.client.render.tilerender.isbhrrender;


//public class RenderFireLampISBRH extends ISBRH{
//	public static int renderId;
//	@Override
//	public int getRenderId(){return renderId;}
//	public RenderFireLampISBRH(int renderId){RenderFireLampISBRH.renderId=renderId;}
//	
//	@Override
//	public boolean renderBlockInW(IBlockAccess world, BlockPos pos,Block bleck, int modelId, RenderBlocks renderer){
//		FireLamp block=(FireLamp)bleck;
//		Tessellator tessellator=Tessellator.instance;
//		
//		tessellator.setColorOpaque_F(1F, 1F, 1F);
//		tessellator.
//		setBrightness(
//				block.
//				getMixedBrightnessForBlock(
//						world, pos));
//		
//		renderer.renderFromInside=true;
//		renderer.renderStandardBlock(block, pos);
//		
//		renderer.renderFromInside=false;
//		renderer.renderStandardBlock(block, pos);
//		return false;
//	}
//	@Override
//	public void renderItemInW(ItemRenderType type, ItemStack item,Object... data){
//		Helper.println("renderItemInW in RenderFireLampISBRH");
//	}
//
//
//
//}
