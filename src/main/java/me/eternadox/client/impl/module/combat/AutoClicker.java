package me.eternadox.client.impl.module.combat;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.NumberSetting;
import org.lwjgl.input.Mouse;

import java.util.Random;

@Info(displayName = "Auto Clicker", category = Category.COMBAT)
public class AutoClicker extends Module {

    private final TimerUtil timer = new TimerUtil();
    private final NumberSetting minCps = new NumberSetting("Min", 6.0,1.0, 20.0, 1.0);
    private final NumberSetting maxCps = new NumberSetting("Max", 9.0, 1.0, 20.0, 1.0);


    public AutoClicker() {
        this.addSettings(minCps, maxCps);
    }
    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null) return;
        this.setSuffix(minCps.getValue()+" - "+maxCps.getValue());

        double cps = new Random().doubles(minCps.getValue(), maxCps.getValue()).findFirst().getAsDouble();
        if (Mouse.isButtonDown(0) && timer.hasTimeElapsed((long) (1000L / cps))){
            mc.clickMouse();
        }

    };
}
