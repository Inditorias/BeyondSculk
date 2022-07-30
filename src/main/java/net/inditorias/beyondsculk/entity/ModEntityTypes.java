package net.inditorias.beyondsculk.entity;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BeyondSculk.MOD_ID);

    public static void register(IEventBus bus){
        ENTITY_TYPES.register(bus);
    }
}
