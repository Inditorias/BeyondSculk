package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blockentities.advanced.ActivatedReinforcedDeepslateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class ActivatedReinforcedDeepslate extends BaseEntityBlock {
    public static final IntegerProperty SOULS = IntegerProperty.create("souls", 0, 16);

    public ActivatedReinforcedDeepslate(Properties properties) {
        super(properties);
    }

    public static int lightLevel(BlockState state){
       return 6;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(SOULS);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ActivatedReinforcedDeepslateBlockEntity(pos, state);
    }

    /*Block Eintity*/

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
        return createTickerHelper(type, ModBlockEntities.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), ActivatedReinforcedDeepslateBlockEntity::tick);
    }
}
