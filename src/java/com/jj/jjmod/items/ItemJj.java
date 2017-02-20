package com.jj.jjmod.items;

import com.jj.jjmod.init.ModCapabilities;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** General superclass for new items and basic implementation. */
public class ItemJj extends Item {
    
    public ItemJj(String name, int stackSize, CreativeTabs tab) {
        
        setupItem(this, name, stackSize, tab);
    }
    
    public ItemJj(String name, int stackSize) {
        
        this(name, stackSize, CreativeTabs.MATERIALS);
    }
    
    public ItemJj(String name) {
        
        this(name, 1);
    }
    
    /** Applies constructor functions statically. */
    public static void setupItem(Item item, String name,
            int stackSize, CreativeTabs tab) {
        
        item.setRegistryName("item_" + name);
        item.setUnlocalizedName(item.getRegistryName().toString());
        item.setMaxStackSize(stackSize);
        item.setCreativeTab(tab);
    }
    
    public static ItemStack newStack(Item item, int count, World world) {
        
        ItemStack stack = new ItemStack(item, count);
        
        if (stack.hasCapability(ModCapabilities.CAP_DECAY, null)) {
            
            stack.getCapability(ModCapabilities.CAP_DECAY, null).setBirthTime(world.getTotalWorldTime());
        }
        
        return stack;
    }
}