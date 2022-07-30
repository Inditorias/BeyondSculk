package net.inditorias.beyondsculk.items.advanced;

import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ActivatedReinforcedDeepslate;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulWaterBottle extends PotionItem {

    private static final int DRINK_DURATION = 64;

    public SoulWaterBottle(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        super.finishUsingItem(stack, level, living);
        if(living instanceof ServerPlayer player){
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        if(stack.isEmpty()){
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if(living instanceof Player player && !(player.getAbilities().instabuild)){
                ItemStack bottles = new ItemStack(Items.GLASS_BOTTLE);
                if(!player.getInventory().add(bottles)){
                    player.drop(bottles, false);
                }
            }
        }
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);

        if(state.getBlock().equals(Blocks.REINFORCED_DEEPSLATE)){
            level.playSound(player, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
            if(!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))){
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
            }
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            level.setBlockAndUpdate(pos, ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK.get().
                    defaultBlockState().
                    setValue(ActivatedReinforcedDeepslate.SOULS, RandomSource.createNewThreadLocalInstance().
                            nextIntBetweenInclusive(8, 16)));
        }
        if(state.getBlock().equals(ModBlocks.ACTIVATED_REINFORCED_DEEPSLATE_BLOCK.get())){
            level.playSound(player, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 0);
            if(!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))){
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
            }
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, pos);
            level.setBlock(pos, state.setValue(ActivatedReinforcedDeepslate.SOULS, 16), 3);
        }

        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(ItemStack p_43001_) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_42997_) {
        return super.getUseAnimation(p_42997_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_42993_, Player p_42994_, InteractionHand p_42995_) {
        return super.use(p_42993_, p_42994_, p_42995_);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "soul_water_bottle";
    }

    @Override
    public void appendHoverText(ItemStack p_42988_, @Nullable Level p_42989_, List<Component> p_42990_, TooltipFlag p_42991_) {
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.isEnchanted();
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowedIn(tab)) {
            stacks.add(new ItemStack(this));
        }
    }
}
