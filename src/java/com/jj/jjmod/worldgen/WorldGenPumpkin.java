package com.jj.jjmod.worldgen;

import java.util.Random;
import com.jj.jjmod.blocks.BlockFruit;
import com.jj.jjmod.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** WorldGenerator for Pumpkin crops. */
public class WorldGenPumpkin extends WorldGenCrop {

    public WorldGenPumpkin(World world, Random rand) {
        
        super(world, rand, ModBlocks.pumpkinCrop.getDefaultState(), 4, 3);
    }

    @Override
    protected boolean generateOne(BlockPos crop) {
        
        EnumFacing fruitOffset = EnumFacing.Plane.HORIZONTAL.random(this.rand);
        BlockPos fruit = crop.offset(fruitOffset);
        BlockPos ground = fruit.down();
        
        if (!this.world.isAirBlock(fruit) ||
                this.world.getBlockState(ground).getBlock() != Blocks.GRASS) {
            
            return false;
        }
        
        this.world.setBlockState(crop, this.crop);
        this.world.setBlockState(fruit, ModBlocks.pumpkinFruit.getDefaultState()
                .withProperty(BlockFruit.STEM, fruitOffset.getOpposite()));
        return true;
    }
}
