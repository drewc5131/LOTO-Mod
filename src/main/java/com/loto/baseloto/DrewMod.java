package com.loto.baseloto;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.loto.baseloto.proxy.CommonProxy;
import com.loto.baseloto.reg.CreateItems;

@Mod(modid = DrewMod.MODID, version = DrewMod.VERSION)
public class DrewMod
{
    public static final String MODID = "baseloto";
    public static final String VERSION = "1.4.3";
    public EventHandler eventHandler = new EventHandler(this);
	public RPCClient rpcClient;
    // TODO: Make server proxy
    @SidedProxy(clientSide = "com.loto.baseloto.proxy.ClientProxy", serverSide = "com.loto.baseloto.proxy.ServerProxy")
    public static CommonProxy proxy;
    
    @Metadata
    public static ModMetadata meta;
    
    @Instance(DrewMod.MODID)
    public static DrewMod modInstance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	this.rpcClient = new RPCClient("413848023005659137");
        proxy.rpcinit(this);
        proxy.rpcupdate(this, "Loading Pack", "loading", "LOADING", null, null);
    	// items blocks and mobs
    	proxy.createItems();
    	proxy.createBlocks();
    	proxy.createMobs();
    	
    	// Register all the item renderers
    	proxy.registerRenderers(); 


       	
    }
	public static CreativeTabs tabDrew = new CreativeTabs("tabDrew"){
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(CreateItems.Gtx970);
		}
		
		
	};
	
	public static CreativeTabs tabLoto = new CreativeTabs("tabLoto")
	{
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(CreateItems.oberlordSword);
		}
	};
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			CreateItems.register(event.getRegistry());
		}
		@SubscribeEvent
		public static void registerItems(ModelRegistryEvent event) {
			CreateItems.registerModels();
		}

	}
	
    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onPostInitEvent(FMLPostInitializationEvent event) {
        proxy.rpcupdate(this, "Main Menu", "mainmenu", "On Main Menu", null, null);
}
	
}
