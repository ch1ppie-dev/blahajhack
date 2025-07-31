package me.eternadox.client.impl.event;

import me.eternadox.bus.event.Event;

public class Event2DRender extends Event {
    private final int width, height;

    public Event2DRender(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
