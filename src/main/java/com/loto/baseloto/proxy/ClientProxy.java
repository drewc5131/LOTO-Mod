package com.loto.baseloto.proxy;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.client.render.blocks.BlockRenderRegister;
import com.loto.baseloto.client.render.items.ItemRenderRegister;
import com.loto.baseloto.client.render.mob.RenderOverlordMinionBossMob;
import com.loto.baseloto.client.render.mob.RenderOverlordMinionMob;
import com.loto.baseloto.mob.EntityOverlordMinionBossMob;
import com.loto.baseloto.mob.EntityOverlordMinionMob;
import com.loto.baseloto.reg.CreateBlocks;
import com.loto.baseloto.reg.CreateItems;
import com.loto.baseloto.reg.CreateMobs;
import com.loto.baseloto.reg.CreateSounds;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initmainproxystuff()
	{
		
	}
	
	@Override
	public void registerRenderers()
	{
		ItemRenderRegister.registerItemRenderer();
		BlockRenderRegister.registerBlockRenderer();
		RenderingRegistry.registerEntityRenderingHandler(EntityOverlordMinionMob.class, new RenderOverlordMinionMob(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityOverlordMinionBossMob.class, new RenderOverlordMinionBossMob(Minecraft.getMinecraft().getRenderManager()));

	}

	@Override
	public void createMobs()
	{
		CreateMobs.createMobs();
	}
	
	@Override
	public void createSounds(){
	}
	
    public void rpcinit(DrewMod main) {
        main.rpcClient.init();
    }

    public void rpcupdate(DrewMod main, String details, String largeImgKey, String largeImgText, String smallImgKey, String smallImgText, boolean updateTime) {
        main.rpcClient.updatePresence(details, largeImgKey, largeImgText, smallImgKey, smallImgText, updateTime);
    }
    
    public void updateRpcPlayerCount(DrewMod main){
    	@Nullable String name = Minecraft.getMinecraft().getSession().getUsername();
    	System.out.println(name);
    	String img = name.toLowerCase();
    	
    	if(!Minecraft.getMinecraft().isSingleplayer()){
        rpcupdate(main, "Multiplayer " + Minecraft.getMinecraft().getCurrentServerData().populationInfo, img, name, "default", Minecraft.getMinecraft().getCurrentServerData().serverIP, false);}

    }
    
}
