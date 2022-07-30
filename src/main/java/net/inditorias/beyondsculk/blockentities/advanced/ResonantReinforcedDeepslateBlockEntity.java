package net.inditorias.beyondsculk.blockentities.advanced;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class ResonantReinforcedDeepslateBlockEntity extends BlockEntity {
    public ResonantReinforcedDeepslateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESONANT_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ResonantReinforcedDeepslateBlockEntity blockEntity) {
        ArrayList<BlockPos> toConvert = chooseNeighbor(level,pos);
        if(toConvert == null){
            return;
        }
        for(BlockPos c: toConvert){
            level.setBlock(c, state.getBlock().defaultBlockState(), 18);
        }
    }

    private static ArrayList<BlockPos> chooseNeighbor(Level level, BlockPos pos){

        //Choose neighbor with the least amount of souls
        ArrayList<BlockPos> options = new ArrayList<BlockPos>();
        if(level.getBlockState(pos.above()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.above()));
        }
        if(level.getBlockState(pos.below()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.below()));
        }
        if(level.getBlockState(pos.west()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.west()));
        }
        if(level.getBlockState(pos.north()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.north()));
        }
        if(level.getBlockState(pos.south()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.south()));
        }
        if(level.getBlockState(pos.east()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            options.add(new BlockPos(pos.east()));
        }

        if(options.isEmpty())return  null;
        return options;
    }




}
