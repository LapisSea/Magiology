package com.magiology.forgepowered.packets.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class AbstractToClientMessage extends AbstractPacket{
	public static class SendingTarget{
		public static enum TypeOfSending{
			AroundPoint,ToAll,ToDimension,ToPlayer;
		}
		public EntityPlayer player;
		public TargetPoint point;
		public final TypeOfSending typeOfSending;
		public final World world;
		public SendingTarget(World world){
			typeOfSending=TypeOfSending.ToAll;
			this.world=world;
		}
		public SendingTarget(World world, EntityPlayer player){
			this.player=player;
			typeOfSending=TypeOfSending.ToPlayer;
			this.world=world;
		}
		public SendingTarget(World world,int dimensionId){
			typeOfSending=TypeOfSending.ToDimension;
			this.world=world;
		}
		public SendingTarget(World world, TargetPoint point){
			this.point=point;
			typeOfSending=TypeOfSending.AroundPoint;
			this.world=world;
		}
	}
	public final SendingTarget target;
	public AbstractToClientMessage(){target=null;}
	public AbstractToClientMessage(SendingTarget target){
		this.target=target;
	}
}
