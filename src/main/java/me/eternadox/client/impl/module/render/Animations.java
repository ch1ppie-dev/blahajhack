package me.eternadox.client.impl.module.render;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventItemRender;
import me.eternadox.client.impl.event.EventTick;
import me.eternadox.client.impl.module.combat.KillAura;
import me.eternadox.client.impl.setting.ModeSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumAction;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

@Info(displayName = "Animations", category = Category.RENDER)
public class Animations extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Eternadox", "1.7", "Eternadox", "Spin");

    public Animations(){
        this.addSettings(mode);
    }

    @Subscribe
    public Consumer<EventItemRender> onItemRender = e -> {
        KillAura killAura = (KillAura) Client.INSTANCE.getModuleManager().getModule("Kill Aura");
        if (e.isUsingItem() && e.getAction() == EnumAction.BLOCK || killAura.isBlocking){
            switch (mode.getMode()) {
                case "1.7":
                    mc.getItemRenderer().transformFirstPersonItem(e.getAnimationProgression(), e.getSwingProgress());
                    mc.getItemRenderer().func_178103_d();

                    e.setCancelled(true);
                break;
                case "Eternadox":
                    mc.getItemRenderer().transformFirstPersonItem(e.getAnimationProgression(), e.getSwingProgress());
                    mc.getItemRenderer().func_178103_d();

                    GL11.glTranslatef(-0.1f, -0.5f,0.2f);
                    GL11.glTranslatef(-0.1f, 0.1f,0.2f);

                    e.setCancelled(true);
                    break;
                case "Spin":
                    mc.getItemRenderer().transformFirstPersonItem(e.getAnimationProgression(), e.getSwingProgress());
                    GL11.glRotatef(System.currentTimeMillis() % 360 / e.getAnimationProgression(), 0.0F, 0.0F, 0.0F);


                    e.setCancelled(true);
                    break;
            }
        }

    };

    @Subscribe
    public Consumer<EventTick> onTick = e -> {
        this.setSuffix(mode.getMode());
    };
}
