package com.loto.baseloto.reg;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import com.loto.baseloto.DrewMod;

public class CreateSounds {
	

	public static SoundEvent machines;
	public static SoundEvent despacito;


	public static void RegisterSounds(IForgeRegistry<SoundEvent> registry)
	{
		despacito = CreateSound("despacito").setRegistryName(new ResourceLocation("baseloto", "despacito"));
		registry.registerAll(despacito);
		
	}
	
	public static SoundEvent CreateSound(String snd){
		ResourceLocation loc = new ResourceLocation("baseloto", snd);
		SoundEvent ev = new SoundEvent(loc);
		return ev;
	}
	
}




