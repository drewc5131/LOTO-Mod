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
// this needs to be removed xd
public class EventHandler {

    DrewMod main;
    

    public EventHandler (DrewMod main) {
        this.main = main;
    }
   
    
}