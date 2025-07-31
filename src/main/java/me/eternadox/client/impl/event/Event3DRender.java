package me.eternadox.client.impl.event;

import me.eternadox.bus.event.Event;

public class Event3DRender extends Event {
    private final float partialTicks;

    public Event3DRender(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
