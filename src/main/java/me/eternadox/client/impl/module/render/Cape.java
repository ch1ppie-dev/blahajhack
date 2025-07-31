package me.eternadox.client.impl.module.render;

import me.eternadox.bus.annotations.Subscribe;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.event.EventItemRender;
import me.eternadox.client.impl.module.combat.KillAura;
import me.eternadox.client.impl.setting.ModeSetting;
import net.minecraft.item.EnumAction;

import java.util.function.Consumer;

@Info(displayName = "Cape", category = Category.RENDER)
public class Cape extends Module {

}
