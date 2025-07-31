package me.eternadox.client.impl.manager;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.manager.Manager;
import me.eternadox.client.api.notification.Notification;
import me.eternadox.client.api.util.Methods;
import me.eternadox.client.impl.event.Event2DRender;
import me.eternadox.client.impl.event.EventTick;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class NotificationManager extends Manager implements Methods {
    private final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    @Override
    public void initialize(){
        super.initialize();
        notifications.clear();

    }

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        for (Notification n : notifications){
            if (n.hasNotificationEnded())
                notifications.remove(n);
        }
    };

    @Subscribe
    public Consumer<Event2DRender> on2DRender = e -> {
       int notificationY = new ScaledResolution(mc).getScaledHeight() - 50;
       int notificationX = new ScaledResolution(mc).getScaledWidth() - 10;

       for (Notification n : notifications){
           if (n.getTimer().hasTimeElapsed(50)){

               String timeRemaining = new DecimalFormat(" (0.0s)").format(((double) n.getDuration() - (double) n.getTimer().getTimeElapsed()) / 1000);
               int notificationStart = notificationX - mc.fontRendererObj.getStringWidth((n.getBody().length() > n.getHeader().length() + timeRemaining.length()) ? n.getBody() : n.getHeader() + timeRemaining) - 20;

               Gui.drawRect(notificationStart, notificationY, notificationX, notificationY + 40, 0xff000000);

               mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.BOLD + n.getHeader() + EnumChatFormatting.RESET + timeRemaining, notificationStart + 5, notificationY + 5, -1);
               mc.fontRendererObj.drawStringWithShadow(n.getBody(),  notificationStart + 5, notificationY + 16, -1);

               double lengthOfBarMultiplier = Math.min((double) n.getTimer().getTimeElapsed() / (double) n.getDuration(), 1);
               int barLength = Math.max(notificationStart, notificationX - (int) (((notificationX - notificationStart) * lengthOfBarMultiplier))); // cancerous code but it works

               Gui.drawRect(notificationStart, notificationY + 39, barLength, notificationY + 40, -1);

               notificationY -= 50;
           }
       }
    };

    public void push(Notification n) {
        notifications.add(n);
    }

}
