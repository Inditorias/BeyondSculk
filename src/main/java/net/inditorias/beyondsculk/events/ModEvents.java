package net.inditorias.beyondsculk.events;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = BeyondSculk.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void tryCreateResonantPortalOnSculkMobDeath(LivingDeathEvent event){
        LivingEntity dead = event.getEntity();
        if(dead.getType() == EntityType.WARDEN){
            Level level = event.getEntity().getLevel();

        }
    }



}
