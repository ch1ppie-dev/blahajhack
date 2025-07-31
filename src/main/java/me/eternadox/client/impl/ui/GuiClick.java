package me.eternadox.client.impl.ui;

import me.eternadox.client.api.module.Category;
import me.eternadox.client.api.module.Module;
import me.eternadox.client.api.util.render.ColorUtil;
import me.eternadox.client.api.util.render.DrawUtil;
import me.eternadox.client.impl.Client;
import me.eternadox.client.impl.module.render.ClickGUI;
import me.eternadox.client.impl.setting.ModeSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.stream.Collectors;

public class GuiClick extends GuiScreen {

    private boolean isDragging = false;
    private int startX, startY;
    private int rectX, rectY = 50;
    private Category selectedCategory = Category.COMBAT;
    private Category currentCategory = Category.COMBAT;
    private int currentCategoryX, currentCategoryY;
    private Module currentModule;
    private int currentModuleX, currentModuleY;
    private final ModeSetting image;

    public GuiClick(){
        ClickGUI clickGUI = (ClickGUI) Client.INSTANCE.getModuleManager().getModule("Click GUI");
        image = (ModeSetting)(clickGUI.getSettings().get(0));
    }
    @Override
    public void keyTyped(char typedChar, int keyCode){
        if (keyCode == Keyboard.KEY_RSHIFT) {
            Client.INSTANCE.getModuleManager().getModule("Click GUI").toggle();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton){
        switch (mouseButton){
            case 0:
                startX = mouseX;
                startY = mouseY;

                if (hovered(rectX, rectY, rectX + 400, rectY + 20, mouseX, mouseY)) {
                        isDragging = true;
                }

                if (currentCategory != null && textHovered(currentCategoryX, currentCategoryY, currentCategory.name(), mouseX, mouseY)){
                    selectedCategory = currentCategory;

                }

                if (currentModule != null && textHovered(currentModuleX, currentModuleY, currentModule.getDisplayName(), mouseX, mouseY)){
                    currentModule.toggle();

                }
                break;
        }
    }
    public boolean hovered(float left, float top, float right, float bottom, int mouseX, int mouseY) {
        return (mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom);
    }
    public boolean textHovered(float x, float y, String text, int mouseX, int mouseY) {
        return hovered(x, y, mc.fontRendererObj.getStringWidth(text)+x, mc.fontRendererObj.FONT_HEIGHT +y, mouseX, mouseY);
    }
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        switch (image.getMode()){
            case "Boykisser":
                DrawUtil.drawImage(new ResourceLocation("blahajhack/images/boykisser.jpg"), this.width - 79, this.height - 89, 79, 89);
                break;
            case "Estradiol":
                DrawUtil.drawImage(new ResourceLocation("blahajhack/images/estrogen.png"), this.width - 50, this.height - 50, 50, 50);
                break;
        }

        Gui.drawRect(rectX, rectY, rectX + 400, rectY + 250, 0x77000000);

        int tabX = (rectX + 35);

        for (Category c : Category.values()){
            String categoryName = c.name().charAt(0) + c.name().substring(1).toLowerCase();
            mc.fontRendererObj.drawStringWithShadow(categoryName, tabX, (rectY + 235), selectedCategory.equals(c) ? 0xff888888 : -1);
            if (c.equals(selectedCategory)){
                Gui.drawRect(tabX, (rectY + 245), tabX + mc.fontRendererObj.getStringWidth(categoryName), (rectY + 246), 0xffffffff);
            }

            if (textHovered(tabX, (rectY + 235), categoryName, mouseX, mouseY)){
                currentCategory = c;
                currentCategoryX = tabX;
                currentCategoryY = rectY + 235;
            }

            tabX += mc.fontRendererObj.getStringWidth(categoryName) + 20;




        }

        if (isDragging) {
                int offsetX = mouseX - startX;
                int offsetY = mouseY - startY;

                rectX += offsetX;
                rectY += offsetY;

                startX = mouseX;
                startY = mouseY;
        }

        List<Module> modulesInCategory = Client.INSTANCE.getModuleManager().getModuleList().stream().filter(m -> m.getCategory() == selectedCategory).collect(Collectors.toList());
        int moduleY = rectY + 20;
        for (Module module : modulesInCategory){
            Gui.drawRect(rectX + 20, moduleY - mc.fontRendererObj.FONT_HEIGHT, rectX + 320, moduleY + 20, 0x77000000);
            mc.fontRendererObj.drawStringWithShadow(module.getDisplayName(), rectX + 25, moduleY, module.isToggled() ? ColorUtil.interpolateTwoColors(((System.currentTimeMillis() + 50) % 500) / 500.0f , 0xff620066, 0xff5d9eba) : -1);

            if (textHovered(rectX + 25, moduleY , module.getDisplayName(), mouseX, mouseY)){
                currentModule = module;
                currentModuleX = rectX + 25;
                currentModuleY = moduleY;
            }

            moduleY += mc.fontRendererObj.FONT_HEIGHT + 25;
        }

    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            isDragging = false;
        }
    }

}
