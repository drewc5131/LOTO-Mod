package com.loto.baseloto.reg;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.block.ChrisMachine;

public class CreateBlocks 
{
	public static Block chrisMachine;
	
	@SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	chrisMachine = new ChrisMachine().setUnlocalizedName("chrisMachine").setCreativeTab(DrewMod.tabDrew);
    	event.getRegistry().registerAll(chrisMachine);
    }
    
}
