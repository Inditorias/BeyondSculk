package net.inditorias.beyondsculk.world.dimension;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;


public class ModDimensions {
    public static final ResourceKey<Level> OTHERWORLD_DIM_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(BeyondSculk.MOD_ID, "otherworld"));
    public static final ResourceKey<DimensionType> OTHERWORLD_DIM_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, OTHERWORLD_DIM_KEY.registry());

    public static void register(){
        System.out.println("Registering ModDimensions for " + BeyondSculk.MOD_ID);
    }
}
