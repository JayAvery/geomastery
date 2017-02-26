package com.jayavery.jjmod.capabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DefaultCapDecay implements ICapDecay {
    
    private static final int DAY_TICKS = 24000;
    private static final int MAX_STAGE = 10;
        
    private int stageSize;
    private long birthTime;
    
    public DefaultCapDecay(int maxDays) {
        
        int maxAge = maxDays * DAY_TICKS;
        this.stageSize = maxAge / MAX_STAGE;
    }

    
    @Override
    public float getRenderFraction() {
        
        if (Minecraft.getMinecraft().world == null) {

            return 0;
        }
        
        long currentTime = Minecraft.getMinecraft().world.getTotalWorldTime();
        long timeDiff = currentTime - this.birthTime;
        long stage = timeDiff / this.stageSize;
        return Math.min(1, Math.max(0, 1F - ((float) stage / MAX_STAGE)));
    }
    
    @Override
    public long getBirthTime() {
        
        return this.birthTime;
    }
    
    @Override
    public int getStageSize() {
        
        return this.stageSize;
    }
    
    @Override
    public void setBirthTime(long birthTime) {
        
        this.birthTime = (birthTime / this.stageSize) * this.stageSize;
    }
    
    @Override
    public void setStageSize(int stageSize) {
        
        this.stageSize = stageSize;
    }
    
    @Override
    public boolean isRot(World world) {
        
        if (world == null) {
            
            return false;
        }
        
        long currentTime = world.getTotalWorldTime();
        long timeDiff = currentTime - this.birthTime;
        return timeDiff >= (this.stageSize * MAX_STAGE);
    }
    
    @Override
    public NBTTagCompound serializeNBT() {
        
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("birthTime", this.birthTime);
        nbt.setInteger("stageSize", this.stageSize);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        
        this.birthTime = nbt.getLong("birthTime");
        this.stageSize = nbt.getInteger("stageSize");
    }
}