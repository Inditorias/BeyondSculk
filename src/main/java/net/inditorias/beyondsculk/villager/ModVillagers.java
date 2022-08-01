package net.inditorias.beyondsculk.villager;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, BeyondSculk.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS,BeyondSculk.MOD_ID);
    public static void register(IEventBus bus){
        POI_TYPES.register(bus);
        VILLAGER_PROFESSIONS.register(bus);
    }
}
