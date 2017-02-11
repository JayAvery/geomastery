package com.jj.jjmod.utilities;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

/** Custom Tool and Armour Materials. */
public class EquipMaterial {

    public static final ToolMaterial FLINT_TOOL =
            EnumHelper.addToolMaterial("flint_tool", 1, 100, 4F, 0.5F, 0);
    public static final ToolMaterial COPPER_TOOL =
            EnumHelper.addToolMaterial("copper_tool", 1, 150, 6F, 1.5F, 0);
    public static final ToolMaterial BRONZE_TOOL =
            EnumHelper.addToolMaterial("bronze_tool", 1, 200, 8F, 2F, 0);
    public static final ToolMaterial STEEL_TOOL =
            EnumHelper.addToolMaterial("steel_tool", 1, 300, 10F, 2.5F, 0);
    public static final ToolMaterial ANTLER_TOOL =
            EnumHelper.addToolMaterial("antler_tool", 1, 100, 4F, 0.0F, 0);
    public static final ToolMaterial WOOD_TOOL =
            EnumHelper.addToolMaterial("wood_tool", 1, 50, 3F, 0.0F, 0);
    
    public static final ArmorMaterial COTTON_APPAREL = EnumHelper
            .addArmorMaterial("cotton_apparel", "jjmod:cotton", 2,
            new int[] {0, 0, 0, 0}, 0,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final ArmorMaterial WOOL_APPAREL = EnumHelper
            .addArmorMaterial("wool_apparel", "jjmod:wool", 3,
            new int[] {0, 0, 0, 0},
            0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final ArmorMaterial FUR_APPAREL = EnumHelper
            .addArmorMaterial("fur_apparel", "jjmod:fur", 4,
            new int[] {0, 0, 0, 0},
            0, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final ArmorMaterial LEATHER_APPAREL = EnumHelper
            .addArmorMaterial("leather_apparel", "jjmod:leather", 7,
            new int[] {1, 2, 3, 1}, 0,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0F);
    public static final ArmorMaterial STEELMAIL_APPAREL = EnumHelper
            .addArmorMaterial("mail_apparel", "jjmod:mail", 11,
            new int[] {1, 4, 5, 2},
            0, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0F);
    public static final ArmorMaterial STEELPLATE_APPAREL = EnumHelper
            .addArmorMaterial("plate_apparel", "jjmod:plate", 15,
            new int[] {2, 5, 6, 2},
            0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);
}