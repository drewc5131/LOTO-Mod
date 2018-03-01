package com.loto.baseloto.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.loto.baseloto.block.ChrisMachine;

public class GuiHandler implements IGuiHandler
{

	public static final int CHRIS_MACHINE_GUI = 69;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == CHRIS_MACHINE_GUI) // TODO
			return null;
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

}
