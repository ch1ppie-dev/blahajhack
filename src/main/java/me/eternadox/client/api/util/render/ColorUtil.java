package me.eternadox.client.api.util.render;

import java.awt.Color;

public class ColorUtil {

    public static int interpolateTwoColors(float step, int color1, int color2) {
        return interpolateTwoColors(step, Color.getColor("", color1), Color.getColor("", color2));
    }

    public static int interpolateTwoColors(float step, Color color1, Color color2) {
        step = Math.min(Math.max(step, 0.0f), 1.0f);

        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * step);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * step);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * step);
        int alpha = (int) (color1.getAlpha() + (color2.getAlpha() - color1.getAlpha()) * step);

        Color newColor = new Color(red, green, blue, alpha);

        return newColor.getRGB();
    }


}