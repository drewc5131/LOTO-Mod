package com.loto.baseloto.namethings;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CustomNamePacket implements IMessage{

	public String customName;
	public int entityId;
	public int delCustomName;
	
	public CustomNamePacket(){}
	
	public CustomNamePacket(String customName, int entityId, int wantDel){
		this.customName = customName;
		this.entityId = entityId;
		this.delCustomName = wantDel;		
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.customName = ByteBufUtils.readUTF8String(buf);
		this.entityId = ByteBufUtils.readVarInt(buf, 4);
		this.delCustomName = ByteBufUtils.readVarInt(buf, 4);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.customName);
		ByteBufUtils.writeVarInt(buf, this.entityId, 4);
		ByteBufUtils.writeVarInt(buf, this.delCustomName, 4);
	}

}
