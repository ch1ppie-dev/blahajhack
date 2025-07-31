package me.eternadox.client.impl.module.move;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.world.MoveUtil;
import me.eternadox.client.impl.event.EventPreMotion;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.ModeSetting;

import java.util.function.Consumer;

@Info(displayName = "Speed", category = Category.MOVE)
public class Speed extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Mospixel Lowhop", "Strafe", "Mospixel Lowhop");


    public Speed(){
        this.addSettings(mode);
    }

    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        if (mc.thePlayer == null) return;


        switch (mode.getMode()) {
            case "Strafe":
                MoveUtil.strafe();
                if (mc.thePlayer.onGround)
                    mc.thePlayer.jump();
            break;
            case "Mospixel Lowhop":
                MoveUtil.setSpeed(0.3);

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.motionY = 0.2;
                    mc.timer.timerSpeed = 1.1f + (float)(Math.random() / 10);
                } else {
                    mc.timer.timerSpeed = 1.0f;
                }

                break;
        }

    };

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        this.setSuffix(mode.getMode());
    };


    @Override
    public void onDisable(){
        super.onDisable();
        mc.timer.timerSpeed = 1.0f;
    }
}
