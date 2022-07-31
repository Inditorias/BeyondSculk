package net.inditorias.beyondsculk.blockentities.advanced;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ActivatedReinforcedDeepslateBlockEntity extends BlockEntity {
    public ActivatedReinforcedDeepslateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ActivatedReinforcedDeepslateBlockEntity blockEntity){

    }
}
