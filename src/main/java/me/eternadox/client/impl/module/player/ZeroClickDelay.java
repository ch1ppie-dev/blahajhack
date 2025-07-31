package me.eternadox.client.impl.module.player;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventTick;

@Info(displayName = "Zero Click Delay", category = Category.PLAYER)
public class ZeroClickDelay extends Module {

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        mc.leftClickCounter = 0;
    };
}
