package com.loto.baseloto.client.render.items;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.reg.CreateItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemRenderRegister
{
	
	public static String modid = DrewMod.MODID;

	public static void registerItemRenderer()
	{
	    reg(CreateItems.chipmunkcard, 0);
	    reg(CreateItems.Gtx970, 0);
	    reg(CreateItems.KappaFish, 0);
	    reg(CreateItems.oberlordSword, 0);
	    reg(CreateItems.oberlordAxe, 0);
	    reg(CreateItems.mythicalPickaxe, 0);
	}

	public static void reg(Item item, int meta)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

}
