package com.loto.baseloto;

import java.util.Collection;

import javax.annotation.Nullable;

import scala.actors.threadpool.Arrays;

import com.mojang.authlib.properties.Property;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class EventHandler {

    DrewMod main;
    
    String knownUsers = "DrewC5131 Chipmunk48 Diamondback88 EBroGames WendelCyka";

    public EventHandler (DrewMod main) {
        this.main = main;
    }
    
    @SubscribeEvent
    public void onClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
    	//String details, String largeImgKey, String largeImgText, String smallImgKey, String smallImgText
    	@Nullable String name = Minecraft.getMinecraft().getSession().getUsername();
    	System.out.println(name);
    	String img = name.toLowerCase();
        
		if (event.isLocal())
            main.proxy.rpcupdate(main, "In Game", img, name, "default", "Singleplayer");
        else
            main.proxy.rpcupdate(main, "In Game", img, name, "default", "On the LOTO Server");
    }
    
    @SubscribeEvent
    public void onClientDisconnectionFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event){
    	main.proxy.rpcupdate(main, "Main Menu", "default", "On Main Menu", null, null);
    }
    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerLoggedInEvent event)
   {
		if (!event.player.world.isRemote)
			event.player.sendMessage(new TextComponentString("§2LOTO MOD 1.4.1\n§r"
					+ "-Tweaked Rich Presense\n"
					+ "-Added natural spawning for Overlord Minions\n"
					+ "§2LOTO MOD 1.4\n§r"
					+ "-Fix rendering for ChipmunkSummoningCard and KappaFish\n"
					+ "-Fix item names\n"
					+ "-Fix ChipmunkSummoningCard behavior\n"
					+ "-Add tiny delay to overlord sword between shooting fireballs to ensure they dont overlap and set eachother off\n"
					+ "-Added Discord Rich Presense"));
   }
    
}