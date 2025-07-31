package me.eternadox.client.impl.module.render;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.misc.TimerUtil;
import me.eternadox.client.api.util.render.DrawUtil;
import me.eternadox.client.api.util.world.MoveUtil;
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
import java.util.function.Consumer;

import static net.minecraft.network.play.client.C02PacketUseEntity.Action.ATTACK;

@Info(displayName = "BPS Counter", category = Category.RENDER)
public class BPSCounter extends Module {
    private TimerUtil timer = new TimerUtil();

    private Entity target;
    @Subscribe
    public Consumer<Event2DRender> on2dRender = e -> {
        if (mc.thePlayer != null)
            mc.fontRendererObj.drawStringWithShadow(MoveUtil.getBlocksPerSecondString() + " BPS", 10, mc.displayHeight - 10, -1);
    };


}
