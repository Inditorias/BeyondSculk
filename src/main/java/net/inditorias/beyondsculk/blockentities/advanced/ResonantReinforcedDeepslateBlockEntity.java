package net.inditorias.beyondsculk.blockentities.advanced;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ActivatedReinforcedDeepslate;
import net.inditorias.beyondsculk.blocks.advancedblocks.OtherworldPortal;
import net.inditorias.beyondsculk.blocks.advancedblocks.SculkPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ResonantReinforcedDeepslateBlockEntity extends BlockEntity {
    public ResonantReinforcedDeepslateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESONANT_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ResonantReinforcedDeepslateBlockEntity blockEntity) {
        ArrayList<BlockPos> convert = getNeighborBlocksFrameBlockPos(level, pos);
        if(convert.isEmpty() && !getNeighborBlocks(level, pos).contains(ModBlocks.OTHERWORLD_PORTAL_BLOCK.get())){
            for (Direction direction : Direction.Plane.VERTICAL) {
                BlockPos framePos = pos.relative(direction);
                if (((OtherworldPortal) ModBlocks.OTHERWORLD_PORTAL_BLOCK.get()).trySpawnPortal(level, framePos)) {
                    level.playSound(null, framePos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 0);
                }
                return;
            }
        }else{
            for(BlockPos p : convert){
                level.setBlock(p, ModBlocks.RESONANT_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18);
            }
        }

    }

    public static ArrayList<BlockPos> getNeighborBlocksFrameBlockPos(Level level, BlockPos pos){
        ArrayList<BlockPos> neighbors = new ArrayList<>();
        if(level.getBlockState(pos.below()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.below());
        }
        if(level.getBlockState(pos.above()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.above());
        }
        if(level.getBlockState(pos.west()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.west());
        }
        if(level.getBlockState(pos.east()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.east());
        }
        if(level.getBlockState(pos.south()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.south());
        }
        if(level.getBlockState(pos.north()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get())){
            neighbors.add(pos.north());
        }

        return neighbors;
    }

    public static Set<Block> getNeighborBlocks(Level level, BlockPos pos){
        HashSet<Block> neighbors = new HashSet<>();
        neighbors.add(level.getBlockState(pos.below()).getBlock());
        neighbors.add(level.getBlockState(pos.above()).getBlock());
        neighbors.add(level.getBlockState(pos.west()).getBlock());
        neighbors.add(level.getBlockState(pos.east()).getBlock());
        neighbors.add(level.getBlockState(pos.south()).getBlock());
        neighbors.add(level.getBlockState(pos.north()).getBlock());

        neighbors.add(level.getBlockState(pos.west().above()).getBlock());
        neighbors.add(level.getBlockState(pos.east().above()).getBlock());
        neighbors.add(level.getBlockState(pos.south().above()).getBlock());
        neighbors.add(level.getBlockState(pos.north().above()).getBlock());

        neighbors.add(level.getBlockState(pos.west().below()).getBlock());
        neighbors.add(level.getBlockState(pos.east().below()).getBlock());
        neighbors.add(level.getBlockState(pos.south().below()).getBlock());
        neighbors.add(level.getBlockState(pos.north().below()).getBlock());

        return neighbors;
    }



}
