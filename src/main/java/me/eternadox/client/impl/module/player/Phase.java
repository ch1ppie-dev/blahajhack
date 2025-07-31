package me.eternadox.client.impl.module.player;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.notification.Notification;
import me.eternadox.client.api.notification.Type;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventCollide;
import me.eternadox.client.impl.event.EventPacket;
import me.eternadox.client.impl.event.EventTick;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

import java.util.function.Consumer;


@Info(displayName = "Phase", category = Category.PLAYER)
public class Phase extends Module {

    private boolean hitBlock;
    private int ticks;
    @Override
    public void onEnable(){
        super.onEnable();
        hitBlock = false;
        ticks = 0;
    }

    @Subscribe
    public Consumer<EventCollide> collisionEventListener = e ->{
        if ((e.getBlock() instanceof BlockGlass || e.getBlock() instanceof BlockStainedGlass)){
            e.setCancelled(true);
        }
    };

    @Subscribe
    public Consumer<EventTick> eventTickConsumer = e ->{
            mc.thePlayer.posY -= 0.08;
            ticks++;

    };
}
