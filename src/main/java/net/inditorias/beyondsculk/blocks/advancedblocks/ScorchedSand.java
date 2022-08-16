package net.inditorias.beyondsculk.blocks.advancedblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ScorchedSand extends FallingBlock {
    private final int dustColor;

    public ScorchedSand(int p_55967_, BlockBehaviour.Properties p_55968_) {
        super(p_55968_);
        this.dustColor = p_55967_;
    }

    public int getDustColor(BlockState p_55970_, BlockGetter p_55971_, BlockPos p_55972_) {
        return this.dustColor;
    }
}
