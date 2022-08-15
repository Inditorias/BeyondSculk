package net.inditorias.beyondsculk.blocks.Vanilla;

import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.SoulCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


import java.util.Map;
import java.util.function.Predicate;

public class BeyondLayeredCauldronBlock extends LayeredCauldronBlock {
    public BeyondLayeredCauldronBlock(Properties properties, Predicate<Biome.Precipitation> precipitationPredicate, Map<Item, CauldronInteraction> interactionMap) {
        super(properties, precipitationPredicate, interactionMap);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        super.randomTick(state, level, pos, rand);
        if(level.getBlockState(pos.below()).is(Blocks.SOUL_FIRE)){
            int fill = state.getValue(BlockStateProperties.LEVEL_CAULDRON);
            level.setBlock(pos, ModBlocks.SOUL_CAULDRON_BLOCK.get().defaultBlockState().setValue(SoulCauldronBlock.FILL, fill), 18);
        }
    }
}
