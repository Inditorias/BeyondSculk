package net.inditorias.beyondsculk.items.advanced;

import net.inditorias.beyondsculk.blocks.ModBlocks;
import net.inditorias.beyondsculk.blocks.advancedblocks.ResonantPortalBlock;
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

public class ResonantCatalyst extends Item {

    public ResonantCatalyst(Properties properties) {
        super(new Properties()
                .tab(CreativeModeTab.TAB_MISC)
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    public ResonantCatalyst() {
        super(new Properties()
                .tab(CreativeModeTab.TAB_MISC)
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() != null){
            if(context.getPlayer().level.dimension() == ModDimensions.OTHERWORLD_DIM_KEY || context.getPlayer().level.dimension() == Level.OVERWORLD){
                for(Direction direction : Direction.Plane.VERTICAL){
                    BlockPos framePos = context.getClickedPos().relative(direction);
                    if(((ResonantPortalBlock) ModBlocks.RESONANT_PORTAL_BLOCK.get()).trySpawnPortal(context.getLevel(), framePos)){
                        context.getLevel().playSound(context.getPlayer(), framePos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);
                        return InteractionResult.CONSUME;
                    }
                    else return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
