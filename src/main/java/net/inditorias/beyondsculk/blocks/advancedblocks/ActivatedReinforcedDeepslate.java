package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.inditorias.beyondsculk.blockentities.ModBlockEntities;
import net.inditorias.beyondsculk.blockentities.advanced.ActivatedReinforcedDeepslateBlockEntity;
import net.inditorias.beyondsculk.blocks.AxisBlock;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraftforge.fml.common.Mod;
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

    /* End Block Entity */

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
                    level.playSound(null, pos, SoundEvents.WARDEN_EMERGE, SoundSource.BLOCKS, .4f, 0);
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

        for (Direction direction : Direction.Plane.VERTICAL) {
            BlockPos framePos = pos.relative(direction);
            ActivatedReinforcedDeepslate.Size frameSize = isUnstableFrame(level, framePos, getPortalAxis(level, pos));
            if(frameSize != null && frameSize.bottomLeft != null){
                if(frameSize.axis == Direction.Axis.X){
                    spawnPortalBoss(level, frameSize.bottomLeft.offset(frameSize.width/2, -1, 0));
                }else{
                    spawnPortalBoss(level, frameSize.bottomLeft.offset(0, -1, frameSize.width/2));
                }
            }
            break;
        }



    }

    private Direction.Axis getPortalAxis(Level level, BlockPos pos){
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                for(int k = -1; k < 2; k++){
                    if(level.getBlockState(pos.offset(i, j, k)).is(ModBlocks.SCULK_PORTAL_BLOCK.get())){
                        return level.getBlockState(pos.offset(i, j, k)).getValue(AxisBlock.AXIS);
                    }
                }
            }
        }
        return Direction.Axis.X;
    }
    public ActivatedReinforcedDeepslate.Size isUnstableFrame(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        ActivatedReinforcedDeepslate.Size ActivatedReinforcedDeepslate$size = new ActivatedReinforcedDeepslate.Size(level, pos,axis);
        if (ActivatedReinforcedDeepslate$size.isValid()) {
            return ActivatedReinforcedDeepslate$size;
        } else {
            return null;
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
        //TODO: Make portal boss model and mob, then summon it
        while(!level.setBlock(pos, ModBlocks.RESONANT_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), 18));
    }

    public static class Size {
        private final LevelAccessor level;
        private final Direction.Axis axis;
        private final Direction rightDir;
        private final Direction leftDir;
        private int portalBlockCount;
        @Nullable
        private BlockPos bottomLeft;
        private int height;
        private int width;

        public Size(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
            this.level = level;
            this.axis = axis;
            if (axis == Direction.Axis.X) {
                this.leftDir = Direction.EAST;
                this.rightDir = Direction.WEST;
            } else {
                this.leftDir = Direction.NORTH;
                this.rightDir = Direction.SOUTH;
            }

            for(BlockPos blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > -64 && this.canConnect(level.getBlockState(pos.below())); pos = pos.below()) {
            }

            int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;
            if (i >= 0) {
                this.bottomLeft = pos.relative(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }

        }

        protected int getDistanceUntilEdge(BlockPos pos, Direction directionIn) {
            int i;
            for(i = 0; i < 22; ++i) {
                BlockPos blockpos = pos.relative(directionIn, i);
                if(!this.canConnect(this.level.getBlockState(blockpos)) || !(this.level.getBlockState(blockpos.below()).is(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()))) {
                    break;
                }
            }

            BlockPos framePos = pos.relative(directionIn, i);
            return this.level.getBlockState(framePos).is(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()) ? i : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int calculatePortalHeight() {
            label56:
            for(this.height = 0; this.height < 21; ++this.height) {
                for(int i = 0; i < this.width; ++i) {
                    BlockPos blockpos = this.bottomLeft.relative(this.rightDir, i).above(this.height);
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    if (!this.canConnect(blockstate)) {
                        break label56;
                    }

                    Block block = blockstate.getBlock();
                    if (block == ModBlocks.SCULK_PORTAL_BLOCK.get()) {
                        ++this.portalBlockCount;
                    }

                    if (i == 0) {
                        BlockPos framePos = blockpos.relative(this.leftDir);
                        if (!(this.level.getBlockState(framePos).is(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()))) {
                            break label56;
                        }
                    } else if (i == this.width - 1) {
                        BlockPos framePos = blockpos.relative(this.rightDir);
                        if (!(this.level.getBlockState(framePos).is(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()))) {
                            break label56;
                        }
                    }
                }
            }

            for(int j = 0; j < this.width; ++j) {
                BlockPos framePos = this.bottomLeft.relative(this.rightDir, j).above(this.height);
                if (!(this.level.getBlockState(framePos).is(ModBlocks.UNSTABLE_REINFORCED_DEEPSLATE_BLOCK.get()))) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean canConnect(BlockState pos) {
            Block block = pos.getBlock();
            return pos.isAir() || block == ModBlocks.SCULK_PORTAL_BLOCK.get();
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks() {
            for(int i = 0; i < this.width; ++i) {
                BlockPos blockpos = this.bottomLeft.relative(this.rightDir, i);

                for(int j = 0; j < this.height; ++j) {
                    this.level.setBlock(blockpos.above(j), ModBlocks.SCULK_PORTAL_BLOCK.get().defaultBlockState().setValue(AxisBlock.AXIS, this.axis), 18);
                }
            }

        }

        private boolean isPortalCountValidForSize() {
            return this.portalBlockCount >= this.width * this.height;
        }

        public boolean validatePortal() {
            return this.isValid() && this.isPortalCountValidForSize();
        }
    }
    
}
