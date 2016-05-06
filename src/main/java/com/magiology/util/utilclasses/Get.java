package com.magiology.util.utilclasses;

import com.magiology.client.render.font.FontRendererMBase;
import com.magiology.util.renderers.TessUtil;
import com.magiology.util.renderers.VertexRenderer;
import com.magiology.util.utilclasses.UtilM.U;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class Get{
	@SideOnly(value=Side.CLIENT)
	public static final class Client{
		public static final EntityPlayer EP(){
			return UtilC.getThePlayer();
		}
		public static final boolean running(){
			return !U.isNull(EP(),W());
		}
		public static final World W(){
			return UtilC.getTheWorld();
		}
	}
	@SideOnly(value=Side.CLIENT)
	public static final class Render{
		
		public static class Font{
			public static final FontRenderer FR(){
				return TessUtil.getFontRenderer();
			}

			public static FontRendererMBase FRB(){
				return TessUtil.getCustomFontRednerer();
			}
		}
		public static EffectRenderer ER(){
			return UtilC.getMC().effectRenderer;
		}
		public static ItemModelMesher IMM(){
			return UtilC.getMC().getRenderItem().getItemModelMesher();
		}
		public static final ItemRenderer IR(){
			return UtilC.getMC().getItemRenderer();
		}
		public static final VertexRenderer NVB(){
			return TessUtil.getVB();
		}
		public static final RenderItem RI(){
			return UtilC.getMC().getRenderItem();
		}
		public static Tessellator T(){
			return Tessellator.getInstance();
		}
		public static final VertexBuffer WR(){
			return TessUtil.getWB();
		}
	}
	
}
