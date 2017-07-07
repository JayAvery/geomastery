/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.blocks;

import java.util.List;
import java.util.Random;
import com.google.common.collect.Lists;
import jayavery.geomastery.main.GeoBlocks;
import jayavery.geomastery.main.GeoItems;
import jayavery.geomastery.main.Geomastery;
import jayavery.geomastery.main.GuiHandler.GuiList;
import jayavery.geomastery.tileentities.TEFurnacePotfire;
import jayavery.geomastery.utilities.BlockMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/** Potfire furnace block. */
public class BlockFurnacePotfire extends BlockComplexAbstract {

    public BlockFurnacePotfire() {

        super("furnace_potfire", BlockMaterial.STONE_HANDHARVESTABLE,
                5F, null);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public int getLightValue(IBlockState state, IBlockAccess world,
            BlockPos pos) {
        
        TileEntity te = world.getTileEntity(pos);
    
        if (te instanceof TEFurnacePotfire) {
            
            if (((TEFurnacePotfire) te).isHeating()) {
                
                return 14;
            }
        }
        
        return 12;
    }
        
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
            IBlockState state, int fortune) {
        
        return Lists.newArrayList(new ItemStack(GeoItems.POT_CLAY));
    }

    @Override
    public TileEntity createTileEntity(World worldIn, IBlockState state) {

        return new TEFurnacePotfire();
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {

        return SIX;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        return CENTRE_FOURTEEN;
    }

    @Override
    public BlockStateContainer createBlockState() {

        return new BlockStateContainer(this);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world,
            BlockPos pos) {

        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return this.getDefaultState();
    }

    @Override
    public boolean activate(EntityPlayer player, World world,
            int x, int y, int z) {

        if (!world.isRemote) {
            
            player.openGui(Geomastery.instance, GuiList.POTFIRE.ordinal(),
                    world, x, y, z);
        }
        
        return true;
    }
}
