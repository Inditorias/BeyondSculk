package net.inditorias.beyondsculk.blocks.advancedblocks;


import net.inditorias.beyondsculk.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoulCauldronBlock extends Block {

    public static final IntegerProperty FILL = IntegerProperty.create("level", 1, 3);

    public SoulCauldronBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        super.use(state, level, pos, player, hand, result);
        if(!level.isClientSide()){
            if(player.getMainHandItem().is(Items.GLASS_BOTTLE) && hand == InteractionHand.MAIN_HAND){
                ItemStack bottles = player.getMainHandItem();
                usedByBottle(player, level, pos, bottles, state);

            }else if(player.getOffhandItem().is(Items.GLASS_BOTTLE) && hand == InteractionHand.OFF_HAND){
                ItemStack bottles = player.getOffhandItem();
                usedByBottle(player, level, pos, bottles, state);
            }
        }
        return InteractionResult.FAIL;
    }

    private void usedByBottle(Player player, Level level, BlockPos pos, ItemStack bottles, BlockState state){
        int fill = state.getValue(FILL);
        if(fill == 1){
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 18);
        }else{
            level.setBlock(pos, state.setValue(FILL, fill - 1), 18);
        }
        level.playSound(player, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1f, level.getRandom().nextFloat());
        if(!player.isCreative()){
            bottles.shrink(1);
        }
        if(!player.getInventory().add(new ItemStack(ModItems.BOTTLE_OF_SOUL_WATER.get()))){
            player.drop(new ItemStack(ModItems.BOTTLE_OF_SOUL_WATER.get()), false);
        }
        player.awardStat(Stats.ITEM_USED.get(bottles.getItem()));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder loot) {
        return new ArrayList<>(Collections.singleton(new ItemStack(Items.CAULDRON)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILL);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }
}
