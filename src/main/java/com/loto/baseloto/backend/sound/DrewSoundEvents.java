package com.loto.baseloto.backend.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.loto.baseloto.DrewMod;

public class DrewSoundEvents {
	

	public static SoundEvent machines;
	public static SoundEvent despacito;


	public void RegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(machines, despacito);
	}
	
}




