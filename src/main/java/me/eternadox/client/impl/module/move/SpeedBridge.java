package me.eternadox.client.impl.module.move;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventTick;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

@Info(displayName = "Speed Bridge", category = Category.MOVE)
public class SpeedBridge extends Module {

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null) return;
        BlockPos playerPosBelow = new BlockPos(mc.thePlayer).add(0, -1, 0);
        if (mc.theWorld.getBlockState(playerPosBelow).getBlock() instanceof BlockAir) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);

        }

    };
}
