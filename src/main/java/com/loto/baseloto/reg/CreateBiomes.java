package com.loto.baseloto.reg;

import com.loto.baseloto.biome.OverlordRealmBiome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CreateBiomes {

	public static Biome overlordRealmBiome;
	public static void register(){
		
		overlordRealmBiome = new OverlordRealmBiome(new Biome.BiomeProperties("overlordRealmBiome"));
		ForgeRegistries.BIOMES.registerAll(overlordRealmBiome);
	}
	
}
