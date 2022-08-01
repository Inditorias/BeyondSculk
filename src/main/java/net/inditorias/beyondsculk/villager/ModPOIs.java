package net.inditorias.beyondsculk.villager;

import com.google.common.collect.ImmutableSet;
import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ResonantPortalBlock;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;

public class ModPOIs {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, BeyondSculk.MOD_ID);

    public static final RegistryObject<PoiType> RESONANT_PORTAL = POI.register("resonant_portal",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.RESONANT_PORTAL_BLOCK.get().getStateDefinition().getPossibleStates())
            , 0, 1));

    public static void register(IEventBus bus){
        POI.register(bus);
    }
}
