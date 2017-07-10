/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.container;

import jayavery.geomastery.container.slots.SlotFurnaceFuel;
import jayavery.geomastery.container.slots.SlotFurnaceInput;
import jayavery.geomastery.container.slots.SlotFurnaceOutput;
import jayavery.geomastery.tileentities.TEFurnaceAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Container for clay furnace. */
public class ContainerFurnaceClay extends ContainerFurnaceAbstract {
    
    /** X-position of the start of input slots. */
    private static final int INPUT_X = 74;
    /** X-position of the start of output slots. */
    private static final int OUTPUT_X = 123;
    /** Y-position of the start of output slots. */
    private static final int OUTPUT_Y = 26;

    public ContainerFurnaceClay(EntityPlayer player, 
            TEFurnaceAbstract<?> furnace, BlockPos pos) {
        
        super(player, furnace, pos);
        this.fireX = 75;
        this.arrowX = 94;
    }

    @Override
    protected void buildInput() {

        for (int i = 0; i < this.size; i++) {
            
            this.addSlotToContainer(new SlotFurnaceInput(this.furnace, i,
                    INPUT_X - (i * SLOT_SIZE), INPUT_Y));
        }
    }

    @Override
    protected void buildFuel() {

        for (int i = 0; i < this.size; i++) {
            
            this.addSlotToContainer(new SlotFurnaceFuel(this.furnace, i,
                    INPUT_X - (i * SLOT_SIZE), FUEL_Y));
        }
    }

    @Override
    protected void buildOutput() {

        for (int col = 0; col < 2; col++) {
            
            for (int row = 0; row < 2; row++) {
                
                this.addSlotToContainer(new SlotFurnaceOutput(this.furnace,
                        ((2 * row) + col), OUTPUT_X + (col * SLOT_SIZE),
                        OUTPUT_Y + (row * SLOT_SIZE)));
            }
        }  
    }
}
