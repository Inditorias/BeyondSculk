package net.inditorias.beyondsculk.items;

import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.items.advanced.SoulWaterBottle;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BeyondSculk.MOD_ID);

    public static final RegistryObject<Item> BOTTLE_OF_SOUL_WATER = ITEMS.register("soul_water_bottle", () ->
            new SoulWaterBottle(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_BREWING)));
    public static final RegistryObject<Item> RESONANT_CATALYST = ITEMS.register("resonant_catalyst", () -> new SoulWaterBottle(new Item.Properties()));
    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
