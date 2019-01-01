package com.loto.baseloto;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import javax.annotation.Nullable;
import javax.swing.JOptionPane;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;
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
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.loto.baseloto.namethings.CommandCustomName;
import com.loto.baseloto.namethings.CustomNamePacket;
import com.loto.baseloto.namethings.CustomNamePacketHandler;
import com.loto.baseloto.proxy.CommonProxy;
import com.loto.baseloto.reg.CreateBlocks;
import com.loto.baseloto.reg.CreateItems;
import com.loto.baseloto.reg.CreateSounds;

@Mod(modid = DrewMod.MODID, version = DrewMod.VERSION)
public class DrewMod {
	public static final String MODID = "baseloto";
	public static final String VERSION = "1.5.4";
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
	public void init(FMLInitializationEvent event) {
		this.rpcClient = new RPCClient("413848023005659137");
		proxy.rpcinit(this);
		proxy.rpcupdate(this, "Loading Pack", "loading", "LOADING", null, null,
				false);

		// Register all the item renderers
		proxy.registerRenderers();
	}

	public static CreativeTabs tabDrew = new CreativeTabs("tabDrew") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(CreateItems.Gtx970);
		}

	};

	public static CreativeTabs tabLoto = new CreativeTabs("tabLoto") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(CreateItems.oberlordSword);
		}
	};

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerSounds(
				RegistryEvent.Register<SoundEvent> event) {
			CreateSounds.RegisterSounds(event.getRegistry());

		}
		

	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.createItems();
		proxy.createBlocks();
		proxy.createMobs();
		network = NetworkRegistry.INSTANCE.newSimpleChannel("LOTONetChannel");
		network.registerMessage(CustomNamePacketHandler.class,
				CustomNamePacket.class, 0, Side.CLIENT);

	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	@SideOnly(Side.CLIENT)
	public void onPostInitEvent(FMLPostInitializationEvent event) {
		proxy.rpcupdate(this, "Main Menu", "mainmenu", "On Main Menu", null,
				null, false);

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandCustomName());
	}

	@SubscribeEvent
    @SideOnly(Side.CLIENT)
	public void guiEvent(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			URL ver;
			try {
				ver = new URL(
						"https://raw.githubusercontent.com/drewc5131/LOTO-Mod/master/ver.txt");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						ver.openStream()));

				String inputLine;
				String version = in.readLine();
				in.close();
				if (Integer.parseInt(version.replace(".", "")) != Integer.parseInt(VERSION.replace(".", ""))) {
					Minecraft.getMinecraft().displayGuiScreen(
							new NewUpdateNotification(version));
					System.out.println(Integer.parseInt(version.replace(".", "")) + ":" + Integer.parseInt(VERSION.replace(".", "")));
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void renderName(PlayerEvent.NameFormat event) {
		NBTTagCompound tag = event.getEntityPlayer().getEntityData();
		if (tag.hasKey("loto_customName")) {
			event.setDisplayname(tag.getString("loto_customName"));
		} else {
			event.setDisplayname(event.getUsername());
		}
	}

	@SubscribeEvent
	public void onJoinWorld(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) {
			WorldServer worldServer = (WorldServer) event.player.world;
			Iterator<? extends EntityPlayer> playersTracking = worldServer
					.getEntityTracker().getTrackingPlayers(player).iterator();
			if (player.getEntityData() != null
					&& player.getEntityData().hasKey("loto_customName")) {
				while (playersTracking.hasNext()) {
					network.sendTo(new CustomNamePacket(player.getEntityData()
							.getString("loto_customName"),
							player.getEntityId(), 0),
							(EntityPlayerMP) playersTracking.next());
				}
			}
		}
		proxy.updateRpcPlayerCount(this);
	}

	@SubscribeEvent
	public void onTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getTarget();
			if (player.getEntityData() != null
					&& player.getEntityData().hasKey("loto_customName")) {
				EntityPlayerMP reciever = (EntityPlayerMP) event
						.getEntityPlayer();
				network.sendTo(
						new CustomNamePacket(player.getEntityData().getString(
								"loto_customName"), player.getEntityId(), 0),
						reciever);
			}
		}
	}

	@SubscribeEvent
	public void onClone(PlayerEvent.Clone event) {
		EntityPlayer original = event.getOriginal();
		EntityPlayer newPlayer = event.getEntityPlayer();
		if (original.getEntityData().hasKey("loto_customName")) {
			newPlayer.getEntityData().setString("loto_customName",
					original.getEntityData().getString("loto_customName"));
		}
	}

	@SubscribeEvent
	public void onClientConnectedToServerEvent(
			FMLNetworkEvent.ClientConnectedToServerEvent event) {
		// String details, String largeImgKey, String largeImgText, String
		// smallImgKey, String smallImgText
		@Nullable
		String name = Minecraft.getMinecraft().getSession().getUsername();
		System.out.println(name);
		String img = name.toLowerCase();

		if (event.isLocal())
			proxy.rpcupdate(this, "Singleplayer", img, name, "default",
					Minecraft.getMinecraft().getIntegratedServer()
							.getWorldName(), true);
		else {
			String playerInfo = Minecraft.getMinecraft().getCurrentServerData().populationInfo;
			proxy.rpcupdate(this, "Multiplayer " + playerInfo, img, name,
					"default",
					Minecraft.getMinecraft().getCurrentServerData().serverIP,
					true);
		}
	}

	@SubscribeEvent
	public void onClientDisconnectionFromServerEvent(
			FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
		proxy.rpcupdate(this, "Main Menu", "mainmenu", "On Main Menu", null,
				null, true);
	}
}

class NewUpdateNotification extends GuiScreen {
	String text = "";
	Minecraft mc;
	String currentVersion;

	public NewUpdateNotification(String version){
		currentVersion = version;
	}
	
	public void initGui() {
		mc = Minecraft.getMinecraft();
		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();

		this.buttonList.add(new GuiButton(0, (this.width / 2) - 50,
				(height / 2) + 20, 100, 20, "Update"));
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		drawCenteredString(mc.fontRenderer,
				"You are currently using an outdated version of the LOTO Mod!",
				width / 2, (height / 2) - 5, Integer.parseInt("FFAA00", 16));

		drawCenteredString(mc.fontRenderer,
				"Please download the latest version.", width / 2,
				(height / 2) + 5, Integer.parseInt("FFAA00", 16));

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {

			try {
				Desktop.getDesktop()
						.browse(new URI(
								"https://github.com/drewc5131/LOTO-Mod/releases/tag/" + currentVersion));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
