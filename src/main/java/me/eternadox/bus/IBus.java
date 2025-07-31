package me.eternadox.bus;

import me.eternadox.bus.event.Event;

public interface IBus {
    void post(Event e);
    void subscribe(Object o);
    void unsubscribe(Object o);

}
