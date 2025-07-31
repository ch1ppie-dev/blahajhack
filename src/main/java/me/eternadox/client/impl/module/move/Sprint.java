package me.eternadox.client.impl.module.move;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventPreMotion;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

@Info(displayName = "Sprint", key = Keyboard.KEY_B, category = Category.MOVE)
public class Sprint extends Module {

    @Subscribe
    public Consumer<EventPreMotion> eventPreMotionConsumer = e -> {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    };

    @Override
    public void onDisable(){
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

}
