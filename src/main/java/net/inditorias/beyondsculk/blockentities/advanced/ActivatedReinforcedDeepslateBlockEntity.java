package net.inditorias.beyondsculk.blockentities.advanced;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ActivatedReinforcedDeepslate;
import net.inditorias.beyondsculk.blocks.advancedblocks.SculkPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.Array;
import java.util.*;

public class ActivatedReinforcedDeepslateBlockEntity extends BlockEntity {
    public ActivatedReinforcedDeepslateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ActivatedReinforcedDeepslateBlockEntity blockEntity) {
        if (state.getValue(ActivatedReinforcedDeepslate.SOULS) > 0) {
            ArrayList<BlockPos> spreadToPos = ActivatedReinforcedDeepslate.chooseNeighbor(level, pos);
            if (spreadToPos != null) {
                for(BlockPos spread : spreadToPos){
                    level.setBlock(spread, ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState().
                            setValue(ActivatedReinforcedDeepslate.SOULS, state.getValue(ActivatedReinforcedDeepslate.SOULS) / 2), 3);
                    level.setBlock(pos, state.setValue(ActivatedReinforcedDeepslate.SOULS, (state.getValue(ActivatedReinforcedDeepslate.SOULS) + 1) / 2), 3);
                    level.playSound(null, spread, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, .75f, 0);
                }

            } else {
                level.setBlock(pos, state.setValue(ActivatedReinforcedDeepslate.SOULS, 0), 3);
            }
        }else if(!ActivatedReinforcedDeepslate.getNeighborBlocks(level, pos).contains(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            for (Direction direction : Direction.Plane.VERTICAL) {
                BlockPos framePos = pos.relative(direction);
                if (((SculkPortal) ModBlocks.SCULK_PORTAL_BLOCK.get()).trySpawnPortal(level, framePos)) {
                    level.playSound(null, framePos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 1.0F, 0);
                    for(int i = 0; i < 15; i++) {
                        if (ActivatedReinforcedDeepslate.AttemptSummonWarden(level, pos)) {
                            level.setBlock(pos, ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18);
                            level.playSound(null, pos, SoundEvents.WARDEN_EMERGE, SoundSource.BLOCKS, 1f, 0);
                            break;
                        }
                    }
                }
            }
        }
    }




}
