package com.loto.baseloto.reg;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.items.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.common.util.EnumHelper;

public class CreateItems
{
	public static final Item.ToolMaterial overlordToolMaterial = EnumHelper.addToolMaterial("overlordToolMaterial", 5, 16542, 40.0F, 32.0F, 30);
	public static final Item.ToolMaterial mythicalToolMaterial = EnumHelper.addToolMaterial("mythicalToolMaterial", 5, 4000, 8.0F, 6.0F, 20);
	public static Item chipmunkcard = new ChipmunkCard().setUnlocalizedName("chipmunkcard").setRegistryName("chipmunkcard").setCreativeTab(DrewMod.tabLoto);
	public static Item Gtx970 = new Gtx970().setUnlocalizedName("gtx970").setRegistryName("gtx970").setCreativeTab(DrewMod.tabDrew);
	public static Item KappaFish = new ItemFood(20, 1.0F, true).setUnlocalizedName("kappafish").setRegistryName("kappaFish").setCreativeTab(DrewMod.tabDrew);
	public static Item overlordSword = new ItemOberlordSword(overlordToolMaterial, 5.0F).setUnlocalizedName("overlordsword").setRegistryName("overlordsword").setCreativeTab(DrewMod.tabLoto);
	public static Item overlordAxe = new ItemOberlordAxe(overlordToolMaterial).setUnlocalizedName("overlordaxe").setRegistryName("overlordaxe").setCreativeTab(DrewMod.tabLoto);
	public static Item overlordPick;
	public static Item fireballlauncher;
	public static Item mythicalPickaxe = new ItemMythicalPickaxe(mythicalToolMaterial).setUnlocalizedName("mythicalpickaxe").setRegistryName("mythicalpickaxe").setCreativeTab(DrewMod.tabLoto);
	public static Item netherShard = new NetherShard().setUnlocalizedName("nethershard").setRegistryName("nethershard").setCreativeTab(DrewMod.tabLoto);
	public static Item despacitoDisk = (new DespacitoDisk("despacito", CreateSounds.despacito)).setUnlocalizedName("despacito").setRegistryName("despacitodisk").setCreativeTab(DrewMod.tabLoto);

	public static ResourceLocation OVERLORD_MINION_DROPS = LootTableList.register(new ResourceLocation("baseloto", "entities/overlord_minion"));
	
    public static void register(){
    	ForgeRegistries.ITEMS.registerAll(chipmunkcard, Gtx970, KappaFish, overlordSword, overlordAxe, mythicalPickaxe, despacitoDisk);
    }
   
    
}
