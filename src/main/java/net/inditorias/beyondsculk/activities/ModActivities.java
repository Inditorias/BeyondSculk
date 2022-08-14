package net.inditorias.beyondsculk.activities;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModActivities {
    public static DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, BeyondSculk.MOD_ID);

    public static void register(IEventBus bus){
        ACTIVITIES.register(bus);
    }

}
