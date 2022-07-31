package net.inditorias.beyondsculk.util;

import net.inditorias.beyondsculk.BeyondSculk;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks{

        public static final TagKey<Block> UNSTABLE_PORTAL_FRAME_BLOCKS = tag("unstable_portal_frame_blocks");
        public static final TagKey<Block> RESONANT_PORTAL_FRAME_BLOCKS = tag("resonant_portal_frame_blocks");

        public static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(BeyondSculk.MOD_ID, name));
        }
        public static TagKey<Block> forgeTag(String name){
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }
    public static class Items{
        public static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(BeyondSculk.MOD_ID, name));
        }

        public static TagKey<Item> forgeTag(String name){
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }
}
