/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.blocks;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import jayavery.geomastery.items.ItemBlockplacer;
import jayavery.geomastery.main.GeoConfig;
import jayavery.geomastery.utilities.BlockMaterial;
import jayavery.geomastery.utilities.BlockWeight;
import jayavery.geomastery.utilities.IDoublingBlock;
import jayavery.geomastery.utilities.ToolType;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Vault block. */
public class BlockVault extends BlockBuilding implements IDoublingBlock {
    
    public static final PropertyEnum<EnumShape> SHAPE =
            PropertyEnum.<EnumShape>create("shape", EnumShape.class);
    public static final PropertyDirection FACING =
            PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    /** Supplier for this block's dropped item. */
    private final Supplier<Item> item;
    /** Whether this block is double. */
    private final boolean isDouble;
    /** Building block weight. */
    private final BlockWeight weight;
    
    public BlockVault(String name, Supplier<Item> item,
            boolean isDouble, BlockWeight weight) {
        
        super(BlockMaterial.STONE_FURNITURE, name,
                CreativeTabs.BUILDING_BLOCKS, 2, ToolType.PICKAXE);
        this.item = item == null ? () -> Item.getItemFromBlock(this) : item;
        this.isDouble = isDouble;
        this.weight = weight;
    }
    
    /** Adds this block's build reqs to the tooltip if config. */
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player,
            List<String> tooltip, boolean advanced) {
        
        if (GeoConfig.buildTooltips) {
            
            tooltip.add(I18n.translateToLocal("geomastery:buildreq.vault"));
            tooltip.add(I18n.translateToLocal(this.getWeight().build()));
            tooltip.add(I18n.translateToLocal(this.getWeight().support()));
        }
    }
    
    @Override
    public boolean shouldDouble(IBlockState state, EnumFacing side) {
        
        EnumShape shape = state.getValue(SHAPE);
        
        if (shape != EnumShape.SINGLE) {
            
            return true;
            
        } else {
            
            EnumFacing facing = state.getValue(FACING);
            return side != facing.rotateY() && side != facing.rotateYCCW();            
        }
    }
    
    @Override
    public boolean isDouble() {
        
        return this.isDouble;
    }
    
    /** Drops handled manually for double->single breaking. */
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos,
            IBlockState state, int fortune) {
        
        return Collections.emptyList();
    }
    
    @Override
    public boolean removedByPlayer(IBlockState state, World world,
            BlockPos pos, EntityPlayer player, boolean willHarvest) {
    
        spawnAsEntity(world, pos, new ItemStack(this.item.get()));
        
        if (this.isDouble() &&
                this.item.get() instanceof ItemBlockplacer.Doubling<?>) {
            
            world.setBlockState(pos, ((ItemBlockplacer.Doubling<?>) this.item
                    .get()).single.get().getDefaultState());
            return false;
            
        } else {
            
            world.setBlockToAir(pos);
            return true;
        }
    }
    
    @Override
    public boolean shouldConnect(IBlockAccess world, IBlockState state,
            BlockPos pos, EnumFacing direcion) {
        
        return false;
    }
    
    @Override
    public BlockWeight getWeight() {
        
        return this.weight;
    }
    
    /** Checks whether there is a valid support at the
     * given direction from this position.
     * @return Whether there is a valid support in the given direction. */
    private boolean hasValidSide(IBlockAccess world,
            BlockPos pos, EnumFacing direction) {
        
        Block block = world.getBlockState(pos.offset(direction)).getBlock();
        return BlockWeight.getWeight(block).canSupport(this.getWeight()) &&
                !(block instanceof BlockVault) && !(block instanceof BlockBeam);
    }
    
    /** @return Whether the vault at the given pos is an external corner. */
    private boolean isExternalCorner(IBlockAccess world,
            BlockPos pos, EnumFacing direction) {
        
        Block block = world.getBlockState(pos.offset(direction)).getBlock();
        Block corner = world.getBlockState(pos.offset(direction.rotateY()))
                .getBlock();
        
        if (block instanceof BlockVault && corner instanceof BlockVault) {
            
            return true;
        }
        
        return false;
    }
    
    /** Checks whether the vault at the given pos is an internal corner,
     * clockwise from the given direction.
     * @return Whether this is an internal corner. */
    private boolean isInternalCorner(IBlockAccess world,
            BlockPos pos, EnumFacing direction) {
        
        return this.hasValidSide(world, pos, direction) &&
                this.hasValidSide(world, pos, direction.rotateY());
    }
   
    /** Checks whether the vault at the given pos is a lintel
     * in the axis of the given direction.
     * @return Whether this is a lintel. */
    private boolean hasLintel(IBlockAccess world,
            BlockPos pos, EnumFacing direction) {
        
        return this.hasValidSide(world, pos, direction) &&
                this.hasValidSide(world, pos, direction.getOpposite()) &&
                !this.hasValidSide(world, pos, direction.rotateY()) &&
                !this.hasValidSide(world, pos, direction.rotateYCCW());        
    }
    
    @Override
    public boolean isValid(World world, BlockPos pos) {
        
        boolean result = false;
        
        for (EnumFacing direction : EnumFacing.HORIZONTALS) {
            
            result = result ? result :
                    (this.hasValidSide(world, pos, direction) ||
                    this.isExternalCorner(world, pos, direction));
        }
        
        return result;
    }
    
    @Override
    public BlockStateContainer createBlockState() {
        
        return new BlockStateContainer(this, SHAPE, FACING);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        state = this.getActualState(state, world, pos);
        return state.getValue(SHAPE) == EnumShape.LINTEL ?
                FULL_BLOCK_AABB : TOP_HALF;
    }
    
    @Override
    public void addCollisionBoxToList(IBlockState state, World world,
            BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> list,
            @Nullable Entity entity, boolean unused) {
        
        state = this.getActualState(state, world, pos);
        int facing = state.getValue(FACING).getHorizontalIndex();
        EnumShape shape = state.getValue(SHAPE);
        
        AxisAlignedBB[] boxes;
        
        switch (shape) {
            
            case SINGLE:
                boxes = VAULT_STRAIGHT[facing];
                break;
                
            case INTERNAL:
                boxes = VAULT_INTERNAL[(facing + 3) % 4];
                break;
                
            case EXTERNAL:
                boxes = VAULT_EXTERNAL[facing];
                break;
                
            case LINTEL:
            default:
                boxes = new AxisAlignedBB[] {FULL_BLOCK_AABB};
                break;
        }
        
        for (AxisAlignedBB box : boxes) {
            
            addCollisionBoxToList(pos, entityBox, list, box);
        }
    }
    
    @Override
    public IBlockState getActualState(IBlockState state,
            IBlockAccess world, BlockPos pos) {
        
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            
            if (this.hasLintel(world, pos, facing)) {
                
                state = state.withProperty(SHAPE, EnumShape.LINTEL);
                state = state.withProperty(FACING, facing);
                return state;
            }
        }
        
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                
            if (this.isInternalCorner(world, pos, facing)) {
                
                state = state.withProperty(SHAPE, EnumShape.INTERNAL);
                state = state.withProperty(FACING, facing);
                return state;
            }
        }
         
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {

            if (this.hasValidSide(world, pos, facing)) {
                
                state = state.withProperty(SHAPE, EnumShape.SINGLE);
                state = state.withProperty(FACING, facing);
                return state;
            }
        }
        
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {

            if (this.isExternalCorner(world, pos, facing)) {
                
                state = state.withProperty(SHAPE, EnumShape.EXTERNAL);
                state = state.withProperty(FACING, facing);
                return state;
            }
        }
        
        return state;
    }
    
    /** Enum defining possible shapes for the vault. */
    public enum EnumShape implements IStringSerializable {
        
        SINGLE("single"), INTERNAL("internal"),
        EXTERNAL("external"), LINTEL("lintel");
        
        private String name;
        
        private EnumShape(String name) {
            
            this.name = name;
        }
        
        @Override
        public String getName() {
            
            return this.name;
        }
    }
}
