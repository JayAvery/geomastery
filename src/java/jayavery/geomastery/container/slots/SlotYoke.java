/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.container.slots;

import jayavery.geomastery.capabilities.ICapPlayer;
import jayavery.geomastery.container.ContainerInventory;
import jayavery.geomastery.main.GeoCaps;
import jayavery.geomastery.main.GeoItems;
import jayavery.geomastery.main.Geomastery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/** Container slot for yoke. */
public class SlotYoke extends Slot {
    
    /** The player playerCap of this slot. */
    private final ICapPlayer playerCap;
    /** The player who owns this slot. */
    private final EntityPlayer player;
    
    public SlotYoke(EntityPlayer player, int x, int y) {
        
        super(null, 0, x, y);
        this.player = player;
        this.playerCap = player.getCapability(GeoCaps.CAP_PLAYER, null);
        this.backgroundName = Geomastery.MODID + ":gui/yoke_slot";
    }
    
    @Override
    public int getSlotStackLimit() {
        
        return 1;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        
        return stack.getItem() == GeoItems.YOKE;
    }
    
    @Override
    public ItemStack getStack() {
        
        return this.playerCap.getYoke();
    }
    
    @Override
    public void putStack(ItemStack stack) {
        
        this.playerCap.putYoke(stack);
        this.onSlotChanged();
    }
    
    @Override
    public void onSlotChanged() {
        
        ContainerInventory.refresh(this.player);
    }
    
    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        
        this.onSlotChanged();
        return stack;
    }
    
    @Override
    public ItemStack decrStackSize(int amount) {
        
        return this.playerCap.getYoke().splitStack(amount);
    }
    
    @Override
    public boolean isHere(IInventory inv, int slot) {
        
        return false;
    }
    
    @Override
    public boolean isSameInventory(Slot other) {
        
        return false;
    }
}
