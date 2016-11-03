package com.magiology.client.rendering.item.renderers;

import org.lwjgl.opengl.GL13;

import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.rendering.item.SIRRegistry.IItemRenderer;
import com.magiology.client.shaders.ShaderHandler;
import com.magiology.client.shaders.programs.MatterJumperShader;
import com.magiology.mc_objects.particles.ParticleBubbleFactory;
import com.magiology.util.interf.Renderable;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.objs.ColorF;
import com.magiology.util.objs.vec.Vec3M;
import com.magiology.util.statics.LogUtil;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.RandUtil;
import com.magiology.util.statics.UtilC;

import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.item.ItemStack;

public class ItemMatterJumperRenderer implements IItemRenderer{
	
	private static final ItemMatterJumperRenderer INSTANCE=new ItemMatterJumperRenderer();
	
	
	private ItemMatterJumperRenderer(){}
	
	public static ItemMatterJumperRenderer get(){
		return INSTANCE;
	}

	@Override
	public void render(ItemStack stack){
		OpenGLM.pushMatrix();
//		OpenGLM.disableTexture2D();
//		OpenGLM.translate(0, 0, -1);
		if(UtilC.getThePlayer().isSneaking())ShaderHandler.get().load();
		
		MatterJumperShader shader=ShaderHandler.getShader(MatterJumperShader.class);
		if(shader!=null)shader.activate();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		OpenGLM.bindTexture(new ResourceLocationM("textures/blocks/CoalLevel2.png"));
		
//		OpenGLM.translate(0.5, 0.5, 0.5);
//		OpenGLM.rotate(new Vec3M(UtilC.fluctuateSmooth(100, 0)*360,UtilC.fluctuateSmooth(80, 140)*180,UtilC.fluctuateSmooth(140, 0)*720).mul(500).sqrt());
//		OpenGLM.translate(-0.5, -0.5, -0.5);
		FastNormalRenderer buff=new FastNormalRenderer();
		buff.begin(true, FastNormalRenderer.POS_UV);
		buff.add(0, 0, 1, 1, 0);
		buff.add(1, 0, 1, 1, 1);
		buff.add(1, 1, 1, 0, 1);
		buff.add(0, 1, 1, 0, 0);
		
		buff.add(0, 1, 0, 0, 0);
		buff.add(1, 1, 0, 0, 1);
		buff.add(1, 0, 0, 1, 1);
		buff.add(0, 0, 0, 1, 0);

		buff.add(0, 0, 0, 1, 0);
		buff.add(0, 0, 1, 1, 1);
		buff.add(0, 1, 1, 0, 1);
		buff.add(0, 1, 0, 0, 0);
		
		buff.add(1, 1, 0, 0, 0);
		buff.add(1, 1, 1, 0, 1);
		buff.add(1, 0, 1, 1, 1);
		buff.add(1, 0, 0, 1, 0);

		buff.add(0, 0, 0, 1, 0);
		buff.add(1, 0, 0, 1, 1);
		buff.add(1, 0, 1, 0, 1);
		buff.add(0, 0, 1, 0, 0);
		
		buff.add(0, 1, 1, 0, 0);
		buff.add(1, 1, 1, 0, 1);
		buff.add(1, 1, 0, 1, 1);
		buff.add(0, 1, 0, 1, 0);
		buff.draw();
		if(shader!=null)shader.deactivate();
		
//		OpenGLM.scale(-0.5F/16F);
//		UtilC.getMC().fontRendererObj.drawString("Penis, your mum. Thank you.", -30, -5, ColorF.WHITE.toCode());
		
//		OpenGLM.enableTexture2D();
		OpenGLM.popMatrix();
	}
	
}
