package me.eternadox.client.impl.module.combat;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.notification.Notification;
import me.eternadox.client.api.notification.Type;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventTick;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAppleGold;

import java.util.function.Consumer;


@Info(displayName = "HvH Alerts", category = Category.COMBAT)
public class HvHAlerts extends Module {




    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        if (mc.thePlayer == null) return;

        if (mc.thePlayer.hurtTime > 0) {
            EntityLivingBase lastAttacker = mc.thePlayer.getLastAttacker();

            if (lastAttacker.isEating() && lastAttacker.getHeldItem().getItem() instanceof ItemAppleGold)
                Client.INSTANCE.getNotificationManager().push(new Notification(Type.WARNING, "Criticals", "Player " + lastAttacker.getName() + " critted you!", 5000));

        }
    };
}
