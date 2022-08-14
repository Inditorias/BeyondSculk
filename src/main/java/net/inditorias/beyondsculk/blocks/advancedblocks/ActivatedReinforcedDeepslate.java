package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blockentities.advanced.ActivatedReinforcedDeepslateBlockEntity;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /*Block Entity*/

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


    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(SOULS) == 0;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        System.out.println("Randomly Ticked");
        //Souls == 0
        //Neighbor has sculkportalblock
        if(!getNeighborBlocks(level, pos).contains(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            return;
        }
        List<Entity> entities = level.getEntities(null, new AABB(pos.offset(-50, -25, -50), pos.offset(50, 25, 50)));
        List<Warden> wardens = new ArrayList<>();
        for(Entity e : entities){
            if(e instanceof Warden w){
                wardens.add(w);
            }
        }
        int switchInt = randomSource.nextIntBetweenInclusive(0, 100);
        int weight = wardens.size() + 2;
        weight *= weight * 2;
        if(wardens.isEmpty() || switchInt > weight){
            for(int i = 0; i < 5; i++) {
                if (ActivatedReinforcedDeepslate.AttemptSummonWarden(level, pos)) {
                    level.setBlock(pos, ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18);
                    level.playSound(null, pos, SoundEvents.WARDEN_EMERGE, SoundSource.BLOCKS, 1f, 0);
                    break;
                }
            }
        }else{
            List<Warden> wardensLowHealth = new ArrayList<>();
            for(Warden w : wardens){
                if(w.getHealth() < 175){
                    wardensLowHealth.add(w);
                }
            }
            if(wardensLowHealth.isEmpty()){
                return;
            }
            Warden healing = wardensLowHealth.get(switchInt % wardensLowHealth.size());
            healing.heal(25 - (switchInt % 7));
            level.setBlock(pos, ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18);
            level.addParticle(ParticleTypes.SCULK_SOUL,
                    pos.getX(), pos.getY(), pos.getZ(), healing.position().x - pos.getX(), healing.position().y - pos.getY(), healing.position().z - pos.getZ());
            level.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.2f, 0);
        }
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

    public static ArrayList<BlockPos> chooseNeighbor(Level level, BlockPos pos){

        //Choose neighbor with least amount of souls
        ArrayList<BlockPos> options = new ArrayList<BlockPos>();
        if(level.getBlockState(pos.above()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.above()));
        }
        if(level.getBlockState(pos.below()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.below()));
        }
        if(level.getBlockState(pos.west()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.west()));
        }
        if(level.getBlockState(pos.north()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.north()));
        }
        if(level.getBlockState(pos.south()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.south()));
        }
        if(level.getBlockState(pos.east()).getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            options.add(new BlockPos(pos.east()));
        }

        if(options.isEmpty())return  null;
        return options;
    }

    public static boolean AttemptSummonWarden(Level level, BlockPos pos){
        //Try to summon a warden in a portal block
        //Find portal blocks in range x:5 y:2 z:5
        //Use random delta
        RandomSource r =  RandomSource.createNewThreadLocalInstance();
        BlockPos spawnLoc = pos.offset(r.nextIntBetweenInclusive(-5, 5), r.nextIntBetweenInclusive(-2, 2), r.nextIntBetweenInclusive(-5, 5));
        MinecraftServer server = level.getServer();
        if(level.getBlockState(spawnLoc).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get()) && server != null) {
            Warden warden = EntityType.WARDEN.create(level);
            ServerLevel serverLevel = server.getLevel(level.dimension());
            if (warden != null && serverLevel != null) {
                List< Player> players = new ArrayList<>();
                List<Entity> entities = level.getEntities(null, new AABB(pos.offset(-50, -25, -50), pos.offset(50, 25, 50)));
                for(Entity e : entities){
                    if(e  instanceof  Player p){
                        players.add(p);
                    }
                }
                if(players.isEmpty()){
                    players.add(level.getNearestPlayer(warden, 128f));
                }
                warden.moveTo(spawnLoc, 0.0f, 0.0f);
                warden.finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(spawnLoc), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                warden.increaseAngerAt(players.get((int)(r.nextFloat() * players.size())), 150, false);
                warden.setHealth(25);
                System.out.println("Created Warden with " + 25 + "HP");
                serverLevel.addFreshEntityWithPassengers(warden);

            }

        }
        return false;
    }
}
