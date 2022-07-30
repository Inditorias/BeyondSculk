package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blockentities.advanced.ActivatedReinforcedDeepslateBlockEntity;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
        if(wardens.isEmpty() || switchInt > weight) {
            if(level.setBlock(pos, ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18)){
                if (ActivatedReinforcedDeepslate.AttemptSummonWarden(level, pos)) {
                    level.playSound(null, pos, SoundEvents.WARDEN_EMERGE, SoundSource.BLOCKS, 1f, 0);
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
        if(isCompletelyUnstable(level, pos)){
            spawnPortalBoss(level, pos);
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

        //Choose neighbor with the least amount of souls
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

    public static boolean AttemptSummonWarden(ServerLevel level, BlockPos pos){
        //Try to summon a warden in a portal block
        //Find portal blocks in range x:5 y:2 z:5
        //Use random delta

        RandomSource r =  RandomSource.createNewThreadLocalInstance();
        ArrayList<BlockPos> spawnLocs = getNeighborBlocksPortalBlockPos(level, pos);
        BlockPos spawnLoc = getSpawnLoc(spawnLocs.get((int)(spawnLocs.size() * r.nextDouble())), pos, r);
        if(!level.getBlockState(spawnLoc).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            spawnLoc = spawnLocs.get((int)(spawnLocs.size() * r.nextDouble()));
        }
            Warden warden = EntityType.WARDEN.create(level);

            if (warden != null) {
                List<Player> players = new ArrayList<>();
                List<Entity> entities = level.getEntities(null, new AABB(pos.offset(-50, -25, -50), pos.offset(50, 25, 50)));
                for (Entity e : entities) {
                    if (e instanceof Player p) {
                        players.add(p);
                    }
                }
                if (players.isEmpty()) {
                    players.add(level.getNearestPlayer(warden, 128f));
                }
                warden.moveTo(spawnLoc, 0.0f, 0.0f);
                warden.setPersistenceRequired();
                warden.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnLoc), MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                warden.increaseAngerAt(players.get((int) (r.nextFloat() * players.size())), 150, false);
                warden.setHealth(25);
                level.addFreshEntityWithPassengers(warden);

            }
        return false;
    }


    private  static BlockPos getSpawnLoc(BlockPos portal, BlockPos frame, RandomSource r){
        Vec3i diff = new Vec3i(portal.getX() - frame.getX(), portal.getY() - frame.getY(), portal.getZ() - frame.getZ());
        diff = diff.multiply((int)(r.nextDouble()*3));
        return frame.offset(diff);
    }
    public static ArrayList<BlockPos> getNeighborBlocksPortalBlockPos(Level level, BlockPos pos){
        ArrayList<BlockPos> neighbors = new ArrayList<>();
        if(level.getBlockState(pos.below()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.below());
        }
        if(level.getBlockState(pos.above()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.above());
        }
        if(level.getBlockState(pos.west()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.west());
        }
        if(level.getBlockState(pos.east()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.east());
        }
        if(level.getBlockState(pos.south()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.south());
        }
        if(level.getBlockState(pos.north()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.north());
        }

        if(level.getBlockState(pos.west().above()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.above().west());
        }
        if(level.getBlockState(pos.east().above()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.above().east());
        }
        if(level.getBlockState(pos.south().above()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.above().south());
        }
        if(level.getBlockState(pos.north().above()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.above().north());
        }

        if(level.getBlockState(pos.west().below()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.west().below());
        }
        if(level.getBlockState(pos.east().below()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.east().below());
        }
        if(level.getBlockState(pos.south().below()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.south().below());
        }
        if(level.getBlockState(pos.north().below()).getBlock().equals(ModBlocks.SCULK_PORTAL_BLOCK.get())){
            neighbors.add(pos.north().below());
        }

        return neighbors;
    }

    private boolean isCompletelyUnstable(Level level, BlockPos startPos){
        BlockPos lastPos = new BlockPos(startPos);
        BlockPos temp;
        BlockPos currPos = getNextPos(level, startPos, lastPos);
        while(currPos != null && currPos != startPos){
            temp = currPos;
            currPos = getNextPos(level, currPos, lastPos);
            lastPos = temp;
        }
        return (currPos == startPos);
    }

    @Nullable
    private BlockPos getNextPos(Level level, BlockPos curr, BlockPos last){
        if(level.getBlockState(curr.above()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.above().equals(last)){
            return curr.above();
        }
        if(level.getBlockState(curr.below()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.below().equals(last)){
            return curr.below();
        }
        if(level.getBlockState(curr.north()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.north().equals(last)){
            return curr.north();
        }
        if(level.getBlockState(curr.south()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.south().equals(last)){
            return curr.south();
        }
        if(level.getBlockState(curr.east()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.east().equals(last)){
            return curr.east();
        }
        if(level.getBlockState(curr.west()).getBlock().equals(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) && !curr.west().equals(last)){
            return curr.west();
        }
        return null;
    }

    private void spawnPortalBoss(ServerLevel level, BlockPos pos){

    }
}
