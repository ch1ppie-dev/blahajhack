package me.eternadox.client.impl.event;

import me.eternadox.bus.event.CancellableEvent;
import net.minecraft.network.Packet;

public class EventPacket extends CancellableEvent {
    private final Packet<?> packet;

    public EventPacket(Packet<?> packet){
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }


}
