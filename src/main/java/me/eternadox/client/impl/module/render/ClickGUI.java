package me.eternadox.client.impl.module.render;

import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.module.annotations.Info;
import me.eternadox.client.impl.setting.ModeSetting;
import me.eternadox.client.impl.setting.NumberSetting;
import me.eternadox.client.impl.ui.GuiClick;
import org.lwjgl.input.Keyboard;

@Info(displayName = "Click GUI", category = Category.RENDER, key = Keyboard.KEY_RSHIFT)

public class ClickGUI extends Module {

    private GuiClick instance;
    private final ModeSetting image = new ModeSetting("Image", "Boykisser",  "Boykisser", "Estradiol");

    public ClickGUI(){
        this.addSettings(image);
    }
    @Override
    public void onEnable(){
        super.onEnable();
        if (instance == null)
            instance = new GuiClick();
        mc.displayGuiScreen(instance);
    }
}
