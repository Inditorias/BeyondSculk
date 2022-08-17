package net.inditorias.beyondsculk.world.biome;

import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.world.dimension.ModDimensions;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, BeyondSculk.MOD_ID);

    public static final ResourceKey<Biome> VOLCANIC_BEACH = register("volcanic_beach");
    public static final ResourceKey<Biome> MUSHROOM_PLAINS = register("mooshroom_plains");

    public static ResourceLocation name(String name){
        return new ResourceLocation(BeyondSculk.MOD_ID, name);
    }

    public static ResourceKey<Biome> register(String name){
        BIOMES.register(name, OverworldBiomes::theVoid);
        return ResourceKey.create(Registry.BIOME_REGISTRY, name(name));
    }

    public static void register(IEventBus bus){
        BIOMES.register(bus);
    }

}
