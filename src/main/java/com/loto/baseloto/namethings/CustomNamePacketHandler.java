package com.loto.baseloto.namethings;

import com.loto.baseloto.DrewMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomNamePacketHandler implements IMessageHandler<CustomNamePacket, IMessage> {

	@Override
	public IMessage onMessage(final CustomNamePacket message, final MessageContext ctx) {
		IThreadListener mainThread = Minecraft.getMinecraft();
		
		mainThread.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityId);
				
				if(player != null){
					NBTTagCompound tag = player.getEntityData();
					if(message.delCustomName == 0){
						tag.setString("loto_customName", message.customName);
					}
					else{
						tag.removeTag("loto_customName");
					}
					player.refreshDisplayName();
				}
			}
		});
		
		return null;
	}

}
