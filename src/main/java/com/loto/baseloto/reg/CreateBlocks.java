package com.loto.baseloto.reg;

import com.loto.baseloto.block.BlockOverlordStone;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.block.ChrisMachine;

public class CreateBlocks 
{
	public static Block chrisMachine;
	public static Block overlordStone;
	
	public static Item chrisMachineItem;
	public static Item overlordStoneItem;
	
    public static void registerBlocks()
    {
    	chrisMachine = new ChrisMachine().setUnlocalizedName("chrismachine").setRegistryName("chrismachine").setCreativeTab(DrewMod.tabDrew);
    	overlordStone = new BlockOverlordStone().setUnlocalizedName("overlordstone").setCreativeTab(DrewMod.tabLoto);
		
    	chrisMachineItem = new ItemBlock(chrisMachine).setRegistryName(chrisMachine.getRegistryName());
    	overlordStoneItem = new ItemBlock(overlordStone).setRegistryName(overlordStone.getRegistryName());

    	System.out.println("[LOTO] Registering Blocks...");
    	ForgeRegistries.BLOCKS.registerAll(chrisMachine, overlordStone);
    	ForgeRegistries.ITEMS.registerAll(chrisMachineItem, overlordStoneItem);
    	System.out.println("[LOTO] Registering Blocks... DONE");

    }
    
}
