package com.magiology.handlers;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Deprecated
@SideOnly(value=Side.CLIENT)
public class ParticleHandler{
//	
//	private TextureManager renderer;
//	private World world=Helper.getTheWorld();
//	private List[] fxLayers={new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
//	private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
//	
//	public ParticleHandler(World world, TextureManager renderer){
//		this.world=world;
//		this.renderer=renderer;
//	}
//	
//	public void addEffect(EntityFXM particle){
//		if(particle==null)return;
//		if(particle.worldObj==null)return;
//		if(particle.worldObj.provider.dimensionId!=world.provider.dimensionId)return;
//		
//		int i=particle.getFXLayer();
//		if(fxLayers[i].size()>=4000-Integer.parseInt(Helper.getMC().effectRenderer.getStatistics()))fxLayers[i].remove(0);
//		fxLayers[i].add(particle);
//	}
//	
//	@SubscribeEvent
//	public void updateEffects(TickEvent.ClientTickEvent event){
//		if(world==null){
//			world=Helper.getTheWorld();
//			if(Helper.getTheWorld()!=null)clearEffects(world);
//		}else{
//			if(Helper.getTheWorld()==null)clearEffects(world);
//			else{
//				World prevWorld=world;
//				world=Helper.getTheWorld();
//				if(prevWorld!=world)clearEffects(world);
//			}
//		}
//		for(int a=0;a<fxLayers.length;a++){
//			int i=a;
//			for(int j=0;j<fxLayers[i].size();++j){
//				EntityFXM entityfx=(EntityFXM)fxLayers[i].get(j);
//				entityfx.onUpdate();
//				if(entityfx==null||entityfx.isDead)fxLayers[i].remove(j--);
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public void renderParticles(RenderWorldLastEvent event){
//		Entity wiewEntity=Helper.getMC().renderViewEntity;
//		float rotX=ActiveRenderInfo.rotationX,rotZ=ActiveRenderInfo.rotationZ,rotYZ=ActiveRenderInfo.rotationYZ,rotXY=ActiveRenderInfo.rotationXY,rotXZ=ActiveRenderInfo.rotationXZ;
//		EntityFXM.interpPosX=Helper.calculateRenderPos(wiewEntity, 'x');
//		EntityFXM.interpPosY=Helper.calculateRenderPos(wiewEntity, 'y');
//		EntityFXM.interpPosZ=Helper.calculateRenderPos(wiewEntity, 'z');
//		for(int k=0;k<3;++k){
//			int i=k;
//			if(!fxLayers[i].isEmpty()){
//				switch (i){
//					case 0:
//					default:renderer.bindTexture(particleTextures);break;
//					case 1:renderer.bindTexture(TextureMap.locationBlocksTexture);break;
//					case 2:renderer.bindTexture(TextureMap.locationItemsTexture);
//				}
//				OpenGLM.color(1.0F, 1.0F, 1.0F, 1.0F);
//				GL11H.SetUpOpaqueRendering(1);
//				NoramlisedVertixBuffer buf=TessHelper.getNVB();
//				buf.cleanUp();
//				for(int j=0;j<fxLayers[i].size();j++){
//					EntityFXM entityfx=(EntityFXM)fxLayers[i].get(j);
//					if(entityfx==null)continue;
//					Tessellator.instance.setBrightness(entityfx.getBrightnessForRender(RenderLoopEvents.partialTicks));
//					//TODO
//					entityfx.renderParticle(buf.createNewSubBuffer(), RenderLoopEvents.partialTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
//				}
//				buf.draw();
//				GL11H.EndOpaqueRendering();
//			}
//		}
//	}
//	public void clearEffects(World world){
//		this.world=world;
//		for(int i=0;i<4;i++)fxLayers[i].clear();
//	}
}
