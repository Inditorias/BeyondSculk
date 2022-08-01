package net.inditorias.beyondsculk.items.advanced;

import net.inditorias.beyondsculk.blocks.AxisBlock;
import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.OtherworldPortal;
import net.inditorias.beyondsculk.world.dimension.ModDimensions;
import net.inditorias.beyondsculk.world.dimension.portal.PortalCreator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import javax.print.attribute.standard.MediaSize;

public class TempOtherworldCatalyst extends Item {
    public TempOtherworldCatalyst(Properties properties) {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block clicked = state.getBlock();
        if (clicked.equals(ModBlocks.RESONANT_REINFORCED_DEEPSLATE_BLOCK.get())) {
            if (level.dimension().equals(Level.OVERWORLD) || level.dimension().equals(ModDimensions.OTHERWORLD_DIM_KEY)) {
                if(PortalCreator.createPortal(level, ModBlocks.RESONANT_REINFORCED_DEEPSLATE_BLOCK.get().defaultBlockState(), (AxisBlock) ModBlocks.OTHERWORLD_PORTAL_BLOCK.get(), pos)){
                    return InteractionResult.PASS;
                }
            }

        }
        return InteractionResult.FAIL;
    }
}
