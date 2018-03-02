package com.loto.baseloto;

import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.loto.baseloto.namethings.CommandCustomName;
import com.loto.baseloto.namethings.CustomNamePacket;
import com.loto.baseloto.namethings.CustomNamePacketHandler;
import com.loto.baseloto.proxy.CommonProxy;
import com.loto.baseloto.reg.CreateItems;

@Mod(modid = DrewMod.MODID, version = DrewMod.VERSION)
public class DrewMod
{
    public static final String MODID = "baseloto";
    public static final String VERSION = "1.5.1";
    public EventHandler eventHandler = new EventHandler(this);
    
	public RPCClient rpcClient;
    // TODO: Make server proxy
    @SidedProxy(clientSide = "com.loto.baseloto.proxy.ClientProxy", serverSide = "com.loto.baseloto.proxy.ServerProxy")
    public static CommonProxy proxy;
    
    public static SimpleNetworkWrapper network;
    
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
	public void preInit(FMLPreInitializationEvent event){
		network = NetworkRegistry.INSTANCE.newSimpleChannel("LOTONetChannel");
		network.registerMessage(CustomNamePacketHandler.class, CustomNamePacket.class, 0, Side.CLIENT);
		
	}
	
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onPostInitEvent(FMLPostInitializationEvent event) {
        proxy.rpcupdate(this, "Main Menu", "mainmenu", "On Main Menu", null, null);
    }
    
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event){
    	event.registerServerCommand(new CommandCustomName());
    }
    
    @SubscribeEvent
    public void renderName(PlayerEvent.NameFormat event){
    	NBTTagCompound tag = event.getEntityPlayer().getEntityData();
    	if(tag.hasKey("loto_customName")){event.setDisplayname(tag.getString("loto_customName"));}
    	else{event.setDisplayname(event.getUsername());}
    }
    
    @SubscribeEvent
    public void onJoinWorld(PlayerLoggedInEvent event){
    	EntityPlayer player = event.player;
    	if(!player.world.isRemote)
    	{
    		WorldServer worldServer = (WorldServer)event.player.world;
    		Iterator<? extends EntityPlayer> playersTracking = worldServer.getEntityTracker().getTrackingPlayers(player).iterator();
	    	if(player.getEntityData() != null && player.getEntityData().hasKey("loto_customName")){
	    		while(playersTracking.hasNext()){
	    			network.sendTo(new CustomNamePacket(player.getEntityData().getString("loto_customName"), player.getEntityId(), 0), (EntityPlayerMP)playersTracking.next());
				}
    		}
    	}
	}
	
    @SubscribeEvent
    public void onTracking(PlayerEvent.StartTracking event){
    	if(event.getTarget() instanceof EntityPlayer){
    		EntityPlayer player = (EntityPlayer) event.getTarget();
    		if(player.getEntityData() != null && player.getEntityData().hasKey("loto_customName")){
    			 EntityPlayerMP reciever  = (EntityPlayerMP) event.getEntityPlayer();
    			 network.sendTo(new CustomNamePacket(player.getEntityData().getString("loto_customName"), player.getEntityId(), 0), reciever);
    		}
    	}
    }
    
    @SubscribeEvent
    public void onClone(PlayerEvent.Clone event){
    	EntityPlayer original = event.getOriginal();
    	EntityPlayer newPlayer = event.getEntityPlayer();
    	if(original.getEntityData().hasKey("loto_customName")){
    		newPlayer.getEntityData().setString("loto_customName", original.getEntityData().getString("loto_customName"));
    	}
    }
    
}
