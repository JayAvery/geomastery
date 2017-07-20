/*******************************************************************************
 * Copyright (C) 2017 Jay Avery
 * 
 * This file is part of Geomastery. Geomastery is free software: distributed
 * under the GNU Affero General Public License (<http://www.gnu.org/licenses/>).
 ******************************************************************************/
package jayavery.geomastery.utilities;

/** Enum defining all tool types. */
public enum EToolType {
    
    PICKAXE("pickaxe"), AXE("axe"), KNIFE("knife"), SICKLE("sickle"),
    MACHETE("machete"), SHOVEL("shovel"), HOE("hoe");
    
    private final String name;
    
    private EToolType(String name) {
        
        this.name = name;
    }
    
    @Override
    public String toString() {
        
        return this.name;
    }
}
