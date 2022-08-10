package net.inditorias.beyondsculk.blocks;

import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.blocks.advancedblocks.*;
import net.inditorias.beyondsculk.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BeyondSculk.MOD_ID);

    public static final RegistryObject<Block> ACTIVATED_REINFORCED_DEEPSLATE_BLOCK = registerBlock("activated_reinforced_deepslate", () ->
        new ActivatedReinforcedDeepslate(BlockBehaviour.Properties.of(Material.STONE)
                .strength(-1).explosionResistance(12000)
                .lightLevel(state -> 6).sound(SoundType.DEEPSLATE)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> UNSTABLE_REINFORCED_DEEPSLATE_BLOCK = registerBlock("unstable_reinforced_deepslate", () ->
        new UnstableReinforcedDeepslate(BlockBehaviour.Properties.of(Material.STONE)
                .strength(-1).explosionResistance(12000)
                .lightLevel(state -> 3).sound(SoundType.DEEPSLATE)), CreativeModeTab.TAB_BUILDING_BLOCKS);
    public static final RegistryObject<Block> RESONANT_REINFORCED_DEEPSLATE_BLOCK = registerBlock("resonant_reinforced_deepslate", () ->
        new ResonantReinforcedDeepslate(BlockBehaviour.Properties.of(Material.STONE)
                .strength(-1).explosionResistance(12000)
                .lightLevel(state -> 12).sound(SoundType.DEEPSLATE)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> OTHERWORLD_PORTAL_BLOCK = registerBlock("otherworld_portal_block", () ->
            new OtherworldPortal(BlockBehaviour.Properties.of(Material.PORTAL).noCollission().strength(-1.0F).sound(SoundType.GLASS).lightLevel(context -> 12)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> SCULK_PORTAL_BLOCK = registerBlock("sculk_portal_block", () ->
            new SculkPortal(BlockBehaviour.Properties.of(Material.PORTAL).noCollission().strength(-1.0F).sound(SoundType.GLASS).lightLevel(context -> 5)), CreativeModeTab.TAB_DECORATIONS);

    private static <T extends Block> RegistryObject<Block> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }


    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

}
