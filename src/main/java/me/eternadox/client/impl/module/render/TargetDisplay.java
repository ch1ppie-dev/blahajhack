package me.eternadox.client.impl.module.render;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.api.util.render.DrawUtil;
import me.eternadox.client.impl.event.Event2DRender;
import me.eternadox.client.impl.event.EventPacket;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

import static net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK;

@Info(displayName = "Target Display", category = Category.RENDER)
public class TargetDisplay extends Module {
    private TimerUtil timer = new TimerUtil();

    private Entity target;
    @Subscribe
    public Consumer<Event2DRender> on2dRender = e -> {
        if (target != null && target instanceof EntityLivingBase)
            drawTargetDisplay();
    };

    private void drawTargetDisplay(){
        // Drawrect(x, y, width + x, height + or - y)

        GlStateManager.pushMatrix();

        GlStateManager.color(1, 1, 1, 1);
        Gui.drawRect(100, 100 + 10, 100 + 220, 100 + 75, 0xCC000000);
        GlStateManager.color(1, 1, 1, 1);

        if ((target instanceof EntityPlayer)) {
            DrawUtil.drawPlayerHead((AbstractClientPlayer) target, 100 + 10, 100 + 20, 48);
        }

        if (target != null && target instanceof EntityLivingBase && !(((EntityLivingBase) target).getHealth() < 1))
            Gui.drawRect(100 + 68, 100 + 48,  (int)(140 * ((EntityLivingBase) target).getHealth() / 20) + 100 + 68, 100 + 68, 0xff5d9eba);

        if (target != null && target instanceof EntityLivingBase) {
            String health = new DecimalFormat("0.0").format((double) (((EntityLivingBase) target).getHealth()) * 5) + "%";
            mc.fontRendererObj.drawStringWithShadow(health, 100 + 118, 100 + 55, -1);
        }

        GL11.glScalef(1.5f, 1.5f, 1.5f);

        mc.fontRendererObj.drawStringWithShadow(target == null  || target.getName() == null ? "null" : target.getName(), (int)((100 + 68) / 1.5), (int)((100 + 30) / 1.5), -1);

        GL11.glScalef(1f, 1f, 1f);

        GlStateManager.popMatrix();

    }

    @Subscribe
    public Consumer<EventPacket> onPacket = e -> {
        if (e.getPacket() instanceof C02PacketUseEntity){
            C02PacketUseEntity useEntityPacket = (C02PacketUseEntity)e.getPacket();

            if (useEntityPacket.getAction() == ATTACK){
                target = useEntityPacket.getEntityFromWorld(mc.theWorld);
                timer.reset();
            }
        }
        if (timer.hasTimeElapsed(500)){
            target = null;
        }
    };

}
