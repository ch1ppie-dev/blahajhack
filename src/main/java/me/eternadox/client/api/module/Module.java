package me.eternadox.client.api.module;

import com.google.gson.JsonObject;
import me.eternadox.client.api.notification.Notification;
import me.eternadox.client.api.notification.Type;
import me.eternadox.client.api.setting.Setting;
import me.eternadox.client.impl.Client;
import me.eternadox.client.api.module.annotations.Info;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {

    private final Info annotation = this.getClass().getDeclaredAnnotation(Info.class);
    private final String displayName = annotation.displayName();
    private String suffix = annotation.suffix();
    private int key = annotation.key();
    private final Category category = annotation.category();

    protected Minecraft mc = Minecraft.getMinecraft();
    private final List<Setting> settings = new ArrayList<>();

    private boolean toggled;

    public void toggle(){
        this.toggled = !toggled;

        if (toggled) {
            Client.INSTANCE.getBus().subscribe(this);
            this.onEnable();
            Client.INSTANCE.getNotificationManager().push(new Notification(Type.SUCCESS, "Enabled", "Enabled module "+this.getDisplayName(), 1000));
        } else {
            Client.INSTANCE.getBus().unsubscribe(this);
            this.onDisable();
            Client.INSTANCE.getNotificationManager().push(new Notification(Type.ERROR, "Disabled", "Disabled module "+this.getDisplayName(), 1000));

        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getKey() {
        return key;
    }

    public String getRenderText(){
        return this.displayName + (!suffix.isEmpty() ? " " + EnumChatFormatting.GRAY + this.suffix : "");
    }

    public Category getCategory() {
        return category;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public void onEnable() { }

    public void onDisable() { }

    public JsonObject convertToJson() {
        JsonObject moduleJsonObject = new JsonObject();

        moduleJsonObject.addProperty("displayName", this.getDisplayName());
        moduleJsonObject.addProperty("toggled", this.isToggled());
        moduleJsonObject.addProperty("bind", this.getKey());

        return moduleJsonObject;
    }
}
