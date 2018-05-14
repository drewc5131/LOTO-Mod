package com.loto.baseloto.reg;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.loto.baseloto.DrewMod;
import com.loto.baseloto.mob.EntityOverlordMinionBossMob;
import com.loto.baseloto.mob.EntityOverlordMinionMob;

public class CreateMobs {
	
    public static void createMobs() 
    {
    	EntityRegistry.registerModEntity(new ResourceLocation("oberlordminion"),
    			EntityOverlordMinionMob.class, "oberlordminion", 6969,
    			DrewMod.modInstance, 64, 1, true, 0x292727, 0xAD2121);
    	
    	EntityRegistry.registerModEntity(new ResourceLocation("oberlordminionBoss"),
    			EntityOverlordMinionBossMob.class, "oberlordminionboss", 6970,
    			DrewMod.modInstance, 64, 1, true, 0x292727, 0xAD2121);
    	
    	EntityRegistry.addSpawn(EntityOverlordMinionMob.class, 50, 2, 4, EnumCreatureType.MONSTER, Biomes.HELL, Biomes.FOREST, Biomes.DEFAULT, Biomes.JUNGLE, Biomes.TAIGA, Biomes.PLAINS, Biomes.EXTREME_HILLS);
    }
    

}
