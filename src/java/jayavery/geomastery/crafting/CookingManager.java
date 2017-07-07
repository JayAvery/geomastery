/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.crafting;

import java.util.Map;
import java.util.Map.Entry;
import com.google.common.collect.Maps;
import jayavery.geomastery.main.GeoCaps;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Smelting recipe and fuel manager. */
public class CookingManager {

    /** Map of inputs to outputs. */
    public final Map<ItemStack, ItemStack> recipes;
    /** Map of inputs to cooking times. */
    private final Map<ItemStack, Integer> cookingTimes;
    /** Map of fuels to burning times. */
    public final Map<ItemStack, Integer> fuels;
    /** Multiplier for cooking times. */
    private final int multiplier;

    public CookingManager(int multiplier) {

        this.recipes = Maps.<ItemStack, ItemStack>newHashMap();
        this.cookingTimes = Maps.<ItemStack, Integer>newHashMap();
        this.fuels = Maps.<ItemStack, Integer>newHashMap();
        this.multiplier = multiplier;
    }

    /** Adds a smelting recipe. */
    public void addCookingRecipe(ItemStack input,
            ItemStack output, int cookTime) {

        this.recipes.put(input, output);
        this.cookingTimes.put(input, cookTime * this.multiplier);
    }
    
    /** Adds a fuel. */
    public void addFuel(ItemStack fuel, int time) {
        
        this.fuels.put(fuel, time);
    }

    /** Gets the smelting result for the input.
     * @return The output ItemStack smelted from the input. */
    public ItemStack getCookingResult(ItemStack input, World world) {

        if (input.hasCapability(GeoCaps.CAP_DECAY, null) &&
                input.getCapability(GeoCaps.CAP_DECAY, null).isRot(world)) {
            
            return ItemStack.EMPTY;
        }
        
        for (Entry<ItemStack, ItemStack> entry : this.recipes.entrySet()) {

            if (ItemStack.areItemsEqual(input, entry.getKey())) {
                
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }
    
    /** Gets the time taken to cook the input.
     * @return The number of ticks taken to cook this
     * input, -1 if the input is not a recipe. */
    public int getCookingTime(ItemStack input) {
        
        for (Entry<ItemStack, Integer> entry : this.cookingTimes.entrySet()) {
            
            if (ItemStack.areItemsEqual(input, entry.getKey())) {
                
                return entry.getValue();
            }
        }
        
        return -1;
    }
    
    /** Gets the cook time for the input.
     * @return The ticks the input fuel cooks for. */
    public int getFuelTime(ItemStack fuel) {
        
        for (Entry<ItemStack, Integer> entry: this.fuels.entrySet()) {

            if (ItemStack.areItemsEqual(fuel, entry.getKey())) {

                return entry.getValue();
            }
        }

        return 0;
    }
}
