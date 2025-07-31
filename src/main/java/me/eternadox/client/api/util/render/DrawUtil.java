package me.eternadox.client.api.util.render;

import me.eternadox.client.api.util.Methods;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class DrawUtil implements Methods {
        public static void drawImage(ResourceLocation location, float x, float y, int width, int height) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.enableTexture2D();

            GlStateManager.color(1, 1, 1, 1);

            mc.getTextureManager().bindTexture(location);

            Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, width, height, width, height);

            GlStateManager.popMatrix();
        }

        public static void drawPlayerHead(AbstractClientPlayer player, int x, int y, int size){

            mc.getTextureManager().bindTexture(player.getLocationSkin());

            Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, size, size, 64, 64);

        }


}
