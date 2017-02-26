package com.jayavery.jjmod.tileentities;

import com.jayavery.jjmod.utilities.IMultipart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/** TileEntity for sawpit crafting block. */
public class TECraftingSawpit extends TileEntity {
    
    /** EnumFacing of this sawpit block. */
    private EnumFacing facing;
    /** Part of this sawpit block. */
    private EnumPartSawpit part;
    
    /** Sets the given state information. */
    public void setState(EnumFacing facing, EnumPartSawpit part) {
        
        this.facing = facing;
        this.part = part;
    }
    
    /** @return The EnumFacing state of this sawpit block. */
    public EnumFacing getFacing() {
        
        return this.facing;
    }
    
    /** @return The EnumPartSawpit state of this sawpit block. */
    public EnumPartSawpit getPart() {
        
        return this.part;
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        
        super.writeToNBT(compound);
        compound.setInteger("facing", this.facing.getHorizontalIndex());
        compound.setInteger("part", this.part.ordinal());
        return compound;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        
        super.readFromNBT(compound);
        this.facing = EnumFacing.getHorizontal(compound.getInteger("facing"));
        this.part = EnumPartSawpit.values()[compound.getInteger("part")];
    }
    
    /** Required to update rendering on the Client. */
    @Override
    public NBTTagCompound getUpdateTag() {
        
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    /** Required to update rendering on the Client. */
    public SPacketUpdateTileEntity getUpdatePacket() {
        
        return new SPacketUpdateTileEntity(this.getPos(), 0,
                this.writeToNBT(new NBTTagCompound()));
    }
    
    /** Required to update rendering on the Client. */
    @Override
    public void onDataPacket(NetworkManager net,
            SPacketUpdateTileEntity packet) {
        
        this.readFromNBT(packet.getNbtCompound());
    }
    
    /** Enum defining parts of the whole sawpit structure. */
    public enum EnumPartSawpit implements IStringSerializable, IMultipart {
        
        B1 ("b1", true), B2("b2", true), B3("b3", true),
        B4("b4", true), B5("b5", true), M1 ("m1", true),
        M2("m2", true), M3("m3", true), M4("m4", true),
        M5("m5", true), T1 ("t1", false), T2("t2", false),
        T3("t3", false), T4("t4", false), T5("t5", false);
        
        private final String name;
        private final boolean isPassable;
        
        private EnumPartSawpit(String name, boolean isPassable) {
            
            this.name = name;
            this.isPassable = isPassable;
        }
        
        @Override
        public boolean shouldDrop() {
            
            return this == B1;
        }

        @Override
        public String getName() {

            return this.name;
        }
        
        /** @return Whether this part has null collision boundng box. */
        public boolean isPassable() {
            
            return this.isPassable;
        }
    }
}