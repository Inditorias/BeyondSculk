package net.inditorias.beyondsculk.blockentities.advanced;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ActivatedReinforcedDeepslate;
import net.inditorias.beyondsculk.blocks.advancedblocks.SculkPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ActivatedReinforcedDeepslateBlockEntity extends BlockEntity {
    public ActivatedReinforcedDeepslateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ActivatedReinforcedDeepslateBlockEntity blockEntity) {
        if (state.getValue(ActivatedReinforcedDeepslate.SOULS) > 0) {
            BlockPos spreadToPos = chooseNeighbor(level, pos);
            if (spreadToPos != null) {
                BlockState spreadToState = level.getBlockState(spreadToPos);
                if (spreadToState.getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK)) {

                } else {
                    level.setBlock(spreadToPos, ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState().
                            setValue(ActivatedReinforcedDeepslate.SOULS, state.getValue(ActivatedReinforcedDeepslate.SOULS) / 2), 3);
                    level.setBlock(pos, state.setValue(ActivatedReinforcedDeepslate.SOULS, (state.getValue(ActivatedReinforcedDeepslate.SOULS) + 1) / 2), 3);
                    level.playSound(null, spreadToPos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 0.2f, 0.5f);
                }
            } else {
                level.setBlock(pos, state.setValue(ActivatedReinforcedDeepslate.SOULS, 0), 3);
            }
        }else if(!getNeighborBlocks(level, pos).contains(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            for (Direction direction : Direction.Plane.VERTICAL) {
                BlockPos framePos = pos.relative(direction);
                if (((SculkPortal) ModBlocks.SCULK_PORTAL_BLOCK.get()).trySpawnPortal(level, framePos)) {
                    level.playSound(null, framePos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    private static Set<Block> getNeighborBlocks(Level level, BlockPos pos){
        HashSet<Block> neighbors = new HashSet<Block>();
        neighbors.add(level.getBlockState(pos.below()).getBlock());
        neighbors.add(level.getBlockState(pos.above()).getBlock());
        neighbors.add(level.getBlockState(pos.west()).getBlock());
        neighbors.add(level.getBlockState(pos.east()).getBlock());
        neighbors.add(level.getBlockState(pos.south()).getBlock());
        neighbors.add(level.getBlockState(pos.north()).getBlock());
        return neighbors;
    }

    private static BlockPos chooseNeighbor(Level level, BlockPos pos){

        //Choose neighbor with least amount of souls
        class BlockContext{
          public BlockPos position;
          public BlockState state(){
              return level.getBlockState(position);
          };
          public int soulStrength(){
              if(state().getBlock().equals(Blocks.REINFORCED_DEEPSLATE))return -1;
              return state().getValue(ActivatedReinforcedDeepslate.SOULS);
          }
          public BlockContext(BlockPos p){
              position = p;
          }
        };

        ArrayList<BlockContext> options = new ArrayList<BlockContext>();
        if(level.getBlockState(pos.above()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.above()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.above()));
        }
        if(level.getBlockState(pos.below()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.below()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.below()));
        }
        if(level.getBlockState(pos.west()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.west()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.west()));
        }
        if(level.getBlockState(pos.north()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.north()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.north()));
        }
        if(level.getBlockState(pos.south()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.south()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.south()));
        }
        if(level.getBlockState(pos.east()).getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK) ||
                level.getBlockState(pos.east()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockContext(pos.east()));
        }

        if(options.isEmpty())return  null;

        options.sort(Comparator.comparingInt(BlockContext::soulStrength));
        return options.get(options.size()-1).position;
    }
}
