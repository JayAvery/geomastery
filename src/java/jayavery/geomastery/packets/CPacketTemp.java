package jayavery.geomastery.packets;

import io.netty.buffer.ByteBuf;
import jayavery.geomastery.capabilities.DefaultCapPlayer;
import jayavery.geomastery.main.GeoCaps;
import jayavery.geomastery.main.Geomastery;
import jayavery.geomastery.utilities.TempStage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** Packet to update the player temperature icon on the Client. */
public class CPacketTemp implements IMessage {
    
    /** The temperature stage. */
    protected TempStage stage;
    
    public CPacketTemp() {}
    
    public CPacketTemp(TempStage stage) {
        
        this.stage = stage;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        this.stage = TempStage.values()[buf.readInt()];
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        
        buf.writeInt(this.stage.ordinal());
    }
    
    public static class Handler
    implements IMessageHandler<CPacketTemp, IMessage> {
        
        @Override
        public IMessage onMessage(CPacketTemp message,
                MessageContext ctx) {
            
            Geomastery.proxy.addMinecraftRunnable(() -> processMessage(message));
            return null;
        }
        
        public void processMessage(CPacketTemp message) {
            
            EntityPlayer player = Geomastery.proxy.getClientPlayer();
            ((DefaultCapPlayer) player.getCapability(GeoCaps
                    .CAP_PLAYER, null)).processTempPacket(message.stage);
        }
    }
}