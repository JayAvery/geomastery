/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.container;

import jayavery.geomastery.blocks.BlockComplexAbstract;
import jayavery.geomastery.container.slots.SlotCompostInput;
import jayavery.geomastery.container.slots.SlotCompostOutput;
import jayavery.geomastery.main.GeoRecipes;
import jayavery.geomastery.tileentities.TECompost;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Compost heap container. */
public class ContainerCompost extends ContainerAbstract {
    
    /** X-position of input slot. */
    private static final int INPUT_X = 25;
    /** Y-position of slots. */
    private static final int INPUT_Y = 27;
    /** X-position of output slot. */
    private static final int OUTPUT_X = 134;
    
    /** Index of input slot. */
    public static final int INPUT_I = 0;
    /** Index of output slot. */
    private static final int OUTPUT_I = 1;
    /** Index of first hotbar slot. */
    public static final int HOT_START = 2;
    /** Index of last hotbar slot. */
    private static final int HOT_END = 10;
    /** Index of first inventory slot. */
    private static final int INV_START = 11;
    
    /** Index of last inventory slot. */
    public final int invEnd;
    /** Drying rack TileEntity of this container. */
    public final TECompost compost;
    /** Position of this container. */
    private final BlockPos pos;
    
    public ContainerCompost(EntityPlayer player, BlockPos pos,
            TECompost compost) {
        
        super(player);
        this.compost = compost;
        this.pos = pos;
        
        this.addSlotToContainer(new SlotCompostInput(compost,
                INPUT_X, INPUT_Y));
        this.addSlotToContainer(new SlotCompostOutput(compost,
                OUTPUT_X, INPUT_Y));
        
        this.buildHotbar();
        this.invEnd = INV_START + this.buildInvgrid();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        boolean correctBlock = this.world.getBlockState(this.pos)
                .getBlock() instanceof BlockComplexAbstract;

        if (correctBlock) {

            return player.getDistanceSq(this.pos.getX() + 0.5,
                    this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64;
        }

        return false;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        
        if (slot != null && slot.getHasStack()) {
            
            ItemStack inSlot = slot.getStack();
            result = inSlot.copy();
            
            if (index == OUTPUT_I) {
                
                if (!this.mergeItemStack(inSlot, HOT_START,
                        this.invEnd + 1, false)) {
                    
                    return ItemStack.EMPTY;
                }
                
            } else if (index != INPUT_I) {
                
                if (GeoRecipes.COMPOST.getType(inSlot) != null) {
                    
                    if (!this.mergeItemStack(inSlot,
                            INPUT_I, INPUT_I + 1, false)) {
                        
                        return ItemStack.EMPTY;
                    }
                    
                } else if (index >= INV_START && index <= this.invEnd) {
                
                if (!this.mergeItemStack(inSlot, HOT_START,
                        HOT_END + 1, false)) {
                    
                    return ItemStack.EMPTY;
                }
                
                } else if (index >= HOT_START && index <= HOT_END) {
                    
                    if (!this.mergeItemStack(inSlot, INV_START,
                            this.invEnd + 1, false)) {
                        
                        return ItemStack.EMPTY;
                    }
                }
                
            } else if (!this.mergeItemStack(inSlot, HOT_START,
                    this.invEnd + 1, false)) {
                
                return ItemStack.EMPTY;
            }
            
            if (inSlot.getCount() == 0) {
                
                slot.putStack(ItemStack.EMPTY);
                
            } else {
                
                slot.onSlotChanged();
            }
            
            if (inSlot.getCount() == result.getCount()) {
                
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, inSlot);
        }
        
        return result;
    }
}
