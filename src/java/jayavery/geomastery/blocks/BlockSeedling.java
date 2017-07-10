/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.blocks;

import java.util.List;
import java.util.Random;
import jayavery.geomastery.main.GeoConfig;
import jayavery.geomastery.utilities.IBiomeCheck;
import jayavery.geomastery.utilities.ITreeGenFactory;
import jayavery.geomastery.utilities.ToolType;
import jayavery.geomastery.worldgen.WorldGenTreeApple;
import jayavery.geomastery.worldgen.WorldGenTreeBanana;
import jayavery.geomastery.worldgen.WorldGenTreeOrange;
import jayavery.geomastery.worldgen.WorldGenTreePear;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeMushroomIsland;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Seedling blocks. */
public abstract class BlockSeedling extends BlockBush implements IBiomeCheck {
    
    /** WorldGenerator factory for this tree. */
    private final ITreeGenFactory treeGenFactory;
    /** Chanc of growth per update tick. */
    private final float growthChance;
    /** Chance of death per update tick if invalid position. */
    private final float deathChance = 0.5F;

    public BlockSeedling(String name, ITreeGenFactory treeGenFactory,
            float growthChance) {
        
        BlockNew.setupBlock(this, name, CreativeTabs.DECORATIONS,
                1F, ToolType.SHOVEL);
        this.setTickRandomly(true); // TEST just added?
        this.treeGenFactory = treeGenFactory;
        this.growthChance = growthChance;
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        return BlockNew.CENTRE_SIXTEEN;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        return NULL_AABB;
    }
    
    /** Grow or die according to chance. */
    @Override
    public void updateTick(World world, BlockPos pos,
            IBlockState state, Random rand) {

        super.updateTick(world, pos, state, rand);

        if (world.isRemote) {
            
            return;
        }
                
        if (!this.isPermitted(world.getBiome(pos)) &&
                rand.nextFloat() <= this.deathChance) {

            world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
            return;
        }

        if (rand.nextFloat() <= this.growthChance) {
            
            this.treeGenFactory.makeTreeGen(world, rand, true)
                    .generateTree(pos);
        }
    }
    
    /** Adds this item's valid biomes to the tooltip if config. */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player,
            List<String> tooltip, boolean advanced) {
        
        if (GeoConfig.cropBiomes) {

            tooltip.add(I18n.translateToLocal(this.getUnlocalizedName() +
                    ".biomes"));
        }
    }
    
    public static class Pear extends BlockSeedling {
        
        public Pear() {
            
            super("seedling_pear", WorldGenTreePear::new, 0.1F);
        }

        @Override
        public boolean isPermitted(Biome biome) {

            return biome instanceof BiomeForest ||
                    biome instanceof BiomeOcean ||
                    biome instanceof BiomePlains || biome == Biomes.RIVER ||
                    biome instanceof BiomeJungle ||
                    biome instanceof BiomeSavanna;
        }
    }
    
    public static class Orange extends BlockSeedling {
        
        public Orange() {
            
            super("seedling_orange",  WorldGenTreeOrange::new, 0.15F);
        }

        @Override
        public boolean isPermitted(Biome biome) {

            return biome instanceof BiomeJungle ||
                    biome instanceof BiomeSavanna ||
                    biome instanceof BiomeDesert || biome instanceof BiomeMesa;
        }
    }
    
    public static class Banana extends BlockSeedling {
        
        public Banana() {
            
            super("seedling_banana", WorldGenTreeBanana::new, 0.2F);
        }

        @Override
        public boolean isPermitted(Biome biome) {

            return biome instanceof BiomeSwamp ||
                    biome instanceof BiomeMushroomIsland ||
                    biome instanceof BiomeJungle;
        }
    }
    
    public static class Apple extends BlockSeedling {
        
        public Apple() {
            
            super("seedling_apple", WorldGenTreeApple::new, 0.1F);
        }

        @Override
        public boolean isPermitted(Biome biome) {

            return biome instanceof BiomeForest || biome == Biomes.RIVER ||
                    biome instanceof BiomePlains || biome == Biomes.BEACH;
        }
    }
}
