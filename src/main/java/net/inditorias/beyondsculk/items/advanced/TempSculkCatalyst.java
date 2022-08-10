package net.inditorias.beyondsculk.items.advanced;

import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.OtherworldPortal;
import net.inditorias.beyondsculk.blocks.advancedblocks.SculkPortal;
import net.inditorias.beyondsculk.world.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TempSculkCatalyst extends Item {
    public TempSculkCatalyst(Properties properties) {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        try {
            if (context.getPlayer() != null) {
                for (Direction direction : Direction.Plane.VERTICAL) {
                    BlockPos framePos = context.getClickedPos().relative(direction);
                     if (((SculkPortal) ModBlocks.SCULK_PORTAL_BLOCK.get()).trySpawnPortal(context.getLevel(), framePos)) {
                        context.getLevel().playSound(context.getPlayer(), framePos, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.CONSUME;
                    } else return InteractionResult.FAIL;
                }
            }

        } catch (
                Exception e) {
        }
        return InteractionResult.FAIL;
    }
}
