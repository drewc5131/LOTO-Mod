package com.loto.baseloto.reg;

import com.loto.baseloto.block.BlockOverlordStone;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.block.ChrisMachine;

public class CreateBlocks 
{
	public static Block chrisMachine;
	public static Block overlordStone;
    public static void registerBlocks(IForgeRegistry<Block> registry)
    {
    	chrisMachine = new ChrisMachine().setUnlocalizedName("chrisMachine").setCreativeTab(DrewMod.tabDrew);
    	overlordStone = new BlockOverlordStone().setUnlocalizedName("overlordStone").setCreativeTab(DrewMod.tabLoto);

    	registry.registerAll(chrisMachine);

    }
    
}
