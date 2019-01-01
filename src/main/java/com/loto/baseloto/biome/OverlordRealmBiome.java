package com.loto.baseloto.biome;

import com.loto.baseloto.block.BlockOverlordStone;
import com.loto.baseloto.mob.EntityOverlordMinionMob;
import com.loto.baseloto.reg.CreateBlocks;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraft.world.biome.Biome;
import net.minecraft.init.Blocks;

public class OverlordRealmBiome extends Biome {

    public OverlordRealmBiome(Biome.BiomeProperties properties) {
        super(properties);
        setRegistryName("overlordRealm");
        topBlock = CreateBlocks.overlordStone.getDefaultState();
        fillerBlock = Blocks.COBBLESTONE.getDefaultState();
        decorator.generateFalls = false;
        decorator.treesPerChunk = 3;
        decorator.flowersPerChunk = 10;
        decorator.grassPerChunk = 10;
        decorator.deadBushPerChunk = 0;
        decorator.mushroomsPerChunk = 0;
        decorator.bigMushroomsPerChunk = 0;
        decorator.reedsPerChunk = 0;
        decorator.cactiPerChunk = 0;
        decorator.sandPatchesPerChunk = 0;
        decorator.gravelPatchesPerChunk = 0;
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityOverlordMinionMob.class, 80, 3, 20));
    }
}
