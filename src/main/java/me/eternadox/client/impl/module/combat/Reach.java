package me.eternadox.client.impl.module.combat;

import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.NumberSetting;

import java.util.Random;

@Info(displayName = "Reach", category = Category.COMBAT)
public class Reach extends Module {

    private final NumberSetting minReach = new NumberSetting("Min", 3.1,1.0, 6.0, 0.1);
    private final NumberSetting maxReach = new NumberSetting("Max", 3.3, 1.0, 6.0, 0.1);

    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null)
            return;
        this.setSuffix(minReach.getValue()+" - "+ maxReach.getValue());



    };

    public Reach() {
        this.addSettings(minReach, maxReach);
    }

    public double getReach(){
        return new Random().doubles(minReach.getValue(), maxReach.getValue()).findFirst().getAsDouble();
    }
}
