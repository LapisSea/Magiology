package com.magiology.handlers.obj.handler.revived.yayformc1_8.obj;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class GroupObject
{
  public ArrayList<Face> faces = new ArrayList<Face>();
  public int glDrawingMode;
  public String name;

  public GroupObject()
  {
	this("");
  }

  public GroupObject(String name)
  {
	this(name, -1);
  }

  public GroupObject(String name, int glDrawingMode)
  {
	this.name = name;
	this.glDrawingMode = glDrawingMode;
  }

  @SideOnly(Side.CLIENT)
  public void render()
  {
	if (faces.size() > 0)
	{
	  Tessellator tessellator = Tessellator.getInstance();
	  WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	  worldRenderer.begin(glDrawingMode, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
	  render(worldRenderer);
	  tessellator.draw();
	}
  }

  @SideOnly(Side.CLIENT)
  public void render(WorldRenderer worldRenderer)
  {
	if (faces.size() > 0)
	{
	  for (Face face : faces)
	  {
		face.addFaceForRender(worldRenderer);
	  }
	}
  }
}