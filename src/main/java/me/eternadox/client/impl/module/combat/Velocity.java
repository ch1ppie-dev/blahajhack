package me.eternadox.client.impl.module.combat;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.event.EventPacket;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.setting.ModeSetting;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Info(displayName = "Velocity", category = Category.COMBAT)
public class Velocity extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Cancel", "Cancel", "Custom", "Grim");
    private final NumberSetting horizontal = new NumberSetting("Horizontal %", () -> mode.getMode().equals("Custom"),0, 0, 100, 1);
    private final NumberSetting vertical = new NumberSetting("Vertical %", () -> mode.getMode().equals("Custom"),0, 0, 100, 1);

    public Velocity() {
        this.addSettings(mode, horizontal, vertical);
    }

    @Subscribe
    public Consumer<EventPacket> onPacket = e -> {
        if (e.getPacket() instanceof S12PacketEntityVelocity){
            switch (mode.getMode()) {
                case "Cancel":
                    e.setCancelled(true);
                break;
                case "Custom":
                    S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) e.getPacket();
                    velocityPacket.motionX *= horizontal.getValue() / 100;
                    velocityPacket.motionY *= vertical.getValue() / 100;
                    velocityPacket.motionZ *= horizontal.getValue() / 100;

                break;
                case "Grim":
                    velocityPacket = (S12PacketEntityVelocity) e.getPacket();

                    if (velocityPacket.getEntityID() != mc.thePlayer.getEntityId()) return;



                    break;
            }

        }


    };

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mode.getMode().equals("Custom")){
            this.setSuffix(horizontal.getValue() + "% " + vertical.getValue() + "%");
        } else {
            this.setSuffix(mode.getMode());
        }
    };

}
