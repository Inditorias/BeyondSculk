package net.inditorias.beyondsculk.blocks.Vanilla;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class VanillaBlocks {

    public static final DeferredRegister<Block> VANILLA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft");

    public static final RegistryObject<LayeredCauldronBlock> WATER_CAULDRON = registerBlock("water_cauldron",
            () -> new BeyondLayeredCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON), LayeredCauldronBlock.RAIN, CauldronInteraction.WATER));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = VANILLA_BLOCKS.register(name, block);
        return toReturn;
    }

    public static void register(IEventBus bus){
        VANILLA_BLOCKS.register(bus);
    }
}
