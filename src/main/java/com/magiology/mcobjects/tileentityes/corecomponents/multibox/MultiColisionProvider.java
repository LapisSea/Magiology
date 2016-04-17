package com.magiology.mcobjects.tileentityes.corecomponents.multibox;

import java.util.List;

import com.magiology.util.utilobjects.vectors.Vec3M;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface MultiColisionProvider{
	
	public boolean changesDetected();
	
	public void detectChanges();
	public List<CollisionBox> getBoxes();
	
	public List<Integer> getGhostHits();
	public CollisionBox getMainBox();
	public int getPointedBoxID();
	public List<Integer> getPrevGhostHits();
	
	public int getPrevPointedBoxID();
	public boolean onGhostHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos);
	
	public boolean onNormalHit(EntityPlayer entity, ItemStack stack, boolean isRightClick, int id, EnumFacing side, Vec3M hitPos);
	public void sendChanges();
	public void setChangesDetected(boolean changes);
	public void setGhostHits(List<Integer> hits);
	
	public void setPointedBoxID(int boxID);
	public void setPrevGhostHits(List<Integer> hits);
	public void setPrevPointedBox(int box);
	public void updateColisionBoxes();
}
