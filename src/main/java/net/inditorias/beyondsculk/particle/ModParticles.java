package net.inditorias.beyondsculk.particle;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, BeyondSculk.MOD_ID);

    public static void register(IEventBus bus){
        PARTICLE_TYPES.register(bus);
    }

}
