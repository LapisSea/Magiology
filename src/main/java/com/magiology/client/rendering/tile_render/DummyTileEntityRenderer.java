package com.magiology.client.rendering.tile_render;

import com.magiology.mc_objects.tileentitys.DummyTileEntity;
import com.magiology.util.m_extensions.TileEntitySpecialRendererM;
import com.magiology.util.objs.Vec3M;

public class DummyTileEntityRenderer extends TileEntitySpecialRendererM<DummyTileEntity>{
	
	
	@Override
	public void renderTileEntityAt(DummyTileEntity tileEntity, Vec3M renderPos, float partialTicks){
		////		PrintUtil.printStackTrace();
		//		OpenGLM.pushMatrix();
		////		EntityPlayer player=UtilC.getThePlayer();
		////		OpenGLM.translate(renderPos.sub(tileEntity.getPos()));
		////		
		////		GL11.glAlphaFunc(GL11.GL_GREATER, 0);
		////		if(player.isSneaking()){
		//////			Minecraft.getMinecraft().getRenderManager().renderEntityStatic(player, partialTicks, true);glGetProgramiv 
		////			GL20.glUseProgram(0);
		////			//5? 
		////		}
		////		Template shader=ShaderHandler.getShader(Template.class);
		//		double tim=UtilC.getWorldTime();
		//		for(int i=0;i<3;i++){
		//			float tim1=(float)((PartialTicksUtil.calculate(tim-1, tim)+i*10)%360);
		//			float mul=Math.min(tim1%30F/25F, 1);
		//			mul=(float)Math.sqrt(mul);
		//			float invertMul=1-mul;
		//			SoftEffectsShader.instance.addCricle(
		//					new Vec3M(tileEntity.getPos()).add(0.5,0.5,0.5),
		//					0.1F+0.4F*mul,
		//					invertMul*3F,
		//					mul*5,
		//					new ColorF(0.3*mul,0.2,0.3+0.7*invertMul, invertMul*2));
		//		}
		//		
		//		float tim2=(float) (PartialTicksUtil.calculate(tim-1, tim)%360)/10;
		//		for(int i=0;i<6;i++){
		//			SoftEffectsShader.instance.addLine(
		//					new Vec3M(tileEntity.getPos()).add(0.5+4*MathUtil.sin(tim2+i*60),0.5,0.5+4*MathUtil.cos(tim2+i*60)),
		//					new Vec3M(tileEntity.getPos()).add(0.5+0.5*MathUtil.sin(tim2+60+i*60),0.5,0.5+0.5*MathUtil.cos(tim2+60+i*60)),
		////					new Vec3M(tileEntity.getPos()).add(0.5,0.5,0.5+3-6*UtilC.fluctuateSmooth(60, 0)),
		////					new Vec3M(tileEntity.getPos()).add(0.5+3-6*UtilC.fluctuateSmooth(60, 15),0.5,0.5),
		//					0.2F,
		//					new ColorF(1, 0.4, 0.1, 0.5));
		//		}
		//
		////		SoftEffectsShader.instance.addLine(
		////				new Vec3M(tileEntity.getPos()).add(0.5+4,0.5,0.5),
		////				new Vec3M(tileEntity.getPos()).add(0.5,0.5,0.5),
		//////				new Vec3M(tileEntity.getPos()).add(0.5,0.5,0.5+3-6*UtilC.fluctuateSmooth(60, 0)),
		//////				new Vec3M(tileEntity.getPos()).add(0.5+3-6*UtilC.fluctuateSmooth(60, 15),0.5,0.5),
		////				0.2F,
		////				new ColorF(1, 0.2, 0.1, 1));
		//		
		////		shader.activate();
		////		Minecraft.getMinecraft().getRenderManager().doRenderEntity(player, renderPos.x, renderPos.y, renderPos.z, 0, partialTicks, false);
		////		shader.deactivate();
		////		
		////		
		//		OpenGLM.popMatrix();
	}
}
