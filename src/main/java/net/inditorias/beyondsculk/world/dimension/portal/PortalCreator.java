package net.inditorias.beyondsculk.world.dimension.portal;

import net.inditorias.beyondsculk.blocks.AxisBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import  net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PortalCreator {
    public static BlockPos Vec3Max(Level level, BlockState block, BlockPos pos){
        //Follow blocks in the positive direction until no more are found.
        //Positive directions are above, south, east.
        //This requires corners (no cheesy cornerless portal here)
        if(level.getBlockState(pos.above()).getBlock().equals(block.getBlock())){
            return Vec3Max(level, block, pos.above());
        }else if(level.getBlockState(pos.south()).getBlock().equals(block.getBlock())){
            return Vec3Max_normalX(level, block, pos.south());
        }else if(level.getBlockState(pos.east()).getBlock().equals(block.getBlock())){
            return Vec3Max_normalZ(level,block,pos.east());
        }
        return pos;

    }

    private static BlockPos Vec3Max_normalZ(Level level, BlockState block, BlockPos pos){
        //Portal is not along south direction
        if(level.getBlockState(pos.above()).getBlock().equals(block.getBlock())){
            return Vec3Max(level, block, pos.above());
        }else if(level.getBlockState(pos.east()).getBlock().equals(block.getBlock())){
            return Vec3Max_normalZ(level,block,pos.east());
        }
        return pos;
    }
    private static BlockPos Vec3Max_normalX(Level level, BlockState block, BlockPos pos){
        //Portal is not along east direction
        if(level.getBlockState(pos.above()).getBlock().equals(block.getBlock())){
            return Vec3Max(level, block, pos.above());
        }else if(level.getBlockState(pos.south()).getBlock().equals(block.getBlock())) {
            return Vec3Max_normalX(level, block, pos.south());
        }
        return pos;
    }

    public static boolean isValidPortal(Level level, BlockState block, BlockPos pos){
        BlockPos vec3max = Vec3Max(level, block, pos);
        if(level.getBlockState(vec3max.west()).getBlock().equals(block.getBlock())){
            return isValidPortalNormalZ(level, block, vec3max);
        }else{
            return isValidPortalNormalX(level, block, vec3max);
        }
    }

    private static boolean isValidPortalNormalZ(Level level, BlockState block, BlockPos vec3max){

        BlockPos traversal = new BlockPos(vec3max);
        while(level.getBlockState(traversal.below()).getBlock().equals(block.getBlock())){
            traversal = traversal.below();
        }
        while(level.getBlockState(traversal.west()).getBlock().equals(block.getBlock())){
            traversal = traversal.west();
        }
        while(level.getBlockState(traversal.above()).getBlock().equals(block.getBlock())){
            traversal = traversal.above();
        }
        while(level.getBlockState(traversal.east()).getBlock().equals(block.getBlock())){
            traversal = traversal.east();
        }
        if(traversal.equals(vec3max)){
            return true;
        }
        return false;
    }
    private static boolean isValidPortalNormalX(Level level, BlockState block, BlockPos vec3max){

        BlockPos traversal = new BlockPos(vec3max);
        while(level.getBlockState(traversal.below()).getBlock().equals(block.getBlock())){
            traversal = traversal.below();
        }
        while(level.getBlockState(traversal.north()).getBlock().equals(block.getBlock())){
            traversal = traversal.north();
        }
        while(level.getBlockState(traversal.above()).getBlock().equals(block.getBlock())){
            traversal = traversal.above();
        }
        while(level.getBlockState(traversal.south()).getBlock().equals(block.getBlock())){
            traversal = traversal.south();
        }
        if(traversal.equals(vec3max)){
            return true;
        }
        return false;
    }

    public static boolean createPortal(Level level, BlockState frameBlock, AxisBlock portalBlock, BlockPos source){
        BlockPos vec3max = Vec3Max(level, frameBlock, source);
        if(isValidPortal(level, frameBlock, vec3max)){
            if(level.getBlockState(vec3max.west()).getBlock().equals(frameBlock.getBlock())){
                BuildPortalNormalZ(level, frameBlock, portalBlock, vec3max);
            }else{
                BuildPortalNormalX(level, frameBlock, portalBlock, vec3max);
            }
            return true;
        }
        return false;
    }

    public static boolean BuildPortalNormalX(Level level, BlockState frame, AxisBlock portalBlock, BlockPos vec3max){
        BlockState portal = portalBlock.defaultBlockState().setValue(portalBlock.AXIS, Direction.Axis.Z);
        BlockPos vec3min = new BlockPos(vec3max);
        for(BlockPos i = new BlockPos(vec3max.below().north()); level.getBlockState(i).getBlock().equals(Blocks.AIR); i = i.below()){
            for(BlockPos j = new BlockPos(i); level.getBlockState(j).getBlock().equals(Blocks.AIR); j = j.north()){
                vec3min = new BlockPos(j);
            }
        }
        if(level.getBlockState(vec3min.below()).getBlock().equals(frame.getBlock()) &&
                level.getBlockState(vec3min.north()).getBlock().equals(frame.getBlock())){
            BlockPos.betweenClosed(vec3min, vec3max.below().north()).forEach((pos) -> {
                level.setBlock(pos, portal, 18);
            });
            return true;
        }else{
            return false;
        }
    }

    public static boolean BuildPortalNormalZ(Level level, BlockState frame, AxisBlock portalBlock, BlockPos vec3max){
        BlockState portal = portalBlock.defaultBlockState().setValue(portalBlock.AXIS, Direction.Axis.X);
        BlockPos vec3min = new BlockPos(vec3max);
        for(BlockPos i = new BlockPos(vec3max.below().west()); level.getBlockState(i).getBlock().equals(Blocks.AIR); i = i.below()){
            for(BlockPos j = new BlockPos(i); level.getBlockState(j).getBlock().equals(Blocks.AIR); j = j.west()){
                vec3min = new BlockPos(j);
            }
        }
        if(level.getBlockState(vec3min.below()).getBlock().equals(frame.getBlock()) &&
        level.getBlockState(vec3min.west()).getBlock().equals(frame.getBlock())){
            BlockPos.betweenClosed(vec3min, vec3max.below().west()).forEach((pos) -> {
                level.setBlock(pos, portal, 18);
            });
            return true;
        }else{
            return false;
        }
    }
}
