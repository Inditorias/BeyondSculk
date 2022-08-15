package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blockentities.advanced.ResonantReinforcedDeepslateBlockEntity;
import net.minecraft.core.BlockPos;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ResonantReinforcedDeepslate extends BaseEntityBlock {
    public ResonantReinforcedDeepslate(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ResonantReinforcedDeepslateBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pstate, Level level, BlockPos pos, BlockState pNewState, boolean isMoving) {
        super.onRemove(pstate, level, pos, pNewState, isMoving);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.RESONANT_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(),
                ResonantReinforcedDeepslateBlockEntity::tick);
    }
}
