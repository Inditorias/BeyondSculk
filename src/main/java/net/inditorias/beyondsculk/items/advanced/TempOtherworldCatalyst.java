package net.inditorias.beyondsculk.items.advanced;

import com.google.common.collect.ImmutableSet;
import net.inditorias.beyondsculk.blocks.AxisBlock;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.OtherworldPortal;
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
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import javax.print.attribute.standard.MediaSize;
import java.util.HashSet;

public class TempOtherworldCatalyst extends Item {
    public TempOtherworldCatalyst(Properties properties) {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null) {
            if (context.getPlayer().level.dimension() == ModDimensions.OTHERWORLD_DIM_KEY || context.getPlayer().level.dimension() == Level.OVERWORLD) {
                for (Direction direction : Direction.Plane.VERTICAL) {
                    BlockPos framePos = context.getClickedPos().relative(direction);
                    if (((OtherworldPortal) ModBlocks.OTHERWORLD_PORTAL_BLOCK.get()).trySpawnPortal(context.getLevel(), framePos)) {
                        context.getLevel().playSound(context.getPlayer(), framePos, SoundEvents.WARDEN_EMERGE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.CONSUME;
                    } else return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}