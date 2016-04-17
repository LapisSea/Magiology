package com.magiology.util.utilobjects.vectors.physics.real.entitymodel;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityPlayerModelColider<T extends EntityPlayer> extends EntityModelColider<T>{
	
	private class ModelBipedColider extends ModelBiped{
		
		public ModelBipedColider(float modelSize)
	    {
	        this(modelSize, 0.0F, 64, 32);
	    }
		public ModelBipedColider(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn)
	    {
	        this.textureWidth = textureWidthIn;
	        this.textureHeight = textureHeightIn;
	        this.bipedHead = new ModelRendererColider(this, 0, 0);
	        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
	        this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	        this.bipedHeadwear = new ModelRendererColider(this, 32, 0);
	        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
	        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	        this.bipedBody = new ModelRendererColider(this, 16, 16);
	        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
	        this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	        this.bipedRightArm = new ModelRendererColider(this, 40, 16);
	        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
	        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
	        this.bipedLeftArm = new ModelRendererColider(this, 40, 16);
	        this.bipedLeftArm.mirror = true;
	        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
	        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
	        this.bipedRightLeg = new ModelRendererColider(this, 0, 16);
	        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
	        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
	        this.bipedLeftLeg = new ModelRendererColider(this, 0, 16);
	        this.bipedLeftLeg.mirror = true;
	        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
	        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
	    }
		
		
		public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale){
			setModelAttributes(EntityPlayerModelColider.this.getMainModel());
			this.isChild=false;
			this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
			matrix.pushMatrix();
			if(entityIn.isSneaking()){
				matrix.translate(0.0F, 3F, 0.0F);
			}
			EntityPlayer player=((EntityPlayer)entityIn);
			boolean 
				hasHelmet=player.getCurrentArmor(3)!=null,
				hasCheastpeace=player.getCurrentArmor(2)!=null,
				hasLegings=player.getCurrentArmor(1)!=null,
				hasBoots=player.getCurrentArmor(0)!=null;
			if(this.isChild){
				float f=2.0F;
				matrix.scale(1.5F/f, 1.5F/f, 1.5F/f);
				matrix.translate(0.0F, 16.0F*scale, 0.0F);
				if(!hasHelmet)this.bipedHead.render(scale);
				matrix.popMatrix();
				matrix.pushMatrix();
				matrix.scale(1.0F/f, 1.0F/f, 1.0F/f);
				matrix.translate(0.0F, 24.0F*scale, 0.0F);
				this.bipedBody.render(scale);
				this.bipedRightArm.render(scale);
				this.bipedLeftArm.render(scale);
				this.bipedRightLeg.render(scale);
				this.bipedLeftLeg.render(scale);
				if(hasHelmet)this.bipedHeadwear.render(scale);
			}else{

				if(!hasHelmet)this.bipedHead.render(scale);
				this.bipedBody.render(scale);
				this.bipedRightArm.render(scale);
				this.bipedLeftArm.render(scale);
				this.bipedRightLeg.render(scale);
				this.bipedLeftLeg.render(scale);
				if(hasHelmet)this.bipedHeadwear.render(scale);
			}

			matrix.popMatrix();
		}
	}
	
	private ModelBipedColider model=new ModelBipedColider(0);

	public EntityPlayerModelColider(RenderPlayer renderPlayer){
		super(renderPlayer.getRenderManager(), renderPlayer.getMainModel(), 0);
		
	}
	@Override
	protected void mainModelGetQuads(T entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale){
		model.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
	}
}
