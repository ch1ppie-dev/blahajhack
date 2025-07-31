package me.eternadox.client.impl.module.render;

import me.eternadox.bus.annotations.Subscribe;
import java.util.function.Consumer;
import me.eternadox.client.impl.Client;
import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.api.util.render.ColorUtil;
import me.eternadox.client.impl.event.Event2DRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Info(displayName = "HUD", key = Keyboard.KEY_O, category = Category.RENDER)
public class HUD extends Module {

    @Subscribe
    public Consumer<Event2DRender> on2dRender = e -> {
        drawWatermark();
        drawArrayList(e.getWidth());

    };

    private void drawWatermark(){
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        String time = dtf.format(LocalTime.now());

        String textToDraw = (Client.INSTANCE.getName().substring(1)+" ["+Client.INSTANCE.getVersion()+"] ["+time+"]"+" ["+ Minecraft.getDebugFPS()+"]")
                .replace("[", EnumChatFormatting.GRAY+"["+EnumChatFormatting.RESET)
                .replace("]", EnumChatFormatting.GRAY+"]"+EnumChatFormatting.RESET);
        //Gui.drawRect(3 , 4, mc.fontRendererObj.getStringWidth(textToDraw)+9, mc.fontRendererObj.FONT_HEIGHT + 10, 0xff080046);

        //Gui.drawRect(4 , 6, mc.fontRendererObj.getStringWidth(textToDraw)+7, mc.fontRendererObj.FONT_HEIGHT + 8, 0x77000000);
        mc.fontRendererObj.drawStringWithShadow(String.valueOf(Client.INSTANCE.getName().charAt(0)), 5, 7, ColorUtil.interpolateTwoColors(((System.currentTimeMillis() + 50) % 500) / 500.0f , 0xff620066, 0xff5d9eba));

        mc.fontRendererObj.drawStringWithShadow(textToDraw, 5 + mc.fontRendererObj.getStringWidth(String.valueOf(Client.INSTANCE.getName().charAt(0))), 7, -1);
        //Gui.drawRect(3, 3, mc.fontRendererObj.getStringWidth(textToDraw)+7, 5, 0xff080046);


    }
    private void drawArrayList(int width){
        int index = 0;
        int y = 5;
        for (Module m : Client.INSTANCE.getModuleManager().getEnabledModuleListSorted()){
            int color = ColorUtil.interpolateTwoColors(((System.currentTimeMillis() + (index * 50)) % 500) / 500.0f , 0xff620066, 0xff5d9eba);
            mc.fontRendererObj.drawStringWithShadow(m.getRenderText(), width - mc.fontRendererObj.getStringWidth(m.getRenderText()) - 5, y, color);
            y += mc.fontRendererObj.FONT_HEIGHT + 3;
            index++;
        }
    }
}
