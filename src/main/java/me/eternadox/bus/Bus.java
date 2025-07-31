package me.eternadox.bus;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.bus.event.Event;
import java.lang.reflect.Field;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class Bus implements IBus {

    private final Map<Class<?>, List<Consumer<Event>>> listeners = new ConcurrentHashMap<>();
    private final Map<Consumer<Event>, Type> types = new HashMap<>();

    @Override
    public void post(Event e) {
        for (Iterator<Map.Entry<Class<?>, List<Consumer<Event>>>> it = listeners.entrySet().iterator();
             it.hasNext();){
                for (Consumer<Event> listener : it.next().getValue()) {
                    if (types
                            .get(listener)
                            .getTypeName()
                            .equals(
                                    e.getClass()
                                            .getTypeName()))
                            listener.accept(e);
                }
        }
    }

    @Override
    public void subscribe(Object o) {
        List<Consumer<Event>> clazzListeners = new ArrayList<>();
        for (Field f : o.getClass().getDeclaredFields()){
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Subscribe.class) && f.getType().isAssignableFrom(Consumer.class)) {
                    Consumer<Event> listener = (Consumer<Event>) (MethodHandles.lookup().unreflectGetter(f).invokeWithArguments(o));

                    types.put(listener, ((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0]);
                    clazzListeners.add(listener);

                }
            } catch (Throwable e){
                e.printStackTrace();
            }
        }

        if (!clazzListeners.isEmpty()){
            listeners.put(o.getClass(), clazzListeners);
        }

    }

    @Override
    public void unsubscribe(Object o) {
        listeners.remove(o.getClass());
    }
}