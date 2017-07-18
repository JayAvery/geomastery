/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.worldgen;

import java.util.ArrayList;
import java.util.Random;
import jayavery.geomastery.blocks.BlockSeedling;
import jayavery.geomastery.main.GeoBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** WorldGenerator for orange trees. */
public class WorldGenTreeOrange extends WorldGenTreeAbstract {
    
    public WorldGenTreeOrange(World world, Random rand, boolean isSapling) {
        
        super(world, rand, isSapling, 30, 4, GeoBlocks.SEEDLING_ORANGE);
    }

    @Override
    public boolean generateTree(BlockPos pos) {
        
        ArrayList<BlockPos> trunks = new ArrayList<BlockPos>();

        trunks.add(pos);
        trunks.add(pos.up());
        trunks.add(pos.up(2));
        trunks.add(pos.up(3));
        
        for (BlockPos trunk : trunks) {
            
            Block found = this.world.getBlockState(trunk).getBlock();
            
            if (!(found instanceof BlockSeedling) &&
                    !found.isReplaceable(this.world, trunk)) {
                
                return false;
            }
        }
        
        for (BlockPos trunk : trunks) {
            
            this.setBlock(trunk, GeoBlocks.WOOD_ORANGE.getDefaultState());
        }
        
        ArrayList<BlockPos> leaves = new ArrayList<BlockPos>();
        
        BlockPos top = pos.up(3);
        
        leaves.add(top.north());
        leaves.add(top.north().up());
        leaves.add(top.north().down());
        leaves.add(top.north().east());
        leaves.add(top.north().west());
        leaves.add(top.north().west().up());
        leaves.add(top.north().east().up());
        leaves.add(top.north().west().down());
        leaves.add(top.north().east().down());
        leaves.add(top.south());
        leaves.add(top.south().up());
        leaves.add(top.south().down());
        leaves.add(top.south().east());
        leaves.add(top.south().west());
        leaves.add(top.south().west().up());
        leaves.add(top.south().east().up());
        leaves.add(top.south().west().down());
        leaves.add(top.south().east().down());
        leaves.add(top.down().west());
        leaves.add(top.down().west(2));
        leaves.add(top.west());
        leaves.add(top.west(2));
        leaves.add(top.up().west());
        leaves.add(top.up().west(2));
        leaves.add(top.up(2).west());
        leaves.add(top.down().east());
        leaves.add(top.down().east(2));
        leaves.add(top.east());
        leaves.add(top.east(2));
        leaves.add(top.up().east());
        leaves.add(top.up().east(2));
        leaves.add(top.up(2).east());
        leaves.add(top.up());
        leaves.add(top.up(2));
        
        for (BlockPos leaf : leaves) {
            
            if (this.world.getBlockState(leaf).getBlock()
                    .isReplaceable(this.world, leaf)) {
            
                this.setBlock(leaf, GeoBlocks.LEAF_ORANGE.getDefaultState());
            }
        }
        
        return true;
    }
}
