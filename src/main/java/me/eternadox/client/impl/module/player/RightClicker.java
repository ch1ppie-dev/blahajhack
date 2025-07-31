package me.eternadox.client.impl.module.player;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Mouse;

import java.util.Random;

@Info(displayName = "Right Clicker", category = Category.PLAYER)
public class RightClicker extends Module {

    private final TimerUtil timer = new TimerUtil();
    private final NumberSetting minCps = new NumberSetting("Min", 12.0,1.0, 20.0, 1.0);
    private final NumberSetting maxCps = new NumberSetting("Max", 20, 1.0, 20.0, 1.0);


    public RightClicker(){
        this.addSettings(minCps, maxCps);
    }

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null) return;
        this.setSuffix(minCps.getValue()+" - "+maxCps.getValue());

        double cps = new Random().doubles(minCps.getValue(), maxCps.getValue()).findFirst().getAsDouble();
        if (Mouse.isButtonDown(1) && timer.hasTimeElapsed((long) (1000L / cps)) && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() instanceof ItemBlock){
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getItemInUse());
        }
    };
}
