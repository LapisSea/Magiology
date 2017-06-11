package com.magiology.client.rendering.item.renderers;

import com.magiology.client.renderers.FastNormalRenderer;
import com.magiology.client.rendering.item.SIRRegistry.IItemRenderer;
import com.magiology.client.shaders.effects.PositionAwareEffect;
import com.magiology.client.shaders.programs.MatterJumperShader;
import com.magiology.core.registry.init.ShadersM;
import com.magiology.forge.events.RenderEvents;
import com.magiology.forge.events.TickEvents;
import com.magiology.util.m_extensions.ResourceLocationM;
import com.magiology.util.objs.color.ColorM;
import com.magiology.util.statics.OpenGLM;
import com.magiology.util.statics.UtilC;
import com.magiology.util.statics.math.PartialTicksUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ItemMatterJumperRenderer implements IItemRenderer{
	
	private static final ItemMatterJumperRenderer INSTANCE=new ItemMatterJumperRenderer();
	
	private ItemMatterJumperRenderer(){}
	
	public static ItemMatterJumperRenderer get(){
		return INSTANCE;
	}
	
	@Override
	public void render(ItemStack stack){
		OpenGLM.pushMatrix();
		
		GL11.glPushAttrib(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_FOG);
		OpenGLM.pushMatrix();
		GlStateManager.loadIdentity();
		RenderEvents.MAIN_FRAME_COPY.forceRender();
		OpenGLM.popMatrix();
		OpenGLM.enableLighting();
		
		if(TickEvents.isWorldRendering()){
			PositionAwareEffect.updateViewTransformation();
			GlStateManager.viewport(0, 0, UtilC.getMC().displayWidth, UtilC.getMC().displayHeight);
		}else{
			ScaledResolution res=new ScaledResolution(UtilC.getMC());
			GlStateManager.viewport(0, 0, res.getScaledHeight(), res.getScaledHeight());
		}
		
		GL11.glPopAttrib();
		
		Framebuffer src=UtilC.getMC().getFramebuffer();
		src.bindFramebufferTexture();
		
		MatterJumperShader shader=ShadersM.MATTER_JUMPER;
		//shader.compile();
		float noiseRad=4;
		//noiseRad=UtilC.fluctuateLinSmooth(200, 0, 0, 1);
		//noiseRad*=noiseRad;
		//noiseRad=2+noiseRad*6+RandUtil.CRF(0.2);
		
		shader.activate(RenderEvents.MAIN_FRAME_COPY, new ColorM(UtilC.fluctuateLinSmooth(210, 0, 0.54, 0.56), UtilC.fluctuateLinSmooth(250, 0, 0.54, 0.56), UtilC.fluctuateLinSmooth(320, 0, 0.54, 0.56), 1), UtilC.getWorldTime()*1D+PartialTicksUtil.partialTicks, 10, noiseRad);
		
		src.bindFramebufferTexture();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		OpenGLM.bindTexture(new ResourceLocationM("textures/blocks/coal_level_2.png"));
		
//		OpenGLM.translate(0.5, 0.5, 0.5);
//		OpenGLM.rotate(new Vec3M(
//			UtilC.fluctuateLinSmooth(100, 0,0,1)*360,
//			UtilC.fluctuateLinSmooth(80, 140, 0, 1)*180,
//			UtilC.fluctuateLinSmooth(140, 0, 0, 1)*720).mul(500).sqrt());
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
		shader.deactivate();
		
		OpenGLM.popMatrix();
	}
	
}
