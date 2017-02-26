package com.jayavery.jjmod.blocks;

import com.jayavery.jjmod.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/** Simple bed block. */
public class BlockBedSimple extends BlockBedPlainAbstract {

    public BlockBedSimple() {
        
        super("bed_simple", 2.0F, 2F, () -> ModItems.bedSimple, null);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        return BlockNew.EIGHT;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        return BlockNew.SIX;
    }
}