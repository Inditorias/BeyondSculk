package net.inditorias.beyondsculk.blocks;

import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.blocks.advancedblocks.ActivatedReinforcedDeepslate;
import net.inditorias.beyondsculk.blocks.advancedblocks.AncientPortalBlock;
import net.inditorias.beyondsculk.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BeyondSculk.MOD_ID);

//    public static final RegistryObject<Block> ACTIVATED_REINFORCED_DEEPSLATE_BLOCK = BLOCKS.register("activated_reinforced_deepslate", () ->
//        new ActivatedReinforcedDeepslate(BlockBehaviour.Properties.of(Material.STONE)
//                .strength(55).explosionResistance(1200)
//                .lightLevel(state -> ActivatedReinforcedDeepslate.lightLevel(state)).sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ACTIVATED_REINFORCED_DEEPSLATE_BLOCK = registerBlock("activated_reinforced_deepslate", () ->
        new ActivatedReinforcedDeepslate(BlockBehaviour.Properties.of(Material.STONE)
                .strength(55).explosionResistance(1200)
                .lightLevel(state -> ActivatedReinforcedDeepslate.lightLevel(state)).sound(SoundType.DEEPSLATE)), CreativeModeTab.TAB_BUILDING_BLOCKS);

    public static final RegistryObject<Block> ANCIENT_PORTAL_BLOCK = registerBlock("ancient_portal", () ->
            new AncientPortalBlock(BlockBehaviour.Properties.of(Material.PORTAL).lightLevel(state -> 15)), CreativeModeTab.TAB_MISC);

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
