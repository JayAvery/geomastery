/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import jayavery.geomastery.items.ItemSimple;
import jayavery.geomastery.utilities.EToolType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/** Harvestable fruit leaf block. */
public class BlockHarvestableLeaves extends BlockLeaves {
    
    public static final PropertyInteger AGE =
            PropertyInteger.create("age", 0, 7);
    
    /** Supplier for fruit item. */
    private final Supplier<Item> fruitRef;
    /** Supplier for seedling block. */
    private final Supplier<Block> seedlingRef;
    /** Chance of growth per update tick. */
    private final float growthChance;
    
    public BlockHarvestableLeaves(String name, Supplier<Item> itemRef,
            Supplier<Block> seedlingRef, float growthChance) {
        
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(AGE, 0));
        this.setTickRandomly(true);
        BlockNew.setupBlock(this, name, CreativeTabs.DECORATIONS,
                0.2F, EToolType.MACHETE);
        this.fruitRef = itemRef;
        this.seedlingRef = seedlingRef;
        this.growthChance = growthChance;
    }
    
    /** Gets seedling by chance and harvestable items if applicable. */
    @Override
    public List<ItemStack> getDrops(IBlockAccess blockAccess, BlockPos pos,
            IBlockState state, int fortune) {
        
        List<ItemStack> items = new ArrayList<ItemStack>();
        
        if (!(blockAccess instanceof World)) {
            
            return items;
        }
        
        World world = (World) blockAccess;
        
        if (world.rand.nextInt(10) == 0) {
            
            items.add(new ItemStack(this.seedlingRef.get()));
        }
                
        if (state.getValue(AGE) == 7) {
            
            items.add(ItemSimple.newStack(this.fruitRef.get(), 1, world));
        }
        
        return items;
    }
    
    /** Grows up according to chance. */
    @Override
    public void updateTick(World world, BlockPos pos,
            IBlockState state, Random rand) {
        
        super.updateTick(world, pos, state, rand);
        
        if (rand.nextFloat() <= this.growthChance) {
            
            int oldAge = state.getValue(AGE);       
            int newAge = (oldAge + 1) > 7 ? 7 : (oldAge + 1);
            IBlockState newState = state.withProperty(AGE, newAge);
            world.setBlockState(pos, newState);  
        }
    }
    
    /** Harvests fruit if full grown. */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos,
            IBlockState state, EntityPlayer player, EnumHand hand,
            EnumFacing side, float hitX, float hitY, float hitZ) {
                
        if (state.getValue(AGE) == 7) {

            IBlockState newState = state.withProperty(AGE, 0);
            world.setBlockState(pos, newState);
            
            if (!world.isRemote) {
                
                for (EnumFacing facing : EnumFacing.VALUES) {
                    
                    if (world.isAirBlock(pos.offset(facing))) {
                        
                        pos = pos.offset(facing);
                        break;
                    }
                }
                
                spawnAsEntity(world, pos,
                        ItemSimple.newStack(this.fruitRef.get(), 1, world));
            }  
            
            return true;
        }
        
        return false;
    } 
    
    @Override
    protected BlockStateContainer createBlockState() {
        
        return new BlockStateContainer(this, AGE, DECAYABLE, CHECK_DECAY);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        
        return this.getDefaultState().withProperty(AGE, meta);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        
        return state.getValue(AGE);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world,
            BlockPos pos, int fortune) {

        return null;
    }

    @Override
    public EnumType getWoodType(int meta) {

        return EnumType.OAK;
    }
}
