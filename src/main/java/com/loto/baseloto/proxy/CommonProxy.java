package com.loto.baseloto.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.gui.GuiHandler;



public class CommonProxy
{
	
	public void initmainproxystuff()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(DrewMod.modInstance, new GuiHandler());
	}

	public void registerRenderers()
	{
	}
	
	public void createBlocks()
	{
	}
	
	public void createItems()
	{
	}
	
	public void createMobs()
	{
	}

	public void rpcupdate(DrewMod main, String details, String largeImgKey, String largeImgText, String smallImgKey, String smallImgText) {
		// TODO Auto-generated method stub
		
	}

	public void rpcinit(DrewMod drewMod) {
		// TODO Auto-generated method stub
		
	}
	
	
}