package com.magiology.client.render.font;

import static com.magiology.util.utilclasses.UtilM.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.magiology.util.renderers.OpenGLM;
import com.magiology.util.renderers.Renderer;
import com.magiology.util.utilclasses.Get.Render.Font;
import com.magiology.util.utilclasses.UtilC;
import com.magiology.util.utilobjects.ColorF;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FontRendererMBase extends FontRenderer{

	protected static final ResourceLocation[] unicodePageLocations=new ResourceLocation[256];
	/**
	 * Digests a string for nonprinting formatting characters then returns a
	 * string containing only that formatting.
	 */
	public static String getFormatFromString(String p_78282_0_){
		String s1="";
		int i=-1;
		int j=p_78282_0_.length();
		while((i=p_78282_0_.indexOf(167, i+1))!=-1){
			if(i<j-1){
				char c0=p_78282_0_.charAt(i+1);
				if(isFormatColor(c0)){
					s1="\u00a7"+c0;
				}else if(isFormatSpecial(c0)){
					s1=s1+"\u00a7"+c0;
				}
			}
		}
		return s1;
	}
	/**
	 * Checks if the char code is a hexadecimal character, used to set colour.
	 */
	protected static boolean isFormatColor(char colorChar){
		return colorChar>=48&&colorChar<=57||colorChar>=97&&colorChar<=102||colorChar>=65&&colorChar<=70;
	}
	/**
	 * Checks if the char code is O-K...lLrRk-o... used to set special
	 * formatting.
	 */
	protected static boolean isFormatSpecial(char formatChar){
		return formatChar>=107&&formatChar<=111||formatChar>=75&&formatChar<=79||formatChar==114||formatChar==82;
	}
	protected ColorF color=new ColorF();
	
	protected int colorCode[]=new int[32],textColor;
	protected boolean randomStyle, boldStyle, italicStyle,underlineStyle,strikethroughStyle;
	protected final TextureManager renderEngine;

	public FontRendererMBase(ResourceLocation res){
		super(UtilC.getMC().gameSettings, res, UtilC.getMC().renderEngine, false);
		renderEngine=UtilC.getMC().renderEngine;
		bindTexture(locationFontTexture);
		for(int i=0;i<32;++i){
			int j=(i>>3&1)*85;
			int k=(i>>2&1)*170+j;
			int l=(i>>1&1)*170+j;
			int i1=(i>>0&1)*170+j;
			if(i==6){
				k+=85;
			}
			if(UtilC.getMC().gameSettings.anaglyph){
				int j1=(k*30+l*59+i1*11)/100;
				int k1=(k*30+l*70)/100;
				int l1=(k*30+i1*70)/100;
				k=j1;
				l=k1;
				i1=l1;
			}
			if(i>=16){
				k/=4;
				l/=4;
				i1/=4;
			}
			colorCode[i]=(k&255)<<16|(l&255)<<8|i1&255;
		}
		onResourceManagerReload(null);
	}

	/**
	 * Apply Unicode Bidirectional Algorithm to string and return a new possibly
	 * reordered string for visual rendering.
	 */
	protected String bidiReorder(String p_147647_1_){
		try{
			Bidi bidi=new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
			bidi.setReorderingMode(0);
			return bidi.writeReordered(2);
		}catch(ArabicShapingException arabicshapingexception){
			return p_147647_1_;
		}
	}

	@Override
	protected void doDraw(float f){
		OpenGLM.disableTexture2D();
		if(strikethroughStyle){
			Renderer.POS.beginQuads();
			Renderer.POS.addVertex(posX, posY+FONT_HEIGHT/2, 0.0D);
			Renderer.POS.addVertex(posX+f, posY+FONT_HEIGHT/2, 0.0D);
			Renderer.POS.addVertex(posX+f, posY+FONT_HEIGHT/2-1.0F, 0.0D);
			Renderer.POS.addVertex(posX, posY+FONT_HEIGHT/2-1.0F, 0.0D);
			Renderer.POS.draw();
		}
		if(underlineStyle){
			Renderer.POS.beginQuads();
			int l=underlineStyle?-1:0;
			Renderer.POS.addVertex(posX+l, posY+FONT_HEIGHT, 0.0D);
			Renderer.POS.addVertex(posX+f, posY+FONT_HEIGHT, 0.0D);
			Renderer.POS.addVertex(posX+f, posY+FONT_HEIGHT-1.0F, 0.0D);
			Renderer.POS.addVertex(posX+l, posY+FONT_HEIGHT-1.0F, 0.0D);
			Renderer.POS.draw();
		}
		OpenGLM.enableTexture2D();
		posX+=((int)f);
	}

	/**
	 * Splits and draws a String with wordwrap (maximum length is parameter k)
	 */
	@Override
	public void drawSplitString(String str, int x, int y, int wrapWidth, int textColor){
		resetStyles();
		this.textColor=textColor;
		str=trimStringNewline(str);
		renderSplitString(str, x, y, wrapWidth, false);
	}

	/**
	 * Draws the specified string. Args: string, x, y, color, dropShadow
	 */
	@Override
	public int drawString(String p_175065_1_, float p_175065_2_, float p_175065_3_, int p_175065_4_, boolean p_175065_5_){
		enableAlpha();
		resetStyles();
		int j;
		if(p_175065_5_){
			j=renderString(p_175065_1_, p_175065_2_+1.0F, p_175065_3_+1.0F, p_175065_4_, true);
			j=Math.max(j, renderString(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false));
		}else{
			j=renderString(p_175065_1_, p_175065_2_, p_175065_3_, p_175065_4_, false);
		}
		return j;
	}

	/**
	 * Draws the specified string.
	 */
	@Override
	public int drawString(String text, int x, int y, int color){
		return drawString(text, x, y, color, false);
	}

	/**
	 * Draws the specified string with a shadow.
	 */
	@Override
	public int drawStringWithShadow(String p_175063_1_, float p_175063_2_, float p_175063_3_, int p_175063_4_){
		return drawString(p_175063_1_, p_175063_2_, p_175063_3_, p_175063_4_, true);
	}

	/**
	 * Get bidiFlag that controls if the Unicode Bidirectional Algorithm should
	 * be run before rendering any string
	 */
	@Override
	public boolean getBidiFlag(){
		return Font.FR().getBidiFlag();
	}

	/**
	 * Returns the width of this character as rendered.
	 */
	@Override
	public int getCharWidth(char p_78263_1_){
		if(p_78263_1_==167){
			return -1;
		}else if(p_78263_1_==32){
			return 4;
		}else if(p_78263_1_==32){
			return 4;
		}else if(p_78263_1_=='\t'){
			return 16;
		}else{
			int i="\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
					.indexOf(p_78263_1_);
			if(p_78263_1_>0&&i!=-1&&!getUnicodeFlag()){
				return charWidth[i];
			}else if(glyphWidth[p_78263_1_]!=0){
				int j=glyphWidth[p_78263_1_]>>>4;
				int k=glyphWidth[p_78263_1_]&15;
				++k;
				return (k-j)/2+1;
			}else{
				return 0;
			}
		}
	}

	/**
	 * Returns the width of this string. Equivalent of
	 * FontMetrics.stringWidth(String s).
	 */
	@Override
	public int getStringWidth(String p_78256_1_){
		if(p_78256_1_==null){
			return 0;
		}else{
			int i=0;
			boolean flag=false;
			for(int j=0;j<p_78256_1_.length();++j){
				char c0=p_78256_1_.charAt(j);
				int k=getCharWidth(c0);
				if(k<0&&j<p_78256_1_.length()-1){
					++j;
					c0=p_78256_1_.charAt(j);
					if(c0!=108&&c0!=76){
						if(c0==114||c0==82){
							flag=false;
						}
					}else{
						flag=true;
					}
					k=0;
				}
				i+=k;
				if(flag&&k>0){
					++i;
				}
			}
			return i;
		}
	}

	/**
	 * Get unicodeFlag controlling whether strings should be rendered with
	 * Unicode fonts instead of the default.png font.
	 */
	@Override
	public boolean getUnicodeFlag(){
		return Font.FR().getUnicodeFlag();
	}

	protected ResourceLocation getUnicodePageLocation(int p_111271_1_){
		if(unicodePageLocations[p_111271_1_]==null){
			unicodePageLocations[p_111271_1_]=new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[]{Integer.valueOf(p_111271_1_)}));
		}
		return unicodePageLocations[p_111271_1_];
	}

	/**
	 * Breaks a string into a list of pieces that will fit a specified width.
	 */
	@Override
	public List listFormattedStringToWidth(String str, int wrapWidth){
		return Arrays.asList(wrapFormattedStringToWidthM(str, wrapWidth).split("\n"));
	}

	/**
	 * Load one of the /font/glyph_XX.png into a new GL texture and store the
	 * texture ID in glyphTextureName array.
	 */
	protected void loadGlyphTexture(int p_78257_1_){
		bindTexture(getUnicodePageLocation(p_78257_1_));
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		readFontTexture();
		readGlyphSizes();
	}

	protected void readFontTexture(){
		BufferedImage bufferedimage;
		try{
			bufferedimage=TextureUtil.readBufferedImage(getResource(locationFontTexture).getInputStream());
		}catch(IOException ioexception){
			throw new RuntimeException(ioexception);
		}
		int i=bufferedimage.getWidth();
		int j=bufferedimage.getHeight();
		int[] aint=new int[i*j];
		bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
		int k=j/16;
		int l=i/16;
		byte b0=1;
		float f=8.0F/l;
		int i1=0;
		while(i1<256){
			int j1=i1%16;
			int k1=i1/16;
			if(i1==32){
				charWidth[i1]=3+b0;
			}
			int l1=l-1;
			while(true){
				if(l1>=0){
					int i2=j1*l+l1;
					boolean flag=true;
					for(int j2=0;j2<k&&flag;++j2){
						int k2=(k1*l+j2)*i;
						if((aint[i2+k2]>>24&255)!=0){
							flag=false;
						}
					}
					if(flag){
						--l1;
						continue;
					}
				}
				++l1;
				charWidth[i1]=(int)(0.5D+l1*f)+b0;
				++i1;
				break;
			}
		}
	}

	protected void readGlyphSizes(){
		InputStream inputstream=null;
		try{
			inputstream=getResource(new ResourceLocation("font/glyph_sizes.bin")).getInputStream();
			inputstream.read(glyphWidth);
		}catch(IOException ioexception){
			throw new RuntimeException(ioexception);
		}finally{
			IOUtils.closeQuietly(inputstream);
		}
	}

	/**
	 * Pick how to render a single character and return the width used.
	 */
	protected float renderCharAtPos(int p_78278_1_, char p_78278_2_, boolean p_78278_3_){
		return p_78278_2_==32?4.0F
				:("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(p_78278_2_)!=-1&&!getUnicodeFlag()?renderDefaultChar(p_78278_1_, p_78278_3_):renderUnicodeChar(p_78278_2_, p_78278_3_));
	}

	/**
	 * Render a single character with the default.png font at current
	 * (posX,posY) location...
	 */
	@Override
	protected float renderDefaultChar(int p_78266_1_, boolean p_78266_2_){
		if(p_78266_1_=='\t'){
			return getCharWidth(' ')*4;
		}
		float f=p_78266_1_%16*8;
		float f1=p_78266_1_/16*8;
		float f2=p_78266_2_?1.0F:0.0F;
		bindTexture(locationFontTexture);
		float f3=charWidth[p_78266_1_]-0.01F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(f/128.0F, f1/128.0F);
		GL11.glVertex3f(posX+f2, posY, 0.0F);
		GL11.glTexCoord2f(f/128.0F, (f1+7.99F)/128.0F);
		GL11.glVertex3f(posX-f2, posY+7.99F, 0.0F);
		GL11.glTexCoord2f((f+f3-1.0F)/128.0F, f1/128.0F);
		GL11.glVertex3f(posX+f3-1.0F+f2, posY, 0.0F);
		GL11.glTexCoord2f((f+f3-1.0F)/128.0F, (f1+7.99F)/128.0F);
		GL11.glVertex3f(posX+f3-1.0F-f2, posY+7.99F, 0.0F);
		GL11.glEnd();
		return charWidth[p_78266_1_];
	}

	/**
	 * Perform actual work of rendering a multi-line string with wordwrap and
	 * with darker drop shadow color if flag is set
	 */
	protected void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow){
		List list=listFormattedStringToWidth(str, wrapWidth);
		for(Iterator iterator=list.iterator();iterator.hasNext();y+=FONT_HEIGHT){
			String s1=(String)iterator.next();
			renderStringAligned(s1, x, y, wrapWidth, textColor, addShadow);
		}
	}

	/**
	 * Render single line string by setting GL color, current (posX,posY), and
	 * calling renderStringAtPos()
	 */
	protected int renderString(String p_180455_1_, float p_180455_2_, float p_180455_3_, int p_180455_4_, boolean p_180455_5_){
		if(p_180455_1_==null){
			return 0;
		}else{
			if(getBidiFlag()){
				p_180455_1_=bidiReorder(p_180455_1_);
			}
			if((p_180455_4_&-67108864)==0){
				p_180455_4_|=-16777216;
			}
			if(p_180455_5_){
				p_180455_4_=(p_180455_4_&16579836)>>2|p_180455_4_&-16777216;
			}
			color.r=(p_180455_4_>>16&255)/255.0F;
			color.g=(p_180455_4_>>8&255)/255.0F;
			color.b=(p_180455_4_&255)/255.0F;
			color.a=(p_180455_4_>>24&255)/255.0F;
			color.bind();
			posX=p_180455_2_;
			posY=p_180455_3_;
			renderStringAtPos(p_180455_1_, p_180455_5_);
			return (int)posX;
		}
	}

	/**
	 * Render string either left or right aligned depending on bidiFlag
	 */
	protected int renderStringAligned(String p_78274_1_, int p_78274_2_, int p_78274_3_, int p_78274_4_, int p_78274_5_, boolean p_78274_6_){
		if(getBidiFlag()){
			int i1=getStringWidth(bidiReorder(p_78274_1_));
			p_78274_2_=p_78274_2_+p_78274_4_-i1;
		}
		return renderString(p_78274_1_, p_78274_2_, p_78274_3_, p_78274_5_, p_78274_6_);
	}

	/**
	 * Render a single line string at the current (posX,posY) and update posX
	 */
	protected void renderStringAtPos(String p_78255_1_, boolean p_78255_2_){
		for(int i=0;i<p_78255_1_.length();++i){
			char c0=p_78255_1_.charAt(i);
			int j;
			int k;
			if(c0==167&&i+1<p_78255_1_.length()){
				j="0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i+1));
				if(j<16){
					randomStyle=false;
					boldStyle=false;
					strikethroughStyle=false;
					underlineStyle=false;
					italicStyle=false;
					if(j<0||j>15){
						j=15;
					}
					if(p_78255_2_){
						j+=16;
					}
					k=colorCode[j];
					textColor=k;
					color=new ColorF((k>>16)/255.0F, (k>>8&255)/255.0F, (k&255)/255.0F, color.a);
					color.bind();
				}else if(j==16){
					randomStyle=true;
				}else if(j==17){
					boldStyle=true;
				}else if(j==18){
					strikethroughStyle=true;
				}else if(j==19){
					underlineStyle=true;
				}else if(j==20){
					italicStyle=true;
				}else if(j==21){
					randomStyle=false;
					boldStyle=false;
					strikethroughStyle=false;
					underlineStyle=false;
					italicStyle=false;
					color.bind();
				}
				++i;
			}else{
				j="\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(c0);
				if(randomStyle&&j!=-1){
					do{
						k=fontRandom.nextInt(charWidth.length);
					}while(charWidth[j]!=charWidth[k]);
					j=k;
				}
				float f1=getCharWidth(c0)/32f;
				boolean flag1=(c0==0||j==-1||getUnicodeFlag())&&p_78255_2_;
				if(flag1){
					posX-=f1;
					posY-=f1;
				}
				float f=renderCharAtPos(j, c0, italicStyle);
				if(flag1){
					posX+=f1;
					posY+=f1;
				}
				if(boldStyle){
					posX+=f1;
					if(flag1){
						posX-=f1;
						posY-=f1;
					}
					renderCharAtPos(j, c0, italicStyle);
					posX-=f1;
					if(flag1){
						posX+=f1;
						posY+=f1;
					}
					++f;
				}
				doDraw(f);
			}
		}
	}

	/**
	 * Render a single Unicode character at current (posX,posY) location using
	 * one of the /font/glyph_XX.png files...
	 */
	@Override
	protected float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_){
		if(p_78277_1_=='\t'){
			return 16;
		}
		if(glyphWidth[p_78277_1_]==0){
			return 0.0F;
		}else{
			int i=p_78277_1_/256;
			loadGlyphTexture(i);
			int j=glyphWidth[p_78277_1_]>>>4;
			int k=glyphWidth[p_78277_1_]&15;
			float f=j;
			float f1=k+1;
			float f2=p_78277_1_%16*16+f;
			float f3=(p_78277_1_&255)/16*16;
			float f4=f1-f-0.02F;
			float f5=p_78277_2_?1.0F:0.0F;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			GL11.glTexCoord2f(f2/256.0F, f3/256.0F);
			GL11.glVertex3f(posX+f5, posY, 0.0F);
			GL11.glTexCoord2f(f2/256.0F, (f3+15.98F)/256.0F);
			GL11.glVertex3f(posX-f5, posY+7.99F, 0.0F);
			GL11.glTexCoord2f((f2+f4)/256.0F, f3/256.0F);
			GL11.glVertex3f(posX+f4/2.0F+f5, posY, 0.0F);
			GL11.glTexCoord2f((f2+f4)/256.0F, (f3+15.98F)/256.0F);
			GL11.glVertex3f(posX+f4/2.0F-f5, posY+7.99F, 0.0F);
			GL11.glEnd();
			return (f1-f)/2.0F+1.0F;
		}
	}

	/**
	 * Reset all style flag fields in the class to false; called at the start of
	 * string rendering
	 */
	protected void resetStyles(){
		randomStyle=false;
		boldStyle=false;
		italicStyle=false;
		underlineStyle=false;
		strikethroughStyle=false;
	}

	/**
	 * Set bidiFlag to control if the Unicode Bidirectional Algorithm should be
	 * run before rendering any string.
	 */
	@Override
	public void setBidiFlag(boolean p_78275_1_){
		Font.FR().setBidiFlag(p_78275_1_);
	}

	/**
	 * Set unicodeFlag controlling whether strings should be rendered with
	 * Unicode fonts instead of the default.png font.
	 */
	@Override
	public void setUnicodeFlag(boolean p_78264_1_){
		Font.FR().setUnicodeFlag(p_78264_1_);
	}

	/**
	 * Determines how many characters from the string will fit into the
	 * specified width.
	 */
	protected int sizeStringToWidth(String str, int wrapWidth){
		int j=str.length();
		int k=0;
		int l=0;
		int i1=-1;
		for(boolean flag=false;l<j;++l){
			char c0=str.charAt(l);
			switch(c0){
			case 10:
				--l;
				break;
			case 167:
				if(l<j-1){
					++l;
					char c1=str.charAt(l);
					if(c1!=108&&c1!=76){
						if(c1==114||c1==82||isFormatColor(c1)){
							flag=false;
						}
					}else{
						flag=true;
					}
				}
				break;
			case 32:
				i1=l;
			default:
				k+=getCharWidth(c0);
				if(flag){
					++k;
				}
			}
			if(c0==10){
				++l;
				i1=l;
				break;
			}
			if(k>wrapWidth){
				break;
			}
		}
		return l!=j&&i1!=-1&&i1<l?i1:l;
	}

	/**
	 * Returns the width of the wordwrapped String (maximum length is parameter
	 * k)
	 */
	@Override
	public int splitStringWidth(String p_78267_1_, int p_78267_2_){
		return FONT_HEIGHT*listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
	}

	/**
	 * Remove all newline characters from the end of the string
	 */
	protected String trimStringNewline(String p_78273_1_){
		while(p_78273_1_!=null&&p_78273_1_.endsWith("\n")){
			p_78273_1_=p_78273_1_.substring(0, p_78273_1_.length()-1);
		}
		return p_78273_1_;
	}

	/**
	 * Trims a string to fit a specified Width.
	 */
	@Override
	public String trimStringToWidth(String p_78269_1_, int p_78269_2_){
		return trimStringToWidth(p_78269_1_, p_78269_2_, false);
	}

	/**
	 * Trims a string to a specified width, and will reverse it if par3 is set.
	 */
	@Override
	public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_){
		StringBuilder stringbuilder=new StringBuilder();
		int j=0;
		int k=p_78262_3_?p_78262_1_.length()-1:0;
		int l=p_78262_3_?-1:1;
		boolean flag1=false;
		boolean flag2=false;
		for(int i1=k;i1>=0&&i1<p_78262_1_.length()&&j<p_78262_2_;i1+=l){
			char c0=p_78262_1_.charAt(i1);
			int j1=getCharWidth(c0);
			if(flag1){
				flag1=false;
				if(c0!=108&&c0!=76){
					if(c0==114||c0==82){
						flag2=false;
					}
				}else{
					flag2=true;
				}
			}else if(j1<0){
				flag1=true;
			}else{
				j+=j1;
				if(flag2){
					++j;
				}
			}
			if(j>p_78262_2_){
				break;
			}
			if(p_78262_3_){
				stringbuilder.insert(0, c0);
			}else{
				stringbuilder.append(c0);
			}
		}
		return stringbuilder.toString();
	}

	/**
	 * Inserts newline and formatting into a string to wrap it within the
	 * specified width.
	 */
	public String wrapFormattedStringToWidthM(String str, int wrapWidth){
		int j=sizeStringToWidth(str, wrapWidth);
		if(str.length()<=j){
			return str;
		}else{
			String s1=str.substring(0, j);
			char c0=str.charAt(j);
			boolean flag=c0==32||c0==10;
			String s2=getFormatFromString(s1)+str.substring(j+(flag?1:0));
			return s1+"\n"+wrapFormattedStringToWidthM(s2, wrapWidth);
		}
	}
}