package net.inditorias.beyondsculk.blockentities;

import net.inditorias.beyondsculk.BeyondSculk;
import net.inditorias.beyondsculk.blockentities.advanced.ActivatedReinforcedDeepslateBlockEntity;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BeyondSculk.MOD_ID);

    public static final RegistryObject<BlockEntityType<ActivatedReinforcedDeepslateBlockEntity>> ACTIVATED_REINFORCED_DEEPSLATE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("activated_reinforced_deepslate_block_entity", () -> BlockEntityType.Builder.of(ActivatedReinforcedDeepslateBlockEntity::new, ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK.get()).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
