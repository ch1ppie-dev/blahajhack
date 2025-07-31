package me.eternadox.client.impl.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.eternadox.client.api.setting.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {
    private boolean toggled;

    public BooleanSetting(String name, Supplier<Boolean> visible, boolean toggled) {
        super(name, visible);
        this.toggled = toggled;
    }

    public BooleanSetting(String name, boolean toggled) {
        super(name);
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !toggled;
    }
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    public JsonObject convertToJson() {

        JsonObject settingJsonObject = new JsonObject();
        settingJsonObject.addProperty("settingName", this.getName());
        settingJsonObject.addProperty("settingValue", this.isToggled());

        return settingJsonObject;
    }


    @Override
    public void setValueJson(JsonElement element) {
        setToggled(element.getAsBoolean());
    }
}
