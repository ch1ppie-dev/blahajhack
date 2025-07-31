package me.eternadox.client.impl.command;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.command.Command;
import me.eternadox.client.api.command.annotations.Info;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.Methods;
import me.eternadox.client.api.util.world.ChatUtil;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventPacket;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

@Info(name = "Ping", aliases = {"ms"})
public class Ping extends Command {

    long time;

    @Override
    public void onExecute(String[] args){
        Client.INSTANCE.getBus().subscribe(this);
        time = System.currentTimeMillis();
        Methods.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));

    }

    @Subscribe
    public Consumer<EventPacket> onPacket = e -> {
        if (e.getPacket() instanceof S3APacketTabComplete){
            time = System.currentTimeMillis() - time;
            ChatUtil.send("Ping: " + time + " ms");
            Client.INSTANCE.getBus().unsubscribe(this);

        }
    };

}
