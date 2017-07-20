/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.blocks;

import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Baby mushroom block. */
public class BlockMushroombaby extends BlockMushroom {
    
    /** Chance of growth per random tick. */
    private static final float GROWTH_CHANCE = 0.2F;
    
    /** The adult block this grows into. */
    private final Supplier<Block> adult;
    
    public BlockMushroombaby(String name, Supplier<Block> adult) {
        
        this.adult = adult;
        BlockNew.setupBlock(this, name, null, 0F, null);
    }
    
    @Override
    public void updateTick(World world, BlockPos pos,
            IBlockState state, Random rand) {
        
        if (rand.nextFloat() < GROWTH_CHANCE) {
            
            world.setBlockState(pos, this.adult.get().getDefaultState());
        }
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        
        return Items.AIR;
    }
}
