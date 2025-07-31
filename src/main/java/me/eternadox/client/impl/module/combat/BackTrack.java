package me.eternadox.client.impl.module.combat;

import com.mojang.authlib.GameProfile;
import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.impl.event.EventPacket;
import me.eternadox.client.impl.event.EventPreMotion;
import me.eternadox.client.impl.setting.BooleanSetting;
import me.eternadox.client.impl.setting.NumberSetting;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK;

@Info(displayName = "Back Track", category = Category.COMBAT)
public class BackTrack extends Module {
    private final TimerUtil timer = new TimerUtil();
    private Entity target;
    private int ticks;
    private final List<Packet> packetQueue = new ArrayList<>();
    private final NumberSetting delay = new NumberSetting("Delay", 300,1.0, 1000.0, 1);
    private final BooleanSetting keepalives = new BooleanSetting("Keepalives", true);
    private final BooleanSetting transactions = new BooleanSetting("Transactions", true);
    private final BooleanSetting realPos = new BooleanSetting("Show Real Position", false);

    protected AbstractClientPlayer fakePlayer;


    public BackTrack() {
        this.addSettings(delay, keepalives, transactions, realPos);
    }

    @Override
    public void onEnable(){
        super.onEnable();
        packetQueue.clear();
        ticks = 0;

        if (realPos.isToggled()) {
            fakePlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.randomUUID(), "bot"));
            fakePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        }


    }

    @Override
    public void onDisable(){
        super.onDisable();

        packetQueue.clear();
        ticks = 0;
        if (realPos.isToggled()) {
            mc.theWorld.removeEntity(fakePlayer);
            fakePlayer = null;
        }

    }
    @Subscribe
    public Consumer<EventPreMotion> onPreMotion = e -> {
        if (mc.thePlayer == null) return;
        this.setSuffix(Double.toString(delay.getValue()));

        if (target != null){
            ticks++;
        }

        if (ticks == delay.getValue() / 10 && mc.getNetHandler().doneLoadingTerrain){

            for (Packet packet : packetQueue){
                if ((packet instanceof S00PacketKeepAlive && !keepalives.isToggled()) || (packet instanceof  S32PacketConfirmTransaction && !transactions.isToggled()))
                    continue;

                packet.processPacket(mc.getNetHandler());

            }
            packetQueue.clear();
        }

    };

    @Subscribe
    public Consumer<EventPacket> onPacket = e -> {
        if (mc.thePlayer == null) return;

        if (e.getPacket() instanceof C02PacketUseEntity){
            C02PacketUseEntity useEntityPacket = (C02PacketUseEntity)e.getPacket();

            if (useEntityPacket.getAction() == ATTACK){
                target = useEntityPacket.getEntityFromWorld(mc.theWorld);
                if (target == null || !(target instanceof AbstractClientPlayer)) return;

                if (realPos.isToggled()) {
                    if (fakePlayer != null && !Objects.equals(fakePlayer.getName().replace(" (" + EnumChatFormatting.AQUA + "Bot" + EnumChatFormatting.RESET + ")", ""), target.getName()) && mc.theWorld.playerEntities.contains(fakePlayer)) {
                        mc.theWorld.removeEntity(fakePlayer);
                    }

                    if (!mc.theWorld.playerEntities.contains(fakePlayer) && target != null && (target.getUniqueID() != null || target.getName() != null)) {
                        fakePlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.randomUUID(), target.getName() + " (" + EnumChatFormatting.AQUA + "Bot" + EnumChatFormatting.RESET + ")"));
                        fakePlayer.setPositionAndRotation(target.posX, target.posY, target.posZ, 0, 0);
                        mc.theWorld.spawnEntityInWorld(fakePlayer);
                    }
                }
                timer.reset();
            }
        }

        if ((e.getPacket() instanceof S14PacketEntity || e.getPacket() instanceof S00PacketKeepAlive || e.getPacket() instanceof S32PacketConfirmTransaction) && target != null && mc.getNetHandler().doneLoadingTerrain){


            if (realPos.isToggled()) {
                if (e.getPacket() instanceof S14PacketEntity) {
                    S14PacketEntity packet = (S14PacketEntity) e.getPacket();
                    if (packet.getEntity(mc.theWorld) == target) {
                        fakePlayer.serverPosX += packet.func_149062_c();
                        fakePlayer.serverPosY += packet.func_149061_d();
                        fakePlayer.serverPosZ += packet.func_149064_e();
                        double newPosX = (double) fakePlayer.serverPosX / 32.0D;
                        double newPosY = (double) fakePlayer.serverPosY / 32.0D;
                        double newPosZ = (double) fakePlayer.serverPosZ / 32.0D;
                        float newRotationYaw = packet.func_149060_h() ? (float) (packet.func_149066_f() * 360) / 256.0F : fakePlayer.rotationYaw;
                        float newRotationPitch = packet.func_149060_h() ? (float) (packet.func_149063_g() * 360) / 256.0F : fakePlayer.rotationPitch;
                        fakePlayer.setPositionAndRotation2(newPosX, newPosY, newPosZ, newRotationYaw, newRotationPitch, 3, false);
                        fakePlayer.onGround = packet.getOnGround();
                    }

                }
            }


            if (ticks != delay.getValue() / 10) {
                if (e.getPacket() instanceof S14PacketEntity && ((S14PacketEntity)e.getPacket()).getEntity(mc.theWorld) != target) return;

                packetQueue.add(e.getPacket());

                e.setCancelled(true);
            }

        }
        if (timer.hasTimeElapsed(500)){
            target = null;
            if (realPos.isToggled() && fakePlayer != null && mc.theWorld.playerEntities.contains(fakePlayer))
                mc.theWorld.removeEntity(fakePlayer);
            ticks = 0;
        }
    };

}
