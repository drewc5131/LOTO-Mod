package com.loto.baseloto.reg;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.backend.sound.DrewSoundEvents;
import com.loto.baseloto.items.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.common.util.EnumHelper;

public class CreateItems
{
	public static final Item.ToolMaterial oberlordToolMaterial = EnumHelper.addToolMaterial("oberlordToolMaterial", 5, 16542, 20.0F, 16.0F, 30);
	public static final Item.ToolMaterial mythicalToolMaterial = EnumHelper.addToolMaterial("mythicalToolMaterial", 5, 4000, 8.0F, 6.0F, 20);
	public static Item chipmunkcard = new ChipmunkCard().setUnlocalizedName("chipmunkcard").setRegistryName("chipmunkcard").setCreativeTab(DrewMod.tabLoto);
	public static Item Gtx970 = new Gtx970().setUnlocalizedName("Gtx970").setRegistryName("Gtx970").setCreativeTab(DrewMod.tabDrew);
	public static Item KappaFish = new ItemFood(20, 1.0F, true).setUnlocalizedName("kappaFish").setRegistryName("kappaFish").setCreativeTab(DrewMod.tabDrew);
	public static Item oberlordSword = new ItemOberlordSword(oberlordToolMaterial, 5.0F).setUnlocalizedName("OberlordSword").setRegistryName("OberlordSword").setCreativeTab(DrewMod.tabLoto);
	public static Item oberlordAxe = new ItemOberlordAxe(oberlordToolMaterial).setUnlocalizedName("OberlordAxe").setRegistryName("OberlordAxe").setCreativeTab(DrewMod.tabLoto);
	public static Item oberlordPick;
	public static Item fireballlauncher;
	public static Item mythicalPickaxe = new ItemMythicalPickaxe(mythicalToolMaterial).setUnlocalizedName("mythicalPickaxe").setRegistryName("mythicalPickaxe").setCreativeTab(DrewMod.tabLoto);
	public static Item netherShard = new NetherShard().setUnlocalizedName("nethershard").setRegistryName("nethershard").setCreativeTab(DrewMod.tabLoto);
	public static Item despacitoDisk = new DespacitoDisk("despacito", DrewSoundEvents.despacito);
	
	public static ResourceLocation OVERLORD_MINION_DROPS = LootTableList.register(new ResourceLocation("baseloto", "entities/overlord_minion"));
	
    public static void register(IForgeRegistry<Item> registry){
    	registry.registerAll(chipmunkcard, Gtx970, KappaFish, oberlordSword, oberlordAxe, mythicalPickaxe);
    }
    
    public static void registerModels(){
    	DrewMod.proxy.registerRenderers();
    	
    }
    
}
