package me.eternadox.client.api.util.font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FontRenderer {
    private Font font;
    private final String path;
    private final float size;

    public FontRenderer(String path, float size){
        this.path = path;
        this.size = size;

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);

            ge.registerFont(font);


        } catch (IOException | FontFormatException e) {

        }
    }

    public Font getFont() {
        return font;
    }

    public String getPath() {
        return path;
    }

    public float getSize() {
        return size;
    }
}
