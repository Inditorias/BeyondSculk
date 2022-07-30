package net.inditorias.beyondsculk;

import com.mojang.logging.LogUtils;
import net.inditorias.beyondsculk.activities.ModActivities;
import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.entity.ModEntityTypes;
import net.inditorias.beyondsculk.fluid.ModFluids;
import net.inditorias.beyondsculk.items.ModItems;
import net.inditorias.beyondsculk.villager.ModPOIs;
import net.inditorias.beyondsculk.villager.ModVillagers;
import net.inditorias.beyondsculk.world.dimension.ModDimensions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BeyondSculk.MOD_ID)
public class BeyondSculk
{
    public static final String MOD_ID = "beyondsculk";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BeyondSculk()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModFluids.register(modEventBus);
        ModDimensions.register();
        ModPOIs.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModActivities.register(modEventBus);
        GeckoLib.initialize();
        modEventBus.addListener(this::commonSetup);
        ModEntityTypes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

//        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RESONANT_PORTAL_BLOCK.get(), RenderType.translucent());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.error("Load Beyond Sculk");
        }
    }
}
